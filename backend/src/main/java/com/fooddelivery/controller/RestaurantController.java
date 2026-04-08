package com.fooddelivery.controller;

import com.fooddelivery.dto.MenuItemResponse;
import com.fooddelivery.dto.RestaurantResponse;
import com.fooddelivery.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantResponse> list(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return restaurantService.search(search);
        }
        return restaurantService.listAll();
    }

    @GetMapping("/{id}")
    public RestaurantResponse get(@PathVariable Long id) {
        return restaurantService.getById(id);
    }

    @GetMapping("/{id}/menu")
    public List<MenuItemResponse> menu(@PathVariable Long id) {
        return restaurantService.menuForRestaurant(id);
    }
}
