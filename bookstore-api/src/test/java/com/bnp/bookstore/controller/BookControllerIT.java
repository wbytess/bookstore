package com.bnp.bookstore.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerIT {
	
	private static final String BASE_API = "/api/books";

	private static final Long BOOK_ID_1 = 1L;
	private static final Long BOOK_ID_2 = 2L;

	private static final String NEW_BOOK_NAME = "New Book";
	private static final String NEW_BOOK_AUTHOR = "New Author";
	private static final double NEW_BOOK_PRICE = 10.50;

	private static final String NEW_BOOK2_NAME = "New Book2";
	private static final String NEW_BOOK2_AUTHOR = "New Author2";
	private static final double NEW_BOOK2_PRICE = 12.50;

	private static final String UPDATED_BOOK_NAME = "Updated Name";
	private static final String UPDATED_BOOK_AUTHOR = "Updated Author";
	private static final double UPDATED_BOOK_PRICE = 59.99;

	private static final String JSON_ID = "$.id";
	private static final String JSON_NAME = "$.name";
	private static final String JSON_AUTHOR = "$.author";
	private static final String JSON_PRICE = "$.price";
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	private Book testBook1;
	private Book testBook2;
	
	@BeforeEach
	void setUp() {
		testBook1 = buildBook(BOOK_ID_1, NEW_BOOK_NAME, NEW_BOOK_AUTHOR, NEW_BOOK_PRICE);
		testBook2 = buildBook(BOOK_ID_2, NEW_BOOK2_NAME, NEW_BOOK2_AUTHOR, NEW_BOOK2_PRICE);
	}

	private Book buildBook(Long id, String name, String author, double price) {
		return new Book(id, name, author, price);
	}

	private Book buildNewBook() {
		return buildBook(null, NEW_BOOK_NAME, NEW_BOOK_AUTHOR, NEW_BOOK_PRICE);
	}

	private Book buildUpdatedBook() {
		return buildBook(BOOK_ID_1, UPDATED_BOOK_NAME, UPDATED_BOOK_AUTHOR, UPDATED_BOOK_PRICE);
	}

	private void assertBookJson(ResultActions result, Book book) throws Exception {
		result.andExpect(jsonPath(JSON_ID).value(book.getId())).andExpect(jsonPath(JSON_NAME).value(book.getName()))
				.andExpect(jsonPath(JSON_AUTHOR).value(book.getAuthor()))
				.andExpect(jsonPath(JSON_PRICE).value(book.getPrice()));
	}

	@Test
	//@WithMockUser
	@DisplayName("Create a new book and return 201 with book payload")
	void createBook_ShouldReturnCreated() throws Exception {
		Book requestBook = buildNewBook();
		Book savedBook = testBook1;

		when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);

		ResultActions result = mockMvc
				.perform(post(BASE_API)
						//.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestBook)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

		assertBookJson(result, savedBook);
		verify(bookService).saveBook(any(Book.class));
	}

	@Test
	//@WithMockUser
	@DisplayName("Update existing book and return updated book")
	void updateBook_ShouldReturnUpdatedBook() throws Exception {
		Book updatedBook = buildUpdatedBook();
		when(bookService.saveBook(any(Book.class))).thenReturn(updatedBook);

		ResultActions result = mockMvc.perform(put(BASE_API + "/{id}", BOOK_ID_1)
				//.with(csrf())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedBook)))
				.andExpect(status().isOk());

		assertBookJson(result, updatedBook);
		verify(bookService, times(1)).saveBook(any(Book.class));
	}

	@Test
	//@WithMockUser
	@DisplayName("Return 404 when deleting non-existent book")
	void deleteNonExistentBook_ShouldReturn404() throws Exception {
		when(bookService.findBookById(BOOK_ID_2)).thenReturn(Optional.empty());

		mockMvc.perform(delete(BASE_API + "/{id}", BOOK_ID_2)
				//.with(csrf())
				).andExpect(status().isNotFound());

		verify(bookService).findBookById(BOOK_ID_2);
		verify(bookService, never()).deleteBook(any());
	}

	@Test
	//@WithMockUser
	@DisplayName("Return book by its ID")
	void getBookById_ShouldReturnBook() throws Exception {
		when(bookService.findBookById(BOOK_ID_1)).thenReturn(Optional.of(testBook1));

		ResultActions result = mockMvc.perform(get(BASE_API + "/{id}", BOOK_ID_1)).andExpect(status().isOk());

		assertBookJson(result, testBook1);
		verify(bookService, times(1)).findBookById(BOOK_ID_1);
	}

	@Test
	//@WithMockUser
	@DisplayName("Return all available books")
	void getAllBooks_ShouldReturnList() throws Exception {
		List<Book> books = Arrays.asList(testBook1, testBook2);
		when(bookService.findAllBooks()).thenReturn(books);

		mockMvc.perform(get(BASE_API)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

		verify(bookService, times(1)).findAllBooks();
	}
}
