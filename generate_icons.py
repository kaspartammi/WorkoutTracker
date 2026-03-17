#!/usr/bin/env python3
"""
Workout Tracker Icon Generator
Generates launcher icons in all required densities
"""

try:
    from PIL import Image, ImageDraw
except ImportError:
    print("❌ PIL (Pillow) not installed")
    print("Install with: pip install Pillow")
    print("Or use Android Studio's Image Asset tool instead")
    exit(1)

import os

# Icon sizes for different densities
SIZES = {
    'mipmap-mdpi': 48,
    'mipmap-hdpi': 72,
    'mipmap-xhdpi': 96,
    'mipmap-xxhdpi': 144,
    'mipmap-xxxhdpi': 192
}

# App theme purple color
BACKGROUND_COLOR = '#6750A4'
FOREGROUND_COLOR = '#FFFFFF'

def create_icon(size):
    """Create a simple dumbbell icon"""
    img = Image.new('RGBA', (size, size), BACKGROUND_COLOR)
    draw = ImageDraw.Draw(img)
    
    # Simple dumbbell shape
    center = size // 2
    
    # Weights (circles on sides)
    weight_radius = size // 6
    draw.ellipse(
        [size * 0.15, center - weight_radius, size * 0.25, center + weight_radius],
        fill=FOREGROUND_COLOR
    )
    draw.ellipse(
        [size * 0.75, center - weight_radius, size * 0.85, center + weight_radius],
        fill=FOREGROUND_COLOR
    )
    
    # Bar (rectangle in middle)
    bar_height = size // 12
    draw.rectangle(
        [size * 0.25, center - bar_height, size * 0.75, center + bar_height],
        fill=FOREGROUND_COLOR
    )
    
    return img

def main():
    print("🏋️ Workout Tracker Icon Generator")
    print("=" * 40)
    
    # Get project directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    res_dir = os.path.join(script_dir, 'app', 'src', 'main', 'res')
    
    if not os.path.exists(res_dir):
        print(f"❌ Resource directory not found: {res_dir}")
        print("Make sure you're running this from the project root")
        exit(1)
    
    print(f"📁 Resource directory: {res_dir}")
    print()
    
    created_count = 0
    
    # Generate icons for each density
    for folder, size in SIZES.items():
        folder_path = os.path.join(res_dir, folder)
        os.makedirs(folder_path, exist_ok=True)
        
        # Create regular icon
        icon_path = os.path.join(folder_path, 'ic_launcher.png')
        icon = create_icon(size)
        icon.save(icon_path, 'PNG')
        print(f"✅ Created {folder}/ic_launcher.png ({size}×{size})")
        created_count += 1
        
        # Create round icon (same image)
        round_icon_path = os.path.join(folder_path, 'ic_launcher_round.png')
        icon.save(round_icon_path, 'PNG')
        print(f"✅ Created {folder}/ic_launcher_round.png ({size}×{size})")
        created_count += 1
    
    print()
    print(f"🎉 Success! Created {created_count} icon files")
    print()
    print("Next steps:")
    print("1. Rebuild project in Android Studio")
    print("2. Run app on device")
    print("3. Check home screen for app icon")
    print()
    print("💡 Tip: You can customize the icon later using:")
    print("   Android Studio → New → Image Asset")

if __name__ == '__main__':
    main()
