package com.bnp.bookstore.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bnp.bookstore.dto.AddToCartRequest;
import com.bnp.bookstore.model.CartItem;
import com.bnp.bookstore.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(CartController.BASE_API)
@RequiredArgsConstructor
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = {
	        "Content-Type",
	        "X-Session-Id"
	    }
	)
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
            @RequestHeader(SESSION_HEADER) String sessionId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        List<CartItem> items = cartService.getCartItems(effectiveSessionId);
        return ResponseEntity.ok(items);
    }

    @GetMapping(TOTAL_PATH)
    public ResponseEntity<Double> getCartTotal(
            @RequestHeader(value = SESSION_HEADER, required = false) String sessionId) {

        String effectiveSessionId = resolveSessionId(sessionId);
        Double total = cartService.getCartTotal(effectiveSessionId);
        return ResponseEntity.ok(roundToTwoDecimalPlaces(total));
    }

    /**
     * Rounds a double value to 2 decimal places using HALF_UP rounding mode.
     */
    private Double roundToTwoDecimalPlaces(Double value) {
        if (value == null) return 0.0;
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
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
