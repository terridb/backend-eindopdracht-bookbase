package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    //       Endpoint to get all authors
    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {

        return ResponseEntity.ok(service.getAllAuthors());
    }

    //       Endpoint to get an author by author-id
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getAuthorById(id));
    }

    //       Endpoint to create an author
    @PostMapping
    public ResponseEntity<AuthorDto> postAuthor(@Valid @RequestBody AuthorInputDto authorInputDto) {
        AuthorDto authorDto = service.postAuthor(authorInputDto);

        URI uri = URI.create("/authors/" + authorDto.id);

        return ResponseEntity.created(uri).body(authorDto);
    }

    //       Endpoint to adjust an author by author-id (put)
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> putAuthor(@PathVariable Long id, @Valid @RequestBody AuthorInputDto authorInputDto) {
        AuthorDto authorDto = service.putAuthor(id, authorInputDto);

        return ResponseEntity.ok(authorDto);
    }

    //       Endpoint to delete an author by author-id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        service.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }

//    todo get request alle boeken van 1 auteur

}
