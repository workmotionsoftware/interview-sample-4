package com.interview.orders.cancellation;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Stands in for the real HTTP client in this sample. The inventory service is not part of the
 * exercise, so this implementation only records that the call was made.
 */
@Component
public class RestInventoryClient implements InventoryClient {

  private static final Logger log = LoggerFactory.getLogger(RestInventoryClient.class);

  @Override
  public void releaseReservation(UUID orderId) {
    log.info("POST /reservations/{}/release", orderId);
  }
}
