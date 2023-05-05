package com.example.stockapp.repository;

import com.example.stockapp.entity.Item;
import com.example.stockapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoty extends JpaRepository<Item, Long> {
    Optional<List<Item>> getItemByProduct(Product product);
}
