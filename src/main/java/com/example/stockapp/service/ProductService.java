package com.example.stockapp.service;


import com.example.stockapp.dto.ProductDto;
import com.example.stockapp.dto.StockDTO;
import com.example.stockapp.entity.Item;
import com.example.stockapp.entity.Order;
import com.example.stockapp.entity.Product;
import com.example.stockapp.entity.User;
import com.example.stockapp.repository.ProductRepository;
import com.example.stockapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void addNewProduct(ProductDto request) {
        Optional<Product> exist = productRepository.findByName(request.getName());
        log.info(request.getName());
        if (exist.isPresent()) {
            throw new RuntimeException("this product already exist in the inventory");
        }
        else {
            Product newProduct = Product.builder()
                    .name(request.getName())
                    .quantity(request.getQuantity())
                    .productNumber(UUID.randomUUID().toString())
                    .build();

            productRepository.save(newProduct);
        }

    }

    public void deleteProduct(long id) {
        Optional<Product> productToDelete = productRepository.findById(id);
        if (productToDelete.isPresent() == false) {
            new RuntimeException("no product to delete");
        } else {
            productRepository.deleteById(id);
        }
    }

    public void editProductStock(StockDTO dto) {
        Product product = productRepository.findById(dto.getProductID()).orElseThrow(() -> new RuntimeException("no product to delete"));
        product.setQuantity(dto.getNewStock());
    }



}
