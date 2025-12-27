@echo off
echo Installing required Android SDK components...
echo.

set ANDROID_HOME=C:\Android\Sdk
set PATH=%ANDROID_HOME%\cmdline-tools\latest\bin;%PATH%

REM Accept all licenses first
echo y | %ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager --licenses >nul 2>&1

REM Install required components
echo Installing Android SDK Platform 33...
%ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager "platforms;android-33" --sdk_root=%ANDROID_HOME%

echo Installing Android SDK Build-Tools 33.0.1...
%ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager "build-tools;33.0.1" --sdk_root=%ANDROID_HOME%

echo Installation complete!
pause
