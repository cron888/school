@echo off
REM Run tests only
call mvn test
if %errorlevel% neq 0 (
    echo Tests failed!
    pause
    exit /b 1
)
echo All tests passed!
pause
