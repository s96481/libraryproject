package com.example.libraryproject.controller;

import com.example.libraryproject.model.Loan;
import com.example.libraryproject.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service) { this.service = service; }

    @GetMapping
    public List<Loan> allActive() { return service.listActiveLoans(); }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> get(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/borrow")
    public ResponseEntity<Loan> borrow(@RequestParam Long bookId, @RequestParam Long readerId, @RequestParam(defaultValue = "14") int days) {
        try {
            Loan loan = service.borrowBook(bookId, readerId, days);
            return ResponseEntity.created(URI.create("/api/loans/" + loan.getId())).body(loan);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/return/{loanId}")
    public ResponseEntity<Loan> returnBook(@PathVariable Long loanId) {
        try {
            Loan loan = service.returnBook(loanId);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/reader/{readerId}")
    public List<Loan> loansForReader(@PathVariable Long readerId) {
        return service.listLoansForReader(readerId);
    }
}
