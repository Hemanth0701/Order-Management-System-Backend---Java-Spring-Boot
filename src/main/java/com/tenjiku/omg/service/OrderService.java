package com.tenjiku.omg.service;

import com.tenjiku.omg.dtos.exit_dto.order.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderDTO checkout(String cartId);

    Page<OrderDTO> getUserOrdersPaged(String userId, int page, int size, String sortBy, String direction);

    OrderDTO getOrderById(String orderId);

    String cancelOrder(String orderId);

    Page<OrderDTO> getOrdersPaged(int page, int size, String sortBy, String direction);
}
