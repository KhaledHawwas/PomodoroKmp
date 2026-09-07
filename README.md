# PomodoroKmp

A highly visual and interactive Pomodoro timer built with **Kotlin Multiplatform** and **Compose Multiplatform**. This project focuses on a "gamified" productivity experience with dynamic, animated backgrounds and smooth transitions.

![Platform Support](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20Desktop%20%7C%20Web-blue)

## ✨ Features

- **🕒 Customizable Timer**: Focus, Short Break, and Long Break sessions with persistent settings.
- **🎨 Dynamic Themes**: Choose from a variety of animated background presets:
    - **Mesh Gradient**: With animation or not
    - **Waves**: each wave has color , frequency,shiftDuration,amplitude and offset
    - **Stars Sky**: A moving starfield with lighting effects.
    - **Rounded Rect Grids**:a 45 angle grid with a moving lighting effect  
    - **Hexagons**: Geometric patterns with customizable borders.
    - **SolidStroke**: customizable colored strips .
    - **Chevron**:like SolidStroke but looks like arrow, and it's moving (or not).
- **🖱️ Interactive Reveal**: Cycle through themes using a long-press. The new theme expands from your finger in a smooth circular reveal animation.
- **💾 Persistent State**: Your timer progress and settings are saved automatically across app restarts.

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) or [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- JDK 11 or higher
- Node.js (for Web target)

### Running the Apps

Use the run configurations in your IDE or the following Gradle commands:

| Target | Command |
| :--- | :--- |
| **Android** | `./gradlew :androidApp:assembleDebug` |
| **Desktop** | `./gradlew :desktopApp:run` (or `:desktopApp:hotRun --auto` for hot reload) |
| **Web (Wasm)** | `./gradlew :webApp:wasmJsBrowserDevelopmentRun` |
| **Web (JS)** | `./gradlew :webApp:jsBrowserDevelopmentRun` |
| **iOS** | Open `iosApp/iosApp.xcworkspace` in Xcode |

## 🛠️ Technology Stack

- **Kotlin Multiplatform (KMP)**: Shared logic across all platforms.
- **Compose Multiplatform**: Declarative UI for Android, iOS, Desktop, and Web.
- **Kotlinx Datetime**: Precise time management and background session calculation.
- **Multiplatform Settings**: Key-value persistence for theme and timer preferences.

## 📁 Project Structure

- `shared/`: The core of the app. Contains the Compose UI, themes, and `TimerRepository`.
    - `commonMain/`: Shared UI and logic.
    - `androidMain/`, `iosMain/`, etc.: Platform-specific implementations.
- `androidApp/`: Android-specific entry point and configuration.
- `desktopApp/`: Desktop (JVM) entry point.
- `webApp/`: Web (Wasm/JS) entry point.
- `iosApp/`: iOS entry point (Swift).

---

Built with ❤️ using Kotlin Multiplatform.
