# interview-sample-4

Code-review interview sample (Java 21, Spring Boot 3.4). No Docker.

## Brief

Open [CANDIDATE.md](CANDIDATE.md). It names the package to review and how to run the checks.

## Run

Track tests stay `@Disabled` until the interviewer enables them. Then:

```bash
mvn test -Dtest=OrderControllerReviewTest
```

Optional:

```bash
mvn spring-boot:run
```
