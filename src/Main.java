import model.Expense;
import service.ExpenseService;
import storage.FileManager;
import utils.Colors;
import utils.InputValidator;
import utils.ReportGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║          SMART EXPENSE MANAGER — Main.java               ║
 * ║          Core Java Console Application                   ║
 * ║          Portfolio Project — Java Developer Intern       ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * Entry point for the Smart Expense Manager application.
 * Handles the main menu loop and delegates to service/util classes.
 *
 * Concepts Demonstrated:
 *   - OOP (model / service / storage / utils layers)
 *   - ArrayList & HashMap (via service and report classes)
 *   - File Handling (CSV read/write via FileManager)
 *   - Exception Handling (try-catch throughout)
 *   - Java Date/Time API (LocalDate)
 *   - Scanner for user input
 *   - ANSI colored terminal output
 */
public class Main {

    // ── Application State ────────────────────────────────────────────────────
    private static final ExpenseService service   = new ExpenseService();
    private static final Scanner        sc        = new Scanner(System.in);
    private static       boolean        unsaved   = false; // tracks unsaved changes

    // ── Entry Point ──────────────────────────────────────────────────────────
    public static void main(String[] args) {
        printBanner();
        autoLoad();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputValidator.readIntInRange(sc,
                Colors.BOLD_WHITE + "  Enter your choice: " + Colors.RESET, 1, 10);

            System.out.println();

            switch (choice) {
                case 1  -> addTransaction();
                case 2  -> viewAllTransactions();
                case 3  -> searchByCategory();
                case 4  -> deleteTransaction();
                case 5  -> viewFinancialSummary();
                case 6  -> saveData();
                case 7  -> sortTransactions();
                case 8  -> monthlyReport();
                case 9  -> filterByDateRange();
                case 10 -> { running = exitApp(); }
            }
        }
    }

    // ── Banner ────────────────────────────────────────────────────────────────
    private static void printBanner() {
        System.out.println(Colors.BOLD_CYAN);
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║        💰  SMART EXPENSE MANAGER  💰                ║");
        System.out.println("  ║           Personal Finance Tracker                  ║");
        System.out.println("  ║                  Core Java Project                  ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println(Colors.RESET);
        System.out.println(Colors.YELLOW +
            "  Track your income and expenses with ease!" + Colors.RESET);
        System.out.println();
    }

    // ── Main Menu ─────────────────────────────────────────────────────────────
    private static void printMainMenu() {
        String unsavedMark = unsaved
            ? Colors.YELLOW + " [Unsaved changes]" + Colors.RESET
            : "";

        System.out.println("\n" + Colors.CYAN +
            "  ─────────────────────────────────────────────" + Colors.RESET);
        System.out.println(Colors.BOLD_WHITE +
            "                  MAIN MENU" + unsavedMark + Colors.RESET);
        System.out.println(Colors.CYAN +
            "  ─────────────────────────────────────────────" + Colors.RESET);

        System.out.println(Colors.GREEN  + "  1." + Colors.RESET + "  ➕  Add Transaction");
        System.out.println(Colors.GREEN  + "  2." + Colors.RESET + "  📋  View All Transactions");
        System.out.println(Colors.GREEN  + "  3." + Colors.RESET + "  🔍  Search by Category");
        System.out.println(Colors.GREEN  + "  4." + Colors.RESET + "  🗑️   Delete Transaction");
        System.out.println(Colors.GREEN  + "  5." + Colors.RESET + "  📊  View Financial Summary");
        System.out.println(Colors.GREEN  + "  6." + Colors.RESET + "  💾  Save Data to File");
        System.out.println(Colors.GREEN  + "  7." + Colors.RESET + "  🔃  Sort Transactions");
        System.out.println(Colors.GREEN  + "  8." + Colors.RESET + "  📅  Monthly Report");
        System.out.println(Colors.GREEN  + "  9." + Colors.RESET + "  📆  Filter by Date Range");
        System.out.println(Colors.RED    + " 10." + Colors.RESET + "  🚪  Exit");

        System.out.println(Colors.CYAN +
            "  ─────────────────────────────────────────────" + Colors.RESET);

        // Quick stats bar
        if (service.getCount() > 0) {
            System.out.printf(Colors.YELLOW +
                "  Balance: " + Colors.RESET + "%s₹ %,.2f%s" +
                Colors.YELLOW + "  |  Transactions: %d%n" + Colors.RESET,
                service.calculateBalance() >= 0 ? Colors.GREEN : Colors.RED,
                service.calculateBalance(),
                Colors.RESET,
                service.getCount());
        }
    }

    // ── Feature 1: Add Transaction ────────────────────────────────────────────
    private static void addTransaction() {
        System.out.println(Colors.BOLD_CYAN +
            "\n  ── ADD NEW TRANSACTION ──" + Colors.RESET);

        try {
            String type = InputValidator.readType(sc);

            String title = InputValidator.readString(sc,
                Colors.CYAN + "  Title (e.g. Lunch, Salary): " + Colors.RESET);

            System.out.println(Colors.YELLOW +
                "\n  Common categories: Food, Transport, Rent, Salary," +
                " Entertainment, Medical, Shopping, Utilities" + Colors.RESET);
            String category = InputValidator.readString(sc,
                Colors.CYAN + "  Category: " + Colors.RESET);

            double amount = InputValidator.readPositiveDouble(sc,
                Colors.CYAN + "  Amount (₹): " + Colors.RESET);

            LocalDate date = InputValidator.readDate(sc,
                Colors.CYAN + "  Date (dd-MM-yyyy or 'today'): " + Colors.RESET);

            // Confirm before adding
            System.out.printf("%n  %s | %-18s | %-12s | ₹%.2f | %s%n",
                type, title, category, amount,
                date.format(Expense.DATE_FORMAT));

            if (InputValidator.readConfirmation(sc,
                    Colors.CYAN + "\n  Confirm add transaction?" + Colors.RESET)) {

                service.addExpense(title, category, amount, type, date);
                unsaved = true;
                System.out.println(Colors.BOLD_GREEN +
                    "\n  ✔ Transaction added successfully!" + Colors.RESET);
            } else {
                System.out.println(Colors.YELLOW +
                    "\n  ✗ Transaction cancelled." + Colors.RESET);
            }

        } catch (IllegalArgumentException e) {
            System.out.println(Colors.BOLD_RED +
                "\n  ✗ Error: " + e.getMessage() + Colors.RESET);
        }
    }

    // ── Feature 2: View All Transactions ─────────────────────────────────────
    private static void viewAllTransactions() {
        List<Expense> all = service.viewAllExpenses();

        if (all.isEmpty()) {
            System.out.println(Colors.YELLOW +
                "\n  No transactions found. Add some transactions first!" +
                Colors.RESET);
            return;
        }

        ReportGenerator.printTransactionTable(all, "ALL TRANSACTIONS");
    }

    // ── Feature 3: Search by Category ────────────────────────────────────────
    private static void searchByCategory() {
        System.out.println(Colors.BOLD_CYAN +
            "\n  ── SEARCH BY CATEGORY ──" + Colors.RESET);

        String category = InputValidator.readString(sc,
            Colors.CYAN + "  Enter category name: " + Colors.RESET);

        try {
            List<Expense> results = service.searchExpenseByCategory(category);
            if (results.isEmpty()) {
                System.out.println(Colors.YELLOW +
                    "\n  No transactions found in category: '" +
                    category + "'" + Colors.RESET);
            } else {
                ReportGenerator.printTransactionTable(results,
                    "TRANSACTIONS IN CATEGORY: " + category.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(Colors.RED +
                "\n  ✗ " + e.getMessage() + Colors.RESET);
        }
    }

    // ── Feature 4: Delete Transaction ────────────────────────────────────────
    private static void deleteTransaction() {
        System.out.println(Colors.BOLD_CYAN +
            "\n  ── DELETE TRANSACTION ──" + Colors.RESET);

        if (service.getCount() == 0) {
            System.out.println(Colors.YELLOW +
                "\n  No transactions to delete." + Colors.RESET);
            return;
        }

        // Show current transactions so user can pick an ID
        ReportGenerator.printTransactionTable(service.viewAllExpenses(),
            "CURRENT TRANSACTIONS");

        int id = InputValidator.readPositiveInt(sc,
            Colors.CYAN + "  Enter Transaction ID to delete: " + Colors.RESET);

        // Find the transaction first to show confirmation
        List<Expense> all = service.viewAllExpenses();
        Expense target = null;
        for (Expense e : all) {
            if (e.getId() == id) { target = e; break; }
        }

        if (target == null) {
            System.out.println(Colors.RED +
                "\n  ✗ Transaction with ID " + id + " not found." + Colors.RESET);
            return;
        }

        System.out.println(Colors.YELLOW +
            "\n  Transaction to delete: " + Colors.RESET);
        System.out.println("  " + target);

        if (InputValidator.readConfirmation(sc,
                Colors.RED + "\n  Are you sure you want to delete this?" + Colors.RESET)) {
            boolean deleted = service.deleteExpense(id);
            if (deleted) {
                unsaved = true;
                System.out.println(Colors.BOLD_GREEN +
                    "\n  ✔ Transaction deleted successfully!" + Colors.RESET);
            }
        } else {
            System.out.println(Colors.YELLOW +
                "\n  Delete cancelled." + Colors.RESET);
        }
    }

    // ── Feature 5: Financial Summary ─────────────────────────────────────────
    private static void viewFinancialSummary() {
        if (service.getCount() == 0) {
            System.out.println(Colors.YELLOW +
                "\n  No transactions available for a summary." + Colors.RESET);
            return;
        }
        ReportGenerator.printFinancialSummary(service);
    }

    // ── Feature 6: Save Data ─────────────────────────────────────────────────
    private static void saveData() {
        try {
            FileManager.saveExpensesToFile(service.getRawList());
            unsaved = false;
            System.out.println(Colors.BOLD_GREEN +
                "\n  ✔ Data saved successfully!" + Colors.RESET);
            System.out.println(Colors.YELLOW +
                "  File location: " + FileManager.getFilePath() + Colors.RESET);
        } catch (IOException e) {
            System.out.println(Colors.BOLD_RED +
                "\n  ✗ Error saving file: " + e.getMessage() + Colors.RESET);
        }
    }

    // ── Feature 7: Sort Transactions ─────────────────────────────────────────
    private static void sortTransactions() {
        System.out.println(Colors.BOLD_CYAN +
            "\n  ── SORT TRANSACTIONS ──" + Colors.RESET);
        System.out.println(Colors.GREEN + "  1." + Colors.RESET + "  Sort by Amount (Low → High)");
        System.out.println(Colors.GREEN + "  2." + Colors.RESET + "  Sort by Date (Newest First)");

        int choice = InputValidator.readIntInRange(sc,
            Colors.CYAN + "  Choose sort option: " + Colors.RESET, 1, 2);

        List<Expense> sorted = (choice == 1)
            ? service.sortByAmount()
            : service.sortByDateDesc();

        String label = (choice == 1)
            ? "SORTED BY AMOUNT (LOW TO HIGH)"
            : "SORTED BY DATE (NEWEST FIRST)";

        ReportGenerator.printTransactionTable(sorted, label);
    }

    // ── Feature 8: Monthly Report ─────────────────────────────────────────────
    private static void monthlyReport() {
        System.out.println(Colors.BOLD_CYAN +
            "\n  ── MONTHLY REPORT ──" + Colors.RESET);

        int month = InputValidator.readIntInRange(sc,
            Colors.CYAN + "  Enter month (1-12): " + Colors.RESET, 1, 12);

        int year = InputValidator.readPositiveInt(sc,
            Colors.CYAN + "  Enter year (e.g. 2024): " + Colors.RESET);

        ReportGenerator.printMonthlyReport(service, month, year);
    }

    // ── Feature 9: Filter by Date Range ──────────────────────────────────────
    private static void filterByDateRange() {
        System.out.println(Colors.BOLD_CYAN +
            "\n  ── FILTER BY DATE RANGE ──" + Colors.RESET);

        LocalDate from = InputValidator.readDate(sc,
            Colors.CYAN + "  From date (dd-MM-yyyy): " + Colors.RESET);

        LocalDate to = InputValidator.readDate(sc,
            Colors.CYAN + "  To date   (dd-MM-yyyy): " + Colors.RESET);

        if (to.isBefore(from)) {
            System.out.println(Colors.RED +
                "\n  ✗ 'To' date cannot be before 'From' date." + Colors.RESET);
            return;
        }

        List<Expense> results = service.filterByDateRange(from, to);
        String label = "TRANSACTIONS: " +
            from.format(Expense.DATE_FORMAT) + " → " +
            to.format(Expense.DATE_FORMAT);

        ReportGenerator.printTransactionTable(results, label);
    }

    // ── Auto Load on Startup ──────────────────────────────────────────────────
    private static void autoLoad() {
        try {
            if (FileManager.dataFileExists()) {
                ArrayList<Expense> loaded = FileManager.loadExpensesFromFile();
                if (!loaded.isEmpty()) {
                    service.setTransactions(loaded);
                    System.out.println(Colors.GREEN +
                        "  ✔ Loaded " + loaded.size() +
                        " transaction(s) from saved file." + Colors.RESET);
                    System.out.println(Colors.YELLOW +
                        "  File: " + FileManager.getFilePath() + Colors.RESET);
                }
            } else {
                System.out.println(Colors.YELLOW +
                    "  No saved data found. Starting fresh." + Colors.RESET);
            }
        } catch (IOException e) {
            System.out.println(Colors.RED +
                "  ⚠ Could not load saved data: " + e.getMessage() + Colors.RESET);
        }
    }

    // ── Exit ──────────────────────────────────────────────────────────────────
    private static boolean exitApp() {
        if (unsaved) {
            System.out.println(Colors.YELLOW +
                "\n  ⚠ You have unsaved changes!" + Colors.RESET);

            if (InputValidator.readConfirmation(sc,
                    Colors.CYAN + "  Save before exiting?" + Colors.RESET)) {
                saveData();
            }
        }

        System.out.println(Colors.BOLD_CYAN +
            "\n  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║   Thank you for using Smart Expense Manager! ║");
        System.out.println("  ║          Keep tracking, keep saving! 💰      ║");
        System.out.println("  ╚══════════════════════════════════════════════╝" +
            Colors.RESET + "\n");

        sc.close();
        return false; // signals main loop to stop
    }
}
