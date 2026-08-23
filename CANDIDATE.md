# Multi-tenant orders API — code review

Meridian Commerce is a B2B marketplace. Several customer organizations share one API. Security review asked engineering to re-check the orders HTTP API before it ships.

Treat the classes in this package as that pull request:

- `com.interview.orders.tenant.OrderController`
- `com.interview.orders.tenant.OrderRepository`
- collaborators in the same package

## What to do

1. Read the controller and repository (and helpers in the same package).
2. Call out anything you would flag in a real review. Name the underlying issue, not only the symptom.
3. Propose and apply fixes for the issues you raised. Be ready to explain each change.

Clarify anything this brief leaves open before you begin.
