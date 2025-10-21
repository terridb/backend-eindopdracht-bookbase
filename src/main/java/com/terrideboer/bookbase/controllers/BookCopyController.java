package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.services.BookCopyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book-copies")
public class BookCopyController {

    private final BookCopyService service;

    public BookCopyController(BookCopyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookCopyDto>> getAllBookCopies() {

        return ResponseEntity.ok(service.getAllBookCopies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookCopyDto> getBookCopyById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getBookCopyById(id));
    }


}
