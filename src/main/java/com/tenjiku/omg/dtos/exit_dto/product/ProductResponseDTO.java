package com.tenjiku.omg.dtos.exit_dto.product;

import com.tenjiku.omg.entity.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

        private String id;

        private String name;

        private String description;

        private String imageUrl;

        private int stock;

        private String createdBy;

        private LocalDateTime createdAt;

        private Type category;

        private double currentPrice;


}
//private List<PriceResponseDTO> prices;