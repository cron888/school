@echo off
REM Clean build with tests
call mvn clean package
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo Build successful!
pause
