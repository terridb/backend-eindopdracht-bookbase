package com.terrideboer.bookbase.models;

import com.terrideboer.bookbase.models.enums.Genre;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isnb;

//    todo coverImage
//    todo author

    @Enumerated(EnumType.STRING)
    private Genre genre;
}
