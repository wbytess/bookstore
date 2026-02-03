package com.bnp.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderItem;
import com.bnp.bookstore.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

	public Order saveOrder(Order order) {
		double totalAmount = order.getOrderItems() == null ? 0.0 :
	        order.getOrderItems().stream()
	            .mapToDouble(OrderItem::getSubtotal)
	            .sum();

	    order.setTotalAmount(totalAmount);
	    Order savedOrder = orderRepository.save(order);

	    return savedOrder;
	}
}
