# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I would converge on one persistence style rather than mixing active-record entities
(Store), repositories (Product), and an explicit domain port plus adapter
(Warehouse). For a small service, repositories backed by Panache are a good
default: resources/use cases do not need to know persistence details, queries
have one home, and the domain can be tested with an in-memory implementation of
the port.

For warehouses I would keep the port-and-adapter boundary because business rules
already require independent use cases and external location resolution. I would
move Store away from static active-record calls for consistency, and avoid
exposing JPA entities directly from REST resources. DTOs and explicit mappers
make the public API independent from database fields and prevent accidental
persistence changes through request payloads. Database constraints should remain
the final guard for uniqueness/referential integrity, while use cases provide
clear, business-oriented validation messages before that point.

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
OpenAPI-first development gives a versioned, reviewable contract, generated
client/server interfaces, and consistent documentation. It is especially useful
when other teams consume the API or when compatibility must be managed over
time. Its costs are additional build tooling, generated-code conventions, and
some friction when an endpoint needs a response shape or behaviour not expressed
well in the specification.

Code-first endpoints are faster for a small internal service and keep routing,
validation, and implementation close together. However, their documentation can
drift and consumers have no contract to validate against unless an OpenAPI
document is generated and published deliberately.

I would use OpenAPI-first for externally consumed or stable domain APIs such as
Warehouse, with the specification as the source of truth and contract tests in
CI. For internal or rapidly changing endpoints, code-first is reasonable, but I
would still generate and publish OpenAPI from annotations. The important choice
is consistency: every exposed endpoint should have an accurate, tested API
contract.

```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
I would prioritise tests by business risk. First, fast unit tests would cover the
warehouse and fulfilment rules: duplicate business-unit codes, invalid locations,
location count/capacity limits, stock/capacity validation, replacement stock
matching, and each of the three fulfilment limits. These tests should use fake
ports/repositories so that failure cases are cheap to express and run.

Second, I would add focused integration tests against the REST API and a test
database for the important HTTP behaviours: success status codes, 400/404/409
responses, archived warehouses disappearing from active reads, and replacement
being transactional. A Store test should force a transaction rollback and assert
that the legacy gateway was not called; a successful commit should assert that it
was called afterwards.

Finally, CI should run unit tests on every change, integration tests on pull
requests, and a small set of API contract tests from the OpenAPI specification.
Coverage should be treated as a signal rather than a target: retain regression
tests for every defect, review changed business branches in code review, and use
coverage reports to find untested validation paths.

```
