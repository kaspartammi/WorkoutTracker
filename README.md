# 🏋️ Workout Tracker - Android App

A fully functional native Android workout tracking app built with **Kotlin** and **Jetpack Compose**, converted from your Figma design.

## ✨ Features

### Core Functionality
- ✅ **Workout Templates** - Create and manage workout routines with custom exercises
- ✅ **Active Workout Tracking** - Real-time tracking of sets, reps, and weight during workouts
- ✅ **Rest Timer** - Automatic countdown timer between sets with skip functionality
- ✅ **Exercise Management** - Add, edit, remove, and reorder exercises with drag-and-drop
- ✅ **Data Persistence** - All workouts and sessions saved locally using DataStore
- ✅ **Workout History** - Track when workouts were last performed
- ✅ **Settings** - Customizable weight units, theme, and rest timer preferences

### UI/UX
- 🎨 **Material Design 3** - Modern, clean interface with adaptive theming
- 🌓 **Dark Mode** - Full support for light/dark themes
- 📱 **Responsive Design** - Optimized for various screen sizes
- ⚡ **Smooth Animations** - Polished transitions and interactions

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Navigation:** Jetpack Navigation Compose
- **Data Persistence:** DataStore Preferences
- **Serialization:** Kotlinx Serialization
- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)

## 📱 Screenshots

The app includes these main screens:
1. **Home Screen** - List of saved workouts with quick start button
2. **Select Workout** - Choose a workout to begin your session
3. **Active Workout** - Track exercises, sets, reps, and weight in real-time
4. **Edit Workout** - Create/modify workout templates
5. **Settings** - Configure app preferences

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or later
- **JDK** 17 or later
- **Android SDK** 34

### Installation

1. **Open in Android Studio**
   ```bash
   # Navigate to the project directory
   cd WorkoutTrackerAndroid
   
   # Open with Android Studio
   # File > Open > Select the WorkoutTrackerAndroid folder
   ```

2. **Sync Gradle**
   - Android Studio will automatically prompt to sync Gradle
   - Or manually: File > Sync Project with Gradle Files

3. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press Shift+F10
   - Select your device/emulator

### Building APK

To build a release APK:

```bash
# In the project root
./gradlew assembleRelease

# The APK will be at:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

## 📂 Project Structure

```
WorkoutTrackerAndroid/
├── app/
│   ├── src/main/
│   │   ├── java/com/workouttracker/app/
│   │   │   ├── data/
│   │   │   │   ├── model/
│   │   │   │   │   └── Models.kt           # Data classes
│   │   │   │   └── repository/
│   │   │   │       └── WorkoutRepository.kt # Data management
│   │   │   ├── ui/
│   │   │   │   ├── navigation/
│   │   │   │   │   └── Navigation.kt       # Navigation setup
│   │   │   │   ├── screens/
│   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   ├── SelectWorkoutScreen.kt
│   │   │   │   │   ├── WorkoutSessionScreen.kt
│   │   │   │   │   ├── EditWorkoutScreen.kt
│   │   │   │   │   └── SettingsScreen.kt
│   │   │   │   ├── theme/
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   └── viewmodel/
│   │   │   │       ├── HomeViewModel.kt
│   │   │   │       ├── WorkoutSessionViewModel.kt
│   │   │   │       ├── EditWorkoutViewModel.kt
│   │   │   │       └── SettingsViewModel.kt
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 🎯 Key Components

### Data Models
- **Workout** - Template containing exercises
- **Exercise** - Individual exercise with sets, reps, weight
- **WorkoutSession** - Active/completed workout session
- **CompletedSet** - Tracking individual set performance
- **UserSettings** - App preferences

### ViewModels
- **HomeViewModel** - Manages workout list and deletion
- **WorkoutSessionViewModel** - Handles active workout tracking and rest timer
- **EditWorkoutViewModel** - Create/edit workout templates
- **SettingsViewModel** - Manage user preferences

### Repository
- **WorkoutRepository** - Single source of truth for all data
  - Persistent storage using DataStore
  - Provides sample workouts for first launch
  - Flow-based reactive data updates

## 🔧 Customization

### Adding Sample Workouts
Edit `WorkoutRepository.kt` → `getSampleWorkouts()` function

### Changing Theme Colors
Edit `app/src/main/java/com/workouttracker/app/ui/theme/Theme.kt`

### Modifying Exercise Categories
Edit `Models.kt` → `ExerciseCategory` enum

## 📝 Usage Guide

### Creating a Workout
1. Tap the **+** button on home screen
2. Enter workout name and description
3. Tap **+ (Add Exercise)** to add exercises
4. Fill in exercise details (name, sets, reps, rest time)
5. Reorder exercises using ↑↓ buttons
6. Tap **SAVE**

### Starting a Workout
1. Tap **"Start Workout"** card on home screen
2. Select a workout template
3. Track each set with reps and weight
4. Rest timer starts automatically
5. Navigate between exercises with ← →
6. Tap **FINISH** when complete

### Editing a Workout
1. Tap **⋮** menu on workout card
2. Select **Edit**
3. Modify exercises and details
4. Tap **SAVE**

## 🐛 Known Issues & Future Enhancements

### Potential Additions
- [ ] Workout history viewer with calendar
- [ ] Progress charts and statistics
- [ ] Exercise library with images
- [ ] Custom exercise categories
- [ ] Backup/restore to cloud
- [ ] Social features (share workouts)
- [ ] Timer notifications for rest periods

## 📄 License

This project was generated from a Figma design and is provided as-is for personal use.

## 🤝 Contributing

This is a personal project converted from Figma, but feel free to:
- Report bugs
- Suggest features
- Fork and customize for your needs

## 📧 Support

For issues or questions about the app, please check:
- Verify Android Studio version (Hedgehog or later)
- Ensure all Gradle dependencies sync correctly
- Check minimum SDK requirements (API 26+)

---

**Built with ❤️ using Kotlin & Jetpack Compose**

Converted from Figma design to native Android app
