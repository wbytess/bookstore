package com.bnp.bookstore.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.bnp.bookstore.model.Order;
import com.bnp.bookstore.model.OrderStatus;
import com.bnp.bookstore.service.OrderService;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerIT {

    private static final String SESSION_ID = "session1010";
    private static final Long ORDER_ID = 1L;
    private static final Long INVALID_ORDER_ID = 12L;
    private static final double ORDER_TOTAL = 7.25;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(ORDER_ID);
        testOrder.setSessionId(SESSION_ID);
        testOrder.setTotalAmount(ORDER_TOTAL);
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setOrderDate(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create a new order for a session")
    void shouldCreateNewOrder() throws Exception {
        when(orderService.placeOrder(anyString(), any())).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .header("X-Session-Id", SESSION_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ORDER_ID))
                .andExpect(jsonPath("$.totalAmount").value(ORDER_TOTAL))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).placeOrder(anyString(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should retrieve an order by ID")
    void shouldRetrieveOrderById() throws Exception {
        when(orderService.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/api/orders/" + ORDER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ORDER_ID))
                .andExpect(jsonPath("$.totalAmount").value(ORDER_TOTAL));

        verify(orderService).getOrderById(ORDER_ID);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 404 when order does not exist")
    void shouldReturnNotFoundForMissingOrder() throws Exception {
        when(orderService.getOrderById(INVALID_ORDER_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/" + INVALID_ORDER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Should retrieve orders for a given session")
    void shouldRetrieveOrdersBySessionId() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getOrdersBySessionId(anyString())).thenReturn(orders);

        mockMvc.perform(get("/api/orders/session")
                        .header("X-Session-Id", SESSION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].totalAmount").value(ORDER_TOTAL));

        verify(orderService).getOrdersBySessionId(anyString());
    }
}
