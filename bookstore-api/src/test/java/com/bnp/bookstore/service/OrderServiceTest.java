package com.bnp.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

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
}
