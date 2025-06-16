package com.tenjiku.omg.repositroy;

import com.tenjiku.omg.entity.Order;
import com.tenjiku.omg.entity.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByUserId(String userId, Pageable pageable);
}
