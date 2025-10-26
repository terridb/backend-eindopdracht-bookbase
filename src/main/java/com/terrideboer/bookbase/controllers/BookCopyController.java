package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyInputDto;
import com.terrideboer.bookbase.services.BookCopyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<BookCopyDto> putBookCopy(@PathVariable Long id, @Valid @RequestBody BookCopyInputDto bookCopyInputDto) {
        BookCopyDto bookCopyDto = service.putBookCopy(id, bookCopyInputDto);

        return ResponseEntity.ok(bookCopyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
        service.deleteBookCopy(id);

        return ResponseEntity.noContent().build();
    }
}
