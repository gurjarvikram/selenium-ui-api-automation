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
| `retry.count` | `1` | Retries for a failed test, applied to every test |
| `env` | `demo` | Environment profile; an unknown name fails the build |
| `timeout.explicit.seconds` | `15` | Explicit wait ceiling |
| `api.log.requests` | `false` | Log API request/response bodies while debugging |
| `timeout.pageload.seconds` | `30` | Page load ceiling |

`ECOM_USER_EMAIL` and `ECOM_USER_PASSWORD` are required and resolve from the environment only — never from a tracked file. Adding a second role means one constant in `UserRole` and two more variables; no test changes.

Suite scope: `smoke` runs one representative test per layer, so the purchase journey runs once. `regression` runs the same journey for every role in the fixture, plus the negative and cross-layer cases.

## Design notes

**Credentials resolve through one place.** `UserManager.standardCustomer()` returns a `User`; the environment-variable names live in `UserRole` and nowhere else. Tests never touch `ConfigManager.getSecret`, so renaming a variable is a one-line change instead of seven. `User.toString()` masks the password on purpose — test names, assertion messages, retry logs and Extent output all stringify what they are handed, and a record's generated `toString` would print it in every one of them.

**URLs are named routes.** `UiRoute` holds the paths (`LOGIN`, `DASHBOARD`, `CART`, `ORDERS`) and `Routes` joins them onto `ui.base.url`, tolerating a trailing slash either way. Deep links used to be built by concatenation at the point of use, which put `"dashboard/dash"` inside a page object. The API side has the same treatment in `ApiEndpoints`.

**Locators live outside the code.** Each page has a file under `src/main/resources/objectrepository/` holding `name = strategy:value` entries. A markup change is a one-line edit in a data file, and every locator for a page is visible in one place. `ObjectRepository` parses and caches them, failing with the available keys when one is missing. Page objects use plain `By` lookups rather than PageFactory proxies, which re-resolve on each use and so do not go stale when the page re-renders.

**Explicit waits only — no implicit wait is ever set.** Mixing the two makes timeouts unpredictable: the implicit wait applies inside each polling cycle of the explicit one, so a documented 15-second wait can take far longer, and a negative check that should fail fast pays the implicit timeout on every poll. Everything goes through `Waits`, and Checkstyle fails the build if `implicitlyWait` or `Thread.sleep` reappears.

**Configuration is layered and rejects unknown environments.** Shared defaults sit in `config.properties`; per-deployment values in `env-{name}.properties`. `-Denv=staging` with no matching profile fails immediately, naming the known environments — a typo in a CI variable stops the build instead of quietly running against the wrong target.

**Test data is role-keyed, not positional.** Fixtures map a role name to its fields, so a test asks for `standardCustomer` rather than row 0 and reordering the file cannot silently repoint a test at different data.

**Failures are typed.** A `FrameworkException` means the harness is misconfigured and no assertion ran; an `AssertionError` means the application misbehaved. That distinction is what makes a red build triageable at a glance.

**Fixtures cannot collide with real data.** API fixture products are named `AUTOMATION FIXTURE <uuid>`. The name was previously a real catalogue product, so under parallel execution a UI test could add the API test's fixture to its cart and then have it deleted mid-run.

**Failure evidence survives the trip.** Screenshots are written under `target/reports/screenshots/` and handed to Extent as a path *relative to the report*, because an absolute path resolves only on the machine that produced it — every image in a downloaded CI artifact would otherwise be broken. Names carry a sequence number so two failing rows of one data-driven test do not overwrite each other.

**Parallelism is split, on purpose.** Every suite authenticates as one account, and that account has one server-side cart. Two browser journeys running concurrently fight over it — one checks out, empties the cart, and the other's assertion fails. The API tests are exempt because they create uniquely named fixtures and never touch the cart, so they still run in parallel alongside a serialized browser block. With per-thread accounts the browser block could parallelise too; that is the change to make against an environment where users can be provisioned.

**Retry is uniform.** `RetryTransformer` applies the analyser to every test through the suite files. Previously three of seven classes opted in by annotation and the rest silently did not. Every retry is logged so flakiness stays visible.

**Thread-safe by construction.** Suites run `parallel="classes"`. The driver lives in a `ThreadLocal` inside `DriverManager` rather than on a base-class field, so parallel classes never share a browser. The Extent listener reads the driver from the same place instead of reflecting it off the test instance, which is what lets the API suite — which starts no browser — share one listener.

**Configuration fails loudly.** `ConfigManager.get` throws with the key name and the three ways to supply it rather than returning `null`. An unsupported browser name is rejected at driver creation, not silently defaulted.

**Grid is a config switch, not a test.** Any suite runs remotely by setting `grid.url`; there is no separate "grid test" class.

**Reports are build output.** Extent HTML and failure screenshots are written under `target/reports/` and uploaded as CI artifacts, so nothing generated is tracked in git.

## CI

`.github/workflows/ci.yml` runs a **quality gate** first — Checkstyle and enforcer — which blocks everything downstream, so a style or anti-pattern regression never spends browser minutes. Then the API suite as a fast functional gate, then UI and hybrid fanned out across Chrome and Firefox headless. Reports and failure screenshots upload on every run, including failures. A nightly cron runs the full regression. Credentials come from repository secrets of the same names.

## Tooling

Selenium 4.47 · REST Assured 6.0 · AssertJ 3.27 · TestNG 7.12 · Jackson 2.22 · Extent Reports 5.1 · Java 21 (LTS) · Maven

Dependabot keeps Maven dependencies and GitHub Actions current, grouped so Selenium and the test tooling arrive as single PRs.

Driver binaries are resolved by Selenium Manager, so there is no WebDriverManager dependency and nothing to install.
