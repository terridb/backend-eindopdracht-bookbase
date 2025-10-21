package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyInputDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.BookCopyMapper;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.models.BookCopy;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import com.terrideboer.bookbase.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    public List<BookCopyDto> getAllBookCopies() {
        List<BookCopy> bookCopies = bookCopyRepository.findAll();
        List<BookCopyDto> dtoBookCopies = new ArrayList<>();

        for (BookCopy bookCopy : bookCopies) {
            dtoBookCopies.add(BookCopyMapper.toDto(bookCopy));
        }

        return dtoBookCopies;
    }

    public BookCopyDto getBookCopyById(Long id) {
        return BookCopyMapper.toDto(
                bookCopyRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Book-copy with id " + id + " not found")));
    }

    public BookCopyDto postBookCopy(BookCopyInputDto bookCopyInputDto, Long bookId) {
        BookCopy bookCopy = BookCopyMapper.toEntity(bookCopyInputDto, null);
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException(("Book with id " + bookId + " not found")));

        bookCopy.setBook(existingBook);

        if (bookCopyInputDto.trackingNumber == null) {
            bookCopy.setTrackingNumber(buildTrackingNumber(bookCopy, bookId));
        }

        existingBook.getBookCopies().add(bookCopy);

        BookCopy savedBookCopy = bookCopyRepository.save(bookCopy);
        return BookCopyMapper.toDto(savedBookCopy);
    }

    private String buildTrackingNumber(BookCopy bookCopy, Long bookId) {
        int copyCode = bookCopy.getBook().getBookCopies().size() + 1;

        return "BB-" + bookId + "-" + copyCode;
    }

}
