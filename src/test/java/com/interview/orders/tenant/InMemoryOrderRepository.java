package com.interview.orders.tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryOrderRepository implements OrderRepository {

  private final ConcurrentMap<UUID, Order> byId = new ConcurrentHashMap<>();

  public void save(Order order) {
    byId.put(order.getId(), order);
  }

  @Override
  public Optional<Order> findByOrderNumber(String orderNumber) {
    return byId.values().stream().filter(o -> o.getOrderNumber().equals(orderNumber)).findFirst();
  }

  @Override
  public List<Order> findByOrgId(UUID orgId) {
    List<Order> result = new ArrayList<>();
    for (Order order : byId.values()) {
      if (order.getOrgId().equals(orgId)) {
        result.add(order);
      }
    }
    return result;
  }

  @Override
  public Optional<Order> findByOrgIdAndOrderNumber(UUID orgId, String orderNumber) {
    return byId.values().stream()
        .filter(o -> o.getOrgId().equals(orgId) && o.getOrderNumber().equals(orderNumber))
        .findFirst();
  }
}
