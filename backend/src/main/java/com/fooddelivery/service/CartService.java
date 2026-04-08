package com.fooddelivery.service;

import com.fooddelivery.dto.AddCartItemRequest;
import com.fooddelivery.dto.CartItemResponse;
import com.fooddelivery.dto.CartResponse;
import com.fooddelivery.dto.UpdateCartItemRequest;
import com.fooddelivery.exception.ApiException;
import com.fooddelivery.model.Cart;
import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.User;
import com.fooddelivery.repository.CartItemRepository;
import com.fooddelivery.repository.CartRepository;
import com.fooddelivery.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserService userService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            MenuItemRepository menuItemRepository,
            UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart() {
        User user = userService.currentUser();
        return cartRepository.findByUserIdWithItems(user.getId())
                .map(this::toCartResponse)
                .orElseGet(() -> new CartResponse(List.of(), BigDecimal.ZERO));
    }

    @Transactional
    public CartResponse addItem(AddCartItemRequest request) {
        User user = userService.currentUser();
        MenuItem menuItem = menuItemRepository.findById(request.menuItemId())
                .orElseThrow(() -> new ApiException("Menu item not found"));

        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseGet(() -> createCart(user));

        if (!cart.getItems().isEmpty()) {
            Long existingRestaurantId = cart.getItems().get(0).getMenuItem().getRestaurant().getId();
            if (!existingRestaurantId.equals(menuItem.getRestaurant().getId())) {
                clearCartItems(cart);
            }
        }

        CartItem existing = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItem.getId())
                .orElse(null);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.quantity());
        } else {
            CartItem line = new CartItem();
            line.setCart(cart);
            line.setMenuItem(menuItem);
            line.setQuantity(request.quantity());
            cart.getItems().add(line);
        }
        cartRepository.save(cart);
        return cartRepository.findByUserIdWithItems(user.getId()).map(this::toCartResponse)
                .orElseThrow();
    }

    @Transactional
    public CartResponse updateItem(UpdateCartItemRequest request) {
        User user = userService.currentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException("Cart is empty"));
        CartItem line = cartItemRepository.findById(request.cartItemId())
                .orElseThrow(() -> new ApiException("Cart line not found"));
        if (!line.getCart().getId().equals(cart.getId())) {
            throw new ApiException("Cart line not found");
        }
        if (request.quantity() <= 0) {
            cart.getItems().remove(line);
            cartItemRepository.delete(line);
        } else {
            line.setQuantity(request.quantity());
            cartItemRepository.save(line);
        }
        cartRepository.save(cart);
        return cartRepository.findByUserIdWithItems(user.getId()).map(this::toCartResponse)
                .orElseGet(() -> new CartResponse(List.of(), BigDecimal.ZERO));
    }

    @Transactional
    public void clearCart(User user) {
        cartRepository.findByUserId(user.getId()).ifPresent(cart -> {
            clearCartItems(cart);
            cartRepository.save(cart);
        });
    }

    private void clearCartItems(Cart cart) {
        List<CartItem> copy = new ArrayList<>(cart.getItems());
        for (CartItem ci : copy) {
            cart.getItems().remove(ci);
            cartItemRepository.delete(ci);
        }
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> lines = cart.getItems().stream()
                .map(this::toCartItemResponse).toList();
        BigDecimal subtotal = lines.stream()
                .map(l -> l.unitPrice().multiply(BigDecimal.valueOf(l.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(lines, subtotal);
    }

    private CartItemResponse toCartItemResponse(CartItem ci) {
        MenuItem m = ci.getMenuItem();
        return new CartItemResponse(
                ci.getId(), m.getId(), m.getName(), m.getPrice(), ci.getQuantity(),
                m.isVegetarian(), m.getRestaurant().getId(), m.getRestaurant().getName());
    }
}
