package com.bnp.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderItem;

@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TestEntityManager entityManager;
	
	private Book book;
	
	@BeforeEach
    void setUp() {
        book = new Book(null, "A Book", "An Author", 10.0);
        book = entityManager.persist(book);
        entityManager.flush();
    }

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
	
	@Test
	@DisplayName("should create an order with items having two books and return the total amount")
	void shouldCreateAnOrderWithItemsAndCalculateTheTotaAmount() {

		Order order = new Order();
		order.setOrderDate(LocalDateTime.now());

		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setBook(book);
		item.setQuantity(2);

		
		order.getOrderItems().add(item);
		order.setTotalAmount(order.calculateTotalOrderPrice());
		
		Order saved = orderRepository.save(order);
		entityManager.flush();

		assertNotNull(saved.getId());
		assertEquals(1, saved.getOrderItems().size());
		assertEquals(2, saved.getOrderItems().get(0).getQuantity());
		assertEquals(20.0, saved.getTotalAmount());
	}

}
