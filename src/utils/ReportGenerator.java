package utils;

import model.Expense;
import service.ExpenseService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

/**
 * ReportGenerator.java
 * Generates all financial reports and summary views.
 * Demonstrates: HashMap, Collections, formatting, utility class pattern
 */
public class ReportGenerator {

    // Prevent instantiation
    private ReportGenerator() {}

    // ── Line separator constant ───────────────────────────────────────────────
    private static final String LINE =
        Colors.CYAN + "─".repeat(65) + Colors.RESET;

    private static final String DOUBLE_LINE =
        Colors.CYAN + "═".repeat(65) + Colors.RESET;

    // ── Financial Summary ─────────────────────────────────────────────────────

    /**
     * Prints the full financial summary report.
     * Shows total income, expenses, balance, and category breakdown.
     */
    public static void printFinancialSummary(ExpenseService service) {
        double income   = service.calculateTotalIncome();
        double expense  = service.calculateTotalExpense();
        double balance  = service.calculateBalance();

        System.out.println("\n" + DOUBLE_LINE);
        System.out.println(Colors.BOLD_CYAN +
            "           💰  FINANCIAL SUMMARY REPORT" + Colors.RESET);
        System.out.println(DOUBLE_LINE);

        System.out.printf("  %-30s : %s%n",
            Colors.GREEN + "Total Income" + Colors.RESET,
            Colors.BOLD_GREEN + String.format("₹ %,.2f", income) + Colors.RESET);

        System.out.printf("  %-30s : %s%n",
            Colors.RED + "Total Expenses" + Colors.RESET,
            Colors.BOLD_RED + String.format("₹ %,.2f", expense) + Colors.RESET);

        System.out.println(LINE);

        String balanceColor = balance >= 0 ? Colors.BOLD_GREEN : Colors.BOLD_RED;
        String balanceSign  = balance >= 0 ? "+" : "";
        System.out.printf("  %-30s : %s%n",
            Colors.BOLD_WHITE + "Net Balance" + Colors.RESET,
            balanceColor + String.format("%s₹ %,.2f", balanceSign, balance) + Colors.RESET);

        System.out.println(DOUBLE_LINE);

        // Category-wise breakdown
        printCategoryBreakdown(service.viewAllExpenses());

        System.out.println();
    }

    /**
     * Prints a breakdown of expenses grouped by category using HashMap.
     */
    public static void printCategoryBreakdown(List<Expense> expenses) {
        // Build category → total map using HashMap
        HashMap<String, Double> categoryMap = new HashMap<>();

        for (Expense e : expenses) {
            if (e.getType().equals("EXPENSE")) {
                categoryMap.merge(e.getCategory(), e.getAmount(), Double::sum);
            }
        }

        if (categoryMap.isEmpty()) {
            System.out.println(Colors.YELLOW +
                "  No expense data available for category breakdown." + Colors.RESET);
            return;
        }

        System.out.println("\n" + Colors.BOLD_YELLOW +
            "  📊  CATEGORY-WISE EXPENSE BREAKDOWN" + Colors.RESET);
        System.out.println(LINE);

        // Sort categories by amount (descending)
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(categoryMap.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Calculate total for percentage
        double totalExpense = categoryMap.values().stream()
                                         .mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : sorted) {
            double percent = (entry.getValue() / totalExpense) * 100;
            String bar     = buildBar(percent);

            System.out.printf("  %-18s %s %s%-8.2f  (%.1f%%)%n",
                Colors.CYAN + entry.getKey() + Colors.RESET,
                bar,
                Colors.YELLOW + "₹",
                entry.getValue(),
                percent);
            System.out.print(Colors.RESET);
        }

        System.out.println(LINE);
    }

    /**
     * Builds a simple ASCII progress bar representing a percentage.
     */
    private static String buildBar(double percent) {
        int filled = (int) (percent / 5); // max 20 chars for 100%
        filled = Math.min(filled, 20);
        int empty  = 20 - filled;
        return Colors.GREEN + "█".repeat(filled) +
               Colors.YELLOW + "░".repeat(empty) +
               Colors.RESET;
    }

    // ── Transaction List Printer ──────────────────────────────────────────────

    /**
     * Prints a formatted table of transactions.
     * @param expenses list to display
     * @param title    header label for the table
     */
    public static void printTransactionTable(List<Expense> expenses, String title) {
        System.out.println("\n" + DOUBLE_LINE);
        System.out.println(Colors.BOLD_CYAN + "  " + title + Colors.RESET);
        System.out.println(DOUBLE_LINE);

        if (expenses.isEmpty()) {
            System.out.println(Colors.YELLOW +
                "  No transactions found." + Colors.RESET);
            System.out.println(DOUBLE_LINE);
            return;
        }

        // Table header
        System.out.printf("  %-4s  %-20s  %-14s  %-10s  %-7s  %-10s%n",
            Colors.BOLD + "ID",
            "Title",
            "Category",
            "Amount",
            "Type",
            "Date" + Colors.RESET);
        System.out.println(LINE);

        // Table rows
        for (Expense e : expenses) {
            String amountColor = e.getType().equals("INCOME")
                ? Colors.GREEN : Colors.RED;
            String sign = e.getType().equals("INCOME") ? "+" : "-";

            System.out.printf("  %-4d  %-20s  %-14s  %s%s%-9.2f%s  %-7s  %-10s%n",
                e.getId(),
                truncate(e.getTitle(), 20),
                truncate(e.getCategory(), 14),
                amountColor, sign,
                e.getAmount(),
                Colors.RESET,
                e.getType().equals("INCOME")
                    ? Colors.GREEN + "INCOME" + Colors.RESET
                    : Colors.RED + "EXPENSE" + Colors.RESET,
                e.getDate().format(Expense.DATE_FORMAT));
        }

        System.out.println(LINE);
        System.out.printf("  Total records: %s%d%s%n",
            Colors.BOLD_WHITE, expenses.size(), Colors.RESET);
        System.out.println(DOUBLE_LINE + "\n");
    }

    // ── Monthly Report ────────────────────────────────────────────────────────

    /**
     * Generates a monthly expense report.
     */
    public static void printMonthlyReport(ExpenseService service, int month, int year) {
        List<Expense> monthly = service.filterByMonth(month, year);

        String monthName = LocalDate.of(year, month, 1)
                                    .getMonth()
                                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        System.out.println("\n" + DOUBLE_LINE);
        System.out.println(Colors.BOLD_CYAN +
            "  📅  MONTHLY REPORT — " + monthName.toUpperCase() + " " + year +
            Colors.RESET);
        System.out.println(DOUBLE_LINE);

        if (monthly.isEmpty()) {
            System.out.println(Colors.YELLOW +
                "  No transactions found for this month." + Colors.RESET);
            System.out.println(DOUBLE_LINE);
            return;
        }

        double income  = 0, expense = 0;
        for (Expense e : monthly) {
            if (e.getType().equals("INCOME"))  income  += e.getAmount();
            else                               expense += e.getAmount();
        }

        printTransactionTable(monthly, "Transactions for " + monthName + " " + year);

        System.out.printf("  %-20s : %s%n",
            Colors.GREEN + "Monthly Income" + Colors.RESET,
            Colors.BOLD_GREEN + String.format("₹ %,.2f", income) + Colors.RESET);
        System.out.printf("  %-20s : %s%n",
            Colors.RED + "Monthly Expenses" + Colors.RESET,
            Colors.BOLD_RED + String.format("₹ %,.2f", expense) + Colors.RESET);

        double net = income - expense;
        String nc  = net >= 0 ? Colors.BOLD_GREEN : Colors.BOLD_RED;
        System.out.printf("  %-20s : %s%n",
            Colors.BOLD_WHITE + "Net Balance" + Colors.RESET,
            nc + String.format("₹ %,.2f", net) + Colors.RESET);

        System.out.println(DOUBLE_LINE + "\n");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Truncates a string to maxLen characters, appending "…" if needed.
     */
    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }

    /**
     * Prints a separator line.
     */
    public static void printLine() {
        System.out.println(LINE);
    }
}
