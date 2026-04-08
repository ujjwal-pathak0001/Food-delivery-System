package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM FoodOrder o JOIN FETCH o.restaurant JOIN FETCH o.items WHERE o.user.id = :userId ORDER BY o.placedAt DESC")
    List<Order> findByUserIdOrderByPlacedAtDesc(Long userId);

    List<Order> findByUserId(Long userId);
}
