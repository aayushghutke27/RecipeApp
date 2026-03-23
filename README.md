# 🍽️ RecipeApp

A modern Android recipe application built with **Kotlin**, **Firebase Authentication**, **Firestore**, and **Room Database** — offering personalized recipe discovery, offline saving, and a clean, intuitive user experience.

---

## 📸 Screenshots

<table>
  <tr>
    <td align="center"><b>Login</b></td>
    <td align="center"><b>Sign Up</b></td>
    <td align="center"><b>Home</td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/c72beb02-e4e4-4659-aef6-858fa27c211b" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/4a5e5d6e-4da0-4ca2-bde3-120df1a28849" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/cccdd4f2-d41b-408d-b0e9-f87604a2ebb9" width="200"/></td>
  </tr>
  <tr>
    <td align="center"><b>Saved Recipes</b></td>l
    <td align="center"><b>My Recipe</b></td>
    <td align="center"><b>My Recipe</td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/4fba751c-e21f-4b83-9e0e-b9ae2cba067c" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/0e51346f-0a4e-419b-a8b6-6c51ee0dff19" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/fd7ccf9d-fcfb-4dc1-b7ec-6a23184e2c33" width="200"/></td>
  </tr>
</table>

---

## ✨ Features

- 🔐 **Firebase Authentication** — Email/password login and Google Sign-In (OAuth)
- ☁️ **Firebase Firestore** — Cloud-synced recipe data via `FirebaseRepository`
- ➕ **Add Your Own Recipes** — Users can contribute recipes via `AddRecipeFragment`
- 👤 **User Profile** — View and manage profile details via `ProfileFragment`
- 🗓️ **Recipe of the Week** — Curated weekly picks with a dedicated adapter
- 🏠 **Personalized Home Screen** — Time-based greetings and category-based recipe filtering
- 💾 **Offline Saved Recipes** — Persistent local storage with Room Database + LiveData
- 📖 **Detailed Recipe View** — Full ingredients, directions, cooking time, and images
- 🔍 **Search Screen** — Dedicated search fragment for quick recipe discovery
- 📋 **My Recipes** — Personal recipe list managed by the logged-in user
- 🏷️ **Category Chips** — Filter by Breakfast, Lunch, Dinner, Soup, Salad
- ❤️ **Favorites** — Save and unsave recipes with real-time icon state updates
- 🔁 **ViewPager Support** — Smooth pager navigation via `ViewPagerAdapter`

---

## 🏗️ Architecture & Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | XML Layouts, Fragments, RecyclerView, ViewBinding |
| Navigation | Android Navigation Component, Bottom Navigation |
| Authentication | Firebase Auth (Email/Password + Google OAuth) |
| Cloud Database | Firebase Firestore (`FirebaseRepository`) |
| Local Database | Room Database (`RecipeDatabase`, `SavedRecipeDao`) |
| Reactive Data | LiveData, ViewModel |
| Data Transfer | Parcelable (`RecipeExtension.kt`) |
| UI Components | ChipGroup, ViewPager, Snackbar, ProgressBar |
| Selectors | Chip, ChipText, NavItem, Outline, TabText color selectors |

---

## 📂 Project Structure

```
RecipeApp/
│
├── app/
│   └── src/
│       └── main/
│           │
│           ├── AndroidManifest.xml
│           │
│           ├── kotlin+java/
│           │   └── com.example.recipeapp/
│           │       │
│           │       ├── model/
│           │       │   └── RecipeDataClass.kt
│           │       │
│           │       ├── repository/
│           │       │   ├── FirebaseRepository.kt
│           │       │   └── SavedRecipeRepository.kt
│           │       │
│           │       ├── AuthActivity.kt
│           │       ├── MainActivity.kt
│           │       │
│           │       ├── LoginFragment.kt
│           │       ├── SignUpFragment.kt
│           │       ├── HomeFragment.kt
│           │       ├── SearchFragment.kt
│           │       ├── ProfileFragment.kt
│           │       ├── MyRecipeFragment.kt
│           │       ├── AddRecipeFragment.kt
│           │       ├── RecipeDetailFragment.kt
│           │       ├── SavedFragment.kt
│           │       ├── SavedRecipeFragment.kt
│           │       │
│           │       ├── Recipe.kt
│           │       ├── RecipeDataBase.kt
│           │       ├── RecipeExtension.kt
│           │       ├── SavedRecipeDao.kt
│           │       ├── SavedRecipeEntity.kt
│           │       │
│           │       ├── RecipeAdapter.kt
│           │       ├── RecipeOfWeekAdapter.kt
│           │       ├── RecommendationRecipeAdapter.kt
│           │       ├── SavedRecipeAdapter.kt
│           │       └── ViewPagerAdapter.kt
│           │
│           └── res/
│               │
│               ├── color/
│               │   ├── chip_selector.xml
│               │   ├── chip_text_selector.xml
│               │   ├── nav_item_color.xml
│               │   ├── outline_color.xml
│               │   └── tab_text_selector.xml
│               │
│               ├── layout/
│               │   ├── activity_auth.xml
│               │   ├── activity_main.xml
│               │   ├── fragment_add_recipe.xml
│               │   ├── fragment_home.xml
│               │   ├── fragment_login.xml
│               │   ├── fragment_my_recipe.xml
│               │   ├── fragment_profile.xml
│               │   ├── fragment_recipe_detaile.xml
│               │   ├── fragment_saved.xml
│               │   ├── fragment_saved_recipe.xml
│               │   ├── fragment_search.xml
│               │   ├── fragment_sign_up.xml
│               │   ├── item_card_recipe_week.xml
│               │   ├── item_my_recipe.xml
│               │   ├── item_recipe_card.xml
│               │   └── item_saved_recipe.xml
│               │
│               ├── menu/
│               │   └── bottom_menu.xml
│               │
│               └── values/
│                   ├── colors.xml
│                   ├── strings.xml
│                   └── themes/
```

---

**Flow:**
```
Splash Screen
      ↓
Authentication (Login / Sign Up)
      ↓
MainActivity (Bottom Navigation)
      ↓

🏠 Home
 ┌─────────────┬─────────────┬─────────────┬─────────────┐
 ↓             ↓             ↓             ↓
💾 Save       📂 Saved       🔍 Search     👤 Profile
 ↓             ↓             ↓             ↓

(Store in     Saved        Search       User Info /
 Room DB)     Screen       Results      Settings
              ↓
        🔀 Switcher View
              ↓
   ┌──────────┴──────────┐
   ↓                     ↓

💾 Saved Recipes        ☁️ My Recipes
(Room Database)         (Firestore)
   ↓                       ↓

📋 Recipe List        ┌───────────────┐
(Offline Access)      ↓               ↓
                 ➕ Add Recipe     📋 Recipe List
                      ↓               ↓
               Firestore (Cloud)      ↓
                      ↓               ↓
               🔄 Auto Sync Updates ──┘
                      ↓

               📖 Recipe Detail

---
```
## 📱 Screens

### 🔑 Login Screen (`LoginFragment`)

Authenticates users via **Email & Password** or **Google Sign-In**.

- Real-time email validation using `android.util.Patterns`
- Password validation with regex
- Login button enabled only when all inputs are valid
- Firebase Email/Password authentication
- Google Sign-In via OAuth 2.0
- Loading indicator during authentication
- Snackbar error messages for failed attempts
---

### 📝 Sign Up Screen (`SignUpFragment`)

Creates new user accounts with enforced password rules.

**Password Requirements:**
- Minimum 6 characters
- At least one uppercase letter
- At least one number
- Confirm password must match

---

### 🏠 Home Screen (`HomeFragment`)

The main dashboard of the app.

- Personalized greeting based on time of day (Morning / Afternoon / Evening)
- Displays the logged-in user's name or email
- **Horizontal RecyclerViews:**
  - Recommended Recipes — powered by `RecommendationRecipeAdapter`
  - Recipe of the Week — powered by `RecipeOfWeekAdapter`
- **Category ChipGroup** — filter by Breakfast, Lunch, Dinner, Soup, Salad
- Save/Unsave recipes with real-time favorite icon updates via Room LiveData
- Search bar navigates to `SearchFragment`
- Logout functionality

---

### ➕ Add Recipe Screen (`AddRecipeFragment`)

Allows users to contribute their own recipes to the app.

- Form inputs for title, description, ingredients, directions, and cooking time
- Data pushed to Firebase Firestore via `FirebaseRepository`

---

### 👤 Profile Screen (`ProfileFragment`)

Displays the currently logged-in user's profile information.

- Shows user name and email from Firebase Auth
- Accessible via bottom navigation

---

### 📋 My Recipes Screen (`MyRecipeFragment`)

Lists all recipes added by the logged-in user.

- Fetches user-specific recipes from Firestore
- Displayed using `item_my_recipe.xml` item layout

---

### 🔍 Search Screen (`SearchFragment`)

Dedicated recipe search interface.

- Navigated to from the Home Screen search bar
- Allows users to search recipes by keyword or category

---

### 💾 Saved Recipes Screen (`SavedRecipeFragment`)

Displays all recipes bookmarked by the user for offline access.

- Data persisted via **Room Database** (`RecipeDatabase`, `SavedRecipeDao`, `SavedRecipeEntity`)
- Live updates with **LiveData** observed from `SavedRecipeRepository`
- RecyclerView using `SavedRecipeAdapter`
- Remove recipes from the saved list
- Empty state UI when no recipes are saved

---

### 📖 Recipe Detail Screen (`RecipeDetailFragment`)

Shows complete information about a selected recipe.

**Displays:** Recipe image · Title & description · Ingredients · Step-by-step directions · Cooking time

- Data passed efficiently using **Parcelable** (via `RecipeExtension.kt`)
- Smooth fragment navigation with back stack support

---


## 👨‍💻 Author

**Ayush Ghutke**
Android Developer | Product Designer

Building intelligent Android apps that combine data, design, and user insights.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue)](href="https://www.linkedin.com/in/aayushghutke27/)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black)](https://github.com/aayushghutke27)

---

## 📄 License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

```
Copyright 2024 Ayush Ghutke
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
```

