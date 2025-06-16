package com.tenjiku.omg.mapper;

import com.tenjiku.omg.dtos.entry_dto.product.PriceDTO;
import com.tenjiku.omg.dtos.entry_dto.product.ProductDTO;
import com.tenjiku.omg.dtos.exit_dto.product.PriceResponseDTO;
import com.tenjiku.omg.dtos.exit_dto.product.ProductResponseDTO;
import com.tenjiku.omg.entity.Price;
import com.tenjiku.omg.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // Entry: DTO → Entity
    public Product toEntity(ProductDTO dto) {

        if (dto == null) return null;

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .stock(dto.getStock())
                .category(dto.getCategory())
                .prices(new ArrayList<>())
                .build();

        if (dto.getPrices() != null) {

            List<Price> prices = new ArrayList<>();
            for (PriceDTO priceDTO : dto.getPrices()) {

                Price price = toPriceEntity(priceDTO, product);

                price.setProduct(product); // ensure bidirectional mapping
                prices.add(price);
            }

            if (product.getPrices().isEmpty())
                for(Price price:prices)
                    product.getPrices().add(price);

            // Set current price
            LocalDateTime now = LocalDateTime.now().withNano(0);


            Price current = null;
            for (Price price : prices) {
                if (!price.getStartDate().isAfter(now)) {
                    if (current == null || price.getStartDate().isAfter(current.getStartDate())) {
                        current = price;
                    }
                }
            }


            product.setCurrentPrice(current);

            current.setOwnerOfCurrentPrice(product);
        }

        return product;
    }


    // Exit: Entity → ResponseDTO
    public ProductResponseDTO toDTO(Product product) {
        if (product == null) return null;

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .stock(product.getStock())
                .createdBy(product.getCreatedBy())
                .createdAt(product.getCreatedAt())
                .category(product.getCategory())
                .currentPrice(product.getCurrentPrice() != null ? product.getCurrentPrice().getAmount() : 0.0)
                .build();
    }

    // Reusable Price DTO Mapper
    private List<PriceResponseDTO> toPriceDTOList(List<Price> prices) {
        if (prices == null) return null;

        return prices.stream()
                .map(price -> PriceResponseDTO.builder()
                        .id(price.getId())
                        .amount(price.getAmount())
                        .startDate(price.getStartDate())
                        .build())
                .collect(Collectors.toList());
    }

    public Price toEntity(PriceDTO priceDTO) {
        if (priceDTO == null) return null;
        return Price.builder()
                .amount(priceDTO.getAmount())
                .startDate(priceDTO.getStartDate())
                .build();
    }

    public Price toPriceEntity(PriceDTO dto, Product product) {

        if (dto == null) return null;

        return Price.builder()
                .amount(dto.getAmount())
                .startDate(dto.getStartDate())
                .product(product)
                .build();
    }

    public PriceDTO toPriceDTO(Price entity) {
        if (entity == null) return null;

        return PriceDTO.builder()
                .amount(entity.getAmount())
                .startDate(entity.getStartDate())
                .build();
    }
}
