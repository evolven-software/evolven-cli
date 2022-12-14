@ECHO OFF
SET scriptpath=%~dp0\evolven.ps1
PowerShell.Exe -NoProfile -ExecutionPolicy Bypass -File %scriptpath% %*