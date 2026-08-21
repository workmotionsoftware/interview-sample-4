package com.interview.orders.tenant;

import java.util.UUID;

public record OrderResponse(UUID id, UUID orgId, String orderNumber) {

  public static OrderResponse from(Order order) {
    return new OrderResponse(order.getId(), order.getOrgId(), order.getOrderNumber());
  }
}
