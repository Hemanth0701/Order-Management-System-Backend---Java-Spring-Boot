package com.tenjiku.omg.dtos.exit_dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {

    private String id;

    private String cartId;

    private String productId;

    private int quantity;

    private double priceAtAddTime;

    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
}
