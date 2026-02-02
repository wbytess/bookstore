package com.bnp.bookstore.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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

}
