package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bnp.bookstore.model.Order;

@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("should create a single order without items and return total amount")
	void shouldCreateAnOrder() {
		Order order = new Order();
		order.setTotalAmount(20.0);
		order.setOrderDate(LocalDateTime.now());

		Order saved = orderRepository.save(order);
		entityManager.flush();

		assertNotNull(saved.getId());
		assertEquals(20.0, saved.getTotalAmount());
	}

}
