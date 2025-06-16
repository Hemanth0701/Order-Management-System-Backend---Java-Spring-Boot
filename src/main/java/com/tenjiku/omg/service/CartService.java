package com.tenjiku.omg.service;

import com.tenjiku.omg.dtos.exit_dto.cart.CartDTO;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    CartDTO viewCart(String userId);

    CartDTO addItemToCart(String userId, String productId, int quantity);

    CartDTO removeItemFromCart(String userId, String productId);
}
