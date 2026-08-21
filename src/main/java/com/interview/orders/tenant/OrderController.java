package com.interview.orders.tenant;

import java.util.List;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orgs/{orgId}/orders")
public class OrderController {

  private final OrderRepository orderRepository;

  public OrderController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @GetMapping("/{orderNumber}")
  public OrderResponse getByNumber(
      @PathVariable UUID orgId,
      @PathVariable String orderNumber,
      @AuthenticationPrincipal AuthenticatedUser user) {
    assertCanAccessOrg(user, orgId);
    Order order =
        orderRepository
            .findByOrderNumber(orderNumber)
            .orElseThrow(() -> new NotFoundException("Order not found"));
    return OrderResponse.from(order);
  }

  @GetMapping
  public List<OrderResponse> search(
      @PathVariable UUID orgId,
      @RequestParam(required = false) String orderNumber,
      @AuthenticationPrincipal AuthenticatedUser user) {
    assertCanAccessOrg(user, orgId);
    if (orderNumber != null) {
      return orderRepository.findByOrderNumber(orderNumber).map(OrderResponse::from).stream()
          .toList();
    }
    return orderRepository.findByOrgId(orgId).stream().map(OrderResponse::from).toList();
  }

  private void assertCanAccessOrg(AuthenticatedUser user, UUID orgId) {
    if (!user.isAdmin() && !user.getOrgId().equals(orgId)) {
      throw new ForbiddenException("Not allowed");
    }
  }
}
