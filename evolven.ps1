$baseDir = Split-Path -Parent "$PSScriptRoot"
$libDir = Join-Path -Path $baseDir -ChildPath "lib"
$launcherNamePattern = "evolven-cli-*.jar"
$launcherPath = $(Get-ChildItem "$libDir/$launcherNamePattern" -ErrorAction SilentlyContinue)
if ( [string]::IsNullOrWhiteSpace($launcherPath) ) {
    $launcherPath = $(Get-ChildItem "$PSScriptRoot/build/output/lib/$launcherNamePattern" -ErrorAction SilentlyContinue)
}
if ( [string]::IsNullOrWhiteSpace($launcherPath)  ) {
    Write-Error "Cannot resolve launcher location."
    exit 1
}
$ArgumentList = @("-jar", "`"$launcherPath`"")
foreach ($arg in $args) {
    $ArgumentList += "`"$arg`""
}
$proc = Start-Process -PassThru -NoNewWindow -Wait -FilePath "java.exe" -ArgumentList $ArgumentList
exit $proc.ExitCode