@echo off
REM Workout Tracker Android - Build Script (Windows)
REM This script helps you build the APK easily

echo.
echo 🏋️ Workout Tracker Android - Build Script
echo ==========================================
echo.

REM Check if gradlew.bat exists
if not exist "gradlew.bat" (
    echo ❌ Error: gradlew.bat not found. Make sure you're in the project root directory.
    echo    Run this script from: WorkoutTrackerAndroid\
    exit /b 1
)

echo 📦 Building APK...
echo.

REM Build debug APK
echo Building debug APK (faster, for testing)...
call gradlew.bat assembleDebug

if %errorlevel% neq 0 (
    echo.
    echo ❌ Build failed. Check the errors above.
    exit /b 1
)

echo.
echo ✅ Debug APK built successfully!
echo 📍 Location: app\build\outputs\apk\debug\app-debug.apk
echo.
echo To install on device:
echo   adb install app\build\outputs\apk\debug\app-debug.apk
echo.

REM Ask if user wants release build
set /p release="Do you want to build a release APK? (y/n): "

if /i "%release%"=="y" (
    echo.
    echo Building release APK (optimized, smaller size)...
    call gradlew.bat assembleRelease
    
    if %errorlevel% neq 0 (
        echo.
        echo ❌ Release build failed. Check the errors above.
        exit /b 1
    )
    
    echo.
    echo ✅ Release APK built successfully!
    echo 📍 Location: app\build\outputs\apk\release\app-release-unsigned.apk
    echo.
    echo ⚠️  Note: This APK is unsigned. To publish to Play Store,
    echo    you need to sign it with a keystore.
    echo.
)

echo.
echo 🎉 Build complete!
echo.
echo Next steps:
echo   1. Test the debug APK on your device/emulator
echo   2. For Play Store: Sign the release APK with your keystore
echo   3. Enjoy tracking your workouts! 💪
echo.
pause
