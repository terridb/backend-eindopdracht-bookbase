package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.bookcopies.BookCopyInputDto;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.models.Book;
import com.terrideboer.bookbase.models.BookCopy;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.Reservation;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

    @Mock
    BookCopyRepository bookCopyRepository;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookCopyService bookCopyService;

    BookCopy copy1 = new BookCopy();
    BookCopy copy2 = new BookCopy();
    BookCopyInputDto inputDto = new BookCopyInputDto();
    BookCopy savedCopy = new BookCopy();

    Book book1 = new Book();
    Loan loan1 = new Loan();
    Reservation reservation1 = new Reservation();

    List<BookCopy> copies;

    @BeforeEach
    void setUp() {
        book1.setId(1L);
        book1.setId(1L);
        book1.setBookCopies(List.of(copy1, copy2));

        loan1.setId(1L);
        reservation1.setId(1L);

        copy1.setId(1L);
        copy1.setTrackingNumber("BB-1-1");
        copy1.setBook(book1);
        copy1.setLoans(List.of(loan1));
        copy1.setReservations(List.of(reservation1));

        copy2.setId(2L);
        copy2.setTrackingNumber("BB-1-2");
        copy2.setBook(book1);

        copies = List.of(copy1, copy2);

        savedCopy.setId(5L);
        savedCopy.setTrackingNumber("BB-1-5");
        savedCopy.setBook(book1);
    }

    @Test
    @DisplayName("getAllBookCopies should show all existing copies")
    public void getAllBookCopiesShouldShowAllBookCopies() {
        Mockito.when(bookCopyRepository.findAll(Sort.by("id").ascending()))
                .thenReturn(copies);

        List<BookCopyDto> dtos = bookCopyService.getAllBookCopies();

        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).id);
        assertEquals(2L, dtos.get(1).id);
    }

    @Test
    @DisplayName("getBookCopyById should return correct copy")
    public void getBookCopyByIdShouldShowCorrectBookCopy() {
        Mockito.when(bookCopyRepository.findById(1L))
                .thenReturn(Optional.of(copy1));

        BookCopyDto copyDto = bookCopyService.getBookCopyById(1L);

        assertEquals(1L, copyDto.id);
        assertEquals("BB-1-1", copyDto.trackingNumber);
        assertEquals(1L, copyDto.book.id);
    }

    @Test
    @DisplayName("getBookCopyById should throw exception when not found")
    public void getBookCopyByIdShouldThrowNotFound() {
        Mockito.when(bookCopyRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookCopyService.getBookCopyById(100L));
    }


    @Test
    @DisplayName("postBookCopy should create a new copy and generate a tracking nr. when none is provided")
    public void postBookCopyWithoutTrackingShouldPostBookCopy() {
        inputDto.trackingNumber = null;

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Mockito.when(bookCopyRepository.save(any())).thenReturn(savedCopy);

        BookCopyDto copyDto = bookCopyService.postBookCopy(inputDto, 1L);

        assertEquals(5L, copyDto.id);
        assertEquals(book1.getId(), copyDto.book.id);
        assertEquals("BB-1-5", copyDto.trackingNumber);
    }

    @Test
    @DisplayName("postBookCopy should create a new copy and keep the provided tracking nr.")
    public void postBookCopyWithTrackingShouldPostBookCopy() {
        inputDto.trackingNumber = "BB-1-6";
        savedCopy.setTrackingNumber("BB-1-6");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Mockito.when(bookCopyRepository.save(any())).thenReturn(savedCopy);

        BookCopyDto copyDto = bookCopyService.postBookCopy(inputDto, 1L);

        assertEquals(5L, copyDto.id);
        assertEquals(book1.getId(), copyDto.book.id);
        assertEquals("BB-1-6", copyDto.trackingNumber);
    }

    @Test
    @DisplayName("postBookCopy should throw not found exception when book is not found")
    public void postBookCopyShouldThrowNotFound() {
        Mockito.when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookCopyService.postBookCopy(inputDto, 100L));
    }

    @Test
    @DisplayName("postBookCopy should throw invalid input exception when given tracking number already exists")
    public void postBookCopyShouldThrowInvalidInput() {
        inputDto.trackingNumber = "BB-1-2";

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Mockito.when(bookCopyRepository.existsByTrackingNumber("BB-1-2")).thenReturn(true);

        assertThrows(InvalidInputException.class, () -> bookCopyService.postBookCopy(inputDto, 1L));
    }

    @Test
    @DisplayName("postBookCopy should generate a new tracking nr. when first nr. already exists")
    public void postBookCopyShouldGenerateNewNumber() {
        inputDto.trackingNumber = null;
        savedCopy.setTrackingNumber("BB-1-5");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Mockito.when(bookCopyRepository.existsByTrackingNumber("BB-1-3")).thenReturn(true);
        Mockito.when(bookCopyRepository.existsByTrackingNumber("BB-1-4")).thenReturn(true);
        Mockito.when(bookCopyRepository.existsByTrackingNumber("BB-1-5")).thenReturn(false);

        Mockito.when(bookCopyRepository.save(any())).thenReturn(savedCopy);

        BookCopyDto copyDto = bookCopyService.postBookCopy(inputDto, 1L);

        assertEquals("BB-1-5", copyDto.trackingNumber);
    }

    @Test
    @DisplayName("updateBookCopy should update copy")
    public void updateBookCopyShouldUpdateExistingBookCopy() {
        savedCopy.setId(1L);

        Mockito.when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy1));
        Mockito.when(bookCopyRepository.save(any())).thenReturn(savedCopy);

        BookCopyDto copyDto = bookCopyService.updateBookCopy(1L, inputDto);

        assertEquals(1L, copyDto.id);
        assertEquals(book1.getId(), copyDto.book.id);
        assertEquals("BB-1-5", copyDto.trackingNumber);
    }

    @Test
    @DisplayName("updateBookCopy should throw not found exception when copy is not found")
    public void updateBookCopyShouldThrowNotFound() {
        Mockito.when(bookCopyRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookCopyService.updateBookCopy(100L, inputDto));
    }

    @Test
    @DisplayName("updateBookCopy should throw invalid input exception when tracking number already exists")
    public void updateBookCopyShouldThrowInvalidInput() {
        inputDto.trackingNumber = "BB-1-2";

        Mockito.when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy1));
        Mockito.when(bookCopyRepository.existsByTrackingNumber("BB-1-2")).thenReturn(true);

        assertThrows(InvalidInputException.class, () -> bookCopyService.updateBookCopy(1L, inputDto));
    }

    @Test
    @DisplayName("deleteBookCopy should delete an existing copy")
    public void deleteBookCopyShouldDeleteBookCopy() {
        Mockito.when(bookCopyRepository.findById(copy1.getId())).thenReturn(Optional.of(copy1));

        bookCopyService.deleteBookCopy(copy1.getId());

        verify(bookCopyRepository).deleteById(copy1.getId());
    }

    @Test
    @DisplayName("deleteBookCopy should throw exception when copy is not found")
    public void deleteBookCopyShouldThrowNotFoundException() {
        Mockito.when(bookCopyRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookCopyService.deleteBookCopy(100L));
    }
}