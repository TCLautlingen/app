# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

### Server (Ktor)
```bash
./gradlew :server:run           # Run server (dev mode)
./gradlew :server:build         # Build server JAR
./gradlew :server:test          # Run server tests
```

### Android
```bash
./gradlew :composeApp:assembleDebug    # Build debug APK
./gradlew :composeApp:assembleRelease  # Build release APK
```

### Desktop (JVM)
```bash
./gradlew :composeApp:run       # Run desktop app
```

### Web
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun   # WASM target (default)
./gradlew :composeApp:jsBrowserDevelopmentRun        # JS target
```

### iOS
Open `/iosApp` in Xcode and run from there.

### Tests
```bash
./gradlew test                   # All tests
./gradlew :server:test           # Server only
./gradlew :composeApp:test       # Compose app only
```

## Architecture

### Module Structure
- **`shared/`** — Serializable domain models (auth, booking, user, court, slot, notification) shared between server and client. Pure Kotlin, no platform code.
- **`server/`** — Ktor REST API server.
- **`composeApp/`** — Compose Multiplatform UI targeting Android, iOS, Desktop, and Web.

---

### Server (`server/`)

**Stack:** Ktor 3.4.2 + Exposed ORM + PostgreSQL + Koin DI + JWT auth

**Application bootstrap** (`Application.kt`): installs Koin, then delegates to plugin functions: `configureDatabase()`, `configureSerialization()`, `configureAuthentication()`, `configureRouting()`.

**Plugin pattern** (`plugins/`):
- `Database.kt` — HikariCP pool setup; provides `withTransaction {}` suspend helper for all DB calls
- `Authentication.kt` — JWT scheme named `"auth-jwt"`; validates token and extracts `userId` claim
- `Serialization.kt` — Kotlinx JSON with `ignoreUnknownKeys=true`, `explicitNulls=false`
- `Routing.kt` — registers all feature route extensions

**Feature module pattern** (repeated for every feature: `auth`, `user`, `booking`, `court`, `slot`, `device`, `notification`, `firebase`):
```
<Feature>Routes.kt            — Ktor route extensions; inject service via Koin
<Feature>Service.kt           — Business logic; coordinates repositories and side-effects
<Feature>Repository.kt        — Interface
Postgres<Feature>Repository.kt — Exposed ORM implementation
Fake<Feature>Repository.kt    — In-memory stub for tests
<Feature>Mapping.kt           — Exposed table/DAO definitions + daoToDomain() converter
```

**DI** (`di/AppModule.kt`): single Koin module; `TESTING` flag swaps `FakeXxxRepository` ↔ `PostgresXxxRepository`. Services are singletons.

**Auth flow:**
- Access tokens: 15-min JWT (HMAC256), secret from `application.yaml`
- Refresh tokens: 30-day opaque 32-byte values stored in DB; old token invalidated on refresh
- Passwords: PBKDF2WithHmacSHA512, 120k iterations, 16-byte salt + constant secret
- `ApplicationCall.userId()` extension extracts the claim in route handlers

**Config:** `server/src/main/resources/application.yaml` — server port, DB URL/credentials, JWT secret, `app.testing` flag.

---

### Client (`composeApp/`)

**Stack:** Compose Multiplatform 1.10.3 + Ktor client + Koin + Navigation3 + RikkaUI

**Architecture:** Clean Architecture + MVVM, feature-sliced vertically.

Each feature under `composeApp/src/commonMain/kotlin/org/tcl/app/<feature>/`:
```
domain/
  <Feature>RemoteDataSource.kt       — interface
  Ktor<Feature>RemoteDataSource.kt   — Ktor HTTP implementation
  Fake<Feature>RemoteDataSource.kt   — stub for tests
presentation/
  <Feature>ViewModel.kt              — ViewModel with StateFlow<State>
  <Feature>Screen.kt                 — @Composable UI
  <Feature>State.kt                  — @Stable data class
  <Feature>Action.kt                 — sealed interface for user intents
  <Feature>Event.kt                  — Channel-based one-shot events (navigation, toasts)
```

**Core utilities** (`core/`):
- `Result<D, E>` — custom sealed type for type-safe error handling; chainable `onSuccess`/`onFailure`/`map`
- `DataError` — sealed hierarchy of network/domain errors
- Secure storage via `KSafe` (encrypted key-value store)
- Ktor client with automatic Bearer token refresh via `ktor-client-auth`

**Navigation** (`navigation/`): `AppGraph` sealed interface with type-safe routes; stack-based with `SavedStateConfiguration` for ViewModel persistence per screen.

**DI** (`di/`): Koin modules per feature + platform-specific `platformModule` (iOS/Android) for context-dependent bindings (e.g., `KSafe` initialization).

**Platform entry points:**
- Android: `AppApplication` (initializes Koin + NotifierManager) → `MainActivity` → `App()` composable
- iOS: `iOSApp.swift` (AppDelegate for Firebase) → `MainViewController` → same `App()` composable

**Push notifications:** `kmpNotifier` library; device FCM token synced to server on login and stored in `DeviceTable`.
