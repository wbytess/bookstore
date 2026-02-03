package com.bnp.bookstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderItem;
import com.bnp.bookstore.model.OrderStatus;
import com.bnp.bookstore.repository.CartRepository;
import com.bnp.bookstore.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	private final CartRepository cartRepository;

	@Transactional(readOnly = true)
	public Optional<Order> getOrderById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	@Transactional(readOnly = true)
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order saveOrder(Order order) {
		double totalAmount = order.getOrderItems() == null ? 0.0
				: order.getOrderItems().stream().mapToDouble(OrderItem::getSubtotal).sum();

		order.setTotalAmount(totalAmount);
		Order savedOrder = orderRepository.save(order);

		return savedOrder;
	}

	public Order updateOrderStatus(Long orderId, OrderStatus status) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found with order id: " + orderId));

		order.setStatus(status);
		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public List<Order> getOrdersBySessionId(String sessionId) {
		return orderRepository.findBySessionId(sessionId);
	}

	public Order placeOrder(String sessionId, Long userId) {
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

	@Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
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
