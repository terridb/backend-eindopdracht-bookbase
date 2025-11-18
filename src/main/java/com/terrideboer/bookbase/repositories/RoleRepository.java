package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleNameIgnoreCase(String roleName);
}
