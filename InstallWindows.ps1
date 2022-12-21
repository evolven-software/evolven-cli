param(
    [switch]$AllUsers,
    [string]$InstallationDirectoryName="evolven-cli",
    [string]$InstallationLocation = $env:APPDATA,
    [switch]$Force
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

if ( -not (Test-Path -Path $InstallationLocation)) {
    Write-Error "Invalid installation location: $InstallationLocation."
    exit 1
}


$BaseDir = $(Join-Path  $PSScriptRoot -ChildPath "build" | Join-Path -ChildPath "output")
if ( -not (Test-Path $BaseDir)) {
    $BaseDir = $PSScriptRoot
}
Write-Debug "Base directory: $BaseDir"
if ($AllUsers) {
    $InstallationLocation = $env:ProgramData
}

$Source = $BaseDir
$InstallationPath = Join-Path $InstallationLocation -ChildPath $InstallationDirectoryName
if (Test-Path -Path $InstallationPath) {
    if ($Force) {
        Write-Warning "Installation location ($InstallationPath) exists. Overiding..."
    } else {
        Write-Error "Installation location ($InstallationPath) exists."
        exit 1
    }
    $Source += [System.IO.Path]::DirectorySeparatorChar + "*"
}
Write-Host "Installing Evolven CLI to $InstallationPath..."
Copy-Item -Recurse -Force $Source $InstallationPath

$BinPath = Join-Path $InstallationPath -ChildPath "bin"
$PathComponents = $env:Path.Split([System.IO.Path]::PathSeparator)

Write-Debug "Paht components: $PathComponents"

if ( -not ($PathComponents -contains $BinPath)) {
    Write-Debug "adding $BinPath to the system path..."
    $env:Path += [System.IO.Path]::PathSeparator + $BinPath
    $EnvironmentVariableTarget = [System.EnvironmentVariableTarget]::User
    if ($AllUsers){
        $EnvironmentVariableTarget = [System.EnvironmentVariableTarget]::Machine
    }
    [Environment]::SetEnvironmentVariable("Path", $env:Path, $EnvironmentVariableTarget)
}

Write-Host "Installation successful!"
Write-Host "You may need to restart your shells for the changes to take effect."



