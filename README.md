# HireJetpack — Jetpack Compose Job App

A modern, single-activity Android application built with **Jetpack Compose**, implementing **MVVM Architecture** and **OOP principles**. The app features a **Login Screen**, a **Home Screen** with a **navigation drawer** and a **job listing feed**, and a redesigned **Profile Screen** — all connected with **type-safe navigation**.

---

## 🛠 Features

- **Jetpack Compose UI:** 100% declarative UI with Material 3 components.
- **MVVM Architecture:** Clean separation of UI logic, state, and data.
- **Navigation Drawer:** Slide-out `ModalNavigationDrawer` on the Home screen with a profile summary header, Home/Profile/Logout entries.
- **Job Listing Feed:** Scrollable `LazyColumn` of job cards (title, company, location, salary, skill tags), backed by a mock `JobRepository`.
- **Job Detail Screen:** Tapping a job card navigates to a full detail view via a parameterized route (`job_detail_screen/{jobId}`), with dedicated Loading / Found / Not-Found states.
- **Redesigned Profile Screen:** Gradient header, circular initials avatar, role badge, and info displayed in an elevated card.
- **Type-Safe Navigation:** Built with `navigation-compose`, routes defined as a sealed class, backstack cleared correctly on login/logout.
- **Reactive State Management:** Kotlin `StateFlow` per screen, collected safely in Compose via `collectAsState`.
- **OOP Concepts:** Encapsulation, Abstraction, Polymorphism, Single Responsibility Principle (SRP), Composition over Inheritance, and DRY (shared `InitialsAvatar` component).

---

## 🧭 App Flow

```text
Login Screen
     │  (successful login)
     ▼
Home Screen  ──(tap a job card)──▶  Job Detail Screen
     │      │                              │
     │      └──(Back)──────────────────────┘
     │
     └──(drawer → Profile, or profile icon in top bar)──▶  Profile Screen
     │                                                              │
     └──────────────────── Logout (from either screen) ────────────┘
                                        │
                                        ▼
                                  Login Screen
```

---

## 🏗 App Architecture & Structure

```text
com.pawan.hirejetpack/
│
├── domain/                         # Pure data models — no Android imports
│   ├── UserProfile.kt              # Logged-in user's data
│   └── Job.kt                      # One job listing
│
├── data/                           # Data sources
│   └── JobRepository.kt            # Mock job data + getJobById() lookup (Retrofit-shaped for easy swap later)
│
├── presentation/
│   ├── state/                      # ViewModels + UI state classes, one pair per screen
│   │   ├── LoginUiState.kt         # Idle / Loading / Success / Error
│   │   ├── LoginViewModel.kt
│   │   ├── HomeUiState.kt          # jobs + isLoading
│   │   ├── HomeViewModel.kt
│   │   ├── JobDetailUiState.kt     # Loading / Found / NotFound
│   │   └── JobDetailViewModel.kt   # Reads jobId via SavedStateHandle, re-fetches from JobRepository
│   │
│   ├── navigation/                 # Routing
│   │   ├── Screen.kt               # Sealed class of routes, incl. parameterized JobDetail
│   │   └── AppNavigation.kt        # NavHost graph wiring screens together
│   │
│   └── ui/                         # Compose screens, grouped by feature
│       ├── components/
│       │   └── InitialsAvatar.kt   # Shared circular avatar (drawer + profile)
│       ├── login/
│       │   └── LoginScreen.kt
│       ├── home/
│       │   ├── HomeScreen.kt       # Drawer + job feed scaffold
│       │   ├── AppDrawerContent.kt # Drawer panel contents
│       │   └── JobCard.kt          # One row in the job feed — clickable, navigates to detail
│       ├── jobdetail/
│       │   └── JobDetailScreen.kt  # Full job info, skill tags, Apply Now (action not yet wired)
│       └── profile/
│           └── ProfileScreen.kt    # Gradient header + info card
│
└── MainActivity.kt                 # Entry point — only hosts setContent { AppNavigation() }
```

---

## 🧩 Key Concepts & Keywords Reference

### 1. Object-Oriented Programming (OOP)

| Concept | Implementation in Code |
| --- | --- |
| **Encapsulation** | `LoginViewModel` and `HomeViewModel` each hold a `private MutableStateFlow` and expose only a `public read-only StateFlow`, preventing the UI from mutating state directly. |
| **Polymorphism** | `LoginUiState` is a `sealed interface`; the UI polymorphically renders different layouts (`Loading`, `Error`, `Success`) based on the concrete instance at runtime. |
| **Single Responsibility (SRP)** | Each ViewModel owns one screen's state; `JobRepository` only knows how to fetch jobs; each Composable file renders exactly one piece of UI. |
| **Composition over Inheritance** | `HomeScreen` is built by composing `ModalNavigationDrawer` + `Scaffold` + `LazyColumn` + `JobCard`, not by extending a base screen class. `InitialsAvatar` is reused via composition in both the drawer and Profile screen. |
| **Type-Safe Abstraction** | `Screen` is a `sealed class` of routes — the compiler catches typos that a raw string route name wouldn't. |
| **DRY** | `InitialsAvatar` and `ProfileInfoRow` exist because the same visual pattern showed up twice — pulled out once, reused everywhere. |

### 2. Jetpack Compose Keywords

| Keyword | Description |
| --- | --- |
| `@Composable` | Annotation telling the Kotlin compiler that a function transforms data into UI nodes. |
| `setContent` | Bridge function attaching Compose layouts to an Android `ComponentActivity`. |
| `remember` | Caches a value in memory across recompositions. |
| `mutableStateOf` | Creates a reactive state holder; updating it triggers recomposition of dependent UI. |
| `collectAsState` | Collects a `StateFlow`'s emissions into reactive Compose `State`. |
| `rememberNavController` | Creates and retains a `NavHostController` across recompositions. |
| `ModalNavigationDrawer` | Renders a slide-out drawer panel alongside main screen content. |
| `rememberDrawerState` / `DrawerState` | Holds whether the drawer is open or closed; `.open()` / `.close()` are suspend functions. |
| `rememberCoroutineScope` | Provides a coroutine scope tied to the composition, used to call suspend functions (like opening the drawer) from click handlers. |
| `Scaffold` | Material layout structure providing slots for TopBar, BottomBar, and body content. |
| `LazyColumn` | Lazily-rendered scrollable list — only composes items currently visible on screen. |
| `SavedStateHandle` | Key-value bag holding the current destination's navigation arguments; injected automatically into a ViewModel and survives process death. |
| `navArgument` | Declares a named, typed argument (e.g. `jobId: String`) on a `composable(...)` route, enabling parameterized navigation like `job_detail_screen/{jobId}`. |

---

## 🚀 Getting Started

### Prerequisites

* **Android Studio:** Ladybug or newer
* **JDK:** 17+
* **Min SDK:** 24 (Android 7.0)
* **Target SDK:** 34 or higher

### Gradle Dependencies

Ensure your `build.gradle.kts` (Module `:app`) contains the following:

```kotlin
dependencies {
    // Jetpack Compose & Material 3
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    // Extended icon set (Menu, AccountCircle, Work, LocationOn, Logout, Home)
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // ViewModel & Navigation for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")
}
```

---

## 💻 How to Run

1. Clone or download this repository.
2. Open the project in **Android Studio**.
3. Confirm the module's `applicationId` / package matches `com.pawan.hirejetpack` (or update it, and the `package` line at the top of every file, to match your own).
4. Perform a **Gradle Sync**.
5. Run the application on an Emulator or connected Physical Device (`Shift + F10`).
6. Enter any sample email and password on the Login screen — this navigates to the Home screen's job feed.
7. Tap a job card to open its detail screen (Back returns to the feed).
8. Tap the menu icon (or the profile icon in the top bar) to open the drawer and reach the Profile screen.

---

## 🔮 Next Steps / Extension Ideas

- Wire up the **Apply Now** button on the Job Detail screen (currently a `TODO`) — likely a confirmation dialog or a short application form.
- Replace `JobRepository`'s hardcoded list with a real Retrofit-backed data source — `getJobs()` and `getJobById()` are already shaped for this swap.
- Add search/filter on the Home feed (by title, location, or tag).
- Add bookmarking/saving jobs, likely backed by local storage (Room or DataStore) rather than in-memory state.
- Introduce a `domain`-layer `UseCase` (e.g. `GetJobsUseCase`) once business rules around filtering/sorting jobs get more complex than "return the list."
- If the app grows past a handful of features, consider splitting `domain`/`data` into `core-model`/`core-network` modules, and each `presentation/ui/<feature>` folder into its own `feature-*` Gradle module (see multi-module architecture notes).