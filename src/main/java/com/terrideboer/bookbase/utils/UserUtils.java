package com.terrideboer.bookbase.utils;

import com.terrideboer.bookbase.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static boolean isOwnerOrAdmin(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth
                        .getAuthority().equals("ROLE_EMPLOYEE") || auth
                        .getAuthority().equals("ROLE_LIBRARIAN"));

        if (isAdmin) return true;

        return user.getEmail().equals(loggedInEmail);
    }
}
