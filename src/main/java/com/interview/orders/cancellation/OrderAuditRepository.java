package com.interview.orders.cancellation;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAuditRepository extends JpaRepository<OrderAuditEntry, UUID> {

  List<OrderAuditEntry> findByOrderId(UUID orderId);

  default void record(UUID orgId, UUID orderId, String action, String reason) {
    save(new OrderAuditEntry(UUID.randomUUID(), orgId, orderId, action, reason, Instant.now()));
  }
}
