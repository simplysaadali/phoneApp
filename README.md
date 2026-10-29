# PhoneApp

<div align="center">

![Android](https://img.shields.io/badge/Android-API%2024%2B-3DDC84?style=for-the-badge&logo=android)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk)
![Room](https://img.shields.io/badge/Database-Room-5C2D91?style=for-the-badge)
![Navigation](https://img.shields.io/badge/Navigation-Android%20Nav%20Component-0A84FF?style=for-the-badge)
![License](https://img.shields.io/badge/Status-Open%20Project-34A853?style=for-the-badge)

<p>
  <strong>A modern Android contact manager</strong> built with Java, Room, LiveData, and Navigation.
  <br>
  Manage contacts, favorites, search, and quick actions with a sleek Material-inspired experience.
</p>

</div>

## ✨ Overview

PhoneApp is a clean and practical Android contact application designed for fast personal contact management. It stores entries locally using Room, provides real-time search and favorites filtering, and supports adding, editing, deleting, and viewing detailed contact information.

It also includes a smart demo dataset with Pakistani sample contacts to help showcase the app immediately when you run it for the first time.

## 🚀 Features

- Add, edit, and delete contacts
- Search contacts live as you type
- Toggle favorites for quick access
- Swipe-to-delete with undo action
- Contact detail view with full information
- Local persistence using Room database
- Modern Android Navigation component flow
- Material UI styling and responsive list layout

## 🧠 Tech Stack

- Java
- Android SDK
- Room Persistence Library
- ViewModel + LiveData
- Navigation Component
- RecyclerView + Material Components
- SQLite-backed local storage

## 📁 Project Structure

```text
PhoneApp/
├── app/
│   ├── src/main/java/com/example/phoneapp/
│   │   ├── data/
│   │   ├── repository/
│   │   ├── ui/
│   │   ├── utils/
│   │   └── PhoneApplication.java
│   ├── src/main/res/
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradlew
├── gradle.properties
├── .gitignore
└── README.md
```

## 🎯 App Flow

<details>
<summary>See how the app works</summary>

1. Launch the app and land on the contact list screen.
2. Search contacts instantly using the toolbar search.
3. Tap a contact to open its details.
4. Add a new contact using the floating action button.
5. Edit or favorite entries using actions in the UI.
6. Swipe left on a contact to delete it, with undo support.
7. Data persists locally in Room so it remains available after restart.

</details>

## 🏗️ Architecture

<details>
<summary>Architecture overview</summary>

This app follows a simple MVVM-style architecture:

- UI layer: Activities and Fragments
- ViewModel layer: manages screen state and transforms data
- Repository layer: handles access to database operations
- Data layer: Room entities, DAOs, and database setup

This separation keeps the app easier to maintain while supporting real-time contact updates.

</details>

## 🧪 Run Locally

### Prerequisites

- Android Studio
- JDK 8+
- Android SDK with API 24+

### Setup

```bash
cd PhoneApp
./gradlew assembleDebug
```

Then open the project in Android Studio and run it on an emulator or physical device.

## 📸 Screenshots

> Add screenshots here once you capture the app UI for a more visual showcase.

Example placeholders:

```md
![Home Screen](docs/screenshots/home.png)
![Add Contact](docs/screenshots/add-contact.png)
![Detail View](docs/screenshots/detail.png)
```

## 💡 Future Ideas

- Contact import/export
- Favorites section in dedicated tab
- Cloud sync support
- Search by phone or email filters
- Contact groups and tags
- Dark mode enhancement

## 📝 Notes

This project is designed as a modern sample Android app and is a great starting point for building a more advanced contact manager, CRM, or personal productivity app.

## 🚀 Quick Status

<div align="center">

[![Made with Android](https://img.shields.io/badge/Made%20with-Android-3DDC84?style=flat-square&logo=android)](https://developer.android.com/)
[![Built with Java](https://img.shields.io/badge/Built%20with-Java-ED8B00?style=flat-square&logo=openjdk)](https://www.java.com/)
[![Powered by Room](https://img.shields.io/badge/Powered%20by-Room-5C2D91?style=flat-square)](https://developer.android.com/training/data-storage/room)

</div>

---

If you want, I can make this README even more premium by adding:
- a dark neon style version,
- a GitHub profile-like hero section,
- animated badges,
- and a screenshot gallery section tailored to your app’s actual UI.
