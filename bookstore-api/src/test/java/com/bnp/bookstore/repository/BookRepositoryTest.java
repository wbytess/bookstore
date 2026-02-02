package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bnp.bookstore.model.Book;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private TestEntityManager entityManager;

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
    
    @Test
    @DisplayName("should find all books with all columns")
    void shouldFindAllBooks() {
        bookRepository.save(new Book(null, "Book 1", "Author 1", 10.99));
        bookRepository.save(new Book(null, "Book 2", "Author 2", 20.99));
        entityManager.flush();

        List<Book> books = bookRepository.findAll();

        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> b.getName().equals("Book 1")));
        assertTrue(books.stream().anyMatch(b -> b.getName().equals("Book 2")));
    }
    
    @Test
    @DisplayName("should find all books by book id")
    void shouldFindBookById() {
        Book book = bookRepository.save(new Book(null, "Test Book", "Test Author", 10.99));
        entityManager.flush();

        Optional<Book> bookById = bookRepository.findById(book.getId());

        assertTrue(bookById.isPresent());
        assertEquals("Test Book", bookById.get().getName());
        assertEquals(10.99, bookById.get().getPrice());
    }
    
    @Test
    @DisplayName("return false when book not found")
    void returnEmptyWhenBookNotFound() {
        Optional<Book> foundBook = bookRepository.findById(1L);

        assertFalse(foundBook.isPresent());
    }

}
