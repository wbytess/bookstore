package com.bnp.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
    
    @Test
    @DisplayName("should save a new book and return it with generated ID")
    void shouldSaveNewBook() {
        Book newBook = new Book(null, "New Book1", "New Author1", 10.99);
        Book savedBook = new Book(1L, "New Book1", "New Author1", 10.99);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Book result = bookService.saveBook(newBook);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("New Book1", result.getName());
        assertEquals(10.99, result.getPrice());
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    @DisplayName("should delete a book by ID")
    void shouldDeleteBook() {
        doNothing().when(bookRepository).deleteById(anyLong());

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("should update existing book and return updated values")
    void shouldUpdateBook() {
        Book existingBook = new Book(1L, "Existing Book", "Author", 10.99);
        Book updatedInfo = new Book(null, "Updated Name", "Updated Author", 35.40);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        // Mock save method to return the updated book
        Book savedBook = new Book(1L, "Updated Name", "Updated Author", 35.40);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Call the service update book method
        Book result = bookService.updateBook(1L, updatedInfo);

        assertEquals(1L, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Author", result.getAuthor());
        assertEquals(35.40, result.getPrice());
        
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }


}