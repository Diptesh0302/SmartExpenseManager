# 💰 Smart Expense Manager

A **console-based personal finance management system** built with Core Java.
Designed as a portfolio project for Java Developer Intern applications.

---

## 📸 Features

| Feature | Description |
|---|---|
| ➕ Add Transaction | Add income or expense with title, category, amount, date |
| 📋 View All | Display all transactions in a formatted table |
| 🔍 Search | Filter transactions by category |
| 🗑️ Delete | Remove a transaction by ID with confirmation |
| 📊 Summary | Financial report with income, expenses, balance, category chart |
| 💾 Save/Load | Persist data to CSV file across sessions |
| 🔃 Sort | Sort by amount or date |
| 📅 Monthly | Monthly income/expense breakdown |
| 📆 Date Filter | Filter transactions between two dates |
| 🎨 Colored UI | ANSI-colored terminal output |

---

## 🏗️ Project Structure

```
SmartExpenseManager/
│
├── src/
│   ├── Main.java                        ← Entry point, menu loop
│   │
│   ├── model/
│   │   └── Expense.java                 ← Transaction data model
│   │
│   ├── service/
│   │   └── ExpenseService.java          ← CRUD business logic
│   │
│   ├── storage/
│   │   └── FileManager.java             ← CSV file read/write
│   │
│   └── utils/
│       ├── Colors.java                  ← ANSI terminal colors
│       ├── InputValidator.java          ← Input reading & validation
│       └── ReportGenerator.java         ← Report formatting & display
│
├── out/                                 ← Compiled .class files (auto-created)
├── expenses.csv                         ← Saved data (auto-created on first save)
│
├── run.bat                              ← Windows build & run script
├── run.sh                               ← Linux/macOS build & run script
└── README.md
```

---

## 🚀 How to Run

### Prerequisites
- **Java JDK 11 or higher** installed
- Check: open Command Prompt / Terminal and type `java -version`
- Download JDK: https://www.oracle.com/java/technologies/downloads/

---

### ▶️ Option 1 — One-Click Script (Easiest)

**Windows:**
```
Double-click run.bat
```
Or in Command Prompt:
```cmd
cd SmartExpenseManager
run.bat
```

**Linux / macOS:**
```bash
cd SmartExpenseManager
chmod +x run.sh
./run.sh
```

---

### ▶️ Option 2 — Manual Commands

**Step 1: Compile**
```bash
# Windows
javac -d out -sourcepath src src/model/Expense.java src/utils/Colors.java src/utils/InputValidator.java src/utils/ReportGenerator.java src/service/ExpenseService.java src/storage/FileManager.java src/Main.java

# Linux / macOS (same command)
```

**Step 2: Run**
```bash
java -cp out Main
```

---

### ▶️ Option 3 — VS Code
1. Open the `SmartExpenseManager` folder in VS Code
2. Install the **"Extension Pack for Java"** extension
3. Open `src/Main.java`
4. Click the ▶️ **Run** button at the top right

---

## 🎮 Usage Guide

```
  ─────────────────────────────────────────────
                  MAIN MENU
  ─────────────────────────────────────────────
  1.  ➕  Add Transaction
  2.  📋  View All Transactions
  3.  🔍  Search by Category
  4.  🗑️   Delete Transaction
  5.  📊  View Financial Summary
  6.  💾  Save Data to File
  7.  🔃  Sort Transactions
  8.  📅  Monthly Report
  9.  📆  Filter by Date Range
 10.  🚪  Exit
```

### Adding a Transaction
```
  Type [E=Expense / I=Income]: E
  Title (e.g. Lunch, Salary): Team Lunch
  Category: Food
  Amount (₹): 450
  Date (dd-MM-yyyy or 'today'): today
  Confirm add transaction? (y/n): y
  ✔ Transaction added successfully!
```

### Data File (expenses.csv)
Data is automatically saved to `expenses.csv` in the project root:
```
id,title,category,amount,type,date
1,Team Lunch,Food,450.0,EXPENSE,15-03-2026
2,Monthly Salary,Salary,50000.0,INCOME,01-03-2026
```

---

## 🧩 Java Concepts Demonstrated

| Concept | Where Used |
|---|---|
| **OOP / Encapsulation** | `Expense.java` — private fields, getters/setters |
| **Inheritance / Polymorphism** | Method overriding (`toString`) |
| **ArrayList** | `ExpenseService` — in-memory transaction storage |
| **HashMap** | `ReportGenerator` — category-wise expense grouping |
| **File Handling** | `FileManager` — BufferedReader/Writer, CSV I/O |
| **Exception Handling** | try-catch in FileManager, InputValidator, Main |
| **Java Date/Time API** | `LocalDate`, `DateTimeFormatter` throughout |
| **Scanner** | `InputValidator` — all user input reading |
| **Streams / Lambdas** | `ExpenseService.setTransactions()` — mapToInt, max |
| **Static utility classes** | `Colors`, `InputValidator`, `ReportGenerator` |
| **Comparator / Sorting** | `sortByAmount()`, `sortByDateDesc()` |
| **try-with-resources** | `FileManager` — automatic stream closing |
| **Constants** | `Colors.java` — ANSI escape code constants |

---

## 📊 Example Financial Summary

```
═════════════════════════════════════════════════════════════════
           💰  FINANCIAL SUMMARY REPORT
═════════════════════════════════════════════════════════════════
  Total Income                   : ₹ 55,000.00
  Total Expenses                 : ₹ 12,450.00
─────────────────────────────────────────────────────────────────
  Net Balance                    : +₹ 42,550.00
═════════════════════════════════════════════════════════════════

  📊  CATEGORY-WISE EXPENSE BREAKDOWN
─────────────────────────────────────────────────────────────────
  Food               ████████░░░░░░░░░░░░  ₹4500.00  (36.1%)
  Transport          █████░░░░░░░░░░░░░░░░  ₹2200.00  (17.7%)
  Rent               ████████████░░░░░░░░  ₹5000.00  (40.2%)
  Entertainment      ██░░░░░░░░░░░░░░░░░░  ₹750.00   (6.0%)
```

---

## 👨‍💻 Author

Built as a Java Developer Intern portfolio project.
Demonstrates clean code, proper project structure, and core Java fundamentals.

Feel free to fork ⭐ and extend with a database backend or GUI!

---

## 🔮 Possible Extensions

- Replace CSV with SQLite database (JDBC)
- Add Swing/JavaFX GUI
- Export reports to PDF
- Multi-user support with login
- REST API with Spring Boot
