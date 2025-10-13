package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {
}
