package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    boolean existsByTrackingNumber(String trackingNumber);

}
