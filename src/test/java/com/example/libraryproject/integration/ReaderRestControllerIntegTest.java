package com.example.libraryproject.integration;

import com.example.libraryproject.LibraryprojectApplication;
import com.example.libraryproject.model.Reader;
import com.example.libraryproject.repository.ReaderRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = LibraryprojectApplication.class
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class ReaderRestControllerIntegTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ReaderRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void whenValidReaderInput_thenCreateReader() throws Exception {
        Reader r = new Reader();
        r.setName("Młody Kawaler");
        r.setEmail("mlody@test.com");

        mvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Młody Kawaler")))
                .andExpect(jsonPath("$.email", is("mlody@test.com")));

        List<Reader> found = repository.findAll();
        assertThat(found).extracting(Reader::getName)
                .containsOnly("John Wayne");
    }

    @Test
    void whenGetAllReaders_thenStatus200() throws Exception {
        Reader r1 = new Reader();
        r1.setName("Grzegorz");
        r1.setEmail("grzeg@test.com");
        Reader r2 = new Reader();
        r2.setName("Eva");
        r2.setEmail("eva@test.com");
        repository.save(r1);
        repository.save(r2);

        mvc.perform(get("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", is("Grzegorz")))
                .andExpect(jsonPath("$[1].name", is("Eva")));
    }

    @Test
    void whenGetReaderById_thenReturnCorrectReader() throws Exception {
        Reader saved = new Reader();
        saved.setName("Filipek Radziszewski");
        saved.setEmail("fradziszewski@example.com");
        repository.save(saved);

        mvc.perform(get("/api/readers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Filipek Radziszewski")))
                .andExpect(jsonPath("$.email", is("fradziszewski@example.com")));
    }

    @Test
    void whenUpdateReader_thenReaderIsUpdated() throws Exception {
        Reader saved = new Reader();
        saved.setName("Old Name");
        saved.setEmail("old@test.com");
        repository.save(saved);

        Reader update = new Reader();
        update.setName("New Name");
        update.setEmail("new@test.com");

        mvc.perform(put("/api/readers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.email", is("new@test.com")));

        Reader updatedFromDb = repository.findById(saved.getId()).orElseThrow();
        assertThat(updatedFromDb.getName()).isEqualTo("New Name");
        assertThat(updatedFromDb.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void whenDeleteReader_thenReaderIsDeleted() throws Exception {
        Reader saved = new Reader();
        saved.setName("ToDelete");
        saved.setEmail("delete@test.com");
        repository.save(saved);

        mvc.perform(delete("/api/readers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}