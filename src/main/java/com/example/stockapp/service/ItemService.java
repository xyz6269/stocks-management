package com.example.stockapp.service;

import com.example.stockapp.dto.ItemDTO;
import com.example.stockapp.entity.Item;
import com.example.stockapp.entity.Product;
import com.example.stockapp.entity.User;
import com.example.stockapp.repository.ItemRepositoty;
import com.example.stockapp.repository.ProductRepository;
import com.example.stockapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final UserRepository userRepository;
    private final ItemRepositoty itemRepositoty;
    private final AuthenticationService authenticationService;
    private final ProductRepository productRepository;

    public void addItemToOrder(ItemDTO dto) {
        User currentUSer = authenticationService.getCurrentUser();
        Product selectedProduct = productRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("no product with the given ID"));
        if (currentUSer.getOrder().getItemList().stream().anyMatch(item -> item.getProduct() == selectedProduct)) {
            throw new RuntimeException("you already ordered this Item previously");
        }
        log.info(selectedProduct.toString());

        Item item = Item.builder()
                .quantity(dto.getQuantity())
                .product(selectedProduct)
                .build();
        itemRepositoty.save(item);
        log.info(item.toString());
        currentUSer.getOrder().getItemList().add(item);
        log.info(String.valueOf(currentUSer.getOrder().getItemList().size()));
        userRepository.save(currentUSer);
    }

    public void removeItem(Long id) {
        User currUser = authenticationService.getCurrentUser();
        Item target = itemRepositoty.findById(id).orElseThrow(() -> new RuntimeException("no item with the given ID"));
        currUser.getOrder().getItemList().remove(target);
        itemRepositoty.deleteById(id);
    }



}
