package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyInputDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
import com.terrideboer.bookbase.dtos.books.BookPatchDto;
import com.terrideboer.bookbase.services.BookCopyService;
import com.terrideboer.bookbase.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookCopyService bookCopyService;

    public BookController(BookService bookService, BookCopyService bookCopyService) {
        this.bookService = bookService;
        this.bookCopyService = bookCopyService;
    }



//    todo add endpoint for all bookcopies for one book

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {

        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookDto> postBook(@Valid @RequestBody BookInputDto bookInputDto) {
        BookDto bookDto = bookService.postBook(bookInputDto);

        URI uri = URI.create("/books/" + bookDto.id);

        return ResponseEntity.created(uri).body(bookDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> putBook(@PathVariable Long id, @Valid @RequestBody BookInputDto bookInputDto) {
        BookDto bookDto = bookService.putBook(id, bookInputDto);

        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> patchBook(@PathVariable Long id, @RequestBody BookPatchDto bookPatchDto) {
        BookDto bookDto = bookService.patchBook(id, bookPatchDto);

        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/{id}/book-copies")
    public ResponseEntity<List<BookCopyDto>> getBookCopiesByBookId(@PathVariable Long id) {

        return ResponseEntity.ok(bookCopyService.getBookCopiesByBookId(id));
    }

    @PostMapping("/{id}/book-copies")
    public ResponseEntity<BookCopyDto> postBookCopy(@PathVariable Long id, @Valid @RequestBody BookCopyInputDto bookCopyInputDto) {
        BookCopyDto bookCopyDto = bookCopyService.postBookCopy(bookCopyInputDto, id);

        URI uri = URI.create("/book-copies/" + bookCopyDto.id);

        return ResponseEntity.created(uri).body(bookCopyDto);
    }
}
