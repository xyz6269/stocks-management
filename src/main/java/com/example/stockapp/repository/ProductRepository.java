package com.example.stockapp.repository;

import com.example.stockapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {
    Optional<Product> findByName(String Name);
    Optional<Product> findByProductNumber(String number);

}
