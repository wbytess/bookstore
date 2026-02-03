package com.bnp.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bnp.bookstore.model.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findBySessionId(String sessionId);

    Optional<CartItem> findBySessionIdAndBookId(String sessionId, Long bookId);

    void deleteBySessionId(String sessionId);
}
