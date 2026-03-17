# 🎨 App Icon Setup Guide

Add a custom icon to make your Workout Tracker app stand out!

## 📱 Current Icon Status

The app currently references default Android launcher icons:
- `@mipmap/ic_launcher` (standard icon)
- `@mipmap/ic_launcher_round` (adaptive icon)

## 🖼️ Quick Icon Setup (Recommended)

### Option 1: Use Android Studio's Image Asset Tool

1. **Open Image Asset Studio**
   ```
   Right-click on app/src/main/res
   → New → Image Asset
   ```

2. **Configure Launcher Icon**
   - Icon Type: **Launcher Icons (Adaptive and Legacy)**
   - Asset Type: **Image** or **Clip Art**
   - Path: Browse to your icon image (PNG, 1024×1024 recommended)

3. **Customize**
   - Foreground Layer: Your icon image
   - Background Layer: Solid color or image
   - Preview all adaptive icon shapes

4. **Generate**
   - Click **Next**
   - Click **Finish**
   - Icons automatically created in all required sizes!

### Option 2: Figma Export Method

Since your design is from Figma, you can export an icon directly:

1. **In Figma**
   - Create a 1024×1024 frame with your app icon design
   - Export as PNG at 1x, 2x, 3x resolutions

2. **Use Image Asset Studio**
   - Follow Option 1 steps above
   - Use your exported PNG as the source

## 🎯 Icon Design Best Practices

### Recommended Specifications
- **Size:** 1024×1024 px (will be scaled down automatically)
- **Format:** PNG with transparency
- **Design:** Simple, recognizable at small sizes
- **Colors:** Use your app's primary colors

### Workout App Icon Ideas
- 💪 Dumbbell silhouette
- 📊 Progress chart/bars
- 🏋️ Weight plate design
- ⚡ Lightning bolt (energy/power)
- 🎯 Target with checkmark

### Adaptive Icon Considerations
Modern Android uses adaptive icons with:
- **Foreground layer:** Your main icon graphic
- **Background layer:** Solid color or pattern
- System shapes: Circle, rounded square, squircle, etc.

## 📂 Manual Icon Setup (Advanced)

If you prefer manual control:

### 1. Create Icon Sizes

Generate your icon in these sizes:

**Mipmap folders** (for launcher icons):
```
app/src/main/res/
├── mipmap-mdpi/ic_launcher.png       (48×48 px)
├── mipmap-hdpi/ic_launcher.png       (72×72 px)
├── mipmap-xhdpi/ic_launcher.png      (96×96 px)
├── mipmap-xxhdpi/ic_launcher.png     (144×144 px)
└── mipmap-xxxhdpi/ic_launcher.png    (192×192 px)
```

### 2. Create Adaptive Icons

For modern adaptive icons, create XML files:

**app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@mipmap/ic_launcher_foreground"/>
</adaptive-icon>
```

**app/src/main/res/values/ic_launcher_background.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="ic_launcher_background">#6750A4</color>
</resources>
```

### 3. Add Foreground Assets

Place foreground icon layers in each mipmap folder:
- `ic_launcher_foreground.png` in each density folder
- Or use vector drawable in `drawable/` folder

## 🔧 Testing Your Icon

### 1. Rebuild the App
```bash
Build > Clean Project
Build > Rebuild Project
```

### 2. Reinstall
- Uninstall old version from device/emulator
- Run app again
- New icon should appear on launcher

### 3. Test Adaptive Shapes
- Long-press home screen → Widgets → look for your app
- Try on different Android versions/OEM skins
- Check all adaptive shapes look good

## 🎨 Color Matching Your Figma Design

Based on your app's theme colors:

**Primary Color:** `#6750A4` (Purple)
**Background Options:**
- Solid: Use primary color
- Gradient: Purple to darker shade
- Minimal: White/light gray with colored foreground

**Icon Foreground Ideas:**
- White dumbbell on purple background
- Purple icon on white background
- Gradient background with simple symbol

## 🚀 Quick Figma Icon Export

1. Create a 1024×1024 frame in Figma
2. Design icon (keep it centered, account for safe zone)
3. Export as PNG
4. Use Android Studio Image Asset tool
5. Done in 5 minutes!

## 📱 Example Icon Setups

### Minimalist
- Background: Solid purple (#6750A4)
- Foreground: White dumbbell icon
- Style: Flat, modern

### Detailed
- Background: Gradient (purple to dark purple)
- Foreground: Stylized workout symbol
- Style: Material Design

### Simple
- Background: White
- Foreground: Purple icon with subtle shadow
- Style: Clean, professional

## ✅ Verification Checklist

- [ ] Icon visible on launcher
- [ ] Icon looks good in all adaptive shapes
- [ ] Icon recognizable at small sizes
- [ ] Colors match app theme
- [ ] No pixelation or artifacts
- [ ] Tested on multiple devices/emulators

## 🎯 Quick Fix: Temporary Icon

If you just want a quick placeholder while designing:

1. Use Android Studio's clip art library
2. Choose "sports" or "fitness" category
3. Pick dumbbell or similar icon
4. Set color to `#6750A4`
5. Generate - done in 30 seconds!

---

**Need inspiration?** Check Material Design icon guidelines or look at popular fitness app icons on the Play Store!
