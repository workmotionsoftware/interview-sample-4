package com.interview.orders.sales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
public class OrderLine {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(nullable = false)
  private String sku;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private BigDecimal unitPrice;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderLineStatus status = OrderLineStatus.ACTIVE;

  protected OrderLine() {
    // for JPA
  }

  public OrderLine(UUID id, Order order, String sku, int quantity, BigDecimal unitPrice) {
    this.id = id;
    this.order = order;
    this.sku = sku;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public UUID getId() {
    return id;
  }

  public Order getOrder() {
    return order;
  }

  public String getSku() {
    return sku;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public OrderLineStatus getStatus() {
    return status;
  }

  public void setStatus(OrderLineStatus status) {
    this.status = status;
  }
}
