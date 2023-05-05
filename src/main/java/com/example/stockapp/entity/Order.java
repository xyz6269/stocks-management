package com.example.stockapp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    private boolean isSubmitted;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> itemList = new ArrayList<>();
    @OneToOne
    private User user;
}
