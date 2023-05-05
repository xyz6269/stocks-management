package com.example.stockapp.repository;

import com.example.stockapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long > {
    Optional<Order> findOrderByOrderNumber(String orderNumber);
}
