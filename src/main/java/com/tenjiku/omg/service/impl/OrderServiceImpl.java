package com.tenjiku.omg.service.impl;

import com.tenjiku.omg.dtos.exit_dto.order.OrderDTO;
import com.tenjiku.omg.entity.*;
import com.tenjiku.omg.entity.enums.OrderStatus;
import com.tenjiku.omg.exception.*;
import com.tenjiku.omg.mapper.OrderMapper;
import com.tenjiku.omg.repositroy.CartRepo;
import com.tenjiku.omg.repositroy.OrderRepository;
import com.tenjiku.omg.repositroy.ProductRepo;
import com.tenjiku.omg.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional // applies to all methods
public class OrderServiceImpl implements OrderService {

    private final CartRepo cartRepository;
    private final ProductRepo productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO checkout(String cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Step 1: Validate and Lock Stock
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            product.setPurchaseCount(product.getPurchaseCount() + item.getQuantity());

            productRepository.save(product);
        }

        // Step 2: Create Order and Items
        Order order = Order.builder()
                .user(cart.getUser())
                .status(OrderStatus.CREATED)
                .totalAmount(cart.getTotalPrice())
                .discount(cart.getDiscountApplied())
                .finalAmount(cart.getFinalPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .priceAtPurchase(cartItem.getProduct().getCurrentPrice().getAmount())
                        .total(cartItem.getItemTotal())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Step 3: Mark cart as checked out
        try {

            cart.getItems().clear();
            cart.calculateTotals();
            Cart savedCart = cartRepository.saveAndFlush(cart);

            cartRepository.findById(cartId)
                    .ifPresent(c -> System.out.println("Status from DB: " + c.getCurrentstatus()));

        } catch (Exception e) {
            e.printStackTrace(); // Proper logging
        }

        return orderMapper.toDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return orderMapper.toDTO(order);
    }

    @Override
    public String cancelOrder(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Order cannot be cancelled");
        }

        // Restore stock for each item
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setStock(product.getStock() + item.getQuantity());
            product.setPurchaseCount(product.getPurchaseCount() - item.getQuantity());

            productRepository.save(product);

        }

        // Update order status
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return "Order cancelled successfully "+orderId;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getUserOrdersPaged(String userId, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);
        return ordersPage.map(orderMapper::toDTO);
    }

}


