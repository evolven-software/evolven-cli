@ECHO OFF
SET SCRIPT_PATH=%~dp0\evolven.ps1
PowerShell -NoProfile -ExecutionPolicy Bypass -File %SCRIPT_PATH% %*
