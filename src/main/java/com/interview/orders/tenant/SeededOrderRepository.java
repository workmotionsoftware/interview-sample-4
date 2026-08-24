package com.interview.orders.tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

/**
 * Fixture-backed store so the application can start without a database. Two organizations share it,
 * which mirrors the production setup.
 */
@Repository
public class SeededOrderRepository implements OrderRepository {

  private static final UUID ORG_A = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  private static final UUID ORG_B = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

  private final ConcurrentMap<UUID, Order> byId = new ConcurrentHashMap<>();

  public SeededOrderRepository() {
    save(new Order(UUID.randomUUID(), ORG_A, "ORD-1001"));
    save(new Order(UUID.randomUUID(), ORG_A, "ORD-1002"));
    save(new Order(UUID.randomUUID(), ORG_B, "ORD-2001"));
  }

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
