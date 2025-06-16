package com.tenjiku.omg.dtos.exit_dto.cart;

import com.tenjiku.omg.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private String id;

    private String userId;

    private List<CartItemDTO> items;

    private double totalPrice;

    private double discountApplied;

    private double finalPrice;

    private Status currentstatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
