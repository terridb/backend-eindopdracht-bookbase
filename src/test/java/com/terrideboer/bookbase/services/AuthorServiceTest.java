package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.repositories.AuthorRepository;
import com.terrideboer.bookbase.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    AuthorService authorService;

    List<Author> authors;
    Author author1 = new Author();
    Author author2 = new Author();

    @BeforeEach
    void setUp() {
        author1.setId(1L);
        author1.setFirstName("Ali");
        author1.setLastName("Hazelwood");
        author1.setDisplayName("Ali Hazelwood");
        author1.setDateOfBirth(LocalDate.parse("1989-12-11"));

        author2.setId(2L);
        author2.setFirstName("Sarah");
        author2.setMiddleNames("Janet");
        author2.setLastName("Maas");
        author2.setDisplayName("Sarah J. Maas");
        author2.setDateOfBirth(LocalDate.parse("1986-03-05"));

        authors = List.of(author1, author2);
    }

    @Test
    @DisplayName("Get all authors should show all authors")
    public void getAllAuthorsShouldShowAllAuthors() {
        Mockito.when(authorRepository.findAll(Sort.by("id").ascending())).thenReturn(authors);

        List<AuthorDto> dtos = authorService.getAllAuthors();

        assertEquals(2, dtos.size());
        assertEquals("Ali Hazelwood", dtos.get(0).displayName);
        assertEquals("Sarah J. Maas", dtos.get(1).displayName);
    }

    @Test
    @DisplayName("Get author by Id should show correct author")
    public void getAuthorByIdShouldShowCorrectAuthor() {
        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.of(author1));

        AuthorDto authorDto = authorService.getAuthorById(author1.getId());

        assertEquals("Ali", authorDto.firstName);
        assertEquals("Hazelwood", authorDto.lastName);
        assertEquals("Ali Hazelwood", authorDto.displayName);
        assertEquals(LocalDate.parse("1989-12-11"), authorDto.dateOfBirth);
        assertEquals(1L, authorDto.id);
    }

    @Test
    @DisplayName("Get books from author should show all books from one author")
    public void getAllBooksFromAuthorShouldShowAllBooks() {
        List<Book> books;
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setTitle("Test Book 1");
        book1.setId(1L);
        book2.setTitle("Test Book 2");
        book2.setId(2L);
        books = List.of(book1, book2);
        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.of(author1));
        Mockito.when(bookRepository.findByAuthorsOrderByIdAsc(Set.of(author1))).thenReturn(books);

        List<BookDto> bookDtos = authorService.getAllBooksFromAuthor(author1.getId());

        assertEquals(2, bookDtos.size());
        assertEquals("Test Book 1", bookDtos.get(0).title);
        assertEquals("Test Book 2", bookDtos.get(1).title);
    }

    @Test
    @DisplayName("Post Author should correctly post a new author")
    public void postAuthorShouldPostAuthor() {
        AuthorInputDto inputDto = new AuthorInputDto();
        inputDto.firstName = "Tina";
        inputDto.middleNames = "Tommy Junior";
        inputDto.lastName = "Test";
        inputDto.displayName = null;
        inputDto.dateOfBirth = LocalDate.parse("2000-01-01");

        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setFirstName("Tina");
        savedAuthor.setMiddleNames("Tommy Junior");
        savedAuthor.setLastName("Test");
        savedAuthor.setDisplayName("Tina T. J. Test");
        savedAuthor.setDateOfBirth(LocalDate.parse("2000-01-01"));

        Mockito.when(authorRepository.save(any())).thenReturn(savedAuthor);

        AuthorDto authorDto = authorService.postAuthor(inputDto);

        assertEquals(savedAuthor.getId(), authorDto.id);
        assertEquals(savedAuthor.getFirstName(), authorDto.firstName);
        assertEquals(savedAuthor.getMiddleNames(), authorDto.middleNames);
        assertEquals(savedAuthor.getLastName(), authorDto.lastName);
        assertEquals(savedAuthor.getDisplayName(), authorDto.displayName);
        assertEquals(savedAuthor.getDateOfBirth(), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("Put Author should correctly update an author")
    public void putAuthorShouldUpdateExistingAuthor() {
        AuthorInputDto inputDto = new AuthorInputDto();
        inputDto.firstName = "Alicia";
        inputDto.middleNames = "Test";
        inputDto.lastName = "Hazelwoods";
        inputDto.displayName = null;
        inputDto.dateOfBirth = author1.getDateOfBirth();

        Author savedAuthor = new Author();
        savedAuthor.setId(author1.getId());
        savedAuthor.setFirstName("Alicia");
        savedAuthor.setMiddleNames("Test");
        savedAuthor.setLastName("Hazelwoods");
        savedAuthor.setDisplayName("Alicia T. Hazelwoods");
        savedAuthor.setDateOfBirth(author1.getDateOfBirth());

        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.of(author1));
        Mockito.when(authorRepository.save(any())).thenReturn(savedAuthor);

        AuthorDto authorDto = authorService.putAuthor(author1.getId(), inputDto);

        assertEquals(savedAuthor.getId(), authorDto.id);
        assertEquals("Alicia", authorDto.firstName);
        assertEquals("Test", authorDto.middleNames);
        assertEquals("Hazelwoods", authorDto.lastName);
        assertEquals("Alicia T. Hazelwoods", authorDto.displayName);
        assertEquals(savedAuthor.getDateOfBirth(), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("Delete Author should correctly delete an author")
    public void deleteAuthorShouldDeleteAuthor() {
        Book book1 = new Book();
        Book book2 = new Book();

        book1.setAuthors(new HashSet<>(Set.of(author1)));
        book2.setAuthors(new HashSet<>(Set.of(author2)));

        author1.setBooks(new HashSet<>(Set.of(book1, book2)));
        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.of(author1));

        authorService.deleteAuthor(author1.getId());

        assertFalse(book1.getAuthors().contains(author1));
        assertFalse(book2.getAuthors().contains(author1));
        verify(authorRepository).delete(author1);
    }
}