package com.bnp.bookstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderItem;
import com.bnp.bookstore.model.OrderStatus;
import com.bnp.bookstore.repository.BookRepository;
import com.bnp.bookstore.repository.CartRepository;
import com.bnp.bookstore.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private static final String ITEM_IN_THE_CART_NOT_FOUND_WITH_ID = "Item in the cart not found with id: ";
	private final CartRepository cartRepository;
	private final BookRepository bookRepository;
	private final OrderRepository orderRepository;

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
    
    public CartItem addOrUpdateCartItem(String sessionId, Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id: " + bookId));

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
    
    public Order placeOrderFromCart(String sessionId, Long userId) {
        List<CartItem> cartItems = cartRepository.findBySessionId(sessionId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Order failed: no items found in the cart for this session.");
        }

        Order order = getOrderObject(sessionId, userId);

        for (CartItem cartItem : cartItems) {
            order.getOrderItems().add(mapToOrderItem(cartItem, order));
        }

        order.setTotalAmount(order.calculateTotalOrderPrice());
        Order savedOrder = orderRepository.save(order);
        cartRepository.deleteBySessionId(sessionId);

        return savedOrder;
    }

    private OrderItem mapToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        return orderItem;
    }


	private Order getOrderObject(String sessionId, Long userId) {
		Order order = new Order();
        order.setSessionId(sessionId);
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
		return order;
	}



}