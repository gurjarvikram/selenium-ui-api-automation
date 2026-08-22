# selenium-ui-api-automation

Hybrid UI **and** API automation framework for the [Swag Labs style ecommerce demo](https://rahulshettyacademy.com/client/) — Selenium 4, REST Assured, TestNG, with API-driven setup and cross-layer verification.

The UI suite and the API suite target the **same application**: the browser drives `rahulshettyacademy.com/client`, and the API layer talks to that app's own `/api/ecom/*` backend. That is what makes the hybrid layer meaningful rather than two unrelated test sets sharing a repository.

---

## Why hybrid

Most UI suites re-drive the login form for every test and assert only on what the front end renders. Both are avoidable.

| Pattern | Where | What it buys |
|---|---|---|
| **API login, injected session** | `ApiLoginUiJourneyTest` | Token from `/auth/login` is written into `localStorage`, so tests start on the dashboard. Removes a page load and a form round-trip from every test, and a broken login form fails *one* test instead of the whole suite. |
| **API setup → UI assertion** | `ApiSetupUiVerificationTest` | Seeds a product and an order over the backend, then checks the UI lists it. If it fails, the defect is in *display*, not in checkout. |
| **UI action → API verification** | `UiOrderApiVerificationTest` | Places the order in the browser, then reads it back from the backend. A confirmation banner only proves the front end *said* the right thing; this proves it was persisted. |
| **API teardown** | `OrderApiTest`, `ApiSetupUiVerificationTest` | Fixtures are deleted over the API in `@AfterMethod`, so the suite is re-runnable against the same account. |

## Layout

```
src/main/java/com/vikram/
├── core/          ConfigManager · DriverFactory · DriverManager (ThreadLocal)
├── ui/pages/      Page objects, assertions included · SessionManager
├── ui/components/ AbstractComponent — shared waits and header navigation
├── api/clients/   AuthClient · ProductClient · OrderClient
├── api/specs/     SpecFactory — base, authenticated and multipart specs
├── api/models/    Request/response POJOs
├── api/endpoints/ ApiEndpoints enum — every route in one place
├── reporting/     ExtentReporterNG
└── utils/         JsonUtils · ScreenshotUtils

src/test/java/com/vikram/
├── base/          BaseUiTest · BaseApiTest · BaseHybridTest
├── listeners/     Listeners (Extent + screenshots) · Retry
└── tests/         ui/ · api/ · hybrid/
```

## Running

Credentials are read from the environment and never committed:

```bash
cp .env.example .env      # fill in a real demo account
set -a && source .env && set +a
```

```bash
mvn test                     # regression (default)
mvn test -P smoke            # one test per layer — the CI gate
mvn test -P api              # API only; no browser required
mvn test -P ui               # browser journeys
mvn test -P hybrid           # cross-layer tests
```

Overrides work as `-Dkey=value` or as upper-cased environment variables:

```bash
mvn test -P ui -Dbrowser=firefox -Dheadless=true
mvn test -P ui -Dgrid.url=http://localhost:4444    # same suite, on a Grid
```

| Key | Default | Purpose |
|---|---|---|
| `browser` | `chrome` | `chrome` · `firefox` · `edge` |
| `headless` | `false` | Headless run |
| `grid.url` | *(empty)* | Route at a Selenium Grid instead of a local browser |
| `ui.base.url` | demo app | Application under test |
| `api.base.url` | demo backend | API root |
| `retry.count` | `1` | Retries for a failed test |
| `api.log.requests` | `false` | Log API request/response bodies while debugging |

`ECOM_USER_EMAIL` and `ECOM_USER_PASSWORD` are required and resolve from the environment only.

## Design notes

**Thread-safe by construction.** Suites run `parallel="classes"`. The driver lives in a `ThreadLocal` inside `DriverManager` rather than on a base-class field, so parallel classes never share a browser. The Extent listener reads the driver from the same place instead of reflecting it off the test instance, which is what lets the API suite — which starts no browser — share one listener.

**Configuration fails loudly.** `ConfigManager.get` throws with the key name and the three ways to supply it rather than returning `null`. An unsupported browser name is rejected at driver creation, not silently defaulted.

**Grid is a config switch, not a test.** Any suite runs remotely by setting `grid.url`; there is no separate "grid test" class.

**Reports are build output.** Extent HTML and failure screenshots are written under `target/reports/` and uploaded as CI artifacts, so nothing generated is tracked in git.

## CI

`.github/workflows/ci.yml` runs the API suite first as a fast gate, then fans out UI and hybrid across Chrome and Firefox headless. Reports and failure screenshots upload on every run, including failures. A nightly cron runs the full regression. Credentials come from repository secrets of the same names.

## Tooling

Selenium 4.47 · REST Assured 6.0 · TestNG 7.12 · Jackson 2.22 · Extent Reports 5.1 · Java 21 (LTS) · Maven

Driver binaries are resolved by Selenium Manager, so there is no WebDriverManager dependency and nothing to install.
