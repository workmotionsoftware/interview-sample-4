package com.interview.orders.reporting;

import com.interview.orders.sales.Customer;
import com.interview.orders.sales.CustomerRepository;
import com.interview.orders.sales.Order;
import com.interview.orders.sales.OrderLine;
import com.interview.orders.sales.OrderRepository;
import com.interview.orders.sales.Partner;
import com.interview.orders.sales.PartnerRepository;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderReportService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final PartnerRepository partnerRepository;

  public OrderReportService(
      OrderRepository orderRepository,
      CustomerRepository customerRepository,
      PartnerRepository partnerRepository) {
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.partnerRepository = partnerRepository;
  }

  @Transactional(readOnly = true)
  public List<OrderSummaryResponse> buildMonthlySummary(UUID orgId, YearMonth month) {
    List<Order> orders = orderRepository.findByOrgIdAndMonth(orgId, month);

    List<OrderSummaryResponse> summaries = new ArrayList<>();

    for (Order order : orders) {

      BigDecimal total = BigDecimal.ZERO;
      for (OrderLine line : order.getLines()) {
        total = total.add(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
      }

      Customer customer = customerRepository.findById(order.getCustomerId()).orElseThrow();

      String partnerName =
          partnerRepository.findById(order.getPartnerId()).map(Partner::getName).orElse("UNKNOWN");

      summaries.add(
          new OrderSummaryResponse(
              order.getOrderNumber(), customer.getName(), partnerName, total));
    }

    return summaries;
  }
}
