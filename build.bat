@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0"

echo === DonutClient 26.2 build ===

where java >nul 2>nul
if errorlevel 1 goto :installjava
for /f "tokens=2 delims==" %%V in ('java -version 2^>^&1 ^| findstr /i "version"') do set "JVER=%%~V"
echo Found Java: %JVER%
echo %JVER% | findstr /b /c:"25" >nul
if not errorlevel 1 goto :javaok

:installjava
echo Java 25 is required. Installing Microsoft OpenJDK 25 with winget...
where winget >nul 2>nul
if errorlevel 1 (
  echo winget was not found. Install Java 25 manually, then run this file again.
  echo Official Microsoft download: https://learn.microsoft.com/en-us/java/openjdk/download
  pause
  exit /b 1
)
winget install --id Microsoft.OpenJDK.25 -e --accept-source-agreements --accept-package-agreements
if errorlevel 1 (
  echo Java 25 installation failed.
  pause
  exit /b 1
)

:javaok
if exist "%ProgramFiles%\Microsoft\jdk-25" set "JAVA_HOME=%ProgramFiles%\Microsoft\jdk-25"
for /d %%D in ("%ProgramFiles%\Microsoft\jdk-25*") do if exist "%%~fD\bin\java.exe" set "JAVA_HOME=%%~fD"
if not defined JAVA_HOME for /d %%D in ("%ProgramFiles%\Eclipse Adoptium\jdk-25*") do if exist "%%~fD\bin\java.exe" set "JAVA_HOME=%%~fD"
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
java -version

if not exist "tools\gradle-9.5.1\bin\gradle.bat" (
  if not exist "tools" mkdir tools
  echo Downloading Gradle 9.5.1...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-9.5.1-bin.zip' -OutFile 'tools\gradle-9.5.1-bin.zip'"
  if errorlevel 1 (
    echo Could not download Gradle.
    pause
    exit /b 1
  )
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path 'tools\gradle-9.5.1-bin.zip' -DestinationPath 'tools' -Force"
)

echo Building...
call "tools\gradle-9.5.1\bin\gradle.bat" build
if errorlevel 1 (
  echo.
  echo Build failed. The error above shows what needs fixing.
  pause
  exit /b 1
)

echo.
echo SUCCESS. JAR files are in:
echo %cd%\build\libs
explorer "%cd%\build\libs"
pause
