package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    //       Endpoint to get all authors
    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(authorService.getAllAuthors(search));
    }

    //       Endpoint to get an author by author-id
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {

        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    //       Endpoint to get all books from one author by author-id
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDto>> getAllBooksFromAuthor(@PathVariable Long id) {

        return ResponseEntity.ok(authorService.getAllBooksFromAuthor(id));
    }

    //       Endpoint to create an author
    @PostMapping
    public ResponseEntity<AuthorDto> postAuthor(@Valid @RequestBody AuthorInputDto authorInputDto) {
        AuthorDto authorDto = authorService.postAuthor(authorInputDto);

        URI uri = URI.create("/authors/" + authorDto.id);

        return ResponseEntity.created(uri).body(authorDto);
    }

    //       Endpoint to adjust an author by author-id (put)
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorInputDto authorInputDto) {
        AuthorDto authorDto = authorService.updateAuthor(id, authorInputDto);

        return ResponseEntity.ok(authorDto);
    }

    //       Endpoint to delete an author by author-id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}
