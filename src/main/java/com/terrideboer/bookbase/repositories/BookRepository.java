package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
