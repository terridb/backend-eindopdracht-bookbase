package com.terrideboer.bookbase.dtos.bookcopies;

import jakarta.validation.constraints.Pattern;

public class BookCopyInputDto {

    @Pattern(regexp = "^BB-[1-9]\\d*-[1-9]\\d*$", message = "Tracking number must follow the following format: BB-bookId-copyNr")
    public String trackingNumber;
}
