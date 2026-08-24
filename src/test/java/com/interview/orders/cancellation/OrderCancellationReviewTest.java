package com.interview.orders.cancellation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.interview.orders.sales.Customer;
import com.interview.orders.sales.CustomerRepository;
import com.interview.orders.sales.Order;
import com.interview.orders.sales.OrderLine;
import com.interview.orders.sales.OrderLineRepository;
import com.interview.orders.sales.OrderLineStatus;
import com.interview.orders.sales.OrderRepository;
import com.interview.orders.sales.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
@Disabled("disabled")
class OrderCancellationReviewTest {

  private static final UUID ORG_A = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  private static final String ORDER_NUMBER = "ORD-1001";

  @Autowired private OrderService orderService;
  @Autowired private OrderRepository orderRepository;
  @Autowired private CustomerRepository customerRepository;

  @MockitoSpyBean private OrderLineRepository orderLineRepository;
  @MockitoSpyBean private OrderAuditRepository auditRepository;
  @MockitoBean private InventoryClient inventoryClient;

  private UUID orderId;

  @BeforeEach
  void setUp() {
    auditRepository.deleteAll();
    orderLineRepository.deleteAll();
    orderRepository.deleteAll();
    customerRepository.deleteAll();

    UUID customerId = UUID.randomUUID();
    customerRepository.save(new Customer(customerId, "Northwind Ltd"));

    orderId = UUID.randomUUID();
    Order order =
        orderRepository.save(
            new Order(orderId, ORG_A, customerId, null, ORDER_NUMBER, LocalDate.of(2026, 3, 4)));

    for (int i = 1; i <= 3; i++) {
      orderLineRepository.save(
          new OrderLine(UUID.randomUUID(), order, "SKU-" + i, i, new BigDecimal("10.00")));
    }
  }

  @Test
  void cancellation_marksOrderAndEveryLine_andRecordsAudit() {
    orderService.cancelOrder(ORG_A, ORDER_NUMBER, "Customer changed their mind");

    assertThat(orderRepository.findById(orderId).orElseThrow().getStatus())
        .isEqualTo(OrderStatus.CANCELLED);
    assertThat(orderLineRepository.findByOrderId(orderId))
        .extracting(OrderLine::getStatus)
        .containsOnly(OrderLineStatus.CANCELLED);
    assertThat(auditRepository.findByOrderId(orderId)).hasSize(1);
    verify(inventoryClient).releaseReservation(orderId);
  }

  @Test
  void cancellation_leavesNothingBehind_whenTheAuditWriteFails() {
    doThrow(new DataIntegrityViolationException("audit store unavailable"))
        .when(auditRepository)
        .record(any(), any(), any(), any());

    assertThatThrownBy(() -> orderService.cancelOrder(ORG_A, ORDER_NUMBER, "Duplicate order"))
        .isInstanceOf(DataIntegrityViolationException.class);

    assertThat(orderRepository.findById(orderId).orElseThrow().getStatus())
        .isEqualTo(OrderStatus.ACTIVE);
    assertThat(orderLineRepository.findByOrderId(orderId))
        .extracting(OrderLine::getStatus)
        .containsOnly(OrderLineStatus.ACTIVE);
    verify(inventoryClient, never()).releaseReservation(any());
  }

  @Test
  void cancellation_leavesNothingBehind_whenOneLineWriteFails() {
    // The first two lines save normally; the third fails the way a lock timeout would.
    doThrow(new DataIntegrityViolationException("row lock timeout"))
        .when(orderLineRepository)
        .save(ArgumentMatchers.<OrderLine>argThat(line -> "SKU-3".equals(line.getSku())));

    assertThatThrownBy(() -> orderService.cancelOrder(ORG_A, ORDER_NUMBER, "Out of stock"))
        .isInstanceOf(DataIntegrityViolationException.class);

    assertThat(orderRepository.findById(orderId).orElseThrow().getStatus())
        .isEqualTo(OrderStatus.ACTIVE);

    List<OrderLine> lines = orderLineRepository.findByOrderId(orderId);
    assertThat(lines).extracting(OrderLine::getStatus).containsOnly(OrderLineStatus.ACTIVE);
    verify(inventoryClient, never()).releaseReservation(any());
  }
}
