@echo off
setlocal

set MAVEN_VERSION=3.9.9
set MAVEN_ROOT=%~dp0.mvn
set MAVEN_HOME=%MAVEN_ROOT%\apache-maven-%MAVEN_VERSION%
set MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd

if exist "%MAVEN_CMD%" goto run

echo Downloading Maven %MAVEN_VERSION%...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "$ErrorActionPreference='Stop';" ^
  "$version='%MAVEN_VERSION%';" ^
  "$root=Join-Path '%~dp0' '.mvn';" ^
  "$distDir=Join-Path $root ('apache-maven-' + $version);" ^
  "if (!(Test-Path $distDir)) {" ^
  "  New-Item -ItemType Directory -Force -Path $root | Out-Null;" ^
  "  $zip=Join-Path $root ('apache-maven-' + $version + '-bin.zip');" ^
  "  Invoke-WebRequest -Uri ('https://archive.apache.org/dist/maven/maven-3/' + $version + '/binaries/apache-maven-' + $version + '-bin.zip') -OutFile $zip;" ^
  "  Expand-Archive -Path $zip -DestinationPath $root -Force;" ^
  "  Remove-Item $zip -Force;" ^
  "}"

if errorlevel 1 exit /b %ERRORLEVEL%

:run
"%MAVEN_CMD%" %*
endlocal