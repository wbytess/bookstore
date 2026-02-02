package com.bnp.bookstore.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookTest {
	
	@Test
    @DisplayName("should create book with no-args constructor")
    void shouldCreateBookWithEmptyArgsConstructor() {

        Book book = new Book();
        book.setId(1L);
        book.setName("Test");
        book.setAuthor("Author");
        book.setPrice(19.99);

        assertEquals(1L, book.getId());
        assertEquals("Test", book.getName());
    }

    @Test
    @DisplayName("should create a book with all columns")
    void shouldCreateABook() {

        Book book = new Book(1L, "Book1", "Author 1", 50.0);

        assertEquals(1L, book.getId());
        assertEquals("Book1", book.getName());
        assertEquals("Author 1", book.getAuthor());
        assertEquals(50.0, book.getPrice());
    }

    
}