@ECHO OFF

REM
SET "SOURCE_DIR=C:\Users\PC06\Desktop\study\L2\S4\WEB-DYN\Hasi-framework-2794\mg\itu\prom16"
SET "TEST_PROJECT_DIR=C:\Users\PC06\Desktop\study\L2\S4\WEB-DYN\TestFramework"
SET "TEMP_DIR=%SOURCE_DIR%\temp"
SET "JAR_NAME=hasina.jar"

REM
MKDIR "%TEMP_DIR%"

REM
javac -d "%TEMP_DIR%" "%SOURCE_DIR%\*.java"

REM
jar cf "%TEMP_DIR%\%JAR_NAME%" -C "%TEMP_DIR%" .

REM
COPY /Y "%TEMP_DIR%\%JAR_NAME%" "%TEST_PROJECT_DIR%\lib"

REM
RD /S /Q "%TEMP_DIR%"
