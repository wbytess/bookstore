package com.bnp.bookstore.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")//Restrict CORS to localhost frontend
public class BookController {

	private final BookService bookService;

	@PostMapping
	public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
		// Ensure client cannot set book id
		book.setId(null);
		Book savedBook = bookService.saveBook(book);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBook.getId())
				.toUri();

		return ResponseEntity.created(location).body(savedBook);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
		book.setId(id);
		Book updatedBook = bookService.saveBook(book);
		return ResponseEntity.ok(updatedBook);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

		if (bookService.findBookById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		bookService.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Book> findBookById(@PathVariable("id") Long bookId) {
	    return bookService.findBookById(bookId)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}

}
