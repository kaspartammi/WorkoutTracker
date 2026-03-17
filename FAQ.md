# ❓ FAQ & Troubleshooting Guide

Common questions and solutions for the Workout Tracker Android app.

## 🚀 Getting Started

### Q: What do I need to run this app?

**A:** You need:
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or higher
- Android SDK 34
- A device or emulator running Android 8.0+ (API 26+)

### Q: How do I open the project?

**A:**
1. Extract the ZIP file
2. Open Android Studio
3. File > Open
4. Navigate to `WorkoutTrackerAndroid` folder
5. Click OK
6. Wait for Gradle sync (2-3 minutes)

### Q: The project won't open in Android Studio

**A:** Check:
- Android Studio version (must be Hedgehog or newer)
- JDK version (Settings > Build > Build Tools > Gradle JDK)
- Try: File > Invalidate Caches > Invalidate and Restart

## 🔧 Build Issues

### Q: Gradle sync failed

**A:** Try these steps in order:

1. **Check internet connection** (Gradle downloads dependencies)

2. **Clean and rebuild:**
   ```
   Build > Clean Project
   Build > Rebuild Project
   ```

3. **Invalidate caches:**
   ```
   File > Invalidate Caches > Invalidate and Restart
   ```

4. **Check Gradle JDK:**
   ```
   Settings > Build, Execution, Deployment > Build Tools > Gradle
   Gradle JDK: Select JDK 17 or higher
   ```

5. **Update Gradle wrapper (if needed):**
   ```
   ./gradlew wrapper --gradle-version 8.2
   ```

### Q: "Could not find or load main class org.gradle.wrapper.GradleWrapperMain"

**A:** Gradle wrapper is missing or corrupted.

**Solution:**
```bash
# Download Gradle wrapper
gradle wrapper --gradle-version 8.2

# Or copy from another Android project
```

### Q: Build fails with "Execution failed for task ':app:compileDebugKotlin'"

**A:** Kotlin compilation error.

**Check:**
- All import statements are correct
- No syntax errors in Kotlin files
- Kotlin plugin version matches in build.gradle.kts
- Clean project and rebuild

### Q: "SDK location not found"

**A:** Android SDK not configured.

**Solution:**
1. Open SDK Manager (Tools > SDK Manager)
2. Install Android SDK 34
3. Create `local.properties` in project root:
   ```properties
   sdk.dir=/Users/YourName/Library/Android/sdk
   ```
   (Path varies by OS - check Android Studio settings)

### Q: Dependencies not downloading

**A:** Network or repository issue.

**Check:**
1. Internet connection stable
2. Not behind firewall blocking downloads
3. Try different network if possible
4. Clear Gradle cache:
   ```bash
   rm -rf ~/.gradle/caches
   ./gradlew --refresh-dependencies
   ```

## 📱 Running the App

### Q: App won't install on device

**A:** Check:

1. **USB Debugging enabled:**
   - Settings > Developer Options > USB Debugging ON
   
2. **Developer Options visible:**
   - Settings > About Phone > Tap "Build Number" 7 times
   
3. **Device connected:**
   - Run `adb devices` in terminal
   - Should show your device listed
   
4. **Minimum Android version:**
   - Device must be Android 8.0 (API 26) or higher
   
5. **Storage space:**
   - Device has enough free space

### Q: "Installation failed with message Invalid File"

**A:** APK corrupted or incompatible.

**Solution:**
1. Clean and rebuild project
2. Uninstall old version from device
3. Run again

### Q: App crashes immediately on launch

**A:** Check:

1. **Logcat for error messages:**
   - View > Tool Windows > Logcat
   - Look for red error messages
   
2. **Common causes:**
   - Missing permissions in manifest
   - Data model serialization errors
   - Repository initialization issues
   
3. **Try:**
   - Uninstall and reinstall app
   - Clear app data: Settings > Apps > Workout Tracker > Clear Data
   - Check Android version compatibility

### Q: Emulator is slow or freezing

**A:** Emulator performance issues.

**Solutions:**
1. **Enable Hardware Acceleration:**
   - Tools > AVD Manager > Edit AVD > Advanced Settings
   - Graphics: Hardware - GLES 2.0
   
2. **Allocate more RAM:**
   - Edit AVD > Advanced Settings
   - RAM: 2048 MB or higher
   
3. **Use smaller device:**
   - Pixel 5 instead of Pixel 7 Pro
   
4. **Close other applications**
   
5. **Use physical device instead** (much faster)

## 💾 Data & Storage

### Q: Where is workout data stored?

**A:** Data is stored locally on the device using Android DataStore in:
```
/data/data/com.workouttracker.app/files/datastore/workout_tracker.preferences_pb
```

Data includes:
- Workout templates
- Workout sessions
- User settings

### Q: How do I backup my data?

**A:** Currently manual backup:

1. **Via ADB:**
   ```bash
   adb backup -f backup.ab com.workouttracker.app
   ```

2. **Restore:**
   ```bash
   adb restore backup.ab
   ```

**Future:** Export/import feature can be added (see CUSTOMIZATION_GUIDE.md)

### Q: Data disappeared after update

**A:** Shouldn't happen, but if it does:

1. **Check app not uninstalled**
2. **Check data not cleared** (Settings > Apps > Storage)
3. **Restore from backup** if available
4. **Prevent in future:**
   - Don't clear app data
   - Don't uninstall before updating

### Q: Can I transfer data to new phone?

**A:** Yes, using Android's built-in backup:

1. **On old phone:**
   - Settings > System > Backup
   - Enable Google backup
   
2. **On new phone:**
   - During setup, restore from Google backup
   - Or: Settings > System > Backup > Restore

**Note:** Requires Google account backup enabled.

## 🎨 UI & Display

### Q: Text is too small/large

**A:** App respects system font size.

**Change:**
- Settings > Display > Font Size
- App will update automatically

### Q: Dark mode not working

**A:** Check:

1. **Settings in app:**
   - Settings > Theme > Dark or System Default
   
2. **System dark mode:**
   - Settings > Display > Dark Theme (if using "System Default")
   
3. **Restart app** after changing

### Q: Colors look different than expected

**A:** Device may have:
- Color filters enabled
- Night mode/blue light filter active
- Custom ROM with modified colors

**Check:** Settings > Accessibility > Color & contrast

### Q: Layout looks broken on tablet

**A:** App is optimized for phones but should work on tablets.

If issues:
- Portrait mode recommended
- Some screens may have excess whitespace (normal)
- Report specific issues for improvements

## 🏋️ App Functionality

### Q: Rest timer not working

**A:** Check:

1. **Setting enabled:**
   - Settings > Rest Timer > ON
   
2. **Complete a set** during workout to trigger timer
   
3. **Timer should appear** in purple banner at top

**If still broken:**
- Check Logcat for errors
- Restart app
- Clear app data

### Q: Can't add exercises to workout

**A:** Check:

1. **Tap "+" button** (bottom right floating button)
2. **Fill in exercise name** (required)
3. **Tap "SAVE"** at top

**If "SAVE" disabled:**
- Workout name must not be empty
- At least one exercise must have a name

### Q: Workouts not saving

**A:** Check:

1. **Tap "SAVE"** button (top right)
2. **Check for errors** in Logcat
3. **Storage permissions** (shouldn't be needed but check)

**Debug:**
- Add logging to repository save functions
- Check DataStore writes

### Q: Exercise order keeps resetting

**A:** Not a known issue.

**If happens:**
- Ensure you tap SAVE after reordering
- Check Logcat for serialization errors
- Clear app data and re-add workouts

## 🔊 Sounds & Notifications

### Q: No sound when rest timer completes

**A:** Feature not implemented in base version.

**To add:**
- See CUSTOMIZATION_GUIDE.md > "Add Sound/Vibration to Rest Timer"
- Requires coding and vibration permission

### Q: Can I get notifications for rest timer?

**A:** Not currently implemented.

**To add:**
- Implement NotificationManager
- Request notification permission
- Create notification channel
- See Android documentation for WorkManager

## 🔄 Updates & Versioning

### Q: How do I update the app?

**A:** 

**Development:**
1. Pull latest code
2. Rebuild and run

**Published app:**
- Updates via Google Play Store automatically
- Or manual APK replacement

### Q: How do I check app version?

**A:** 
- Settings > About > Version: 1.0.0
- Or in build.gradle.kts: `versionName`

### Q: Will updates delete my data?

**A:** No, updates preserve data if:
- Updating (not uninstalling and reinstalling)
- Same signing key used
- Database migrations properly handled

## 🐛 Known Issues

### Issue: Rapid button clicking can cause duplicate entries

**Status:** Minor UI bug
**Workaround:** Disable button after click
**Fix:** Add click debouncing

### Issue: Very long workout names may overflow on small screens

**Status:** Edge case
**Workaround:** Keep names under 30 characters
**Fix:** Add text ellipsis and truncation

### Issue: No confirmation when deleting workouts from swipe

**Status:** Not applicable (using menu, not swipe)
**Fix:** Delete requires menu tap + dialog confirmation

## 📦 Publishing

### Q: How do I publish to Play Store?

**A:** See `PLAY_STORE_GUIDE.md` for complete walkthrough.

**Quick steps:**
1. Create signing key
2. Build signed AAB
3. Create Play Console listing
4. Upload AAB
5. Submit for review

### Q: Do I need to change package name?

**A:** Yes, before publishing:

**Change:**
```kotlin
// build.gradle.kts
applicationId = "com.yourcompany.workouttracker"
```

**Refactor:**
1. Right-click package in Android Studio
2. Refactor > Rename
3. Update throughout project

### Q: Play Store rejected my app

**A:** Common reasons:

1. **Privacy Policy missing**
2. **Content rating incomplete**
3. **Screenshots insufficient**
4. **Target SDK too low**
5. **Permissions not justified**

**Solution:** Check rejection email for specific reason and fix.

## 🛠️ Advanced

### Q: How do I add new features?

**A:** See `CUSTOMIZATION_GUIDE.md` for examples.

**General process:**
1. Decide on feature
2. Update data models if needed
3. Update repository/ViewModels
4. Create/modify UI screens
5. Test thoroughly
6. Increment version

### Q: Can I use this app commercially?

**A:** Yes! The code is provided for you to use, modify, and publish.

**Remember to:**
- Change app name and branding
- Update package name
- Create your own icon
- Write your own privacy policy
- Comply with Play Store policies

### Q: How do I contribute improvements?

**A:** If you've added features and want to share:

1. Document your changes
2. Test on multiple devices
3. Create clear before/after examples
4. Share via GitHub repository (if created)

## 💬 Getting Help

### Resources:

**Documentation:**
- README.md - Project overview
- QUICK_START.md - Setup guide
- SCREENS_GUIDE.md - Feature walkthrough
- CUSTOMIZATION_GUIDE.md - Modification guide
- PLAY_STORE_GUIDE.md - Publishing guide

**External Resources:**
- [Android Developers](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android)
- [Reddit r/androiddev](https://reddit.com/r/androiddev)

**Debugging Tools:**
- Logcat (Android Studio)
- Layout Inspector
- Profiler
- Database Inspector

### Q: Where can I report bugs?

**A:** 
- Check Logcat for error messages
- Document steps to reproduce
- Note device/Android version
- Share error logs

### Q: Can I hire someone to customize this?

**A:** Yes! Many Android developers available:
- Upwork
- Fiverr
- Toptal
- Local developers

**Typical costs:**
- Minor customizations: $50-200
- New features: $200-1000+
- Complete redesign: $1000-5000+

## 🎓 Learning More

### Q: I want to learn Android development, where do I start?

**A:** Great resources:

**Official:**
- [Android Basics with Compose](https://developer.android.com/courses/android-basics-compose/course)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)

**YouTube:**
- Philipp Lackner (Compose tutorials)
- Stevdza-San (Kotlin & Compose)
- Android Developers (official channel)

**Books:**
- "Android Programming: The Big Nerd Ranch Guide"
- "Kotlin in Action"

**Practice:**
- Modify this app!
- Add features from CUSTOMIZATION_GUIDE
- Build your own variations

---

## 📞 Still Need Help?

**Before asking:**
1. ✅ Read relevant documentation
2. ✅ Check Logcat for errors
3. ✅ Try clean and rebuild
4. ✅ Search Stack Overflow
5. ✅ Google the exact error message

**When asking:**
- Provide Android Studio version
- Share error messages/logs
- Describe steps to reproduce
- Mention what you've already tried
- Include device/emulator details

**Remember:** Most issues are:
- Gradle configuration (70%)
- Missing dependencies (15%)
- SDK/JDK version mismatch (10%)
- Code errors (5%)

Good luck, and happy coding! 🚀💪
