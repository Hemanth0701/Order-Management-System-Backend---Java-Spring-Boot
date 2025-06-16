package com.tenjiku.omg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@ToString(exclude = {"cart"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cartItems") // or "order_table"
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Cart cart;

    @ManyToOne
    private Product product;

    private int quantity;
    private double priceAtAddTime;

    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onAdd() {
        this.addedAt = LocalDateTime.now();
        this.updatedAt = addedAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public double getItemTotal() {
        return priceAtAddTime * quantity;
    }
}

