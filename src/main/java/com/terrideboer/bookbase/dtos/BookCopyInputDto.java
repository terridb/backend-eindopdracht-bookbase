package com.terrideboer.bookbase.dtos;

import jakarta.validation.constraints.NotNull;

public class BookCopyInputDto {

    @NotNull(message = "Book id is required")
    public Long bookId;
}
