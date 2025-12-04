package com.example.libraryproject.repository;

import com.example.libraryproject.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    Optional<Reader> findByEmail(String email);
}
