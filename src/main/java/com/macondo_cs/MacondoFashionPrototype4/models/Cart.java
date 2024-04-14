package com.macondo_cs.MacondoFashionPrototype4.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Carts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "productName")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "productPrice")
    private double productPrice;

    @Column(name = "sum")
    private double sum;

    @Column(name = "isFinished")
    private Boolean isFinished;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "productId")
    private Long productId;
}

