package com.bnp.bookstore.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("should create an order for two books")
    void shouldCreateOrderForTwoBooks() {

        Book book = new Book(1L, "New Book", "Author", 10.99);
        OrderItem item = new OrderItem();
        item.setBook(book);
        item.setQuantity(2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);

        Order order = new Order();
        order.setOrderItems(items);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(2, order.getOrderItems().get(0).getQuantity());
    }

}
