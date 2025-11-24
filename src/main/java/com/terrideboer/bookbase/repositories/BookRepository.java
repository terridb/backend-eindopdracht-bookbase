package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorsOrderByIdAsc(Set<Author> authors);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByIsbnContainingIgnoreCase(String isbn);

    List<Book> findByAuthorsDisplayNameContainingIgnoreCase(String name);
}
