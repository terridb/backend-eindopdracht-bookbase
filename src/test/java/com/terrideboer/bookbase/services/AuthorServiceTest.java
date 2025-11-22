package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.authors.AuthorDto;
import com.terrideboer.bookbase.dtos.authors.AuthorInputDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
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

    Author author1 = new Author();
    Author author2 = new Author();

    Author tina = new Author();
    Author savedTina = new Author();
    AuthorInputDto tinaDto = new AuthorInputDto();

    List<Author> authors;

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

        tina.setId(5L);
        tina.setFirstName("OldName");
        tina.setMiddleNames("Tommy Junior");
        tina.setLastName("Test");
        tina.setDateOfBirth(LocalDate.parse("2000-01-01"));

        tinaDto.firstName = "Tina";
        tinaDto.middleNames = "Tommy Junior";
        tinaDto.lastName = "Test";
        tinaDto.dateOfBirth = LocalDate.parse("2000-01-01");

        savedTina.setId(5L);
        savedTina.setFirstName("Tina");
        savedTina.setMiddleNames("Tommy Junior");
        savedTina.setLastName("Test");
        savedTina.setDateOfBirth(LocalDate.parse("2000-01-01"));
    }

    @Test
    @DisplayName("getAllAuthors should show all existing authors")
    public void getAllAuthorsShouldShowAllAuthors() {
        Mockito.when(authorRepository.findAll(Sort.by("id").ascending()))
                .thenReturn(authors);

        List<AuthorDto> dtos = authorService.getAllAuthors();

        assertEquals(2, dtos.size());
        assertEquals("Ali Hazelwood", dtos.get(0).displayName);
        assertEquals("Sarah J. Maas", dtos.get(1).displayName);
    }

    @Test
    @DisplayName("getAuthorById should return correct author")
    public void getAuthorByIdShouldShowCorrectAuthor() {
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));

        AuthorDto authorDto = authorService.getAuthorById(1L);

        assertEquals("Ali", authorDto.firstName);
        assertEquals("Hazelwood", authorDto.lastName);
        assertEquals("Ali Hazelwood", authorDto.displayName);
        assertEquals(LocalDate.parse("1989-12-11"), authorDto.dateOfBirth);
        assertEquals(1L, authorDto.id);
    }

    @Test
    @DisplayName("getAuthorById should throw exception when not found")
    public void getAuthorByIdShouldThrowNotFound() {
        Mockito.when(authorRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> authorService.getAuthorById(100L));
    }

    @Test
    @DisplayName("getAllBooksFromAuthor should return books for existing author")
    public void getAllBooksFromAuthorShouldShowAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Test Book 1");
        Book book2 = new Book();
        book2.setTitle("Test Book 2");

        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));
        Mockito.when(bookRepository.findByAuthorsOrderByIdAsc(Set.of(author1))).thenReturn(List.of(book1, book2));

        List<BookDto> bookDtos = authorService.getAllBooksFromAuthor(1L);

        assertEquals(2, bookDtos.size());
        assertEquals("Test Book 1", bookDtos.get(0).title);
        assertEquals("Test Book 2", bookDtos.get(1).title);
    }

    @Test
    @DisplayName("getAllBooksFromAuthor should throw exception when author is not found")
    public void getAllBooksFromAuthorShouldThrowNotFound() {
        Mockito.when(authorRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> authorService.getAllBooksFromAuthor(100L));
    }

    @Test
    @DisplayName("postAuthor should create a new author and generate a displayName when none is provided")
    public void postAuthorWithoutDisplayNameShouldPostAuthor() {
        tinaDto.displayName = null;
        savedTina.setDisplayName("Tina T. J. Test");

        Mockito.when(authorRepository.save(any())).thenReturn(savedTina);

        AuthorDto authorDto = authorService.postAuthor(tinaDto);

        assertEquals(5L, authorDto.id);
        assertEquals("Tina", authorDto.firstName);
        assertEquals("Tommy Junior", authorDto.middleNames);
        assertEquals("Test", authorDto.lastName);
        assertEquals("Tina T. J. Test", authorDto.displayName);
        assertEquals(LocalDate.parse("2000-01-01"), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("postAuthor should create a new author and keep the provided displayName")
    public void postAuthorWithDisplayNameShouldPostAuthor() {
        tinaDto.displayName = "Tina J. Test";
        savedTina.setDisplayName("Tina J. Test");

        Mockito.when(authorRepository.save(any())).thenReturn(savedTina);

        AuthorDto authorDto = authorService.postAuthor(tinaDto);

        assertEquals(5L, authorDto.id);
        assertEquals("Tina", authorDto.firstName);
        assertEquals("Tommy Junior", authorDto.middleNames);
        assertEquals("Test", authorDto.lastName);
        assertEquals("Tina J. Test", authorDto.displayName);
        assertEquals(LocalDate.parse("2000-01-01"), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("postAuthor should create a new author and build displayName without middleNames")
    public void postAuthorShouldBuildDiplayNameWithoutMiddleNames() {
        tinaDto.middleNames = null;
        tinaDto.displayName = null;
        savedTina.setMiddleNames(null);
        savedTina.setDisplayName("Tina Test");

        Mockito.when(authorRepository.save(any())).thenReturn(savedTina);

        AuthorDto authorDto = authorService.postAuthor(tinaDto);

        assertEquals(5L, authorDto.id);
        assertEquals("Tina", authorDto.firstName);
        assertNull(authorDto.middleNames);
        assertEquals("Test", authorDto.lastName);
        assertEquals("Tina Test", authorDto.displayName);
        assertEquals(LocalDate.parse("2000-01-01"), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("updateAuthor should update author and generate a displayName when none is provided")
    public void updateAuthorWithoutDisplayNameShouldUpdateExistingAuthor() {
        tinaDto.displayName = null;
        savedTina.setDisplayName("Tina T. J. Test");

        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.of(tina));
        Mockito.when(authorRepository.save(any())).thenReturn(savedTina);

        AuthorDto authorDto = authorService.updateAuthor(5L, tinaDto);

        assertEquals(5L, authorDto.id);
        assertEquals("Tina", authorDto.firstName);
        assertEquals("Tommy Junior", authorDto.middleNames);
        assertEquals("Test", authorDto.lastName);
        assertEquals("Tina T. J. Test", authorDto.displayName);
        assertEquals(LocalDate.parse("2000-01-01"), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("updateAuthor should update author and keep the provided displayName")
    public void updateAuthorWithDisplayNameShouldUpdateExistingAuthor() {
        tinaDto.displayName = "Tina J. Test";
        savedTina.setDisplayName("Tina J. Test");

        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.of(tina));
        Mockito.when(authorRepository.save(any())).thenReturn(savedTina);

        AuthorDto authorDto = authorService.updateAuthor(5L, tinaDto);

        assertEquals(5L, authorDto.id);
        assertEquals("Tina", authorDto.firstName);
        assertEquals("Tommy Junior", authorDto.middleNames);
        assertEquals("Test", authorDto.lastName);
        assertEquals("Tina J. Test", authorDto.displayName);
        assertEquals(LocalDate.parse("2000-01-01"), authorDto.dateOfBirth);
    }

    @Test
    @DisplayName("updateAuthor should throw exception when author is not found")
    public void updateAuthorShouldThrowNotFoundException() {
        Mockito.when(authorRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> authorService.updateAuthor(100L, tinaDto));
    }

    @Test
    @DisplayName("deleteAuthor should delete an existing author")
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

    @Test
    @DisplayName("deleteAuthor should throw exception when author is not found")
    public void deleteAuthorShouldThrowNotFoundException() {
        Mockito.when(authorRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> authorService.deleteAuthor(100L));
    }
}