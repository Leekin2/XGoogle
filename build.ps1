param([string]$Jdk = $env:JAVA_HOME)
$ErrorActionPreference = 'Stop'
if ($Jdk) { $env:JAVA_HOME = $Jdk }
$java = Join-Path $env:JAVA_HOME 'bin/java.exe'
$javac = Join-Path $env:JAVA_HOME 'bin/javac.exe'
# Pack the companion module first, so the APK never embeds a stale ZIP.
& (Join-Path $PSScriptRoot 'build-companion.ps1') | Out-Null
$tests = Join-Path $PSScriptRoot 'build/policy-tests'
New-Item -ItemType Directory -Force -Path $tests | Out-Null
$source = Join-Path $PSScriptRoot 'app/src/main/java/io/github/leekin2/xgoogle'
& $javac -encoding UTF-8 -d $tests "$source/GateBinding.java" "$source/FeaturePolicy.java" "$source/SystemCompatibility.java" "$source/WalletPowerPolicy.java" "$source/NativeQuickEntryPolicy.java" "$source/Gcj02.java" "$source/WgsRegions.java" "$PSScriptRoot/tests/GateBindingTest.java" "$PSScriptRoot/tests/FeaturePolicyTest.java" "$PSScriptRoot/tests/SystemCompatibilityTest.java" "$PSScriptRoot/tests/WalletPowerPolicyTest.java" "$PSScriptRoot/tests/NativeQuickEntryPolicyTest.java" "$PSScriptRoot/tests/Gcj02Test.java"
if ($LASTEXITCODE -ne 0) { throw 'Policy test compilation failed' }
foreach ($test in @('GateBindingTest','FeaturePolicyTest','SystemCompatibilityTest','WalletPowerPolicyTest','NativeQuickEntryPolicyTest','Gcj02Test')) {
    & $java -cp $tests "io.github.leekin2.xgoogle.$test"
    if ($LASTEXITCODE -ne 0) { throw "$test failed" }
}
Push-Location $PSScriptRoot
try {
    & ./gradlew.bat :app:testHotwordUid :app:testWallpaper :app:testCircleGesture :app:testContextualSearch :app:testHookLifecycle :app:testGoogleRestoreAccess :app:assembleRelease --console=plain
    if ($LASTEXITCODE -ne 0) { throw 'Gradle build failed' }
} finally { Pop-Location }
Write-Output (Join-Path $PSScriptRoot 'app/build/outputs/apk/release/app-release.apk')
