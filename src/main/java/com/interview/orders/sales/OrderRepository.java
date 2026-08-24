package com.interview.orders.sales;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  Optional<Order> findByOrgIdAndOrderNumber(UUID orgId, String orderNumber);

  List<Order> findByOrgIdAndPlacedAtBetween(UUID orgId, LocalDate from, LocalDate to);

  default List<Order> findByOrgIdAndMonth(UUID orgId, YearMonth month) {
    return findByOrgIdAndPlacedAtBetween(orgId, month.atDay(1), month.atEndOfMonth());
  }
}
