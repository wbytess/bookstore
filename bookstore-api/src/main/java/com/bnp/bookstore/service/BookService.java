package com.bnp.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

}
