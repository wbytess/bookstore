package com.bnp.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.repository.BookRepository;
import com.bnp.bookstore.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final String SESSION_ID = "test-session-123";

    private static final Long BOOK_ID = 1L;
    private static final Long CART_ITEM_ID = 1L;
    private static final Long INVALID_BOOK_ID = 999L;

    private static final String BOOK_TITLE = "Test Book";
    private static final String BOOK_AUTHOR = "Test Author";
    private static final double BOOK_PRICE = 29.99;

    private static final int INITIAL_QUANTITY = 2;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartService cartService;

    private Book testBook;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testBook = new Book(BOOK_ID, BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE);
        testCartItem = new CartItem(CART_ITEM_ID, testBook, INITIAL_QUANTITY, SESSION_ID);
    }

    @Test
    @DisplayName("Should add new cart item when item does not already exist")
    void shouldAddNewCartItem() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(testBook));
        when(cartRepository.findBySessionIdAndBookId(SESSION_ID, BOOK_ID))
                .thenReturn(Optional.empty());
        when(cartRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        CartItem result =
                cartService.addOrUpdateCartItem(SESSION_ID, BOOK_ID, INITIAL_QUANTITY);

        assertNotNull(result);
        assertEquals(INITIAL_QUANTITY, result.getQuantity());
        verify(bookRepository).findById(BOOK_ID);
        verify(cartRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should increase quantity when cart item already exists")
    void shouldIncreaseQuantityForExistingCartItem() {
        CartItem existingItem =
                new CartItem(CART_ITEM_ID, testBook, INITIAL_QUANTITY, SESSION_ID);

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(testBook));
        when(cartRepository.findBySessionIdAndBookId(SESSION_ID, BOOK_ID))
                .thenReturn(Optional.of(existingItem));
        when(cartRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CartItem result =
                cartService.addOrUpdateCartItem(SESSION_ID, BOOK_ID, 3);

        assertEquals(5, result.getQuantity());
        verify(cartRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should throw exception when book is not found")
    void shouldThrowExceptionWhenBookDoesNotExist() {
        when(bookRepository.findById(INVALID_BOOK_ID))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> cartService.addOrUpdateCartItem(
                        SESSION_ID, INVALID_BOOK_ID, INITIAL_QUANTITY));
    }

    @Test
    @DisplayName("Should return cart items for given session")
    void shouldReturnCartItemsForSession() {
        when(cartRepository.findBySessionId(SESSION_ID))
                .thenReturn(Arrays.asList(testCartItem));

        List<CartItem> result = cartService.getCartItems(SESSION_ID);

        assertEquals(1, result.size());
        verify(cartRepository).findBySessionId(SESSION_ID);
    }

    @Test
    @DisplayName("Should calculate total cart price for session")
    void shouldCalculateCartTotal() {
        CartItem item1 =
                new CartItem(1L, testBook, 2, SESSION_ID);
        CartItem item2 =
                new CartItem(2L,
                        new Book(2L, "Book 2", "Author 2", 39.99),
                        1,
                        SESSION_ID);

        when(cartRepository.findBySessionId(SESSION_ID))
                .thenReturn(Arrays.asList(item1, item2));

        Double total = cartService.getCartTotal(SESSION_ID);

        assertEquals(99.97, total, 0.01);
    }

    @Test
    @DisplayName("Should update quantity of an existing cart item")
    void shouldUpdateCartItemQuantity() {
        when(cartRepository.findById(CART_ITEM_ID))
                .thenReturn(Optional.of(testCartItem));
        when(cartRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CartItem result =
                cartService.updateCartItemQuantity(CART_ITEM_ID, 5);

        assertEquals(5, result.getQuantity());
        verify(cartRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should remove cart item by ID")
    void shouldRemoveCartItemById() {
        doNothing().when(cartRepository).deleteById(CART_ITEM_ID);

        cartService.removeCartItem(CART_ITEM_ID);

        verify(cartRepository).deleteById(CART_ITEM_ID);
    }

    @Test
    @DisplayName("Should clear all cart items for a session")
    void shouldClearCartForSession() {
        doNothing().when(cartRepository).deleteBySessionId(SESSION_ID);

        cartService.clearCart(SESSION_ID);

        verify(cartRepository).deleteBySessionId(SESSION_ID);
    }
}
