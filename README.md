# Jetpack Compose Authentication App

A modern, single-activity Android application built with **Jetpack Compose**, implementing **MVVM Architecture** and **OOP principles**. This project demonstrates a clean implementation of authentication flow featuring a **Login Screen**, a **Profile Screen**, and **Type-Safe Navigation**.

---

## 🛠 Features

- **Jetpack Compose UI:** 100% declarative UI with Material 3 components.
- **MVVM Architecture:** Clean separation of UI logic and business state.
- **Type-Safe Navigation:** Built with `navigation-compose` to handle screen transitions and backstack clearing.
- **Reactive State Management:** Uses Kotlin `StateFlow` collected safely in Compose as lifecycle-aware state.
- **OOP Concepts:** Built with Encapsulation, Abstraction, Single Responsibility Principle (SRP), and Polymorphism.

---

## 🏗 App Architecture & Structure

```text
com.example.mycomposeapp/
│
├── domain/            # Data models and domain contracts
│   └── UserProfile    # Data class representing user state
│
├── presentation/      # UI layer and state management
│   ├── ui/            # Compose screens and navigation
│   │   ├── AppNavigation.kt
│   │   ├── LoginScreen.kt
│   │   └── ProfileScreen.kt
│   └── state/         # ViewModels and UI state representations
│       ├── LoginViewModel.kt
│       └── LoginUiState.kt
│
└── MainActivity.kt    # App entry point initializing Compose setContent
