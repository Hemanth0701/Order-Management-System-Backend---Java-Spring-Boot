package com.tenjiku.omg.controller;

import com.tenjiku.omg.dtos.entry_dto.product.PriceDTO;
import com.tenjiku.omg.dtos.entry_dto.product.ProductDTO;
import com.tenjiku.omg.dtos.entry_dto.product.StockUpdateRequest;
import com.tenjiku.omg.dtos.exit_dto.product.ProductResponseDTO;
import com.tenjiku.omg.dtos.update_dto.product.ProductUpdateDTO;
import com.tenjiku.omg.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    //  Create a new product
    @PostMapping(value = "/create/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@PathVariable String adminId, @Valid @RequestBody ProductDTO productDTO){

        ProductResponseDTO productResponseDTO=productService.createProduct(adminId,productDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(productResponseDTO.getName())
                .toUri();

        return ResponseEntity.created(location).body(productResponseDTO);// if there is no any other instance
    }

    //  Add stock to a product
    @PutMapping (value = "/add-stock/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> addStock(
            @PathVariable String id,
            @Valid @RequestBody StockUpdateRequest quantity) {

        ProductResponseDTO updatedProduct = productService.addStock(id, quantity.getQuantity());

        return ResponseEntity.ok(updatedProduct);
    }

    //  Get all products
    @GetMapping(value = "/all")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponseDTO> products = productService.getAllProductsPaginated(page, size);
        return ResponseEntity.ok(products);
    }

    //  Get Popular product
    @GetMapping(value = "/popular")
    public ResponseEntity<List<ProductResponseDTO>> getPopularProducts() {
        return ResponseEntity.ok(productService.getPopularProducts());
    }

    //  Get product by ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {

        ProductResponseDTO product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    //get product created by Admin
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByAdmin(
            @PathVariable String adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponseDTO> productPage = productService.getProductsByAdmin(adminId, pageable);
        return ResponseEntity.ok(productPage);
    }

    //  Update product
    @PutMapping(value = "/update/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable String productId,
             @RequestBody ProductUpdateDTO productDTO) {

        ProductResponseDTO updatedProduct = productService.updateProduct(productId, productDTO);

        return ResponseEntity.ok(updatedProduct);
    }

    // Update existing product price
    @PutMapping(value = "/updatePrice/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ProductResponseDTO> updateProductPrice(@PathVariable String productId, @Valid @RequestBody PriceDTO priceDTO){

        return ResponseEntity.ok(productService.updateProductPrice(productId,priceDTO));
    }

    //  Delete product
    @DeleteMapping(value = "/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }


}
