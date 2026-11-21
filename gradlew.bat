@echo off
setlocal EnableExtensions
set "GRADLE_VERSION=8.14.8"
set "GRADLE_HOME=%USERPROFILE%\.gradle\nexusmotion\gradle-%GRADLE_VERSION%"
set "DIST_URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip"
set "CACHE_DIR=%USERPROFILE%\.gradle\nexusmotion"
set "ZIP_FILE=%CACHE_DIR%\gradle-%GRADLE_VERSION%-bin.zip"

if not exist "%GRADLE_HOME%\bin\gradle.bat" (
  if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"
  echo Downloading Gradle %GRADLE_VERSION% from the official Gradle distribution host...
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ErrorActionPreference='Stop'; [Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; $url='%DIST_URL%'; $zip='%ZIP_FILE%'; $checksum=$zip+'.sha256'; try { Invoke-WebRequest -UseBasicParsing -Uri ($url+'.sha256') -OutFile $checksum; $sumText=[System.IO.File]::ReadAllText($checksum); $match=[regex]::Match($sumText,'(?i)[0-9a-f]{64}'); if(-not $match.Success){throw 'Could not read the Gradle SHA-256 checksum.'}; $expected=$match.Value.ToLowerInvariant(); Invoke-WebRequest -UseBasicParsing -Uri $url -OutFile $zip; $actual=(Get-FileHash -Algorithm SHA256 $zip).Hash.ToLowerInvariant(); if($actual -ne $expected){Remove-Item $zip -Force; throw 'Gradle distribution checksum mismatch.'}; Expand-Archive -LiteralPath $zip -DestinationPath '%CACHE_DIR%' -Force; Remove-Item $zip -Force } finally { if(Test-Path $checksum){Remove-Item $checksum -Force}}"
  if errorlevel 1 exit /b 1
)
call "%GRADLE_HOME%\bin\gradle.bat" %*
exit /b %ERRORLEVEL%
