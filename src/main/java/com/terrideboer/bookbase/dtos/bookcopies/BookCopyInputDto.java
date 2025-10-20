package com.terrideboer.bookbase.dtos.bookcopies;

import jakarta.validation.constraints.NotNull;

public class BookCopyInputDto {

    @NotNull(message = "Book id is required")
    public Long bookId;

    public String trackingNumber;
}
