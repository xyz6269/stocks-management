package com.example.stockapp.controller;


import com.example.stockapp.dto.ItemDTO;
import com.example.stockapp.entity.Item;
import com.example.stockapp.entity.Order;
import com.example.stockapp.service.ItemService;
import com.example.stockapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stockapp")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    @PostMapping("/user/create-order")
    public void createOrder() {
        orderService.createOrder();
    }

    @PostMapping("/user/add-item-order")
    public void addItemstoOrder(@RequestBody() ItemDTO dto) {
        itemService.addItemToOrder(dto);
    }

    @GetMapping("/admin/get-user-order/{id}")
    public List<Item> getUserOrder(@PathVariable() Long id) {
        return orderService.getUserOrder(id);
    }

    @DeleteMapping("/user/remove-user-item/{id}")
    public void deleteItem(@PathVariable() Long id) {
         itemService.removeItem(id);
    }

    @DeleteMapping("/admin/reject-user-order/{id}")
    public void rejectOrder(@PathVariable() Long id) {
        orderService.rejectOrder(id);
    }

    @PostMapping("/admin/validate-user-order/{id}")
    public String validateOrder(@PathVariable() Long id) {
        return orderService.validateOrder(id);
    }

    @GetMapping("/user/my-order")
    public List<Item> myOrder() {
        return orderService.checkMyOrder();
    }

    @PostMapping("/admin/optimize-order/{id}")
    public List<Item>optimizeOrder(@PathVariable() Long id) {
        return orderService.optimizeOrder(id);
    }

    @PostMapping("/user/submit-order")
    public void submitUserOrder() {
         orderService.submitOrder();
    }
    
    @PostMapping("/admin/all-submit-order")
    public List<Order> getAllsubmittedOrder() {
        return orderService.allSubmittedOrders();
    }




}
