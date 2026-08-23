package com.interview.orders.reporting;

import static org.assertj.core.api.Assertions.assertThat;

import com.interview.orders.sales.Customer;
import com.interview.orders.sales.CustomerRepository;
import com.interview.orders.sales.Order;
import com.interview.orders.sales.OrderLine;
import com.interview.orders.sales.OrderLineRepository;
import com.interview.orders.sales.OrderRepository;
import com.interview.orders.sales.Partner;
import com.interview.orders.sales.PartnerRepository;
import jakarta.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("disabled")
class OrderReportQueryCountTest {

  private static final UUID ORG_A = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  private static final YearMonth MONTH = YearMonth.of(2026, 3);
  private static final int ORDER_COUNT = 20;

  /**
   * One query for the orders, plus a small, constant number for the related data. Deliberately
   * generous: the point is that the count must not grow with the number of orders.
   */
  private static final int STATEMENT_BUDGET = 6;

  @Autowired private OrderReportService reportService;
  @Autowired private OrderRepository orderRepository;
  @Autowired private OrderLineRepository orderLineRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private PartnerRepository partnerRepository;
  @Autowired private EntityManagerFactory entityManagerFactory;

  @BeforeEach
  void setUp() {
    orderLineRepository.deleteAll();
    orderRepository.deleteAll();
    customerRepository.deleteAll();
    partnerRepository.deleteAll();

    for (int i = 0; i < ORDER_COUNT; i++) {
      UUID customerId = UUID.randomUUID();
      UUID partnerId = UUID.randomUUID();
      customerRepository.save(new Customer(customerId, "Customer " + i));
      partnerRepository.save(new Partner(partnerId, "Partner " + i));

      Order order =
          orderRepository.save(
              new Order(
                  UUID.randomUUID(),
                  ORG_A,
                  customerId,
                  partnerId,
                  "ORD-" + (1000 + i),
                  MONTH.atDay(1 + (i % 27))));

      orderLineRepository.save(
          new OrderLine(UUID.randomUUID(), order, "SKU-A", 2, new BigDecimal("15.50")));
      orderLineRepository.save(
          new OrderLine(UUID.randomUUID(), order, "SKU-B", 1, new BigDecimal("40.00")));
    }
  }

  @Test
  void monthlySummary_doesNotSendOneQueryPerOrder() {
    Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
    statistics.clear();

    List<OrderSummaryResponse> summaries = reportService.buildMonthlySummary(ORG_A, MONTH);

    assertThat(summaries).hasSize(ORDER_COUNT);
    assertThat(statistics.getPrepareStatementCount())
        .as(
            "statements sent to the database for a single %d-order report — this must stay flat as"
                + " the report grows",
            ORDER_COUNT)
        .isLessThanOrEqualTo(STATEMENT_BUDGET);
  }

  @Test
  void monthlySummary_totalsEachOrderCorrectly() {
    List<OrderSummaryResponse> summaries = reportService.buildMonthlySummary(ORG_A, MONTH);

    assertThat(summaries)
        .allSatisfy(
            summary -> assertThat(summary.total()).isEqualByComparingTo(new BigDecimal("71.00")));
  }
}
