package com.bnp.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnp.bookstore.exception.ResourceNotFoundException;
import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.repository.BookRepository;
import com.bnp.bookstore.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private static final String ITEM_IN_THE_CART_NOT_FOUND_WITH_ID = "Item in the cart not found with id: ";
	private final CartRepository cartRepository;
	private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(String sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    @Transactional(readOnly = true)
    public Double getCartTotal(String sessionId) {
        List<CartItem> items = cartRepository.findBySessionId(sessionId);
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_IN_THE_CART_NOT_FOUND_WITH_ID + cartItemId));

        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public void clearCart(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }
    
    public CartItem addOrUpdateCartItem(String sessionId, Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ITEM_IN_THE_CART_NOT_FOUND_WITH_ID + bookId));

        Optional<CartItem> existingCartItem =
                cartRepository.findBySessionIdAndBookId(sessionId, bookId);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartRepository.save(cartItem);
        }

        CartItem newCartItem = new CartItem(null, book, quantity, sessionId);
        return cartRepository.save(newCartItem);
    }
    
}