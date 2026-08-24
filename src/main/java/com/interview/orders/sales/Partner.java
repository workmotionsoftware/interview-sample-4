package com.interview.orders.sales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "partners")
public class Partner {

  @Id private UUID id;

  @Column(nullable = false)
  private String name;

  protected Partner() {
    // for JPA
  }

  public Partner(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
