package com.tenjiku.omg.service.impl;

import com.tenjiku.omg.dtos.entry_dto.product.PriceDTO;
import com.tenjiku.omg.dtos.entry_dto.product.ProductDTO;
import com.tenjiku.omg.dtos.exit_dto.product.ProductResponseDTO;
import com.tenjiku.omg.dtos.update_dto.product.ProductUpdateDTO;
import com.tenjiku.omg.entity.Admin;
import com.tenjiku.omg.entity.Price;
import com.tenjiku.omg.entity.Product;
import com.tenjiku.omg.entity.UserDetails;
import com.tenjiku.omg.entity.enums.Role;
import com.tenjiku.omg.exception.*;
import com.tenjiku.omg.mapper.ProductMapper;
import com.tenjiku.omg.repositroy.ProductRepo;
import com.tenjiku.omg.repositroy.UserRepo;
import com.tenjiku.omg.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional // applies to all methods
public class ProductServiceImpl implements ProductService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(String adminId, ProductDTO productDTO) {

        Admin admin = getAdminOrThrow(adminId);

        Product product = productMapper.toEntity(productDTO);

        product.setCreatedBy(admin.getId());

        Product savedProduct = productRepo.save(product);

        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO addStock(String id, int quantity) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity must be positive");
        }

        product.setStock(product.getStock() + quantity);
        Product savedProduct = productRepo.save(product);

        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProductsPaginated(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> productPage = productRepo.findAllActive(pageable);

        return productPage.map(productMapper::toDTO);
    }

    @Override
   // @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(String id) {
        return productMapper.toDTO(productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("Product not found")));
    }

    @Override
    public ProductResponseDTO updateProduct(String id, ProductUpdateDTO productDTO) {

        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        // Update fields
        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getImageUrl() != null) {
            existingProduct.setImageUrl(productDTO.getImageUrl());
        }
        existingProduct.setStock(productDTO.getStock());

        // Handle prices (optional logic — here, replacing all existing)
        if (productDTO.getPrices() != null) {
            //existingProduct.getPrices().clear();
            for (PriceDTO priceDTO : productDTO.getPrices()) {
                Price price = new Price();
                price.setAmount(priceDTO.getAmount());
                price.setStartDate(priceDTO.getStartDate());
                price.setProduct(existingProduct);
                existingProduct.getPrices().add(price);
            }
        }

        Product savedProduct = productRepo.save(existingProduct);

        return productMapper.toDTO(savedProduct);
    }

    @Override
    public void deleteProduct(String id) {
        
        Product product =productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        if (product.isDeleted()) {
            throw new ProductAlreadyDeletedException("Product is already deleted.");

        }

        product.setDeleted(true);
        product.setDeletedAt(Instant.now());

        productRepo.save(product);
    }

    @Override
    public ProductResponseDTO updateProductPrice(String productId, PriceDTO priceDTO) {

        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        List<Price> existingPrices = existingProduct.getPrices();

        Price newPrice = productMapper.toEntity(priceDTO);

        // Fix: Set startDate to now if null to avoid DB NOT NULL constraint error
        if (newPrice.getStartDate() == null) {
            newPrice.setStartDate(LocalDateTime.now());
        }

        // Check for duplicate based on amount and startDate value
        boolean exists = existingPrices.stream()
                .anyMatch(price ->
                        price.getAmount() == newPrice.getAmount() &&
                                price.getStartDate().equals(newPrice.getStartDate())
                );

        if (!exists) {
            newPrice.setProduct(existingProduct); // maintain bidirectional relation
            existingPrices.add(newPrice);

            Product savedProduct = productRepo.save(existingProduct);

            return productMapper.toDTO(savedProduct);
        } else {
            throw new PriceAlreadyExistException("The price with the same amount and start date already exists.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getPopularProducts() {

        List<ProductResponseDTO> dtoList = new ArrayList<>();

        for (Product product : productRepo.findTop10ByOrderByPurchaseCountDesc()) {
            dtoList.add(productMapper.toDTO(product));
        }

        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByAdmin(String adminId, Pageable pageable) {
        return productRepo.findByCreatedByAndNotDeleted(adminId, pageable)
                .map(productMapper::toDTO); // ✅ productMapper::toDTO must accept Product and return ProductResponseDTO

    }

    private Admin getAdminOrThrow(String adminId){

        UserDetails user = userRepo.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found with Id: " + adminId));

        if (user.getRole() != Role.ADMIN) {
            throw new PermissionDeniedException("User does not have admin privileges");
        }
        return (Admin) user;
    }
}