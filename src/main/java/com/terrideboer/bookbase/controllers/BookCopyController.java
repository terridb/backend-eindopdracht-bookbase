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

    private final BookCopyService bookCopyService;

    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    //       Endpoint to get all book-copies
    @GetMapping
    public ResponseEntity<List<BookCopyDto>> getAllBookCopies() {

        return ResponseEntity.ok(bookCopyService.getAllBookCopies());
    }

    //       Endpoint to get a book-copy by id
    @GetMapping("/{id}")
    public ResponseEntity<BookCopyDto> getBookCopyById(@PathVariable Long id) {

        return ResponseEntity.ok(bookCopyService.getBookCopyById(id));
    }

    //       Endpoint to adjust a book-copy by id
    @PatchMapping("/{id}")
    public ResponseEntity<BookCopyDto> updateBookCopy(@PathVariable Long id, @Valid @RequestBody BookCopyInputDto bookCopyInputDto) {
        BookCopyDto bookCopyDto = bookCopyService.updateBookCopy(id, bookCopyInputDto);

        return ResponseEntity.ok(bookCopyDto);
    }

    //       Endpoint to delete a book-copy by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
        bookCopyService.deleteBookCopy(id);

        return ResponseEntity.noContent().build();
    }
}
