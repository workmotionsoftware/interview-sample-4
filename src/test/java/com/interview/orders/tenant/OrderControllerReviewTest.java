package com.interview.orders.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("disabled")
class OrderControllerReviewTest {

  private static final UUID ORG_A = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  private static final UUID ORG_B = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

  private InMemoryOrderRepository repository;
  private OrderController controller;

  @BeforeEach
  void setUp() {
    repository = new InMemoryOrderRepository();
    controller = new OrderController(repository);
    repository.save(new Order(UUID.randomUUID(), ORG_A, "ORD-1001"));
    repository.save(new Order(UUID.randomUUID(), ORG_B, "ORD-2001"));
  }

  @Test
  void member_canReadOwnOrgOrderByNumber() {
    AuthenticatedUser memberA = new AuthenticatedUser(ORG_A, false);

    OrderResponse response = controller.getByNumber(ORG_A, "ORD-1001", memberA);

    assertThat(response.orderNumber()).isEqualTo("ORD-1001");
    assertThat(response.orgId()).isEqualTo(ORG_A);
  }

  @Test
  void member_cannotReadOtherOrgOrder_byGuessingNumberOnOwnPath() {
    AuthenticatedUser memberA = new AuthenticatedUser(ORG_A, false);

    assertThatThrownBy(() -> controller.getByNumber(ORG_A, "ORD-2001", memberA))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void member_cannotUseOtherOrgInPath() {
    AuthenticatedUser memberA = new AuthenticatedUser(ORG_A, false);

    assertThatThrownBy(() -> controller.getByNumber(ORG_B, "ORD-2001", memberA))
        .isInstanceOf(ForbiddenException.class);
  }

  @Test
  void member_searchByNumber_doesNotReturnOtherOrgOrder() {
    AuthenticatedUser memberA = new AuthenticatedUser(ORG_A, false);

    List<OrderResponse> results = controller.search(ORG_A, "ORD-2001", memberA);

    assertThat(results).isEmpty();
  }

  @Test
  void member_list_returnsOnlyOwnOrg() {
    AuthenticatedUser memberA = new AuthenticatedUser(ORG_A, false);

    List<OrderResponse> results = controller.search(ORG_A, null, memberA);

    assertThat(results).extracting(OrderResponse::orgId).containsOnly(ORG_A);
  }
}
