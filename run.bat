@echo off
echo.
echo  ============================================
echo   Smart Expense Manager - Build ^& Run
echo  ============================================
echo.

REM Create output directory for compiled .class files
if not exist "out" mkdir out

echo  [1/2] Compiling Java source files...
javac -d out -sourcepath src ^
  src\model\Expense.java ^
  src\utils\Colors.java ^
  src\utils\InputValidator.java ^
  src\utils\ReportGenerator.java ^
  src\service\ExpenseService.java ^
  src\storage\FileManager.java ^
  src\Main.java

IF %ERRORLEVEL% NEQ 0 (
  echo.
  echo  [ERROR] Compilation failed. Make sure Java JDK is installed.
  echo  Download from: https://www.oracle.com/java/technologies/downloads/
  pause
  exit /b 1
)

echo  [2/2] Compilation successful!
echo.
echo  Starting Smart Expense Manager...
echo  ============================================
echo.

REM Run the application from the out directory
java -cp out Main

pause
