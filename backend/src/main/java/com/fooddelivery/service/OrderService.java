package com.fooddelivery.service;

import com.fooddelivery.dto.OrderItemResponse;
import com.fooddelivery.dto.OrderResponse;
import com.fooddelivery.dto.PlaceOrderRequest;
import com.fooddelivery.exception.ApiException;
import com.fooddelivery.model.Cart;
import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.model.OrderStatus;
import com.fooddelivery.model.User;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.CartRepository;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserService userService;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            CartService cartService,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.userService = userService;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        User user = userService.currentUser();
        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ApiException("Cart is empty"));
        if (cart.getItems().isEmpty()) {
            throw new ApiException("Cart is empty");
        }

        Restaurant restaurant = cart.getItems().get(0).getMenuItem().getRestaurant();
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cart.getItems()) {
            if (!ci.getMenuItem().getRestaurant().getId().equals(restaurant.getId())) {
                throw new ApiException("Cart contains items from multiple restaurants");
            }
            BigDecimal lineTotal = ci.getMenuItem().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(lineTotal);
            OrderItem oi = new OrderItem();
            oi.setItemName(ci.getMenuItem().getName());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getMenuItem().getPrice());
            orderItems.add(oi);
        }

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setTotalAmount(total);
        order.setDeliveryAddress(request.deliveryAddress());
        order.setStatus(OrderStatus.PLACED);
        for (OrderItem oi : orderItems) {
            oi.setOrder(order);
            order.getItems().add(oi);
        }
        order = orderRepository.save(order);
        cartService.clearCart(user);
        return toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> myOrders() {
        User user = userService.currentUser();
        return orderRepository.findByUserIdOrderByPlacedAtDesc(user.getId()).stream()
                .map(this::toOrderResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        User user = userService.currentUser();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ApiException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new ApiException("Order not found");
        }
        return toOrderResponse(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(oi -> new OrderItemResponse(oi.getItemName(), oi.getQuantity(), oi.getUnitPrice()))
                .toList();
        return new OrderResponse(
                order.getId(), order.getStatus(), order.getTotalAmount(),
                order.getDeliveryAddress(), order.getPlacedAt(),
                order.getRestaurant().getName(), items);
    }
}
