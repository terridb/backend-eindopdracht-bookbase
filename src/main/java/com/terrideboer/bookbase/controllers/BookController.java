package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {

        return ResponseEntity.ok(service.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getBookById(id));
    }
}
