# 🎨 Customization Guide - Make It Your Own!

This guide shows you how to customize the Workout Tracker app to match your brand, add features, or modify behavior.

## 🌈 Quick Customizations (5-30 minutes)

### 1. Change App Name

**File:** `app/src/main/res/values/strings.xml`
```xml
<string name="app_name">Your App Name</string>
```

**Launcher name** (can be different):
`app/src/main/AndroidManifest.xml`
```xml
<application
    android:label="YourGymTracker"
```

### 2. Change App Colors

**File:** `app/src/main/java/com/workouttracker/app/ui/theme/Theme.kt`

```kotlin
// Primary color (used for buttons, highlights)
private val md_theme_light_primary = Color(0xFF1E88E5)  // Blue
private val md_theme_dark_primary = Color(0xFF90CAF9)   // Light Blue

// Primary container (used for cards, emphasized areas)
private val md_theme_light_primaryContainer = Color(0xFFBBDEFB)
private val md_theme_dark_primaryContainer = Color(0xFF1565C0)
```

**Popular Color Schemes:**

Fitness Red:
```kotlin
private val md_theme_light_primary = Color(0xFFD32F2F)
private val md_theme_light_primaryContainer = Color(0xFFFFCDD2)
```

Gym Orange:
```kotlin
private val md_theme_light_primary = Color(0xFFFF6F00)
private val md_theme_light_primaryContainer = Color(0xFFFFE0B2)
```

Athletic Green:
```kotlin
private val md_theme_light_primary = Color(0xFF388E3C)
private val md_theme_light_primaryContainer = Color(0xFFC8E6C9)
```

### 3. Change App Icon

**Quick Method:**
1. Right-click `app/src/main/res`
2. New > Image Asset
3. Select your icon image
4. Choose foreground/background
5. Generate

**Manual Method:**
See `ICON_SETUP.md` for detailed instructions.

### 4. Modify Sample Workouts

**File:** `app/src/main/java/com/workouttracker/app/data/repository/WorkoutRepository.kt`

Function: `getSampleWorkouts()`

```kotlin
private fun getSampleWorkouts(): List<Workout> {
    return listOf(
        Workout(
            id = UUID.randomUUID().toString(),
            name = "My Custom Workout",
            description = "Your description here",
            exercises = listOf(
                Exercise(
                    id = UUID.randomUUID().toString(),
                    name = "Your Exercise",
                    sets = 3,
                    reps = "12",
                    category = ExerciseCategory.CHEST,
                    restTime = 60
                )
                // Add more exercises...
            )
        )
    )
}
```

### 5. Adjust Default Rest Time

**File:** `app/src/main/java/com/workouttracker/app/data/model/Models.kt`

```kotlin
data class UserSettings(
    val weightUnit: WeightUnit = WeightUnit.LBS,
    val theme: AppTheme = AppTheme.SYSTEM,
    val restTimerEnabled: Boolean = true,
    val defaultRestTime: Int = 90  // Changed from 60 to 90 seconds
)
```

## 🔧 Medium Customizations (30min - 2 hours)

### 6. Add New Exercise Categories

**File:** `app/src/main/java/com/workouttracker/app/data/model/Models.kt`

```kotlin
@Serializable
enum class ExerciseCategory {
    CHEST, 
    BACK, 
    LEGS, 
    SHOULDERS, 
    ARMS, 
    CORE, 
    CARDIO,
    OLYMPIC_LIFTS,  // NEW
    PLYOMETRICS,    // NEW
    MOBILITY,       // NEW
    OTHER
}
```

Then update UI to display new categories in edit screen.

### 7. Add Exercise Notes Display

**File:** `app/src/main/java/com/workouttracker/app/ui/screens/WorkoutSessionScreen.kt`

In `ExerciseSessionCard`:
```kotlin
if (exercise.notes.isNotBlank()) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = "Note",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                exercise.notes,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
```

### 8. Add Workout Duration Tracking

**Modify WorkoutSession model:**
```kotlin
@Serializable
data class WorkoutSession(
    val id: String,
    val workoutId: String,
    val workoutName: String,
    val startTime: Long,
    val endTime: Long? = null,
    val completedExercises: List<CompletedExercise> = emptyList(),
    val notes: String = ""
) {
    // Add computed property
    val durationMinutes: Int?
        get() = if (endTime != null) {
            ((endTime - startTime) / 1000 / 60).toInt()
        } else null
}
```

**Display in UI:**
```kotlin
session.durationMinutes?.let { duration ->
    Text("Duration: $duration minutes")
}
```

### 9. Add Sound/Vibration to Rest Timer

**File:** `app/src/main/java/com/workouttracker/app/ui/screens/WorkoutSessionScreen.kt`

Add at top of file:
```kotlin
import android.content.Context
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator

@Composable
fun WorkoutSessionScreen(...) {
    val context = LocalContext.current
    
    // In rest timer completion:
    LaunchedEffect(restTimeRemaining) {
        if (restTimeRemaining == 0 && isResting) {
            // Play sound
            val notification = RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
            )
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()
            
            // Vibrate
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, 
                VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
```

**Add permission** to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.VIBRATE" />
```

### 10. Change Typography/Fonts

**File:** `app/src/main/java/com/workouttracker/app/ui/theme/Type.kt`

```kotlin
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

// Add custom font (place TTF files in res/font/)
val Roboto = FontFamily(
    Font(R.font.roboto_regular),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Roboto,  // Use custom font
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    // ... update other text styles
)
```

## 🚀 Advanced Features (2+ hours)

### 11. Add Workout History Screen

Create new screen showing past sessions:

**1. Create ViewModel:**
```kotlin
class HistoryViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val sessions: StateFlow<List<WorkoutSession>> = _sessions.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getSessionsFlow().collect { sessions ->
                _sessions.value = sessions.sortedByDescending { it.startTime }
            }
        }
    }
}
```

**2. Create UI Screen:**
```kotlin
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateBack: () -> Unit
) {
    val sessions by viewModel.sessions.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sessions) { session ->
                SessionCard(session = session)
            }
        }
    }
}
```

**3. Add to Navigation:**
Update `Navigation.kt` with new route and screen.

### 12. Add Exercise Images

**1. Add image field to Exercise:**
```kotlin
data class Exercise(
    val id: String,
    val name: String,
    val sets: Int = 3,
    val reps: String = "10",
    val weight: String = "",
    val restTime: Int = 60,
    val notes: String = "",
    val category: ExerciseCategory = ExerciseCategory.OTHER,
    val imageUrl: String? = null  // NEW
)
```

**2. Display in UI:**
```kotlin
imageUrl?.let { url ->
    AsyncImage(
        model = url,
        contentDescription = exercise.name,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}
```

**3. Add Coil dependency** in `build.gradle.kts`:
```kotlin
implementation("io.coil-kt:coil-compose:2.5.0")
```

### 13. Add Charts/Statistics

**1. Add MPAndroidChart:**
```kotlin
// build.gradle.kts
implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
```

**2. Create stats calculations:**
```kotlin
fun calculateWeeklyProgress(sessions: List<WorkoutSession>): Map<String, Int> {
    return sessions
        .filter { it.endTime != null }
        .groupBy { /* group by week */ }
        .mapValues { it.value.size }
}
```

**3. Display chart in new screen**

### 14. Add Export/Import Workouts

**1. Export to JSON:**
```kotlin
fun exportWorkouts(workouts: List<Workout>): String {
    return Json.encodeToString(workouts)
}
```

**2. Save to file:**
```kotlin
fun saveToFile(context: Context, content: String, filename: String) {
    val file = File(context.getExternalFilesDir(null), filename)
    file.writeText(content)
    
    // Share file
    val uri = FileProvider.getUriForFile(context, 
        "${context.packageName}.provider", file)
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/json"
        putExtra(Intent.EXTRA_STREAM, uri)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Export Workouts"))
}
```

**3. Import from JSON:**
```kotlin
fun importWorkouts(json: String): List<Workout> {
    return Json.decodeFromString(json)
}
```

### 15. Add Workout Templates Library

Create predefined workout templates users can add:

```kotlin
object WorkoutTemplates {
    val BEGINNER_PUSH = Workout(
        id = UUID.randomUUID().toString(),
        name = "Beginner Push",
        description = "Perfect for beginners",
        exercises = listOf(
            Exercise(name = "Push-ups", sets = 3, reps = "10"),
            Exercise(name = "Dumbbell Press", sets = 3, reps = "10"),
            Exercise(name = "Tricep Dips", sets = 3, reps = "8")
        )
    )
    
    val ALL_TEMPLATES = listOf(
        BEGINNER_PUSH,
        BEGINNER_PULL,
        BEGINNER_LEGS,
        INTERMEDIATE_UPPER,
        // etc...
    )
}
```

Create UI screen to browse and add templates.

## 🎨 Styling Customizations

### Rounded Corners

**File:** `Theme.kt`
```kotlin
val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)  // More rounded
)
```

### Card Elevation

```kotlin
Card(
    elevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp  // Increased from 2.dp
    )
)
```

### Button Styles

Create custom button variants:
```kotlin
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),  // Taller button
        shape = RoundedCornerShape(12.dp),   // More rounded
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF6F00)  // Custom color
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
```

## 🧪 Testing Your Customizations

### 1. Preview in Android Studio

Add preview composables:
```kotlin
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WorkoutTrackerTheme {
        // Your screen here
    }
}
```

### 2. Test on Multiple Devices

- Small phone (< 5")
- Regular phone (5-6")
- Large phone (> 6")
- Tablet (7"+)

### 3. Test Dark Mode

```kotlin
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    WorkoutTrackerTheme(darkTheme = true) {
        // Your screen
    }
}
```

### 4. Test Different Font Sizes

Settings > Display > Font Size (on device)

## 📋 Customization Checklist

Before publishing your customized app:

- [ ] Changed app name and package
- [ ] Updated all colors to match brand
- [ ] Created custom app icon
- [ ] Modified sample workouts
- [ ] Tested on multiple devices
- [ ] Tested in light and dark mode
- [ ] Updated strings.xml
- [ ] Created new screenshots
- [ ] Updated privacy policy
- [ ] Incremented version code/name

## 🎓 Learning Resources

- **Jetpack Compose:** https://developer.android.com/jetpack/compose
- **Material Design 3:** https://m3.material.io/
- **Kotlin Coroutines:** https://kotlinlang.org/docs/coroutines-overview.html
- **Android Architecture:** https://developer.android.com/topic/architecture

## 💡 Ideas for Advanced Features

- **Voice announcements** for set completion
- **Apple Watch/Wear OS** companion app
- **Social sharing** of workouts
- **Progress photos** tracking
- **Body measurements** logging
- **Nutrition tracking** integration
- **Rest day** recommendations
- **1RM calculator**
- **Plate calculator** (for barbell loading)
- **Workout streaks** and badges
- **Custom sounds/music** for rest timer
- **Bluetooth heart rate** monitor integration

---

## 🎉 Make It Yours!

The app is fully open source and customizable. Don't be afraid to experiment - that's how you learn!

**Pro Tip:** Create a new git branch before major customizations so you can always revert if needed.

Happy customizing! 🚀
