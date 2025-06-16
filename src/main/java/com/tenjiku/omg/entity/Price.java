package com.tenjiku.omg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"product", "ownerOfCurrentPrice"}) // 👈 prevent infinite loop
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 👈 IMPORTANT: Only include fields explicitly marked
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include // 👈 Include 'id' in equals/hashCode
    private String id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @com.fasterxml.jackson.annotation.JsonBackReference // 👈 prevents circular for product → prices → product
    private Product product;

    @OneToOne(mappedBy = "currentPrice")
    @com.fasterxml.jackson.annotation.JsonBackReference // 👈 prevents circular for product → currentPrice → product
    private Product ownerOfCurrentPrice;
}
