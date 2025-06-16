package com.tenjiku.omg.dtos.exit_dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceResponseDTO {
    private String id;
    private double amount;
    private LocalDateTime startDate;
}
