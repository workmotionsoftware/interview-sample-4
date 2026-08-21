package com.interview.orders.tenant;

import java.util.UUID;

public class Order {

  private final UUID id;
  private final UUID orgId;
  private final String orderNumber;

  public Order(UUID id, UUID orgId, String orderNumber) {
    this.id = id;
    this.orgId = orgId;
    this.orderNumber = orderNumber;
  }

  public UUID getId() {
    return id;
  }

  public UUID getOrgId() {
    return orgId;
  }

  public String getOrderNumber() {
    return orderNumber;
  }
}
