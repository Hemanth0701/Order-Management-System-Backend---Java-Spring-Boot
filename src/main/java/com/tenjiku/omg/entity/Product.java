package com.tenjiku.omg.entity;

import com.tenjiku.omg.entity.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"currentPrice", "prices"}) // 👈 prevent infinite loop
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 👈 IMPORTANT: Only include fields explicitly marked
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include // 👈 Include 'id' in equals/hashCode
    private String id;

    private String name;
    private String description;
    private String imageUrl;
    private int stock;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "current_price_id")
    @com.fasterxml.jackson.annotation.JsonManagedReference // 👈 manages currentPrice
    private Price currentPrice;

    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean isDeleted = false;
    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    private Type category;

    private int purchaseCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference // 👈 manages prices
    private List<Price> prices = new ArrayList<>();
}