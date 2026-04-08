package com.fooddelivery.service;

import com.fooddelivery.dto.MenuItemResponse;
import com.fooddelivery.dto.RestaurantResponse;
import com.fooddelivery.exception.ApiException;
import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> listAll() {
        return restaurantRepository.findAll().stream().map(this::toRestaurantResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> search(String q) {
        if (q == null || q.isBlank()) {
            return listAll();
        }
        return restaurantRepository.findByNameContainingIgnoreCaseOrCuisineContainingIgnoreCase(q.trim(), q.trim())
                .stream().map(this::toRestaurantResponse).toList();
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getById(Long id) {
        return restaurantRepository.findById(id).map(this::toRestaurantResponse)
                .orElseThrow(() -> new ApiException("Restaurant not found"));
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> menuForRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ApiException("Restaurant not found");
        }
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(this::toMenuItemResponse).toList();
    }

    private RestaurantResponse toRestaurantResponse(Restaurant r) {
        return new RestaurantResponse(
                r.getId(), r.getName(), r.getCuisine(), r.getRating(),
                r.getDeliveryTimeMinutes(), r.getImageUrl(), r.getDescription(), r.getArea());
    }

    private MenuItemResponse toMenuItemResponse(MenuItem m) {
        return new MenuItemResponse(
                m.getId(), m.getRestaurant().getId(), m.getName(), m.getDescription(),
                m.getPrice(), m.isVegetarian(), m.getImageUrl());
    }
}
