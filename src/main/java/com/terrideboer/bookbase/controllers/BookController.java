package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.IdInputDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyInputDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
import com.terrideboer.bookbase.dtos.books.BookPatchDto;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.services.BookCopyService;
import com.terrideboer.bookbase.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    //       Endpoint to get all books
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {

        return ResponseEntity.ok(bookService.getAllBooks());
    }

    //       Endpoint to get a book by book-id
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    //       Endpoint to create a book
    @PostMapping
    public ResponseEntity<BookDto> postBook(@Valid @RequestBody BookInputDto bookInputDto) {
        BookDto bookDto = bookService.postBook(bookInputDto);

        URI uri = URI.create("/books/" + bookDto.id);

        return ResponseEntity.created(uri).body(bookDto);
    }

    //    Endpoint to upload a book-cover (image) to a book
    @PatchMapping("/{id}/image")
    public ResponseEntity<BookDto> uploadImageToBook(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        BookDto bookDto = bookService.uploadImageToBook(id, file);
        return ResponseEntity.ok(bookDto);
    }

    //       Endpoint to adjust a book by book-id (put)
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> putBook(@PathVariable Long id, @Valid @RequestBody BookInputDto bookInputDto) {
        BookDto bookDto = bookService.putBook(id, bookInputDto);

        return ResponseEntity.ok(bookDto);
    }

    //       Endpoint to delete a book by book-id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    //       Endpoint to adjust a book by book-id (patch)
    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> patchBook(@PathVariable Long id, @RequestBody BookPatchDto bookPatchDto) {
        BookDto bookDto = bookService.patchBook(id, bookPatchDto);

        return ResponseEntity.ok(bookDto);
    }

    //       Endpoint to get all book-copies from one book by book-id
    @GetMapping("/{id}/book-copies")
    public ResponseEntity<List<BookCopyDto>> getBookCopiesByBookId(@PathVariable Long id) {

        return ResponseEntity.ok(bookCopyService.getBookCopiesByBookId(id));
    }

    //       Endpoint to create a new book-copy by book-id
    @PostMapping("/{id}/book-copies")
    public ResponseEntity<BookCopyDto> postBookCopy(@PathVariable Long id, @Valid @RequestBody BookCopyInputDto bookCopyInputDto) {
        BookCopyDto bookCopyDto = bookCopyService.postBookCopy(bookCopyInputDto, id);

        URI uri = URI.create("/book-copies/" + bookCopyDto.id);

        return ResponseEntity.created(uri).body(bookCopyDto);
    }

    //    Endpoint to assign an author to a book
    @PatchMapping("/{id}/author")
    public ResponseEntity<BookDto> assignAuthorToBook(@PathVariable Long id, @RequestBody IdInputDto authorId) {

        BookDto bookDto = bookService.assignAuthorToBook(id, authorId.id);
        return ResponseEntity.ok(bookDto);
    }
}
