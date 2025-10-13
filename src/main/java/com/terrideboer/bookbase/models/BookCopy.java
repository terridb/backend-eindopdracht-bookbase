package com.terrideboer.bookbase.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "book_copies")
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer trackingNumber;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToMany(mappedBy = "bookCopy")
    private List<Loan> loans;

    @OneToMany(mappedBy = "bookCopy")
    private List<Reservation> reservations;

}
