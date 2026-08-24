# Meridian Commerce — code review

Meridian Commerce is a B2B marketplace. Several customer organizations share one API and one
database. Security review asked engineering to re-check the orders code before it ships.

Treat what follows as a pull request you have been asked to review. For each part:

1. Read the code.
2. Call out anything you would flag in a real review. Name the underlying issue, not only the
   symptom.
3. Propose and apply a fix for the issues you raised, and be ready to explain each change.

Clarify anything a brief leaves open before you begin. You will not be asked to complete every part.

---

## Part 1 — Orders HTTP API

Package `com.interview.orders.tenant`.

- `OrderController`
- `OrderRepository`
- collaborators in the same package

---

## Part 2 — Cancelling an order

Package `com.interview.orders.cancellation`, plus the entities in `com.interview.orders.sales`.

- `OrderService`

**Reported by support:** some orders sit in the database with status `CANCELLED` while their line
items are still `ACTIVE`, and there is no audit row for the cancellation. It does not reproduce
locally.

---

## Part 3 — Monthly order summary

Package `com.interview.orders.reporting`, plus the entities in `com.interview.orders.sales`.

- `OrderReportService`

**Reported by the on-call engineer:** this report takes about eight seconds for an organization with
500 orders. The database is barely working — low CPU, nothing in the slow query log — but the trace
for a single request shows roughly fifteen hundred separate queries.

---

## Part 4 — Order list screen

Directory `frontend/`.

- `OrderList.jsx`

Read-only: there is no build set up and nothing to install. Expect some general questions about
React and about the browser side of an API like this one.

---

## Part 5 — How you would build the next change

Nothing to read in advance. You will be given a small feature and asked to walk through how you
would actually build it with the tools you use day to day — how you prepare, plan, implement and
verify. The feature:

> Let an HR manager download their company's orders for a given month as a CSV. It has to respect
> tenant isolation, it must not load a whole month into memory for a large customer, and it needs
> tests.

You will not be asked to write it.

---

## Running things

Java 21 and Maven. No Docker, no database to install — the app uses an in-memory H2 database.

```bash
mvn test          # compiles and runs the suite
mvn spring-boot:run
```

There are test classes in the repository that are disabled. They are used after the session; leave
them alone unless you are asked otherwise.
