package com.bnp.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bnp.bookstore.model.Book;
import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderItem;
import com.bnp.bookstore.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    
    private Book book;

    @BeforeEach
    void setup() {
        book = new Book(1L, "New Book1", "New Author1", 10.00);

        // lenient stub to prevent unnecessary stubbing exception
        lenient().when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // simulate DB-generated ID
            return order;
        });
    }

    @Test
    @DisplayName("should get an order by order id")
    void shouldGetOrderById() {

        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(10.50);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> orderById = orderService.getOrderById(1L);

        assertTrue(orderById.isPresent());
        assertEquals(10.50, orderById.get().getTotalAmount());
    }
	
	@Test
	@DisplayName("should create an order with a single item having two books and return total amount")
	void shouldCreateAnOrder() {
		Order order = new Order();

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setBook(book);
        item.setQuantity(2);

        order.getOrderItems().add(item);

        Order saved = orderService.saveOrder(order);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1, saved.getOrderItems().size());
        assertEquals(2, saved.getOrderItems().get(0).getQuantity());
        assertEquals(20.0, saved.getTotalAmount());
	}

}
