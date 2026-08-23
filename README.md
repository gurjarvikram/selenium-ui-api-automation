# selenium-ui-api-automation

Hybrid **UI and API** test automation framework — Selenium 4, REST Assured and TestNG driving the
same application from both layers, with API-driven setup and cross-layer verification.

[![CI](https://github.com/gurjarvikram/selenium-ui-api-automation/actions/workflows/ci.yml/badge.svg)](https://github.com/gurjarvikram/selenium-ui-api-automation/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21%20LTS-orange)](https://adoptium.net/)
[![Selenium](https://img.shields.io/badge/Selenium-4.47-brightgreen)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.12-blue)](https://testng.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

The UI suite and the API suite target the **same application**: the browser drives
[`rahulshettyacademy.com/client`](https://rahulshettyacademy.com/client/), and the API layer talks to
that app's own `/api/ecom/*` backend. That is what makes the hybrid layer meaningful rather than two
unrelated test sets sharing a repository.

**Contents** — [Why hybrid](#why-hybrid) · [What is covered](#what-is-covered) ·
[Tech stack](#tech-stack) · [Getting started](#getting-started) · [Running the suites](#running-the-suites) ·
[Configuration](#configuration) · [Reports and failure evidence](#reports-and-failure-evidence) ·
[Project layout](#project-layout) · [Design notes](#design-notes) · [Quality gate](#quality-gate) ·
[CI](#ci) · [Troubleshooting](#troubleshooting)

---

## Why hybrid

Most UI suites re-drive the login form for every test and assert only on what the front end renders.
Both are avoidable.

| Pattern | Where | What it buys |
|---|---|---|
| **API login, injected session** | `ApiLoginUiJourneyTest` | Token from `/auth/login` is written into `localStorage`, so tests start on the dashboard. Removes a page load and a form round-trip from every test, and a broken login form fails *one* test instead of the whole suite. |
| **API setup → UI assertion** | `ApiSetupUiVerificationTest` | Seeds a product and an order over the backend, then checks the UI lists it. If it fails, the defect is in *display*, not in checkout. |
| **UI action → API verification** | `UiOrderApiVerificationTest` | Places the order in the browser, then reads it back from the backend. A confirmation banner only proves the front end *said* the right thing; this proves it was persisted. |
| **API teardown** | `OrderApiTest`, `ApiSetupUiVerificationTest` | Fixtures are deleted over the API in `@AfterMethod`, so the suite is re-runnable against the same account. |

## What is covered

Eleven tests across three layers. Every test carries TestNG groups, which is how the suite files
select them.

| Layer | Class | Test | Groups |
|---|---|---|---|
| API | `AuthApiTest` | `loginReturnsToken` | `smoke`, `api` |
| API | `AuthApiTest` | `loginRejectsWrongPassword` | `negative`, `api` |
| API | `OrderApiTest` | `createsAndDeletesProduct` | `smoke`, `api` |
| API | `OrderApiTest` | `placesOrderForCreatedProduct` | `regression`, `api` |
| UI | `SubmitOrderTest` | `submitOrderAsStandardCustomer` | `smoke` |
| UI | `SubmitOrderTest` | `submitOrder` *(data-driven, one row per role)* | `regression` |
| UI | `ErrorValidationsTest` | `rejectsIncorrectPassword` | `negative`, `regression` |
| UI | `ErrorValidationsTest` | `cartDoesNotShowUnaddedProduct` | `negative`, `regression` |
| Hybrid | `ApiLoginUiJourneyTest` | `completesCheckoutAfterApiLogin` | `smoke`, `hybrid` |
| Hybrid | `ApiSetupUiVerificationTest` | `orderCreatedViaApiIsListedInUi` | `regression`, `hybrid` |
| Hybrid | `UiOrderApiVerificationTest` | `orderPlacedInUiIsPersistedInBackend` | `regression`, `hybrid` |

`smoke` runs one representative test per layer, so the purchase journey runs once. `regression` runs
the same journey for every role in the fixture, plus the negative and cross-layer cases.

## Tech stack

| Concern | Choice |
|---|---|
| Browser automation | Selenium 4.47 (Selenium Manager resolves drivers — nothing to install) |
| API testing | REST Assured 6.0 + Jackson 2.22 |
| Test runner | TestNG 7.12 |
| Assertions | AssertJ 3.27 |
| Reporting | Extent Reports 5.1 |
| Logging | SLF4J 2.0 (simple binding) |
| Build | Maven, Java 21 (LTS) |
| Static analysis | Checkstyle + Maven Enforcer, bound to `validate` |
| CI | GitHub Actions, with Dependabot grouping Maven and Actions updates |

## Getting started

### Prerequisites

| Requirement | Version | Notes |
|---|---|---|
| JDK | 21 or newer | Enforced by `maven-enforcer-plugin`; the build fails on anything older |
| Maven | 3.8 or newer | Also enforced |
| Browser | Chrome, Firefox or Edge | Only for the UI and hybrid suites — the API suite needs none |
| Demo account | — | A registered account on the demo site; the suites do not self-register |

Driver binaries are resolved by Selenium Manager at run time, so there is no WebDriverManager
dependency and nothing to download by hand. The first run needs network access to fetch the driver.

### Quick start

```bash
git clone https://github.com/gurjarvikram/selenium-ui-api-automation.git
cd selenium-ui-api-automation

cp .env.example .env             # fill in a real demo account
set -a && source .env && set +a  # export it into the shell

mvn test -P smoke                # one test per layer, ~1 minute
```

Credentials are read from the environment and never committed. `ECOM_USER_EMAIL` and
`ECOM_USER_PASSWORD` are required and resolve from the environment only — never from a tracked file.
Adding a second role means one constant in `UserRole` and two more variables; no test changes.

## Running the suites

Each profile selects a TestNG suite file under `testSuites/`. Checkstyle and the enforcer run first
in every one of them, at the `validate` phase.

```bash
mvn test                     # regression (default) — everything
mvn test -P smoke            # one test per layer — the CI gate
mvn test -P api              # API only; no browser required
mvn test -P ui               # browser journeys
mvn test -P hybrid           # cross-layer tests
```

```bash
mvn test -P ui -Dbrowser=firefox -Dheadless=true
mvn test -P ui -Dgrid.url=http://localhost:4444    # same suite, on a Grid
mvn test -Dsuite.file=testSuites/api.xml           # any suite file, without a profile
```

> **Running a single class** with `-Dtest=SubmitOrderTest` works, but Surefire then ignores
> `suiteXmlFiles` — and the Extent listener and retry analyser are registered *in* the suite files.
> Expect no HTML report and no retries from that form. Prefer `-Dsuite.file=` for anything you want
> reported.

## Configuration

Values resolve in this order, first match winning: `-Dkey=value` → environment variable
(upper-cased, dots to underscores) → `env-{name}.properties` → `config.properties`. Credentials are
the exception: they resolve from the environment only.

| Key | Env variable | Default | Purpose |
|---|---|---|---|
| `browser` | `BROWSER` | `chrome` | `chrome` · `firefox` · `edge` |
| `headless` | `HEADLESS` | `false` | Headless run |
| `grid.url` | `GRID_URL` | *(empty)* | Route at a Selenium Grid instead of a local browser |
| `retry.count` | `RETRY_COUNT` | `1` | Retries for a failed test, applied to every test |
| `env` | `ENV` | `demo` | Environment profile; an unknown name fails the build |
| `ui.base.url` | `UI_BASE_URL` | demo site | Front end under test |
| `api.base.url` | `API_BASE_URL` | demo site | Backend under test |
| `timeout.explicit.seconds` | `TIMEOUT_EXPLICIT_SECONDS` | `15` | Explicit wait ceiling |
| `timeout.pageload.seconds` | `TIMEOUT_PAGELOAD_SECONDS` | `30` | Page load ceiling |
| `api.log.requests` | `API_LOG_REQUESTS` | `false` | Log API request/response bodies while debugging |

`api.log.requests` is off by default because it prints the login payload and bearer token in
plaintext. Turn it on only while debugging a call.

The two base URLs are configuration rather than secrets, so they live in `env-demo.properties` and
come from repository **variables** in CI — not the secret store — which keeps them readable in the
logs where they are useful.

## Reports and failure evidence

Everything generated is build output under `target/`, and nothing is tracked in git.

| Artefact | Path |
|---|---|
| Extent HTML report | `target/reports/index.html` |
| Failure screenshots | `target/reports/screenshots/` |
| TestNG / Surefire XML | `target/surefire-reports/` |

```bash
mvn test -P smoke
xdg-open target/reports/index.html   # macOS: open
```

CI uploads `target/reports/` as an artifact on every run, including failures.

## Project layout

```
src/main/java/com/vikram/
├── core/          ConfigManager · DriverFactory · DriverManager (ThreadLocal)
├── core/users/    UserRole · User (password-masking) · UserManager
├── ui/pages/      Page objects · SessionManager
├── ui/components/ AbstractComponent — shared header navigation
├── ui/            ObjectRepository (locators) · Waits (explicit-only)
├── ui/            UiRoute · Routes — named routes, no URL string building
├── api/clients/   AuthClient · ProductClient · OrderClient
├── api/specs/     SpecFactory — base, authenticated and multipart specs
├── api/models/    Request/response POJOs
├── api/endpoints/ ApiEndpoints enum — every route in one place
├── core/exceptions/ FrameworkException · ConfigurationException
├── reporting/     ExtentReporterNG
└── utils/         JsonUtils · ScreenshotUtils

src/main/resources/objectrepository/   one .properties file per page
src/test/resources/config/             config.properties + env-{name}.properties

src/test/java/com/vikram/
├── base/          BaseUiTest · BaseApiTest · BaseHybridTest
├── listeners/     Listeners (Extent + screenshots) · Retry · RetryTransformer
└── tests/         ui/ · api/ · hybrid/

testSuites/        api · ui · hybrid · smoke · regression
config/            checkstyle.xml
.github/workflows/ ci.yml
```

## Design notes

**Credentials resolve through one place.** `UserManager.standardCustomer()` returns a `User`; the
environment-variable names live in `UserRole` and nowhere else. Tests never touch
`ConfigManager.getSecret`, so renaming a variable is a one-line change instead of seven.
`User.toString()` masks the password on purpose — test names, assertion messages, retry logs and
Extent output all stringify what they are handed, and a record's generated `toString` would print it
in every one of them.

**URLs are named routes.** `UiRoute` holds the paths (`LOGIN`, `DASHBOARD`, `CART`, `ORDERS`) and
`Routes` joins them onto `ui.base.url`, tolerating a trailing slash either way. Deep links used to be
built by concatenation at the point of use, which put `"dashboard/dash"` inside a page object. The
API side has the same treatment in `ApiEndpoints`.

**Locators live outside the code.** Each page has a file under
`src/main/resources/objectrepository/` holding `name = strategy:value` entries. A markup change is a
one-line edit in a data file, and every locator for a page is visible in one place.
`ObjectRepository` parses and caches them, failing with the available keys when one is missing. Page
objects use plain `By` lookups rather than PageFactory proxies, which re-resolve on each use and so
do not go stale when the page re-renders.

**Explicit waits only — no implicit wait is ever set.** Mixing the two makes timeouts unpredictable:
the implicit wait applies inside each polling cycle of the explicit one, so a documented 15-second
wait can take far longer, and a negative check that should fail fast pays the implicit timeout on
every poll. Everything goes through `Waits`, and Checkstyle fails the build if `implicitlyWait` or
`Thread.sleep` reappears.

**Configuration is layered and rejects unknown environments.** Shared defaults sit in
`config.properties`; per-deployment values in `env-{name}.properties`. `-Denv=staging` with no
matching profile fails immediately, naming the known environments — a typo in a CI variable stops the
build instead of quietly running against the wrong target.

**Test data is role-keyed, not positional.** Fixtures map a role name to its fields, so a test asks
for `standardCustomer` rather than row 0 and reordering the file cannot silently repoint a test at
different data.

**Failures are typed.** A `FrameworkException` means the harness is misconfigured and no assertion
ran; an `AssertionError` means the application misbehaved. That distinction is what makes a red build
triageable at a glance.

**Fixtures cannot collide with real data.** API fixture products are named `FIXTURE-<random>`,
generated per call from a UUID. The name was previously a real catalogue product, so under parallel
execution a UI test could add the API test's fixture to its cart and then have it deleted mid-run.

**Failure evidence survives the trip.** Screenshots are written under `target/reports/screenshots/`
and handed to Extent as a path *relative to the report*, because an absolute path resolves only on
the machine that produced it — every image in a downloaded CI artifact would otherwise be broken.
Names carry a sequence number so two failing rows of one data-driven test do not overwrite each
other.

**Parallelism is split, on purpose.** Every suite authenticates as one account, and that account has
one server-side cart. Two browser journeys running concurrently fight over it — one checks out,
empties the cart, and the other's assertion fails. The API tests are exempt because they create
uniquely named fixtures and never touch the cart, so they still run in parallel alongside a
serialized browser block. With per-thread accounts the browser block could parallelise too; that is
the change to make against an environment where users can be provisioned.

**Retry is uniform.** `RetryTransformer` applies the analyser to every test through the suite files.
Previously three of seven classes opted in by annotation and the rest silently did not. Every retry
is logged so flakiness stays visible.

**Thread-safe by construction.** Suites run `parallel="classes"`. The driver lives in a `ThreadLocal`
inside `DriverManager` rather than on a base-class field, so parallel classes never share a browser.
The Extent listener reads the driver from the same place instead of reflecting it off the test
instance, which is what lets the API suite — which starts no browser — share one listener.

**Configuration fails loudly.** `ConfigManager.get` throws with the key name and the three ways to
supply it rather than returning `null`. An unsupported browser name is rejected at driver creation,
not silently defaulted.

**Grid is a config switch, not a test.** Any suite runs remotely by setting `grid.url`; there is no
separate "grid test" class.

**Reports are build output.** Extent HTML and failure screenshots are written under `target/reports/`
and uploaded as CI artifacts, so nothing generated is tracked in git.

## Quality gate

Checkstyle and the enforcer are bound to the `validate` phase, so they run ahead of every `mvn test`
and fail the build before a browser starts. To run them alone:

```bash
mvn checkstyle:check enforcer:enforce
```

Checkstyle carries the usual style rules plus three framework-specific bans, each with the reason in
its failure message:

- `Thread.sleep` — use an explicit wait via `com.vikram.ui.Waits`
- `implicitlyWait` — implicit waits must not be mixed with explicit ones
- `System.out` / `System.err` — use the SLF4J logger

The enforcer requires Maven 3.8+ and Java 21+, and bans duplicate dependency versions in the POM. Its
rules sit at plugin level rather than execution level, so `mvn enforcer:enforce` from the command
line sees exactly the rules the build does.

## CI

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs on push and pull request to `main`, on
manual dispatch, and nightly at 02:00 UTC as a canary against the live demo site.

| Job | Runs | Why |
|---|---|---|
| **Quality gate** | `checkstyle:check enforcer:enforce` | Blocks everything downstream, so a style or anti-pattern regression never spends browser minutes |
| **API contracts** | `mvn -P api test` | No browser, finishes in seconds, catches contract breaks early |
| **Smoke on chrome** | `mvn -P smoke test -Dheadless=true` | One representative journey per layer, on one browser |

Each job gates the next. The wider journeys stay in `mvn -P regression`, run locally or on demand,
rather than spending browser minutes on every push to re-prove the same page objects.

The workflow uses a repository-wide `concurrency` group with `cancel-in-progress: false`. Every suite
authenticates as the same account with a single server-side cart, so two overlapping runs — two pull
requests, or a push landing during the nightly — would check out and empty each other's carts and
surface as an unrelated "cart is empty" failure. Queueing rather than cancelling keeps every commit
tested; the trade is that a second run waits a couple of minutes.

Reports and failure screenshots upload on every run, including failures, with seven-day retention.
Credentials come from repository secrets (`ECOM_USER_EMAIL`, `ECOM_USER_PASSWORD`); base URLs come
from repository variables.

## Troubleshooting

| Symptom | Cause and fix |
|---|---|
| `Missing credential 'ECOM_USER_EMAIL'` | The `.env` file was not exported. Run `set -a && source .env && set +a`, or pass `-DECOM_USER_EMAIL=...` |
| `Unknown environment 'staging'` | `-Denv` named a profile that does not exist. Add `src/test/resources/config/env-staging.properties` and register the name in `ConfigManager.KNOWN_ENVIRONMENTS` |
| `Missing configuration key '<key>'` | The key is absent from both properties files. The message names the three ways to supply it |
| API tests return 401 | The demo account's password changed, or the account was reset. Re-check `.env` against a fresh login on the site |
| "Cart is empty" in an unrelated test | Two runs are sharing one account. Browser suites are serialized for this reason — do not run two of them concurrently against the same credentials |
| Checkstyle fails on `Thread.sleep` | Intentional. Replace it with an explicit wait from `com.vikram.ui.Waits` |
| No HTML report after a run | `-Dtest=` bypasses the suite files, and the listeners live there. Use a profile or `-Dsuite.file=` |
| Driver download fails on first run | Selenium Manager needs network access to fetch the browser driver. Behind a proxy, set `HTTPS_PROXY` |

## License

[MIT](LICENSE) © gurjarvikram

## Author

Built and maintained by [@gurjarvikram](https://github.com/gurjarvikram).
Issues and pull requests are welcome.
