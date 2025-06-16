package com.tenjiku.omg.service.impl;

import com.tenjiku.omg.dtos.exit_dto.cart.CartDTO;
import com.tenjiku.omg.entity.*;
import com.tenjiku.omg.entity.enums.Role;
import com.tenjiku.omg.entity.enums.Status;
import com.tenjiku.omg.exception.PermissionDeniedException;
import com.tenjiku.omg.exception.ProductNotFoundException;
import com.tenjiku.omg.exception.UserNotFoundException;
import com.tenjiku.omg.mapper.CartMapper;
import com.tenjiku.omg.repositroy.*;
import com.tenjiku.omg.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional // applies to all methods
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepository;
    private final UserRepo userRepo;
    private final ProductRepo productRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartDTO viewCart(String userId) {
        return cartMapper.toDTO(getOrCreateCart(userId));  // Use your mapper here to return DTO
    }

   @Override
    public CartDTO addItemToCart(String userId, String productId, int quantity) {

        // 1. Get or create cart for user
        Cart cart = getOrCreateCart(userId);

        // 2. Ensure items list is initialized
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        // 3. Fetch product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));


        // 4. Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct() != null &&
                        item.getProduct().getId() != null &&
                        Objects.equals(item.getProduct().getId(), productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // 5. Update existing cart item
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPriceAtAddTime(product.getCurrentPrice().getAmount());
            item.setUpdatedAt(LocalDateTime.now());
        } else {
            // 6. Add new cart item
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .priceAtAddTime(product.getCurrentPrice().getAmount())
                    .addedAt(LocalDateTime.now())
                    .build();
            cart.getItems().add(newItem);
        }

        // 7. Recalculate total
        cart.calculateTotals();

        // 8. Save cart
        Cart savedCart = cartRepository.save(cart);

        // 9. Return DTO
        return cartMapper.toDTO(savedCart);
    }

    @Override
    public CartDTO removeItemFromCart(String userId, String productId) {

        Cart cart = getOrCreateCart(userId);

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        cart.calculateTotals();

        return cartMapper.toDTO( cartRepository.save(cart));
    }

    private Cart getOrCreateCart(String userId){

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() !=Role.USER) {
            throw new PermissionDeniedException("Admin doesn't have a cart");
        }
        return  cartRepository.findByUserAndCurrentstatus((User) user, Status.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser((User) user);
                    newCart.setCurrentstatus(Status.ACTIVE);
                    return cartRepository.save(newCart);
                });

    }

}
