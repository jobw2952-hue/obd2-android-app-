@echo off
echo ========================================
echo OBD2 Scanner - Direct Gradle Build
echo ========================================
echo.

REM Set environment variables
set JAVA_HOME=C:\Android\JDK\jdk21.0.9_10
set ANDROID_HOME=C:\Android\Sdk
set ANDROID_SDK_ROOT=C:\Android\Sdk
set GRADLE_USER_HOME=E:\.gradle

REM Clean first
echo Cleaning project...
call gradlew.bat clean --no-daemon

echo.
echo Building OBD2 Scanner for VW Polo 2013...
echo.

REM Build with minimal settings - NO PowerShell to cause RemoteException
call gradlew.bat assembleDebug --no-daemon --console=plain --max-workers=1

if %ERRORLEVEL% EQU 0 (
    echo.
    echo  BUILD SUCCESSFUL! 
    echo.
    echo Your OBD2 Scanner APK is ready!
    echo Location: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo  Ready for VW Polo 2013 P0036 diagnostics!
    echo 1. Install APK on Android phone
    echo 2. Connect NIOT OBD adapter
    echo 3. Turn ignition ON
    echo 4. Open app and scan for P0036!
) else (
    echo.
    echo  Build failed.
    echo.
    echo FINAL SOLUTION: Use Android Studio
    echo Download: https://developer.android.com/studio
)

echo.
pause
