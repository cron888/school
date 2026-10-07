@echo off
REM Clean and build the project
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo Build successful!
pause
