package com.terrideboer.bookbase.models;

import com.terrideboer.bookbase.models.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
//    todo password

    @Enumerated(EnumType.STRING)
    private Role role;

}
