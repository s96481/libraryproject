package com.example.libraryproject;

import com.example.libraryproject.model.Book;
import com.example.libraryproject.repository.BookRepository;
import com.example.libraryproject.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setup() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    @Test
    void testCreateBook() {
        Book book = new Book("Test", "Author", "2020");

        when(bookRepository.findByIsbn("2020")).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.create(book);

        assertEquals("Test", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testGetBookById() {
        Book book = new Book("A", "B", "2000");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.get(1L);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getTitle());
    }

    @Test
    void testListAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(
                new Book("A", "B", "1999")
        ));

        List<Book> list = bookService.listAll();

        assertEquals(1, list.size());
    }
}
