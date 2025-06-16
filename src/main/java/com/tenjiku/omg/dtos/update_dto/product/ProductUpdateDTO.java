package com.tenjiku.omg.dtos.update_dto.product;

import com.tenjiku.omg.dtos.entry_dto.product.PriceDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO {

    @NotBlank(message = "Product name must not be empty")
    @Size(max = 255, message = "Product name must be at most 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @Size(max = 2048, message = "Image URL must be at most 2048 characters")
    @Pattern(
            regexp = "^(https?://).*$",
            message = "Image URL must be a valid URL starting with http or https"
    )
    private String imageUrl;

    @Min(value = 0, message = "Stock must be zero or positive")
    private int stock;

    private List<PriceDTO> prices;
}
