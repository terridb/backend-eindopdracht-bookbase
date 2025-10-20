package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
import com.terrideboer.bookbase.dtos.books.BookPatchDto;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.BookMapper;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> dtoBooks = new ArrayList<>();

        for (Book book : books) {
            dtoBooks.add(BookMapper.toDto(book));
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

    public BookDto putBook(Long id, BookInputDto bookInputDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + id + " not found")));

        Book updatedBook = BookMapper.toEntity(bookInputDto, existingBook);
        Book savedBook = bookRepository.save(updatedBook);
        return BookMapper.toDto(savedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + id + " not found")));

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
            if (bookPatchDto.isbn.matches("^[0-9]{10,13}$")) {
                throw new InvalidInputException("ISBN must contain 10 to 13 digits");
            }
            existingBook.setIsbn(bookPatchDto.isbn.trim());
        }

//        Todo imageurl
        if (bookPatchDto.imageUrl != null) {
            existingBook.setImageUrl(bookPatchDto.imageUrl);
        }

//        Todo validatie enum
        if (bookPatchDto.genre != null) {
            existingBook.setGenre(bookPatchDto.genre);
        }

        Book savedBook = bookRepository.save(existingBook);
        return BookMapper.toDto(savedBook);

    }

}
