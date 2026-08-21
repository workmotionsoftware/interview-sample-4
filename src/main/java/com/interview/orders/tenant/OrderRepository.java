package com.interview.orders.tenant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

  Optional<Order> findByOrderNumber(String orderNumber);

  List<Order> findByOrgId(UUID orgId);

  Optional<Order> findByOrgIdAndOrderNumber(UUID orgId, String orderNumber);
}
