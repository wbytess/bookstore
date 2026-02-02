package com.bnp.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;
    
    List<Book> books;
    
    private Book book;

    @BeforeEach
    void setUp() {
    	book = new Book(1L, "Test Book1", "Test Author1", 10.50);
        books = Arrays.asList(
        		book,
                new Book(2L, "Test Book2", "Test Author2", 20.99)
        );
    }

    @Test
    @DisplayName("should return all inserted books")
    void shouldGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAllBooks();

        assertEquals(2, result.size());
        assertEquals("Test Book1", result.get(0).getName());
        assertEquals("Test Book2", result.get(1).getName());
        verify(bookRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("should return a single book by id")
    void shouldReturnBookById() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Book1", result.get().getName());
        assertEquals(10.50, result.get().getPrice());
        verify(bookRepository, times(1)).findById(1L);
    }

}