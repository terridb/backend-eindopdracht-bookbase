package com.terrideboer.bookbase;

import com.terrideboer.bookbase.dtos.authors.AuthorSummaryDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.mappers.BookMapper;
import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.models.enums.Genre;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookMapperTests {

    @Test
    public void toDtoShouldAddAuthorSummaryDtos() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setDisplayName("Sarah J. Maas");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setDisplayName("Steven King");

        Set<Author> authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);

        Book book = new Book();
        book.setId(1L);
        book.setIsbn("9781635575569");
        book.setTitle("Boektitel");
        book.setGenre(Genre.FANTASY);
        book.setAuthors(authors);

        BookDto bookDto = BookMapper.toDto(book);

        assertEquals(2, bookDto.authors.size());

        boolean foundAuthor1 = false;
        boolean foundAuthor2 = false;

        for (AuthorSummaryDto author : bookDto.authors) {
            if (author.displayName.equals("Sarah J. Maas")) {
                foundAuthor1 = true;
            }
            if (author.displayName.equals("Steven King")) {
                foundAuthor2 = true;
            }
        }

        assertTrue(foundAuthor1);
        assertTrue(foundAuthor2);
    }
}
