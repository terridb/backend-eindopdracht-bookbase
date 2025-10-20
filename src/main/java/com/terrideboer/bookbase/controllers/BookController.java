package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
import com.terrideboer.bookbase.dtos.books.BookPatchDto;
import com.terrideboer.bookbase.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<BookDto> postBook(@Valid @RequestBody BookInputDto bookInputDto) {
        BookDto bookDto = service.postBook(bookInputDto);

        URI uri = URI.create("/books/" + bookDto.id);

        return ResponseEntity.created(uri).body(bookDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> putBook(@PathVariable Long id, @Valid @RequestBody BookInputDto bookInputDto) {
        BookDto bookDto = service.putBook(id, bookInputDto);

        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        service.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> patchBook(@PathVariable Long id, @RequestBody BookPatchDto bookPatchDto) {
        BookDto bookDto = service.patchBook(id, bookPatchDto);

        return ResponseEntity.ok(bookDto);
    }
}
