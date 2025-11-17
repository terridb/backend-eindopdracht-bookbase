package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.authors.AuthorSummaryDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
import com.terrideboer.bookbase.dtos.books.BookSummaryDto;
import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.models.Book;

import java.util.HashSet;
import java.util.Set;

public class BookMapper {

    public static Book toEntity(BookInputDto bookInputDto, Book book) {
        if (book == null) {
            book = new Book();
        }

        book.setTitle(bookInputDto.title);
        book.setIsbn(bookInputDto.isbn);
        book.setGenre(bookInputDto.genre);

        return book;
    }

    //    todo duplicate code
    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.id = book.getId();
        bookDto.title = book.getTitle();
        bookDto.isbn = book.getIsbn();

        if (book.getImageUrl() != null) {
            bookDto.imageUrl = "/uploads/" + book.getImageUrl();
        } else {
            bookDto.imageUrl = book.getImageUrl();
        }

        bookDto.genre = book.getGenre();

        if (book.getAuthors() != null) {
            Set<AuthorSummaryDto> authorSummaryDtos = new HashSet<>();

            for (Author author : book.getAuthors()) {
                AuthorSummaryDto authorSummaryDto = new AuthorSummaryDto();
                authorSummaryDto.id = author.getId();
                authorSummaryDto.displayName = author.getDisplayName();
                authorSummaryDtos.add(authorSummaryDto);
            }

            bookDto.authors = authorSummaryDtos;
        }

        return bookDto;
    }

    public static BookSummaryDto toSummaryDto(Book book) {
        BookSummaryDto bookSummaryDto = new BookSummaryDto();

        bookSummaryDto.id = book.getId();
        bookSummaryDto.title = book.getTitle();
        bookSummaryDto.isbn = book.getIsbn();
        bookSummaryDto.genre = book.getGenre();

        if (book.getAuthors() != null) {
            Set<AuthorSummaryDto> authorSummaryDtos = new HashSet<>();

            for (Author author : book.getAuthors()) {
                AuthorSummaryDto authorSummaryDto = new AuthorSummaryDto();
                authorSummaryDto.id = author.getId();
                authorSummaryDto.displayName = author.getDisplayName();
                authorSummaryDtos.add(authorSummaryDto);
            }

            bookSummaryDto.authors = authorSummaryDtos;
        }

        return bookSummaryDto;
    }
}
