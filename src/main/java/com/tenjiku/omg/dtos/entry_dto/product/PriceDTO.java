package com.tenjiku.omg.dtos.entry_dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDTO {
    private double amount;
    private LocalDateTime startDate;
}
