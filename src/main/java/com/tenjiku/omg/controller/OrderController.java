package com.tenjiku.omg.controller;

import com.tenjiku.omg.dtos.exit_dto.order.OrderDTO;
import com.tenjiku.omg.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/checkout/{cartId}")
    public ResponseEntity<OrderDTO> checkout(@PathVariable String cartId) {
        return ResponseEntity.ok(orderService.checkout(cartId));
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<Page<OrderDTO>> getUserOrdersPaged(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok(orderService.getUserOrdersPaged(userId, page, size, sortBy, direction));
    }

    @GetMapping("/admin/allOrders")
    public ResponseEntity<Page<OrderDTO>> getOrdersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok(orderService.getOrdersPaged(page, size, sortBy, direction));
    }

    @PutMapping(value = "/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
