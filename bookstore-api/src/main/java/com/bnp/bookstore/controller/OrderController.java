package com.bnp.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderStatus;
import com.bnp.bookstore.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(OrderController.BASE_API)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    public static final String BASE_API = "/api/orders";
    public static final String SESSION_HEADER = "X-Session-Id";

    private final OrderService orderService;

    private String resolveSessionId(String headerSessionId) {
        return headerSessionId != null ? headerSessionId : UUID.randomUUID().toString();
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId,
            @RequestParam(required = false) Long userId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        Order order = orderService.placeOrder(effectiveSessionId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/session")
    public ResponseEntity<List<Order>> getOrdersForSession(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        List<Order> orders = orderService.getOrdersBySessionId(effectiveSessionId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order cancelledOrder = orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        return ResponseEntity.ok(cancelledOrder);
    }
}
