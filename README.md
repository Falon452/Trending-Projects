## Installation

Java 17

## Demo

https://github.com/user-attachments/assets/232bd6b1-ccfb-4307-bb13-ddfb3aadcd6e


## Requirements

> The project's objective is to list trending projects from GitHub ✅, allow users to tap on one of them ✅, and display their details ✅. You are free to choose the appearance of the app, but adhering to Android's Material Design guidelines is highly encouraged✅. The better the app looks, the better!✅
- > Requirements Language: Write your application in Kotlin. ✅
- > Compatibility: Ensure your application is compatible with the latest version of Android.  The layout of the screens must work on all supported Android 14 (API Level 34) devices.  ✅
- > Architecture: Use good architecture ✅ and design patterns ✅, adhering to SOLID principles ✅. 
- > UI Framework: Use Jetpack Compose for the UI. ✅ Incorporating some animations is a big plus. ✅
- > Simplicity: The project needs to work just by running it ✅ - Keep it simple ✅ :) Theming: Make sure the app works both in dark and light mode ✅ with the ability to change it within the app. ✅ 
- > Additional Notes: If there is something not specified, please feel free to make your own decisions. Let us know if you need anything or have any questions about the project.

The app allows users to **choose a creation date** for a repository. A project is considered **trending** when it has **over 100 stars**.  
The **default creation date** is **one week ago**.  

GitHub API does not provide an endpoint for trending repositories. Therefore, I made the assumption that **repositories with over 100 stars** are considered trending.  

The code is unit tested using JUnit 5 and MockK.

**Adhered to Material Design 3.**  

### Project Details display the following information:  
- **Number of stars**  
- **Owner name**  
- **Repository name**  
- **Description**  
- ⭐ **README.md** 

##  Animations 

- 🚀 **Splash Screen Animation**: Shrink + Fade Out 
- ✨ **Shimmers**: Supports both **dark mode** & **light mode** 
- ⭐ **Shared Element Transition**: Smooth animation when clicking on a project ⭐

##  Features

- Infinite scrollable list with pull-to-refresh    
- Dark theme toggle
- ⭐Date picker
- ⭐Project details with README fetched  
- ⭐Open repository URL (either GitHub mobile app if installed or browser)  

## Key Libraries

- **Jetpack Compose**
- **Hilt**
- **Retrofit and OkHttp**
- **Kotlin Coroutines**
- **Coil**
- **Compose Markdown**
- **AndroidX Libraries**
- **Junit 5, mockk**
  
## Module structure

The app follows a **multi-module** architecture with a **feature-based** module structure.

![Untitled-2025-01-25-1814](https://github.com/user-attachments/assets/54ab3236-e8e7-4e1a-bc76-3bcfd6da8286)

## App Flow

![floww](https://github.com/user-attachments/assets/b957ed39-3ac0-4ad8-8726-2622a993cd4c)

## Error handling

![Errors](https://github.com/user-attachments/assets/a13ae8ff-64af-401f-ac8c-e4bbd5e529e5)
⚠️Very large READMEs crashed the application, so I set a limit of 1,000 lines for the README to be displayed.

⚠️The GitHub API may block a user if they make too many requests. While the API allows authenticated users to make more requests, I kept it simple, and it works as expected without authentication
