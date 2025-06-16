package com.tenjiku.omg.mapper;

import com.tenjiku.omg.dtos.exit_dto.order.OrderDTO;
import com.tenjiku.omg.dtos.exit_dto.order.OrderItemDTO;
import com.tenjiku.omg.entity.Order;
import com.tenjiku.omg.entity.OrderItem;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .discount(order.getDiscount())
                .finalAmount(order.getFinalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(toItemDTOList(order.getOrderItems()))
                .build();
    }

    public OrderItemDTO toItemDTO(OrderItem item) {
        if (item == null || item.getProduct() == null) return null;

        return OrderItemDTO.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .priceAtPurchase(item.getPriceAtPurchase())
                .total(item.getTotal())
                .build();
    }

    public List<OrderItemDTO> toItemDTOList(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return List.of();
        return items.stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
    }
}