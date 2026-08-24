package com.interview.orders.cancellation;

import com.interview.orders.sales.Order;
import com.interview.orders.sales.OrderStatus;
import java.time.Instant;
import java.util.UUID;

public record CancelledOrder(UUID orderId, String orderNumber, OrderStatus status,
    Instant cancelledAt) {

  public static CancelledOrder from(Order order) {
    return new CancelledOrder(
        order.getId(), order.getOrderNumber(), order.getStatus(), order.getCancelledAt());
  }
}
