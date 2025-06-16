package com.tenjiku.omg.controller;

import com.tenjiku.omg.dtos.exit_dto.cart.CartDTO;
import com.tenjiku.omg.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @GetMapping(value = "/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }

    @PostMapping(value = "/{userId}/addProduct")
    public ResponseEntity<CartDTO> addItem(@PathVariable String userId,
                                        @RequestParam String productId,
                                        @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, quantity));
    }

    @DeleteMapping(value = "/{userId}/removeProduct")
    public ResponseEntity<CartDTO> removeItem(@PathVariable String userId,
                                           @RequestParam String productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, productId));
    }
}
