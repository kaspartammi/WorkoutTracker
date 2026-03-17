# 📱 App Screens & Features - Detailed Guide

This document provides a detailed walkthrough of each screen in the Workout Tracker app and how they work together.

## 🏠 Screen 1: Home Screen

**Purpose:** Main dashboard and workout management

### Features:
- **Quick Start Card** (Purple container at top)
  - "Start Workout" button
  - Navigates to workout selection
  - Prominent placement for easy access

- **Workout List**
  - Cards displaying each saved workout
  - Shows workout name and description
  - Displays number of exercises
  - Shows "last performed" date
  - Overflow menu (⋮) for edit/delete

- **Empty State**
  - Friendly message when no workouts exist
  - Dumbbell icon
  - Prompt to create first workout

- **FAB (Floating Action Button)**
  - Purple "+" button in bottom-right
  - Creates new workout

- **Top Bar**
  - App title: "Workout Tracker"
  - Settings icon (⚙️)

### User Flow:
```
Launch App → Home Screen
├─→ Tap "Start Workout" → Select Workout Screen
├─→ Tap "+" FAB → New Workout Screen
├─→ Tap "⋮" on card → Edit/Delete options
└─→ Tap "⚙️" → Settings Screen
```

### Data Displayed:
- Workout template name
- Description
- Exercise count
- Last performed timestamp
- Total workouts count

---

## 🎯 Screen 2: Select Workout Screen

**Purpose:** Choose which workout to begin

### Features:
- **Back Button**
  - Returns to Home

- **Workout Selection Cards**
  - Larger, clickable cards
  - Shows all workout details
  - Preview of first 3 exercises
  - "Tap to start" indicator
  - Exercise count badge

- **Exercise Preview**
  - Shows first 3 exercises inline
  - Format: "• Exercise Name: Sets × Reps"
  - "And X more..." for additional exercises

- **Empty State**
  - Message if no workouts available
  - Prompt to create workout first

### User Flow:
```
Select Workout Screen
├─→ Tap any workout card → Start Session
└─→ Tap back → Return to Home
```

### Selection Logic:
- All saved workouts displayed
- Sorted by most recent first
- One-tap to start
- No confirmation needed

---

## 🏋️ Screen 3: Active Workout Session

**Purpose:** Real-time workout tracking and set logging

### Features:

#### Header Section:
- **Close Button** (X)
  - Shows finish confirmation dialog
  
- **Workout Name**
  - Current workout title displayed
  
- **Finish Button**
  - Text button "FINISH"
  - Saves session and returns home

- **Progress Bar**
  - Linear indicator showing exercise progress
  - Updates as you navigate exercises

#### Rest Timer Banner (when active):
- **Prominent Purple Container**
  - Timer icon
  - "Rest Time" label
  - Countdown display (MM:SS format)
  - "Skip Rest" button
  
- **Auto-triggers** after completing a set
- **Countdown behavior:**
  - Ticks down every second
  - Disappears when reaching 0:00
  - Can be skipped manually

#### Exercise Navigation:
- **Navigation Bar**
  - ← Previous exercise button
  - "Exercise X of Y" counter
  - → Next exercise button
  - Buttons disabled at boundaries

#### Current Exercise Card:
- **Exercise Details** (Gray container)
  - Exercise name (large, bold)
  - Notes (if any)
  - Info chips:
    - Sets: "3"
    - Reps: "10-12"
    - Rest: "60s"

#### Active Set Input (Purple container):
- **Set Counter**
  - "Set X of Y" display
  
- **Input Fields**
  - Reps: Number input
  - Weight: Number input
  - Pre-filled with suggested values
  
- **Action Buttons**
  - "Skip Set" (outlined)
  - "Complete" (filled, with checkmark)

#### Completed Sets History:
- **Section Header:** "Completed Sets"
- **Set Cards** (reverse chronological)
  - Set number
  - Reps performed
  - Weight used
  - Checkmark icon OR "Skipped" label

### User Flow:
```
Active Session
├─→ Enter reps/weight → Tap "Complete"
│   └─→ Rest timer starts automatically
├─→ During rest → Tap "Skip Rest" (optional)
├─→ Navigate exercises → ← → buttons
├─→ Skip a set → Tap "Skip Set"
└─→ Finish workout → Tap "FINISH" → Confirmation → Home
```

### Data Tracking:
- Each set logs:
  - Reps completed
  - Weight used
  - Timestamp
  - Completion status
- Session records:
  - Start time
  - End time
  - All completed sets
  - Workout template used

### Smart Features:
- **Auto-rest timer** after each set
- **Input persistence** (previous values remembered)
- **Progress indicator** across top
- **Real-time countdown** during rest
- **Set history** visible immediately

---

## ✏️ Screen 4: Edit/Create Workout Screen

**Purpose:** Build and modify workout templates

### Features:

#### Top Bar:
- **Close Button** (X)
  - Returns without saving
  
- **Title**
  - "New Workout" or "Edit Workout"
  
- **Save Button**
  - Enabled only when valid
  - Shows loading spinner while saving

#### Workout Details Card:
- **Section Header:** "Workout Details"
- **Name Input**
  - Required field
  - Placeholder: "e.g., Push Day, Leg Day"
  
- **Description Input**
  - Optional field
  - Multi-line (3 rows)
  - Placeholder: "e.g., Chest, Shoulders, and Triceps"

#### Exercises Section:
- **Section Header**
  - "Exercises" title
  - Exercise count badge
  
- **Empty State**
  - Icon, message, and help text
  - Shows when no exercises added

#### Exercise Cards (Expandable):
- **Collapsed View:**
  - Exercise number badge
  - Exercise name
  - Up/Down reorder buttons (if applicable)
  - Expand/collapse button (^/v)
  - Delete button (trash icon)

- **Expanded View:**
  - Exercise name input
  - Sets input (number)
  - Reps input (text, allows "10" or "8-12")
  - Rest time input (seconds)
  - Notes input (optional, 2 lines)
  
- **Reorder Controls:**
  - Up arrow (↑) - moves exercise up
  - Down arrow (↓) - moves exercise down
  - Disabled at list boundaries

#### FAB (Floating Action Button):
- **"+" Button**
  - Adds new blank exercise
  - Auto-expands for editing

### User Flow:
```
Edit Workout Screen
├─→ Enter workout name (required)
├─→ Enter description (optional)
├─→ Tap "+" → Add exercise
│   ├─→ Enter exercise details
│   ├─→ Reorder with ↑↓
│   └─→ Delete with trash icon
├─→ Tap "SAVE" → Return to Home
└─→ Tap "X" → Discard changes
```

### Validation Rules:
- **Workout name:** Required, non-empty
- **At least 1 exercise** with name required
- Save button disabled until valid
- Blank exercises filtered out on save

### Smart Features:
- **Auto-expand** new exercises
- **Collapse others** to save space
- **Drag-free reordering** with buttons
- **Inline deletion** with confirmation
- **Real-time validation** feedback

---

## ⚙️ Screen 5: Settings Screen

**Purpose:** Configure app preferences and view information

### Features:

#### Top Bar:
- **Back Button** (←)
  - Returns to previous screen
  
- **Title:** "Settings"

#### General Section:
- **Section Header:** "General"

- **Weight Unit Preference**
  - Icon: Scale (⚖️)
  - Current selection: "LBS" or "KG"
  - Tap → Opens selection dialog
  - Radio button selection

- **Theme Preference**
  - Icon: Palette (🎨)
  - Current: "Light", "Dark", or "System Default"
  - Tap → Opens theme dialog
  - Radio button selection
  - Applied immediately

#### Workout Section:
- **Section Header:** "Workout"

- **Rest Timer Toggle**
  - Icon: Timer
  - Switch control (right side)
  - Enables/disables rest timer feature
  - Affects all workouts

- **Default Rest Time**
  - Icon: Hourglass
  - Shows current value (seconds)
  - Tap → Number input dialog
  - Only enabled if timer is on
  - Grayed out when timer disabled

#### About Section:
- **Section Header:** "About"

- **Version Info**
  - Icon: Info (ℹ️)
  - Shows version "1.0.0"
  - Non-clickable

- **App Info Card** (Purple container)
  - Dumbbell icon
  - App name
  - Brief description
  - Branded look

### User Flow:
```
Settings Screen
├─→ Tap "Weight Unit" → Select LBS/KG → Auto-save
├─→ Tap "Theme" → Select Light/Dark/System → Auto-save
├─→ Toggle "Rest Timer" → Enable/disable → Auto-save
├─→ Tap "Default Rest Time" → Enter seconds → Save
└─→ Tap back → Return to previous screen
```

### Data Persistence:
- All settings saved immediately
- Applied across entire app
- Persists between sessions
- No explicit "save" needed

### Setting Effects:
- **Weight Unit:** Affects display format in workouts
- **Theme:** Changes UI immediately
- **Rest Timer:** Enables/disables auto-timer during sessions
- **Default Rest Time:** Used when creating new exercises

---

## 🔄 Cross-Screen Features

### Navigation Patterns:
```
Home ⟷ Settings
  ↓↑
Select Workout
  ↓
Active Session → (completes) → Home
  
Home → Edit Workout → (saves) → Home
```

### Data Flow:
```
Workout Templates (Repository)
    ↓
Home Screen (displays)
    ↓
Select Screen (chooses)
    ↓
Active Session (tracks)
    ↓
Completed Session (saves)
    ↓
Updates Template (last performed)
    ↓
Refreshes Home Screen
```

### Shared UI Elements:
- **Material 3 Design** throughout
- **Purple primary color** (#6750A4)
- **Consistent card elevations**
- **Icon consistency** (Material Icons)
- **Typography hierarchy**
- **Touch targets** (48dp minimum)

### Persistent Features:
- **Back navigation** preserves state
- **Confirmation dialogs** for destructive actions
- **Loading states** during saves
- **Error handling** with user-friendly messages
- **Smooth animations** between screens

---

## 📊 User Journey Examples

### First-Time User:
1. Launch app → See sample workouts
2. Tap "Start Workout"
3. Select "Push Day"
4. Complete sets with guided interface
5. Experience rest timer
6. Finish workout → Return to home
7. See "last performed" updated

### Creating Custom Workout:
1. From Home → Tap "+"
2. Name workout "My Routine"
3. Add description
4. Tap "+" to add exercise
5. Fill in "Bench Press" details
6. Add more exercises
7. Reorder if needed
8. Tap "SAVE"
9. See new workout on home

### Editing Existing Workout:
1. From Home → Tap "⋮" on workout
2. Select "Edit"
3. Modify exercise sets/reps
4. Add or remove exercises
5. Tap "SAVE"
6. Changes reflected immediately

---

## 🎨 Design Highlights

### Color System:
- **Primary:** Purple (#6750A4)
- **Containers:** Light purple for emphasis
- **Surface:** White (light) / Dark (dark mode)
- **Error:** Red for destructive actions

### Typography Scale:
- **Headlines:** Large, bold for screen titles
- **Titles:** Medium, semi-bold for cards
- **Body:** Regular for content
- **Labels:** Small for metadata

### Spacing System:
- **Cards:** 16dp padding
- **Lists:** 12dp gaps
- **Sections:** 8dp margins
- **Dense areas:** 4dp micro-spacing

### Component Consistency:
- **Cards:** Elevated, rounded corners
- **Buttons:** Filled (primary), Outlined (secondary)
- **Input fields:** Outlined style
- **Icons:** Material Design, 24dp standard

---

This comprehensive guide shows how every screen works together to create a seamless workout tracking experience! 🏋️💪
