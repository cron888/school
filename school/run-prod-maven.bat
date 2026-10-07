@echo off
REM Run with production profile
call mvn spring-boot:run -Dspring-boot.run.profiles=prod
if %errorlevel% neq 0 (
    echo Application failed to start!
    pause
    exit /b 1
)
