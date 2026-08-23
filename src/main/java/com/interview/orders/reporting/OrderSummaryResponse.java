package com.interview.orders.reporting;

import java.math.BigDecimal;

public record OrderSummaryResponse(String orderNumber, String customerName, String partnerName,
    BigDecimal total) {}
