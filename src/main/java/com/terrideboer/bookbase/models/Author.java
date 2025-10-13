package com.terrideboer.bookbase.models;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String middleNames;
    private String lastName;
    private LocalDate dateOfBirth;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;
}
