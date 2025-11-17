package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
