@echo off
echo ========================================
echo    OBD2 Scanner - Final Status Check
echo ========================================
echo.
echo Checking project status...

REM Check key files
if exist "build.gradle" (
    echo  build.gradle - OK
) else (
    echo  build.gradle - MISSING
)

if exist "app\build.gradle" (
    echo  app/build.gradle - OK
) else (
    echo  app/build.gradle - MISSING
)

if exist "local.properties" (
    echo  local.properties - OK
    type local.properties
) else (
    echo  local.properties - MISSING
)

if exist "app\src\main\java\com\niot\obdscanner\MainActivity.java" (
    echo  MainActivity.java - OK
) else (
    echo  MainActivity.java - MISSING
)

echo.
echo Java Version:
java -version 2>&1 | find "version"
echo.
echo To build: gradlew assembleDebug
echo To install: gradlew installDebug
echo.
pause
