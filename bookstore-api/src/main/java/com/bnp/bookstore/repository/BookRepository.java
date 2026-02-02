package com.bnp.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bnp.bookstore.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
