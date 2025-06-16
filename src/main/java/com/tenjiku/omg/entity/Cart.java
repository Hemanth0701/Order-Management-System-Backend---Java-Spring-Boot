package com.tenjiku.omg.entity;

import com.tenjiku.omg.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"items"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "carts") // or "order_table"
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonManagedReference // 👈 manages prices
    private List<CartItem> items = new ArrayList<>();

    private double totalPrice;
    private double discountApplied = 0.0;
    private double finalPrice = 0.0;
    @Enumerated(EnumType.STRING)
    private Status currentstatus;// ACTIVE, CHECKED_OUT, ABANDONED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void calculateTotals() {
        this.totalPrice = items.stream()
                .mapToDouble(CartItem::getItemTotal)
                .sum();
        this.finalPrice = totalPrice - discountApplied;
    }
}
