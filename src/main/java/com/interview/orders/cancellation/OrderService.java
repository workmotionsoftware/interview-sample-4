package com.interview.orders.cancellation;

import com.interview.orders.sales.Order;
import com.interview.orders.sales.OrderLine;
import com.interview.orders.sales.OrderLineRepository;
import com.interview.orders.sales.OrderLineStatus;
import com.interview.orders.sales.OrderRepository;
import com.interview.orders.sales.OrderStatus;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderLineRepository orderLineRepository;
  private final OrderAuditRepository auditRepository;
  private final InventoryClient inventoryClient;

  public OrderService(
      OrderRepository orderRepository,
      OrderLineRepository orderLineRepository,
      OrderAuditRepository auditRepository,
      InventoryClient inventoryClient) {
    this.orderRepository = orderRepository;
    this.orderLineRepository = orderLineRepository;
    this.auditRepository = auditRepository;
    this.inventoryClient = inventoryClient;
  }

  public CancelledOrder cancelOrder(UUID orgId, String orderNumber, String reason) {
    Order order =
        orderRepository
            .findByOrgIdAndOrderNumber(orgId, orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Order not found"));

    this.applyCancellation(order, reason);

    auditRepository.record(orgId, order.getId(), "ORDER_CANCELLED", reason);
    inventoryClient.releaseReservation(order.getId());

    return CancelledOrder.from(order);
  }

  @Transactional
  public void applyCancellation(Order order, String reason) {
    order.setStatus(OrderStatus.CANCELLED);
    order.setCancellationReason(reason);
    order.setCancelledAt(Instant.now());
    orderRepository.save(order);

    for (OrderLine line : orderLineRepository.findByOrderId(order.getId())) {
      line.setStatus(OrderLineStatus.CANCELLED);
      orderLineRepository.save(line);
    }
  }
}
