# interview-sample-4

Code-review interview sample (Java 21, Spring Boot 3.4, in-memory H2). No Docker.

## Brief

Candidates start at [CANDIDATE.md](CANDIDATE.md). It names the packages to review, the symptom
reported for each one, and how to run the checks.

## Layout

| Path | Part |
|---|---|
| `com.interview.orders.tenant` | 1 — orders HTTP API |
| `com.interview.orders.cancellation` | 2 — cancelling an order |
| `com.interview.orders.reporting` | 3 — monthly order summary |
| `com.interview.orders.sales` | JPA entities and repositories shared by parts 2 and 3 |
| `frontend/` | 4 — order list screen, read-only |

## For interviewers

Each part ships with a verification suite that is `@Disabled` so `mvn test` stays green for the
candidate and the assertions do not give the answers away. To run them against a candidate's fix:

```bash
mvn test -Djunit.jupiter.conditions.deactivate='*'
```

Against the code as committed, three of those tests fail and each failure is the planted issue:

- `OrderCancellationReviewTest` — the order commits as `CANCELLED` even though the cancellation
  failed part way through (twice: once for a failing audit write, once for a failing line write).
- `OrderReportQueryCountTest` — 61 statements for a 20-order report, against a budget of 6.

Part 1's suite passes only once the cross-tenant reads are scoped.

The interviewer guide, answer keys and scorecard live in Confluence.
