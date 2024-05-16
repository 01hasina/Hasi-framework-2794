@ECHO OFF

REM Définir les variables (modifier les valeurs entre guillemets)

SET APP_DIR=%~dp0
SET BIN_DIR=%APP_DIR%bin
SET LIB_DIR=%APP_DIR%lib
SET APP_NAME=Hasi-framework
SET JAR_FILE=%APP_NAME%.jar
SET JAR_DIR=%APP_DIR%JAR
SET SRC_DIR=%APP_DIR%src
SET TEMP_JAVA_DIR=%APP_DIR%tempjava
SET TEST_LIB=../Sprint1_test/lib


REM Copier les *.java dans un dossier temporaire tempjava
    MKDIR "%TEMP_JAVA_DIR%"
    for /R "%SRC_DIR%" %%G IN (*.java) DO (
        XCOPY /Y "%%G" "%APP_DIR%\tempjava"
    )

REM Créer le répertoire d'application dans Tomcat (vérifier si existant)
IF EXIST "%BIN_DIR%" (
    ECHO Le répertoire d'application existe déjà. On le supprime.
    RD /S /Q "%BIN_DIR%"
)
MKDIR %BIN_DIR%

REM Créer le répertoire d'application dans Tomcat (vérifier si existant)
IF EXIST "%JAR_DIR%" (
    ECHO Le répertoire d'application existe déjà. On le supprime.
    RD /S /Q "%JAR_DIR%"
)
MKDIR %JAR_DIR%

REM Compiler les classes Java
javac -cp "%LIB_DIR%\*" -d "%BIN_DIR%" "%TEMP_JAVA_DIR%\*.java"

REM Supprimer le dossier temporaire apres compilation
IF EXIST "%TEMP_JAVA_DIR%" (
    ECHO Le répertoire d'application existe déjà. On le supprime.
    RD /S /Q "%TEMP_JAVA_DIR%"
)
     

REM Create a .jar file from the temporary directory
jar cvf "%JAR_DIR%\%JAR_FILE%" -C "%BIN_DIR%" .

REM Copy the .jar file to the webapps directory of Test
COPY /Y "%JAR_DIR%\%JAR_FILE%" "%TEST_LIB%"