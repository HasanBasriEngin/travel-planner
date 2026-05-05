@echo off
cd /d "%~dp0"

set "JAVA_EXE=java"
set "JAVA_FALLBACK=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot\bin\java.exe"

echo Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    if exist "%JAVA_FALLBACK%" (
        "%JAVA_FALLBACK%" -version >nul 2>&1
        if errorlevel 1 (
            echo Java 17 is required to run this project.
            echo Java was not found on PATH, and the fallback Java path could not be used:
            echo %JAVA_FALLBACK%
            pause
            exit /b 1
        )
        set "JAVA_EXE=%JAVA_FALLBACK%"
    ) else (
        echo Java 17 is required to run this project.
        echo Java was not found on PATH.
        echo Fallback Java path not found:
        echo %JAVA_FALLBACK%
        pause
        exit /b 1
    )
)

if not exist "target\TravelPlanner.jar" (
    echo TravelPlanner.jar not found. Building project...
    echo Checking Maven...
    mvn -version >nul 2>&1
    if errorlevel 1 (
        echo Maven is required to build this project when TravelPlanner.jar is missing.
        echo Please install Maven and try again.
        pause
        exit /b 1
    )

    mvn clean package
    if errorlevel 1 (
        echo Build failed. Please review the Maven errors above.
        pause
        exit /b 1
    )
)

if exist "target\TravelPlanner.jar" (
    echo Running TravelPlanner.jar...
    "%JAVA_EXE%" -jar "target\TravelPlanner.jar"
    pause
    exit /b 0
)

echo TravelPlanner.jar could not be found after the build.
pause
exit /b 1
