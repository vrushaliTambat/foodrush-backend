package com.vrush.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;

//    food is nothing but food category like pizza multiple order items(3 pizza for example ) can have pizza
    @ManyToOne
    private Food food;

    private int quantity;
    private Long totalPrice;
    private List<String> ingredients;

}
