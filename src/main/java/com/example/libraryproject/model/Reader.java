package com.example.libraryproject.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;

    public Reader() {}

    public Reader(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Reader{" + "id=" + id + ", name='" + name + '\'' +
                ", email='" + email + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;
        return Objects.equals(id, reader.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

