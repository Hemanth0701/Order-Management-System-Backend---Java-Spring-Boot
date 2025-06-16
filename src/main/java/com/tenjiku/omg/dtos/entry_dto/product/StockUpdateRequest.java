package com.tenjiku.omg.dtos.entry_dto.product;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class StockUpdateRequest {
    @Min(1)
    private int quantity;
}
