# WeatherGlovy

WeatherGlovy is an Android application designed to enhance your weather experience. It fetches current weather information, overlays it on an image, and allows you to save the personalized weather snapshot. 

---

## Features

### Core Functionality
- **Weather Fetching**: Retrieve real-time weather data for your current location or any specified location.
- **Image Customization**: Capture or select an image and overlay weather data on it.
- **Save Weather Snapshots**: Save the weather-enhanced image to your device for sharing or future reference.

---

### Technology Stack

#### Programming Language:
- **Kotlin**: Used for modern, concise, and safe Android development.

#### Architecture:
- **MVVM (Model-View-ViewModel)**: Implements modular and maintainable architecture.

#### UI:
- **Jetpack Compose**: For building a dynamic and responsive UI.
- **ViewBinding**: Simplifies interaction with UI components in XML layout files.

#### Dependency Injection:
- **Hilt-Dagger**: For standard and seamless dependency injection.

#### Networking:
- **Retrofit**: A type-safe HTTP client for API integration.

#### Image Loading:
- **Coil**: An image loading library for Android backed by Kotlin Coroutines.

#### Storage:
- **SharedPreferences**: For saving and retrieving small amounts of app-related data.

#### Concurrency:
- **Coroutines**: For managing background tasks and asynchronous operations.

---

### Additional Features:
- **Unit Testing**: Ensures code reliability and helps prevent future bugs.
- **JitPack Compose**: For advanced and modular UI design and state management.

---

## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- Java 11 or higher
- Internet connection for API usage

### Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in Android Studio.
3. Sync the project to download dependencies.
4. Run the app on an emulator or physical device.

---

## Modules
- **Weather Module**: Handles fetching and displaying weather data.
- **Image Overlay Module**: Manages image selection, weather overlay, and customization.
- **Save Module**: Implements saving the customized image locally.

---

## Libraries Used

| Library          | Purpose                                   |
|------------------|-------------------------------------------|
| **Kotlin**       | Primary programming language.             |
| **Compose**      | For building modern, responsive UI.       |
| **Hilt-Dagger**  | Dependency injection framework.           |
| **Retrofit**     | API communication.                        |
| **Coil**         | Loading and caching images.               |
| **ViewBinding**  | Easier view handling in XML layouts.      |
| **SharedPreferences** | Storing lightweight data locally. |

---

## Contribution Guidelines
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with a clear description of the changes.



