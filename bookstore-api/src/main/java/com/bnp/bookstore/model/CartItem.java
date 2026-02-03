package com.bnp.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Book is required")
    private Book book;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    public CartItem(Long id, Book book, Integer quantity, String sessionId) {
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.id = id;
        this.book = book;
        this.quantity = quantity;
        this.sessionId = sessionId;
    }

    public Double getSubtotal() {
        if (book != null && book.getPrice() != null && quantity != null) {
            return book.getPrice() * quantity;
        }
        return 0.0;
    }
}
