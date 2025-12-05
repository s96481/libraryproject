package com.example.libraryproject;

import com.example.libraryproject.service.ReaderService;

import com.example.libraryproject.model.Reader;
import com.example.libraryproject.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReaderServiceTest {

    private ReaderRepository repo;
    private ReaderService service;

    @BeforeEach
    void setup() {
        repo = mock(ReaderRepository.class);
        service = new ReaderService(repo);
    }

    @Test
    void testGetReader() {
        Reader r = new Reader();
        r.setId(1L);
        r.setName("Test");
        when(repo.findById(1L)).thenReturn(Optional.of(r));

        Optional<Reader> result = service.get(1L);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }

    @Test
    void testCreateReader() {
        Reader r = new Reader();
        r.setName("Adam");
        when(repo.save(ArgumentMatchers.any())).thenReturn(r);

        Reader result = service.create(r);

        assertEquals("Adam", result.getName());
        verify(repo, times(1)).save(r);
    }

    @Test
    void testDeleteReader() {
        service.delete(5L);
        verify(repo, times(1)).deleteById(5L);
    }
}
