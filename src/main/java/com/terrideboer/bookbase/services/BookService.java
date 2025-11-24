package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
import com.terrideboer.bookbase.dtos.books.BookPatchDto;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.BookCopyMapper;
import com.terrideboer.bookbase.mappers.BookMapper;
import com.terrideboer.bookbase.models.Author;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.models.BookCopy;
import com.terrideboer.bookbase.repositories.AuthorRepository;
import com.terrideboer.bookbase.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookCopyService bookCopyService;

    private final Path fileStoragePath;

    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            BookCopyService bookCopyService,
            @Value("${my.upload_location}") String fileStorageLocation
    ) throws IOException {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookCopyService = bookCopyService;
        this.fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStoragePath);
    }

    public List<BookDto> getAllBooks(String search) {
        List<BookDto> dtoBooks = new ArrayList<>();

        if (search == null || search.isBlank()) {
            List<Book> books = bookRepository.findAll(Sort.by("id").ascending());

            for (Book book : books) {
                dtoBooks.add(BookMapper.toDto(book));
            }

        } else {
            Set<Book> books = new HashSet<>();
            books.addAll(bookRepository.findByTitleContainingIgnoreCase(search.trim()));
            books.addAll(bookRepository.findByIsbnContainingIgnoreCase(search.trim()));
            books.addAll(bookRepository.findByAuthorsDisplayNameContainingIgnoreCase(search.trim()));

            for (Book book : books) {
                dtoBooks.add(BookMapper.toDto(book));
            }

        }

        return dtoBooks;
    }

    public BookDto getBookById(Long id) {
        return BookMapper.toDto(
                bookRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Book with id " + id + " not found")));
    }

    public BookDto postBook(BookInputDto bookInputDto) {
        Book book = BookMapper.toEntity(bookInputDto, null);

        Book savedBook = bookRepository.save(book);

        return BookMapper.toDto(savedBook);
    }

    public BookDto uploadImageToBook(Long id, MultipartFile file) throws IOException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + id + " not found")));

        if (existingBook.getImageUrl() != null) {
            Path oldFilePath = fileStoragePath.resolve(existingBook.getImageUrl());
            Files.delete(oldFilePath);
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = fileStoragePath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        existingBook.setImageUrl(fileName);
        Book savedBook = bookRepository.save(existingBook);

        return BookMapper.toDto(savedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + id + " not found")));

        if (book.getBookCopies() != null && !book.getBookCopies().isEmpty()) {
            for (BookCopy bookCopy : book.getBookCopies()) {
                bookCopyService.deleteBookCopy(bookCopy.getId());
            }
        }

        for (Author author : book.getAuthors()) {
            author.getBooks().remove(book);
            authorRepository.save(author);
        }
        book.getAuthors().clear();

        bookRepository.delete(book);
    }

    public BookDto patchBook(Long id, BookPatchDto bookPatchDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + id + " not found")));

        if (bookPatchDto.title != null) {
            if (bookPatchDto.title.length() < 2 || bookPatchDto.title.length() > 150) {
                throw new InvalidInputException("Title must be between 2 and 150 characters");
            }
            existingBook.setTitle(bookPatchDto.title);
        }

        if (bookPatchDto.isbn != null) {
            if (!bookPatchDto.isbn.matches("^[0-9]{10,13}$")) {
                throw new InvalidInputException("ISBN must contain 10 to 13 digits");
            }
            existingBook.setIsbn(bookPatchDto.isbn.trim());
        }

        if (bookPatchDto.genre != null) {
            existingBook.setGenre(bookPatchDto.genre);
        }

        Book savedBook = bookRepository.save(existingBook);
        return BookMapper.toDto(savedBook);
    }

    public BookDto assignAuthorToBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + bookId + " not found")));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RecordNotFoundException(("Author with id " + authorId + " not found")));

        book.getAuthors().add(author);
        author.getBooks().add(book);

        Book savedBook = bookRepository.save(book);

        return BookMapper.toDto(savedBook);
    }

    public List<BookCopyDto> getBookCopiesByBookId(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + id + " not found")));

        List<BookCopy> bookCopies = book.getBookCopies();
        List<BookCopyDto> dtoBookCopies = new ArrayList<>();

        for (BookCopy bookCopy : bookCopies) {
            dtoBookCopies.add(BookCopyMapper.toDto(bookCopy));
        }

        return dtoBookCopies;
    }

}
