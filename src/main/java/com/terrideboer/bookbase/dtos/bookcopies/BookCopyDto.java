package com.terrideboer.bookbase.dtos.bookcopies;

import com.terrideboer.bookbase.dtos.books.BookSummaryDto;

public class BookCopyDto {
    public Long id;
    public String trackingNumber;
    public BookSummaryDto book;
}
