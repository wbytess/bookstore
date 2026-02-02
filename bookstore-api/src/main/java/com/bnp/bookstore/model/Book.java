package com.bnp.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Book author is required")
    @Column(nullable = false)
    private String author;

    @Positive(message = "Book price must be positive")
    @Column(nullable = false)
    private Double price;

    public Book(Long id, String name, String author, Double price) {
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Book price must not be negative");
        }
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
    }
}
