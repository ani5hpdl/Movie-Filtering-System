# SmartFlix - Cinematic Movie Filtering System

SmartFlix is a premium, professional Android application built with Jetpack Compose that offers a cinematic movie-browsing experience. It features personalized recommendations based on user preferences, a robust admin dashboard, and cloud-integrated image management.

## 🚀 Features

### For Users
*   **Cinematic Onboarding**: A professional splash screen with intelligent auto-login (Remember Me) functionality.
*   **Personalized Experience**: A genre-based questionnaire upon first login to tailor recommendations.
*   **Dynamic Dashboard**: High-end UI featuring "Trending Now," "Recommendations For You" (filtered by user taste), and "Just Released" sections.
*   **Deep Search**: Search movies by title with real-time filtering and genre grouping.
*   **Local Watchlist**: Add movies to your personal library, persisted locally using SharedPreferences and GSON.
*   **Immersive Details**: Rich movie detail pages with cinematic gradients, ratings, and synopsis.
*   **Profile Management**: Customizable user profiles with cloud-synced profile pictures.

### For Administrators (test@email.com)
*   **Exclusive Admin Panel**: Dedicated dashboard to manage the entire movie catalog.
*   **Movie Management**: Add movies directly from the phone gallery with automatic Cloudinary upload.
*   **User Management**: View and manage registered users.
*   **Full CRUD**: Ability to update or delete any movie in the database.

## 🛠 Tech Stack

*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Backend**: [Firebase](https://firebase.google.com/) (Authentication, Realtime Database)
*   **Image Cloud**: [Cloudinary Android SDK](https://cloudinary.com/documentation/android_integration)
*   **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/)
*   **Local Storage**: SharedPreferences & [GSON](https://github.com/google/gson)
*   **Asynchronous Tasks**: Kotlin Coroutines & SingleThreadExecutors

## 🧪 Testing

The project maintains a high standard of reliability with automated testing:

*   **Unit Tests (`src/test`)**: 
    *   `UserViewModelTest`: Verifies authentication and registration logic using Mockito.
    *   `MovieViewModelTest`: Verifies movie management operations and repository interactions.
*   **Instrumental Tests (`src/androidTest`)**:
    *   `LoginInstrumentedTest`: Verifies the UI flow and navigation of the login screen on real devices.
    *   `SignupInstrumentedTest`: Automates the registration journey to ensure UI stability.

To run tests:
```bash
# Run Unit Tests
./gradlew test

# Run Instrumental Tests (Emulator/Device required)
./gradlew connectedAndroidTest
```

## 📦 Installation & Setup

1.  **Clone the Repo**:
    ```bash
    git clone https://github.com/yourusername/MovieFilteringSystem.git
    ```
2.  **Firebase Setup**:
    *   Create a project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add your `google-services.json` to the `app/` directory.
    *   Enable **Email/Password Authentication** and **Realtime Database**.
3.  **Cloudinary Setup**:
    *   Update your Cloudinary credentials in `MovieRepoImpl.kt`:
    ```kotlin
    "cloud_name" to "your_name",
    "api_key" to "your_key",
    "api_secret" to "your_secret"
    ```
4.  **Build**:
    *   Sync project with Gradle files and run on your device.

---
Developed with ❤️ for cinema lovers.
