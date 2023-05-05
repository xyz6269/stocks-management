package com.example.stockapp.controller;

import com.example.stockapp.dto.ProductDto;
import com.example.stockapp.dto.StockDTO;
import com.example.stockapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stockapp")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/add-product")
    public void addNewProduct(@RequestBody ProductDto dto) {
        productService.addNewProduct(dto);
    }

    @DeleteMapping("/admin/remove-product/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/admin/optimize-order")
    public void optimizeOrder(@RequestBody() StockDTO dto) {
        productService.editProductStock(dto);
    }
}
