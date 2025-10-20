package com.terrideboer.bookbase.dtos;

import jakarta.validation.constraints.NotNull;

public class IdInputDto {
    @NotNull(message = "Id is required")
    public Long id;
}
