Login Screen (LoginFragment)
The Login Screen allows users to authenticate using Email & Password or Google Sign-In via Firebase Authentication.

Key Features:
Email & password validation using regex and Android Patterns
Login button enabled only when inputs are valid
Firebase Email/Password authentication
Google Sign-In integration using OAuth
Loading indicator during authentication
Error handling with Snackbar messages

Flow:
User enters email and password
Inputs are validated in real-time
On successful login, user is navigated to the Main Activity

Users can switch to Sign Up Screen if they don’t have an account

<img width="457" height="917" alt="image" src="https://github.com/user-attachments/assets/c72beb02-e4e4-4659-aef6-858fa27c211b" />



Sign Up Screen (SignUpFragment)
The Sign Up Screen allows new users to create an account using email and password.

Key Features:
Real-time input validation
Password rules:
Minimum 6 characters
At least one uppercase letter
At least one number
Confirm password validation
Firebase user creation
Loading indicator during account creation

Flow:
User enters email, password, and confirm password
Validation errors are shown inline
On successful signup, user is redirected to the Login Screen

<img width="468" height="924" alt="image" src="https://github.com/user-attachments/assets/4a5e5d6e-4da0-4ca2-bde3-120df1a28849" />

Home Screen (HomeFragment)
The Home Screen is the main dashboard of the app, displaying recipes and user greetings.

Key Features:
Personalized greeting based on time of day
Displays logged-in user’s name/email

Horizontal lists:
Recommended Recipes
Recipe of the Week

Category filtering using Chip Group
Save/Unsave recipes (favorites)

Logout functionality

Sections:
Search Bar – Navigates to search screen
Category Chips – Filter recipes (Breakfast, Lunch, Dinner, Soup, Salad)
Recipe Lists – Scrollable horizontal RecyclerViews

Flow:
Recipes are loaded locally
Saved recipe IDs are observed from Room DB
Favorite icon state updates in real-time

<img width="485" height="932" alt="image" src="https://github.com/user-attachments/assets/cccdd4f2-d41b-408d-b0e9-f87604a2ebb9" />

Saved Recipes Screen (SavedRecipeFragment)
The Saved Recipes Screen shows all recipes saved by the user for offline access.

Key Features:
Data stored locally using Room Database
Live updates using LiveData
RecyclerView to display saved recipes
Remove recipe from saved list
Empty state UI when no recipes are saved

Flow:
Saved recipes are fetched from Room DB
Converted into UI-friendly data models
User can remove recipes from saved list

<img width="485" height="927" alt="image" src="https://github.com/user-attachments/assets/4fba751c-e21f-4b83-9e0e-b9ae2cba067c" />


Recipe Detail Screen (RecipeDetailFragment)
The Recipe Detail Screen shows complete information about a selected recipe.

Key Features:
Displays:
Recipe image
Title
Description
Ingredients
Directions
Cooking time

Data passed using Parcelable
Back navigation support

Flow:
User taps a recipe card
Full recipe details are displayed
Supports smooth fragment navigation

<img width="492" height="918" alt="image" src="https://github.com/user-attachments/assets/0e51346f-0a4e-419b-a8b6-6c51ee0dff19" />

<img width="476" height="926" alt="image" src="https://github.com/user-attachments/assets/fd6f6b44-138d-47c0-b67f-4166fc7f9078" />

<img width="479" height="921" alt="image" src="https://github.com/user-attachments/assets/fd7ccf9d-fcfb-4dc1-b7ec-6a23184e2c33" />



