package com.interview.orders.cancellation;

import java.util.UUID;

/** Calls the inventory service over HTTP to give reserved stock back. */
public interface InventoryClient {

  void releaseReservation(UUID orderId);
}
