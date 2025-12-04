package com.example.libraryproject.service;

import com.example.libraryproject.model.Book;
import com.example.libraryproject.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Book create(Book book) {
        // check isbn
        if (repo.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("ISBN already exists: " + book.getIsbn());
        }

        return repo.save(book);
    }

    public List<Book> listAll() {
        return repo.findAll();
    }

    public Optional<Book> get(Long id) {
        return repo.findById(id);
    }

    public Optional<Book> getByIsbn(String isbn) {
        return repo.findByIsbn(isbn);
    }

    public Book update(Long id, Book update) {

        return repo.findById(id).map(existing -> {

            // isbn validate
            Optional<Book> isbnOwner = repo.findByIsbn(update.getIsbn());
            if (isbnOwner.isPresent() && !isbnOwner.get().getId().equals(id)) {
                throw new IllegalArgumentException("ISBN already exists: " + update.getIsbn());
            }

            existing.setTitle(update.getTitle());
            existing.setAuthor(update.getAuthor());
            existing.setIsbn(update.getIsbn());
            existing.setAvailable(update.isAvailable());

            return repo.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
