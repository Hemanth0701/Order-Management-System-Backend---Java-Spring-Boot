package com.tenjiku.omg.repositroy;

import com.tenjiku.omg.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    // Override default to include isDeleted = false
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    Page<Product> findAllActive(Pageable pageable);

    // Filter by admin and exclude soft-deleted products
    @Query("SELECT p FROM Product p WHERE p.createdBy = :adminId AND p.isDeleted = false")
    Page<Product> findByCreatedByAndNotDeleted( String adminId, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.prices")
    List<Product> findAllWithPrices();

    List<Product> findTop10ByOrderByPurchaseCountDesc();

}
