package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByDisplayNameContainingIgnoreCase(String displayName);
}
