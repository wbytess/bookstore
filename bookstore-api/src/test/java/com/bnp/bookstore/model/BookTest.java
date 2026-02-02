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

    
}