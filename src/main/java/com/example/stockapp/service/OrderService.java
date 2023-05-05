package com.example.stockapp.service;


import com.example.stockapp.entity.Item;
import com.example.stockapp.entity.Order;
import com.example.stockapp.entity.User;
import com.example.stockapp.repository.ItemRepositoty;
import com.example.stockapp.repository.OrderRepository;
import com.example.stockapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ItemRepositoty itemRepositoty;

    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final ItemService itemService;

    public void createOrder() {
        User currentUser = authenticationService.getCurrentUser();
        log.info(currentUser.toString());
        if (currentUser.getOrder() != null) {
            throw new RuntimeException("your previous order is still being processed my the admin you can't submit a new one");
        }
        Order newOrder = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .user(currentUser)
                .build();
        orderRepository.save(newOrder);

    }

    public List<Item> getUserOrder(Long id) {
        User targetUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("no user with the given Id"));
        if (targetUser.getOrder() == null) {
            throw new RuntimeException("this user has no order");
        }
        return targetUser.getOrder().getItemList();
    }

    public List<Item> checkMyOrder() {
        User currUser = authenticationService.getCurrentUser();
        return getUserOrder(currUser.getId());
    }

    public void rejectOrder(Long id) {
        User targetUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("no user with the given ID"));
        Order orderToDelete = targetUser.getOrder();
        log.info(orderToDelete.getItemList().toString());
        orderRepository.deleteById(orderToDelete.getId());
    }

    public void submitOrder() {
        User currentUser = authenticationService.getCurrentUser();
        Order userOrder = currentUser.getOrder();
        userOrder.setSubmitted(true);
    }

    public String validateOrder(Long id) {
        User targetUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("no user with the given ID"));
        Order orderToValidate = targetUser.getOrder();
        orderToValidate.getItemList().forEach(item -> item.getProduct().setQuantity(item.getProduct().getQuantity()-item.getQuantity()));

        return "this order has been validated we will notify you when it's ready for shipping";
    }

    public List<Order> allSubmittedOrders() {
        List<Order> allOrders = orderRepository.findAll();
        List<Order> submittedOrders = new ArrayList<>();

        for (Order order: allOrders) {
            if (order.isSubmitted()) submittedOrders.add(order);
        }

        return submittedOrders;
    }

    public List<Item> optimizeOrder(Long id) {
        log.info("wtffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
       List<Item> orderItems = getUserOrder(id);
       List<Long> unwantedItems = new ArrayList<>();
        for (Item item: orderItems) {
            if (item.getQuantity() >= (item.getProduct().getQuantity())/5) {
                unwantedItems.add(item.getId());
            }
        }
        log.info(unwantedItems.toString()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        unwantedItems.forEach(aLong -> orderItems.remove(itemRepositoty.findById(aLong).orElseThrow(() -> new RuntimeException("no item with the given id was found"))));
        unwantedItems.forEach(itemRepositoty::deleteById);
        log.info("tf is you problem bruuuuuuuuuuuuuuuuh");

        if (orderItems.isEmpty()) rejectOrder(userRepository.findById(id).orElseThrow(() -> new RuntimeException("no user with the given ID")).getId());

        return orderItems;
    }




}
