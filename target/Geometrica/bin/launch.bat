@echo off
(echo.set sh=CreateObject^("Wscript.Shell"^)
echo.sh.Run """%~nx0"" 1", 0)>launch.vbs
if "%~1"=="" (start "" "launch.vbs"&exit /b)
set JLINK_VM_OPTIONS=
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m com.angon/pro.angon.App %*
