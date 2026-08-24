package com.interview.orders.cancellation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_audit_entries")
public class OrderAuditEntry {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID orgId;

  @Column(nullable = false)
  private UUID orderId;

  @Column(nullable = false)
  private String action;

  private String reason;

  @Column(nullable = false)
  private Instant recordedAt;

  protected OrderAuditEntry() {
    // for JPA
  }

  public OrderAuditEntry(UUID id, UUID orgId, UUID orderId, String action, String reason,
      Instant recordedAt) {
    this.id = id;
    this.orgId = orgId;
    this.orderId = orderId;
    this.action = action;
    this.reason = reason;
    this.recordedAt = recordedAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getOrgId() {
    return orgId;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public String getAction() {
    return action;
  }

  public String getReason() {
    return reason;
  }

  public Instant getRecordedAt() {
    return recordedAt;
  }
}
