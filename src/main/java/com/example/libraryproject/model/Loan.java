package com.example.libraryproject.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private Reader reader;

    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan() {}

    public Loan(Book book, Reader reader, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.reader = reader;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Book getBook() { return book; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return "Loan{" + "id=" + id + ", book=" + book + ", reader=" + reader +
                ", loanDate=" + loanDate + ", dueDate=" + dueDate + ", returnDate=" + returnDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
