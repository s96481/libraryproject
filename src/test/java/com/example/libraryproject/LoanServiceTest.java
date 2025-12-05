package com.example.libraryproject;

import com.example.libraryproject.service.LoanService;
import com.example.libraryproject.model.Book;
import com.example.libraryproject.model.Loan;
import com.example.libraryproject.model.Reader;
import com.example.libraryproject.repository.BookRepository;
import com.example.libraryproject.repository.LoanRepository;
import com.example.libraryproject.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanRepository loanRepo;
    private ReaderRepository readerRepo;
    private BookRepository bookRepo;
    private LoanService service;

    @BeforeEach
    void setup() {
        loanRepo = mock(LoanRepository.class);
        readerRepo = mock(ReaderRepository.class);
        bookRepo = mock(BookRepository.class);
        service = new LoanService(loanRepo, bookRepo, readerRepo);

    }

    @Test
    void testCreateLoan() {
        Reader r = new Reader();
        r.setId(1L);
        Book b = new Book();
        b.setId(2L);
        b.setAvailable(true);

        when(readerRepo.findById(1L)).thenReturn(Optional.of(r));
        when(bookRepo.findById(2L)).thenReturn(Optional.of(b));
        when(loanRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = service.borrowBook(2L, 1L, 14);

        assertNotNull(loan);
        assertEquals(r, loan.getReader());
        assertEquals(b, loan.getBook());
        assertFalse(b.isAvailable());
        verify(loanRepo, times(1)).save(any());
    }
}
