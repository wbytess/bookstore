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
    
    @Test
    @DisplayName("should calculate total price for an order with two books")
    void shouldCalculateTotalPriceForAnOrder() {
    	
    	Book book = new Book(1L, "New Book", "Author", 10.00);
        OrderItem item = new OrderItem();
        item.setBook(book);
        item.setQuantity(2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);

        Order order = new Order();
        order.setOrderItems(items);
        
        assertEquals(20, order.calculateTotalOrderPrice(), 0.01);
    }
    
    @Test
    @DisplayName("should calculate total price for an order with items each item have two books")
    void shouldCalculateTotalPriceForAnOrderWithTwoItems() {
    	
    	Book book = new Book(1L, "New Book", "Author", 10.00);
        OrderItem item = new OrderItem();
        item.setBook(book);
        item.setQuantity(2);
        
        Book cleanCode = new Book(1L, "Clean Code", "Clean Code Author", 7.25);
        OrderItem itemTwo = new OrderItem();
        itemTwo.setBook(cleanCode);
        itemTwo.setQuantity(2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        items.add(itemTwo);

        Order order = new Order();
        order.setOrderItems(items);
        
        assertEquals(34.50, order.calculateTotalOrderPrice(), 0.01);//(10*2) +(7.25*2) =34.50
    }

}
