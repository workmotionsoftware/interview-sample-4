package com.interview.orders.tenant;

import java.util.UUID;

public class AuthenticatedUser {

  private final UUID orgId;
  private final boolean admin;

  public AuthenticatedUser(UUID orgId, boolean admin) {
    this.orgId = orgId;
    this.admin = admin;
  }

  public UUID getOrgId() {
    return orgId;
  }

  public boolean isAdmin() {
    return admin;
  }
}
