package com.tenjiku.omg.service;

import com.tenjiku.omg.dtos.entry_dto.product.PriceDTO;
import com.tenjiku.omg.dtos.entry_dto.product.ProductDTO;
import com.tenjiku.omg.dtos.exit_dto.product.ProductResponseDTO;
import com.tenjiku.omg.dtos.update_dto.product.ProductUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ProductResponseDTO createProduct(String adminId, @Valid ProductDTO productDTO);

    ProductResponseDTO addStock(String id, int quantity);

    Page<ProductResponseDTO> getAllProductsPaginated(int page, int size);

    ProductResponseDTO getProductById(String id);

    ProductResponseDTO updateProduct(String id, ProductUpdateDTO productDTO);

    void deleteProduct(String id);

    ProductResponseDTO updateProductPrice(String productId, @Valid PriceDTO priceDTO);

    List<ProductResponseDTO> getPopularProducts();

    Page<ProductResponseDTO> getProductsByAdmin(String adminId, Pageable pageable);
}
