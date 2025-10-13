package com.terrideboer.bookbase.models;

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
//    todo genre
}
