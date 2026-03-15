#!/bin/bash

echo ""
echo " ============================================"
echo "  Smart Expense Manager - Build & Run"
echo " ============================================"
echo ""

# Create output directory
mkdir -p out

echo " [1/2] Compiling Java source files..."

javac -d out -sourcepath src \
  src/model/Expense.java \
  src/utils/Colors.java \
  src/utils/InputValidator.java \
  src/utils/ReportGenerator.java \
  src/service/ExpenseService.java \
  src/storage/FileManager.java \
  src/Main.java

if [ $? -ne 0 ]; then
  echo ""
  echo " [ERROR] Compilation failed. Make sure Java JDK is installed."
  echo " Install: sudo apt install default-jdk   (Ubuntu/Debian)"
  echo "          brew install openjdk           (macOS)"
  exit 1
fi

echo " [2/2] Compilation successful!"
echo ""
echo " Starting Smart Expense Manager..."
echo " ============================================"
echo ""

java -cp out Main
