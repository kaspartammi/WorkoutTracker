#!/bin/bash

# Workout Tracker Android - Build Script
# This script helps you build the APK easily

echo "🏋️ Workout Tracker Android - Build Script"
echo "=========================================="
echo ""

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "❌ Error: gradlew not found. Make sure you're in the project root directory."
    echo "   Run this script from: WorkoutTrackerAndroid/"
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

echo "📦 Building APK..."
echo ""

# Build debug APK
echo "Building debug APK (faster, for testing)..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Debug APK built successfully!"
    echo "📍 Location: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "To install on device:"
    echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
else
    echo "❌ Build failed. Check the errors above."
    exit 1
fi

# Ask if user wants release build
read -p "Do you want to build a release APK? (y/n) " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "Building release APK (optimized, smaller size)..."
    ./gradlew assembleRelease
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ Release APK built successfully!"
        echo "📍 Location: app/build/outputs/apk/release/app-release-unsigned.apk"
        echo ""
        echo "⚠️  Note: This APK is unsigned. To publish to Play Store,"
        echo "   you need to sign it with a keystore."
        echo ""
    else
        echo "❌ Release build failed. Check the errors above."
        exit 1
    fi
fi

echo ""
echo "🎉 Build complete!"
echo ""
echo "Next steps:"
echo "  1. Test the debug APK on your device/emulator"
echo "  2. For Play Store: Sign the release APK with your keystore"
echo "  3. Enjoy tracking your workouts! 💪"
