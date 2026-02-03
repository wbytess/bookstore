package com.bnp.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private static final String ITEM_IN_THE_CART_NOT_FOUND_WITH_ID = "Item in the cart not found with id: ";
	private final CartRepository cartRepository;


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
                .orElseThrow(() -> new RuntimeException(ITEM_IN_THE_CART_NOT_FOUND_WITH_ID + cartItemId));

        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public void clearCart(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }
}