# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

### Server (Ktor)
```bash
./gradlew :server:run           # Run server (dev mode, auto-reload)
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

### Web (WASM)
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun   # Default browser target
./gradlew :composeApp:jsBrowserDevelopmentRun        # Legacy JS target
```

### iOS
Open `/iosApp` in Xcode and run from there.

### Tests
```bash
./gradlew test                   # All tests
./gradlew :server:test           # Server only
./gradlew :composeApp:test       # Compose app only
```

### Local Database (Docker)
```bash
docker compose up -d postgres    # Start only PostgreSQL
docker compose up -d             # Full stack (server + postgres + caddy)
docker compose exec postgres psql -U tclapp -d tclapp   # psql shell

# Grant admin to a user
docker compose exec -it postgres psql -U tclapp -d tclapp -c "UPDATE users SET is_admin = true WHERE email = 'email@example.com';"
```

### Server Setup
1. Copy `.env.example` to `.env` and fill in secrets.
2. Place `firebase-adminsdk.json` in `server/src/main/resources/` and update the filename reference in `FirebaseService.kt`.

---

## Architecture

### Module Structure
- **`shared/`** — `@Serializable` domain models (auth, booking, user, court, slot, notification) and `BookingWsMessage`. Shared between server and all client targets. Pure Kotlin, no platform code.
- **`server/`** — Ktor REST + WebSocket API server.
- **`composeApp/`** — Compose Multiplatform UI targeting Android, iOS, Desktop (JVM), and Web (WASM/JS).

---

### Server (`server/`)

**Stack:** Ktor 3 + Exposed ORM + PostgreSQL (HikariCP) + Koin DI + JWT auth + Firebase Admin SDK

**Bootstrap** (`Application.kt`): installs Koin, then delegates to `configureDatabase()`, `configureSerialization()`, `configureWebSockets()`, `configureHTTP()` (CORS + JWT auth), `configureRouting()` — all in `Plugins.kt`.

**Feature module pattern** (repeated for auth, user, booking, court, slot, device, notification, firebase):
```
<Feature>Routes.kt             — Ktor route extensions registered in Routing.kt
<Feature>Service.kt            — Business logic; coordinates repositories + side-effects
<Feature>Repository.kt         — Interface
Postgres<Feature>Repository.kt — Exposed ORM implementation
Fake<Feature>Repository.kt     — In-memory stub (used when TESTING = true in AppModule)
```

**DI** (`AppModule.kt`): single Koin module; `testing` flag in the file swaps Fake ↔ Postgres repositories.

**Auth flow:**
- Access tokens: 15-min JWT (HMAC256), secret from env `JWT_SECRET`
- Refresh tokens: 30-day opaque values stored in DB; old token invalidated on refresh
- `ApplicationCall.userId()` extension (`JwtConfig.kt`) extracts the claim in route handlers
- JWT auth also accepts `?token=<jwt>` as a query parameter (required for browser WebSocket connections, which cannot set custom headers)

**WebSocket** (`BookingWebSocketRoutes.kt` + `BookingWebSocketService.kt`):
- Endpoint: `GET /v1/bookings/ws` (requires auth)
- `BookingWebSocketService` maps `userId → CopyOnWriteArraySet<DefaultWebSocketSession>`
- `notifyUsers(userIds, message)` encodes JSON once, creates a fresh `Frame.Text` per session send, and proactively unregisters dead sessions on send failure
- `BookingService` calls `notifyUsers` after create/delete, targeting creator + all players

---

### Client (`composeApp/`)

**Stack:** Compose Multiplatform 1.10 + Ktor client + Koin + Navigation3 + RikkaUI

**UI library: RikkaUI** replaces Material3 entirely. Never import `androidx.compose.material3` in UI code. Use `RikkaTheme.colors.*` and `RikkaTheme.spacing.*` for all tokens. Key components: `Scaffold`, `TopAppBar`, `Button`, `Card`/`CardContent`, `Input`, `Text` (with `TextVariant.H1/Lead/Large/Small`), `Avatar`, `Spinner`, `AlertDialog`, `Sheet`, `Fab`, `Toggle`, `NavigationBar`.

**Architecture:** Clean Architecture + MVVM, feature-sliced vertically.

Each feature under `composeApp/src/commonMain/kotlin/org/tcl/app/<feature>/`:
```
data/
  Ktor<Feature>RemoteDataSource.kt   — Ktor HTTP implementation of the domain interface
  Fake<Feature>RemoteDataSource.kt   — stub for tests
domain/
  <Feature>RemoteDataSource.kt       — interface
presentation/
  <Feature>ViewModel.kt              — ViewModel with StateFlow<State>
  <Feature>Screen.kt                 — @Composable; takes (state, onAction, ...) — no ViewModel refs inside
  <Feature>State.kt                  — @Stable data class
  <Feature>Action.kt                 — sealed interface for user intents
  <Feature>Event.kt                  — Channel-based one-shot events (navigation, toasts)
```

**Core utilities** (`core/`):
- `Result<D, E>` — custom sealed type; chainable `onSuccess`/`onFailure`/`map`
- `DataError` — sealed hierarchy of network/domain errors
- `SecureStorage` — interface; `KSafeSecureStorage` (Android: EncryptedSharedPreferences, iOS: Keychain, JVM: file) vs `WebSecureStorage` (WASM/JS: `localStorage`, JSON-serialized). Provided via `platformModule`.
- `BackendApiClient` — singleton Ktor client with automatic Bearer token refresh. Uses a separate `refreshClient` (no Auth plugin) to avoid re-entrancy. After login/registration, always call `backendApiClient.client.clearAuthTokens()` before setting logged-in state so the Ktor Auth cache is invalidated.

**WebSocket client** (`booking/data/BookingWebSocketDataSource.kt`):
- Singleton with its own `CoroutineScope`; starts the connection loop in `init {}`.
- Exposes `messages: SharedFlow<BookingWsMessage>` and `isConnected: StateFlow<Boolean>`.
- Exponential backoff reconnection (1s → 2s → … → 60s). On reconnect, consumers should re-sync state via REST.
- Token passed as `?token=<jwt>` query param — browser WebSocket API cannot set headers.

**Navigation** (`navigation/`): `AppGraph` sealed interface with type-safe routes. `NavigationRoot` is the single composable that owns navigation state.

**App-level auth state** (`AppViewModel`):
- `checkAuth()` on init: reads refresh token from `SecureStorage`; if present calls `/auth/refresh`; on success sets `isLoggedIn = true` and `currentUserId`.
- `setLoggedIn()`: called by manual login and registration flows. Clears Ktor Auth cache, sets `isLoggedIn = true`, then launches a coroutine to `getCurrentUser()` and `updateUserId()`.
- `setLoggedOut()`: invalidates server refresh token, clears storage, clears Ktor Auth cache.

**DI** (`di/`): Koin modules per feature assembled in `AppModule.kt` (`appModule = includes(platformModule, coreModule, ...)`). `platformModule` is `expect val` with `actual` per source set (androidMain, iosMain, jvmMain, webMain).

**Platform entry points:**
- Android: `AppApplication` → `MainActivity` → `App()`
- iOS: `iOSApp.swift` → `MainViewController` → `App()`
- Desktop/Web: `main()` → `App()`

**Source sets:**
- `commonMain` — shared UI + business logic
- `androidMain`, `iosMain`, `jvmMain`, `webMain` — platform-specific DI (`platformModule`) and entry points
