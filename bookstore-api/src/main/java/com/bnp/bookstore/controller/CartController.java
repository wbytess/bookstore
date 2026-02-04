package com.bnp.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bnp.bookstore.dto.AddToCartRequest;
import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(CartController.BASE_API)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    public static final String BASE_API = "/api/cart";
    public static final String SESSION_HEADER = "X-Session-Id";
    public static final String TOTAL_PATH = "/total";
    public static final String ID_PATH = "/{id}";

    private final CartService cartService;

    private String resolveSessionId(String headerSessionId) {
        return headerSessionId != null ? headerSessionId : UUID.randomUUID().toString();
    }

    @PostMapping
    public ResponseEntity<CartItem> addCartItem(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId,
            @Valid @RequestBody AddToCartRequest request) {

        String effectiveSessionId = resolveSessionId(sessionId);
        CartItem cartItem = cartService.addOrUpdateCartItem(
                effectiveSessionId,
                request.getBookId(),
                request.getQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        List<CartItem> items = cartService.getCartItems(effectiveSessionId);
        return ResponseEntity.ok(items);
    }

    @GetMapping(TOTAL_PATH)
    public ResponseEntity<Double> getCartTotal(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        Double total = cartService.getCartTotal(effectiveSessionId);
        return ResponseEntity.ok(total);
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        CartItem updated = cartService.updateCartItemQuantity(id, quantity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        cartService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        cartService.clearCart(effectiveSessionId);
        return ResponseEntity.noContent().build();
    }
}
