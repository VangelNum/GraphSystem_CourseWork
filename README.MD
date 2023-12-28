# Compose Multiplatform Application

## Before running!
 - install JDK 17 on your machine
 - add `local.properties` file to the project root and set a path to Android SDK there

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration

To build the application bundle:
 - run `./gradlew :composeApp:assembleDebug`
 - find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

### Desktop
Run the desktop application: `./gradlew :composeApp:run`

### Browser
Run the browser application: `./gradlew :composeApp:jsBrowserDevelopmentRun`

[Отчет по курсовой.docx](https://github.com/VangelNum/GraphSystem_CourseWork/files/13788330/default.docx)
![image](https://github.com/VangelNum/GraphSystem_CourseWork/assets/91003195/21af2abb-6ca1-4052-90c3-843b7d72b624)
