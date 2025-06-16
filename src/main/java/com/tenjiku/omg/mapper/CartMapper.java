package com.tenjiku.omg.mapper;

import com.tenjiku.omg.dtos.exit_dto.cart.CartDTO;
import com.tenjiku.omg.dtos.exit_dto.cart.CartItemDTO;
import com.tenjiku.omg.entity.Cart;
import com.tenjiku.omg.entity.CartItem;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public  CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser() != null ? cart.getUser().getId() : null)
                .totalPrice(cart.getTotalPrice())
                .discountApplied(cart.getDiscountApplied())
                .finalPrice(cart.getFinalPrice())
                .currentstatus(cart.getCurrentstatus())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .items(cart.getItems() != null ?
                        cart.getItems().stream()
                                .map(this::toCartItemDTO)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    public  CartItemDTO toCartItemDTO(CartItem item) {
        if (item == null) return null;

        return CartItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .quantity(item.getQuantity())
                .priceAtAddTime(item.getPriceAtAddTime())
                .addedAt(item.getAddedAt())
                .updatedAt(item.getUpdatedAt())
                .build();

    }

}