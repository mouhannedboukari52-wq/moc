# Daily Tracker — Android App

A modern Android app for tracking daily food and sport routines with reminders and health tips.

## Features

- **Custom Authentication** — Register/Login with username + password (SHA-256 hashed), stored in Firebase Realtime Database
- **Routine Management** — Create Food/Sport routines with daily time reminders
- **Daily Tracking** — Mark routines as done for today
- **Health Tips** — Food and Sport tips loaded from Firebase (local defaults included)
- **Notifications** — AlarmManager reminders + foreground service with persistent notification
- **Material Design** — Modern card-based UI with BottomNavigationView

## Tech Stack

- **Language**: Java
- **UI**: XML layouts + Material Components
- **Navigation**: BottomNavigationView (MainActivity + Fragments)
- **Backend**: Firebase Realtime Database
- **Build**: Gradle 8.x

## Firebase Setup

### Step 1 — Create a Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Add project"** → enter project name → create

### Step 2 — Add Android App
1. In your Firebase project, click the Android icon
2. Enter package name: `com.dailytracker.app`
3. Download **`google-services.json`**
4. Place it in the `app/` folder:
   ```
   app/
   └── google-services.json   ← place here
   ```

### Step 3 — Enable Realtime Database
1. In Firebase Console → **Build → Realtime Database**
2. Click **"Create Database"**
3. Choose a region and start in **test mode** (or use rules below)

### Step 4 — Database Security Rules (for testing)
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```
> ⚠️ These rules allow open access. Set proper rules before production.

### Step 5 — Expected Database Structure
```
daily-tracker-db/
├── users/
│   └── {userId}/
│       ├── uid: "..."
│       ├── name: "username"
│       └── passwordHash: "sha256hash..."
├── routines/
│   └── {userId}/
│       └── {routineId}/
│           ├── id: "..."
│           ├── userId: "..."
│           ├── title: "Morning Jog"
│           ├── category: "SPORT"
│           ├── hour: 7
│           └── minute: 30
├── daily_logs/
│   └── {userId}/
│       └── {routineId}/
│           └── {date (yyyy-MM-dd)}/
│               ├── routineId: "..."
│               ├── date: "2024-01-15"
│               └── done: true
└── tips/
    ├── FOOD/
    │   └── {tipId}/
    │       ├── id: "..."
    │       ├── category: "FOOD"
    │       ├── title: "Stay Hydrated"
    │       └── description: "Drink at least 8 glasses..."
    └── SPORT/
        └── {tipId}/
            ├── id: "..."
            ├── category: "SPORT"
            ├── title: "30 Minutes Daily"
            └── description: "Aim for 30 minutes..."
```

## Project Structure

```
app/src/main/java/com/dailytracker/app/
├── activities/
│   ├── SplashActivity.java
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   ├── MainActivity.java
│   └── AddRoutineActivity.java
├── fragments/
│   ├── HomeFragment.java
│   ├── RoutinesFragment.java
│   ├── TipsFragment.java
│   └── ProfileFragment.java
├── adapters/
│   ├── RoutineAdapter.java
│   └── TipAdapter.java
├── models/
│   ├── User.java
│   ├── RoutineItem.java
│   ├── Tip.java
│   └── DailyLog.java
├── data/
│   ├── DbPaths.java
│   └── FirebaseRepository.java
├── utils/
│   ├── HashUtils.java
│   ├── DateUtils.java
│   ├── NotificationUtils.java
│   └── SessionManager.java
├── service/
│   └── ReminderForegroundService.java
└── receiver/
    ├── ReminderReceiver.java
    └── BootReceiver.java
```

## Building the App

1. Open in **Android Studio Hedgehog** or later
2. Place `google-services.json` in the `app/` folder
3. Sync Gradle
4. Build and run on device/emulator (API 21+)

## Notification Permissions

- Android 13+ (API 33): the app requests `POST_NOTIFICATIONS` permission at runtime
- Android 12+ (API 31): exact alarms require `SCHEDULE_EXACT_ALARM`

## License

MIT