package com.example.libraryproject.integration;

import com.example.libraryproject.LibraryprojectApplication;
import com.example.libraryproject.model.Book;
import com.example.libraryproject.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = LibraryprojectApplication.class
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class BookRestControllerIntegTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void whenValidBookInput_thenCreateBook() throws Exception {
        Book b = new Book("Test Title", "Author", "9788300000335");
        b.setAvailable(true);

        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(b)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.author", is("Author")))
                .andExpect(jsonPath("$.isbn", is("9788300000335")))
                .andExpect(jsonPath("$.available", is(true)));

        List<Book> found = repository.findAll();
        assertThat(found).extracting(Book::getTitle)
                .containsOnly("Test Title");
    }

    @Test
    void whenGetAllBooks_thenStatus200() throws Exception {
        repository.save(new Book("A", "B", "9788300000456"));
        repository.save(new Book("C", "D", "9788300000455"));

        mvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].title", is("A")))
                .andExpect(jsonPath("$[1].title", is("C")));
    }

    @Test
    void whenGetBookById_thenReturnCorrectBook() throws Exception {
        Book saved = repository.save(new Book("Unique", "Author", "9788300000724"));

        mvc.perform(get("/api/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Unique")))
                .andExpect(jsonPath("$.author", is("Author")))
                .andExpect(jsonPath("$.isbn", is("9788300000724")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void whenGetBookByIsbn_thenReturnCorrectBook() throws Exception {
        Book saved = repository.save(new Book("ISBN Test", "Author", "9788300000727"));

        mvc.perform(get("/api/books/isbn/" + saved.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("ISBN Test")))
                .andExpect(jsonPath("$.isbn", is("9788300000727")));
    }

    @Test
    void whenUpdateBook_thenBookIsUpdated() throws Exception {
        Book saved = repository.save(new Book("Old Title", "Author", "9788300000823"));
        Book update = new Book("New Title", "New Author", "9788300000823");
        update.setAvailable(false);

        mvc.perform(put("/api/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.author", is("New Author")))
                .andExpect(jsonPath("$.available", is(false)));

        Book updatedFromDb = repository.findById(saved.getId()).orElseThrow();
        assertThat(updatedFromDb.getTitle()).isEqualTo("New Title");
        assertThat(updatedFromDb.isAvailable()).isFalse();
    }

    @Test
    void whenDeleteBook_thenBookIsDeleted() throws Exception {
        Book saved = repository.save(new Book("ToDelete", "Author", "9788300000823"));

        mvc.perform(delete("/api/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
