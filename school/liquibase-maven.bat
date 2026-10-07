@echo off
REM Run Liquibase migrations
call mvn liquibase:update
if %errorlevel% neq 0 (
    echo Liquibase update failed!
    pause
    exit /b 1
)
echo Migrations applied successfully!
pause
