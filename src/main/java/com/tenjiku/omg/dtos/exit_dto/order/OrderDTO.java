package com.tenjiku.omg.dtos.exit_dto.order;

import com.tenjiku.omg.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String id;
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
