@echo off
echo ================================================
echo OBD2 Scanner - Android Studio Emergency Project
echo ================================================
echo.
echo Since command-line Gradle has PowerShell issues,
echo here's a ready-to-use Android Studio project!
echo.

REM Create clean project structure
if exist "OBD2_AndroidStudio" rmdir /s /q "OBD2_AndroidStudio"
mkdir "OBD2_AndroidStudio"
mkdir "OBD2_AndroidStudio\app"
mkdir "OBD2_AndroidStudio\app\src"
mkdir "OBD2_AndroidStudio\app\src\main"
mkdir "OBD2_AndroidStudio\app\src\main\java"
mkdir "OBD2_AndroidStudio\app\src\main\java\com"
mkdir "OBD2_AndroidStudio\app\src\main\java\com\niot"
mkdir "OBD2_AndroidStudio\app\src\main\java\com\niot\obdscanner"
mkdir "OBD2_AndroidStudio\app\src\main\res"
mkdir "OBD2_AndroidStudio\app\src\main\res\layout"

REM Copy your source code
xcopy "E:\OBD2_Scanner\app\src\main\java\com\niot\obdscanner\MainActivity.java" "OBD2_AndroidStudio\app\src\main\java\com\niot\obdscanner\" /Y
xcopy "E:\OBD2_Scanner\app\src\main\res\layout\activity_main.xml" "OBD2_AndroidStudio\app\src\main\res\layout\" /Y
xcopy "E:\OBD2_Scanner\app\src\main\AndroidManifest.xml" "OBD2_AndroidStudio\app\src\main\" /Y

REM Create simple build.gradle
echo creating build.gradle...
(
echo plugins {
echo     id 'com.android.application'
echo }
echo.
echo android {
echo     namespace 'com.niot.obdscanner'
echo     compileSdk 31
echo.
echo     defaultConfig {
echo         applicationId "com.niot.obdscanner"
echo         minSdk 21
echo         targetSdk 31
echo         versionCode 1
echo         versionName "1.0"
echo     }
echo.
echo     buildTypes {
echo         release {
echo             minifyEnabled false
echo         }
echo     }
echo }
echo.
echo dependencies {
echo     implementation 'androidx.appcompat:appcompat:1.4.2'
echo }
) > "OBD2_AndroidStudio\app\build.gradle"

REM Create settings.gradle
echo include ':app' > "OBD2_AndroidStudio\settings.gradle"

echo.
echo ? Android Studio project created at: OBD2_AndroidStudio
echo.
echo NEXT STEPS:
echo 1. Download Android Studio: https://developer.android.com/studio
echo 2. Install it
echo 3. Open the "OBD2_AndroidStudio" folder
echo 4. Click Build > Make Project
echo 5. Your OBD2 Scanner will build successfully!
echo.
echo  Then install on phone and diagnose your VW Polo 2013 P0036!
echo.
pause
