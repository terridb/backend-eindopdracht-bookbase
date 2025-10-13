package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.BookDto;
import com.terrideboer.bookbase.dtos.BookInputDto;
import com.terrideboer.bookbase.models.Book;

public class BookMapper {

    public static Book toEntity(BookInputDto bookInputDto, Book book) {
        if (book == null) {
            book = new Book();
        }

        book.setTitle(bookInputDto.title);
        book.setIsbn(bookInputDto.isbn);
        book.setImageUrl(bookInputDto.imageUrl);
        book.setGenre(bookInputDto.genre);

        return book;
    }

    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.id = book.getId();
        bookDto.title = book.getTitle();
        bookDto.isbn = book.getIsbn();
        bookDto.imageUrl = book.getImageUrl();
        bookDto.genre = book.getGenre();

//        if (book.getAuthors() != null) {
////           todo authormapper maken
//        }
//
//        if (book.getBookCopies() != null) {
////           todo bookcopyid mapper maken
//        }

        return bookDto;
    }
}
