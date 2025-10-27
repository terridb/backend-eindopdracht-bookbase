package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.models.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    boolean existsByTrackingNumber(String trackingNumber);

    List<BookCopy> findByBook(Book book);
}
