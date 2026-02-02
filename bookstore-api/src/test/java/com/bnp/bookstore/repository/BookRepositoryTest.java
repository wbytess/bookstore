package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bnp.bookstore.model.Book;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("should create a book with all columns")
    void shouldSaveBook() {
        Book book = new Book(null, "Test Book", "Test Author", 10.50);

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getName());
        assertEquals("Test Author", savedBook.getAuthor());
        assertEquals(10.50, savedBook.getPrice());
    }

}
