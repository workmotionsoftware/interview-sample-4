package com.interview.orders.sales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID orgId;

  @Column(nullable = false)
  private UUID customerId;

  private UUID partnerId;

  @Column(nullable = false)
  private String orderNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status = OrderStatus.ACTIVE;

  @Column(nullable = false)
  private LocalDate placedAt;

  private String cancellationReason;

  private Instant cancelledAt;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderLine> lines = new ArrayList<>();

  protected Order() {
    // for JPA
  }

  public Order(UUID id, UUID orgId, UUID customerId, UUID partnerId, String orderNumber,
      LocalDate placedAt) {
    this.id = id;
    this.orgId = orgId;
    this.customerId = customerId;
    this.partnerId = partnerId;
    this.orderNumber = orderNumber;
    this.placedAt = placedAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getOrgId() {
    return orgId;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public UUID getPartnerId() {
    return partnerId;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public LocalDate getPlacedAt() {
    return placedAt;
  }

  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public Instant getCancelledAt() {
    return cancelledAt;
  }

  public void setCancelledAt(Instant cancelledAt) {
    this.cancelledAt = cancelledAt;
  }

  public List<OrderLine> getLines() {
    return lines;
  }
}
