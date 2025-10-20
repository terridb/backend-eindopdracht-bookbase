package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.dtos.books.BookInputDto;
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

}
