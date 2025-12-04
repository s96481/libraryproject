package com.example.libraryproject.service;

import com.example.libraryproject.model.Reader;
import com.example.libraryproject.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {
    private final ReaderRepository repo;

    public ReaderService(ReaderRepository repo) {
        this.repo = repo;
    }

    public Reader create(Reader reader) {
        // email uniq m.b
        if (repo.findByEmail(reader.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + reader.getEmail());
        }
        return repo.save(reader);
    }

    public List<Reader> listAll() {
        return repo.findAll();
    }

    public Optional<Reader> get(Long id) {
        return repo.findById(id);
    }

    public Reader update(Long id, Reader update) {
        return repo.findById(id).map(existing -> {
            repo.findByEmail(update.getEmail()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new IllegalArgumentException("Email already in use: " + update.getEmail());
                }
            });

            existing.setName(update.getName());
            existing.setEmail(update.getEmail());

            return repo.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Reader not found: " + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}