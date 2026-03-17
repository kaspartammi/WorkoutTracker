#!/bin/bash

echo "🏋️ Workout Tracker - Quick Icon Fix"
echo "====================================="
echo ""

# Check if running from correct directory
if [ ! -f "app/build.gradle.kts" ]; then
    echo "❌ Error: Run this script from the project root directory"
    echo "   cd WorkoutTrackerAndroid"
    echo "   ./generate_icons_quick.sh"
    exit 1
fi

echo "Checking for icon generation tools..."
echo ""

# Try Python method
if command -v python3 &> /dev/null; then
    if python3 -c "import PIL" &> /dev/null 2>&1; then
        echo "✅ Found Python 3 with PIL/Pillow"
        echo "📦 Generating icons using Python..."
        python3 generate_icons.py
        exit 0
    else
        echo "⚠️  Python 3 found but PIL/Pillow not installed"
        echo "   Install with: pip3 install Pillow"
    fi
fi

# Try ImageMagick method
if command -v convert &> /dev/null; then
    echo "✅ Found ImageMagick"
    echo "📦 Generating icons using ImageMagick..."
    
    cd app/src/main/res
    
    # Create all mipmap directories
    mkdir -p mipmap-mdpi mipmap-hdpi mipmap-xhdpi mipmap-xxhdpi mipmap-xxxhdpi
    
    # Generate base icon (purple circle with white center)
    convert -size 192x192 xc:"#6750A4" \
        -fill white -draw "circle 96,96 96,48" \
        mipmap-xxxhdpi/ic_launcher.png
    
    # Resize for other densities
    convert mipmap-xxxhdpi/ic_launcher.png -resize 144x144 mipmap-xxhdpi/ic_launcher.png
    convert mipmap-xxxhdpi/ic_launcher.png -resize 96x96 mipmap-xhdpi/ic_launcher.png
    convert mipmap-xxxhdpi/ic_launcher.png -resize 72x72 mipmap-hdpi/ic_launcher.png
    convert mipmap-xxxhdpi/ic_launcher.png -resize 48x48 mipmap-mdpi/ic_launcher.png
    
    # Copy for round icons
    for dir in mipmap-*; do
        cp $dir/ic_launcher.png $dir/ic_launcher_round.png
    done
    
    cd - > /dev/null
    
    echo ""
    echo "✅ Icons generated successfully!"
    echo ""
    echo "Next steps:"
    echo "1. Rebuild project in Android Studio"
    echo "2. Run app on device"
    exit 0
fi

# No tools found
echo "❌ No icon generation tools found"
echo ""
echo "🔧 Solutions:"
echo ""
echo "Option 1: Install Python PIL/Pillow"
echo "  pip3 install Pillow"
echo "  ./generate_icons_quick.sh"
echo ""
echo "Option 2: Install ImageMagick"
echo "  # Ubuntu/Debian:"
echo "  sudo apt-get install imagemagick"
echo "  # macOS:"
echo "  brew install imagemagick"
echo "  ./generate_icons_quick.sh"
echo ""
echo "Option 3: Use Android Studio (EASIEST)"
echo "  1. Right-click app/src/main/res"
echo "  2. New → Image Asset"
echo "  3. Choose Clip Art → Search 'fitness'"
echo "  4. Set background color: #6750A4"
echo "  5. Next → Finish"
echo ""
echo "See ICON_FIX.md for detailed instructions"
