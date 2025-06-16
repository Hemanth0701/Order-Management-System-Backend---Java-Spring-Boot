package com.tenjiku.omg.dtos.exit_dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String productId;
    private String productName;
    private int quantity;
    private double priceAtPurchase;
    private double total;
}
