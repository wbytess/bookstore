package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.model.CartItem;

@DataJpaTest
class CartRepositoryTest {

    private static final String SESSION_ID = "test-session-123";
    private static final String OTHER_SESSION_ID = "other-session";

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book book;

    @BeforeEach
    void setUp() {
        book = entityManager.persist(
                new Book(null, "Test Book", "Test Author", 10.50)
        );
        entityManager.flush();
    }

    @Test
    @DisplayName("Should return cart item when session ID and book ID match")
    void shouldFindCartItemBySessionIdAndBookId() {
        cartRepository.save(createCartItem(2, SESSION_ID));
        entityManager.flush();

        Optional<CartItem> result =
                cartRepository.findBySessionIdAndBookId(SESSION_ID, book.getId());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getQuantity());
    }

    @Test
    @DisplayName("Should delete all cart items associated with a session ID")
    void shouldDeleteCartItemsBySessionId() {
        cartRepository.save(createCartItem(2, SESSION_ID));
        cartRepository.save(createCartItem(1, SESSION_ID));
        entityManager.flush();

        cartRepository.deleteBySessionId(SESSION_ID);
        entityManager.flush();

        List<CartItem> items = cartRepository.findBySessionId(SESSION_ID);
        assertTrue(items.isEmpty());
    }
    
    @Test
    @DisplayName("Should return empty list when no cart items exist for session ID")
    void shouldReturnEmptyListWhenNoCartItemsExist() {
        List<CartItem> items =
                cartRepository.findBySessionId("non-existent-session");

        assertTrue(items.isEmpty());
    }
    
    @Test
    @DisplayName("Should persist a cart item with quantity and session ID")
    void shouldSaveCartItem() {
        CartItem cartItem = createCartItem(2, SESSION_ID);

        CartItem savedItem = cartRepository.save(cartItem);
        entityManager.flush();

        assertNotNull(savedItem.getId());
        assertEquals(2, savedItem.getQuantity());
        assertEquals(SESSION_ID, savedItem.getSessionId());
    }

    @Test
    @DisplayName("Should return all cart items for a given session ID")
    void shouldFindCartItemsBySessionId() {
        cartRepository.save(createCartItem(2, SESSION_ID));
        cartRepository.save(createCartItem(1, SESSION_ID));
        cartRepository.save(createCartItem(3, OTHER_SESSION_ID));
        entityManager.flush();

        List<CartItem> items = cartRepository.findBySessionId(SESSION_ID);

        assertEquals(2, items.size());
        assertTrue(items.stream()
                .allMatch(item -> SESSION_ID.equals(item.getSessionId())));
    }

    private CartItem createCartItem(int quantity, String sessionId) {
        return new CartItem(null, book, quantity, sessionId);
    }
}
