package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.books.BookDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.BookCopyMapper;
import com.terrideboer.bookbase.mappers.BookMapper;
import com.terrideboer.bookbase.models.BookCopy;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = bookCopyRepository;
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

}
