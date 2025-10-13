package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
