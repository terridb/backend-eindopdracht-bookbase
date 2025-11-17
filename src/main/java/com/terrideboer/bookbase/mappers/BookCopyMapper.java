package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyInputDto;
import com.terrideboer.bookbase.models.BookCopy;

public class BookCopyMapper {

    public static BookCopy toEntity(BookCopyInputDto bookCopyInputDto, BookCopy bookCopy) {
        if (bookCopy == null) {
            bookCopy = new BookCopy();
        }

        bookCopy.setTrackingNumber(bookCopyInputDto.trackingNumber);

        return bookCopy;
    }

    public static BookCopyDto toDto(BookCopy bookCopy) {
        BookCopyDto bookCopyDto = new BookCopyDto();

        bookCopyDto.id = bookCopy.getId();
        bookCopyDto.trackingNumber = bookCopy.getTrackingNumber();

        if (bookCopy.getBook() != null) {
            bookCopyDto.book = BookMapper.toSummaryDto(bookCopy.getBook());
        }

        return bookCopyDto;
    }
}
