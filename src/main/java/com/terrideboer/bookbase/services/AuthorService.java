package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.AuthorMapper;
import com.terrideboer.bookbase.mappers.BookMapper;
import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.repositories.AuthorRepository;
import com.terrideboer.bookbase.repositories.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll(Sort.by("id").ascending());
        List<AuthorDto> dtoAuthors = new ArrayList<>();

        for (Author author : authors) {
            dtoAuthors.add(AuthorMapper.toDto(author));
        }

        return dtoAuthors;
    }

    public AuthorDto getAuthorById(Long id) {
        return AuthorMapper.toDto(
                authorRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Author with id " + id + " not found")));
    }

    public List<BookDto> getAllBooksFromAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Author with id " + id + " not found"));

        List<Book> books = bookRepository.findByAuthorsOrderByIdAsc(Set.of(author));
        List<BookDto> dtoBooks = new ArrayList<>();

        for (Book book : books) {
            dtoBooks.add(BookMapper.toDto(book));
        }

        return dtoBooks;
    }

    public AuthorDto postAuthor(AuthorInputDto authorInputDto) {
        Author author = AuthorMapper.toEntity(authorInputDto, null);

        if (authorInputDto.displayName == null) {
            author.setDisplayName(buildDisplayName(author));
        }

        Author savedAuthor = authorRepository.save(author);
        return AuthorMapper.toDto(savedAuthor);
    }

    public AuthorDto updateAuthor(Long id, AuthorInputDto authorInputDto) {
        Author existingAuthor =  authorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Author with id " + id + " not found"));

        Author updatedAuthor = AuthorMapper.toEntity(authorInputDto, existingAuthor);

        if (authorInputDto.displayName == null) {
            updatedAuthor.setDisplayName(buildDisplayName(updatedAuthor));
        }

        Author savedAuthor = authorRepository.save(updatedAuthor);
        return AuthorMapper.toDto(savedAuthor);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Author with id " + id + " not found")));

            for (Book book : author.getBooks()) {
                book.getAuthors().remove(author);
                bookRepository.save(book);
            }
            author.getBooks().clear();

        authorRepository.delete(author);
    }

    private String buildDisplayName(Author author) {
        String displayName = author.getFirstName();

        if (author.getMiddleNames() != null) {
            String[] namesArray = author.getMiddleNames().trim().split(" ");
            StringBuilder initials = new StringBuilder();

            for (String name : namesArray) {
                initials.append(name.charAt(0)).append(".");
            }

            displayName += " " + initials;
        }

        displayName += " " + author.getLastName();

        return displayName;
    }

}
