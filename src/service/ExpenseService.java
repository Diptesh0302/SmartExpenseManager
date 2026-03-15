package service;

import model.Expense;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ExpenseService.java
 * Core business logic layer — manages all CRUD operations on transactions.
 * Demonstrates: ArrayList, Collections, Stream API basics, OOP
 */
public class ExpenseService {

    // In-memory list of all transactions
    private ArrayList<Expense> transactions;

    // Auto-incrementing ID counter
    private int nextId;

    // ── Constructor ──────────────────────────────────────────────────────────
    public ExpenseService() {
        this.transactions = new ArrayList<>();
        this.nextId       = 1;
    }

    // ── Add ──────────────────────────────────────────────────────────────────

    /**
     * Adds a new transaction to the list.
     * @param title    description of the transaction
     * @param category category name (e.g. Food, Rent)
     * @param amount   positive numeric value
     * @param type     "EXPENSE" or "INCOME"
     * @param date     transaction date
     * @throws IllegalArgumentException if amount is negative or zero
     */
    public void addExpense(String title, String category,
                           double amount, String type, LocalDate date) {

        // Validation
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (!type.equalsIgnoreCase("EXPENSE") && !type.equalsIgnoreCase("INCOME")) {
            throw new IllegalArgumentException("Type must be EXPENSE or INCOME.");
        }

        Expense expense = new Expense(nextId++, title.trim(),
                                      category.trim(), amount,
                                      type.toUpperCase(), date);
        transactions.add(expense);
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    /**
     * Returns a copy of all transactions (defensive copy).
     */
    public List<Expense> viewAllExpenses() {
        return new ArrayList<>(transactions);
    }

    /**
     * Searches transactions by category (case-insensitive).
     */
    public List<Expense> searchExpenseByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        List<Expense> result = new ArrayList<>();
        for (Expense e : transactions) {
            if (e.getCategory().equalsIgnoreCase(category.trim())) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Searches transactions by type: EXPENSE or INCOME.
     */
    public List<Expense> searchByType(String type) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : transactions) {
            if (e.getType().equalsIgnoreCase(type)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Filters transactions by month and year.
     * @param month 1-12
     * @param year  e.g. 2024
     */
    public List<Expense> filterByMonth(int month, int year) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : transactions) {
            if (e.getDate().getMonthValue() == month &&
                e.getDate().getYear() == year) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Filters transactions within a date range (inclusive).
     */
    public List<Expense> filterByDateRange(LocalDate from, LocalDate to) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : transactions) {
            LocalDate d = e.getDate();
            if (!d.isBefore(from) && !d.isAfter(to)) {
                result.add(e);
            }
        }
        return result;
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    /**
     * Deletes a transaction by ID.
     * @return true if found and deleted, false if ID not found
     */
    public boolean deleteExpense(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == id) {
                transactions.remove(i);
                return true;
            }
        }
        return false;
    }

    // ── Calculations ─────────────────────────────────────────────────────────

    /**
     * Calculates total of all EXPENSE transactions.
     */
    public double calculateTotalExpense() {
        double total = 0;
        for (Expense e : transactions) {
            if (e.getType().equals("EXPENSE")) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /**
     * Calculates total of all INCOME transactions.
     */
    public double calculateTotalIncome() {
        double total = 0;
        for (Expense e : transactions) {
            if (e.getType().equals("INCOME")) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /**
     * Calculates net balance: Income - Expenses.
     */
    public double calculateBalance() {
        return calculateTotalIncome() - calculateTotalExpense();
    }

    // ── Sorting ──────────────────────────────────────────────────────────────

    /**
     * Returns transactions sorted by amount (ascending).
     */
    public List<Expense> sortByAmount() {
        List<Expense> sorted = new ArrayList<>(transactions);
        sorted.sort(Comparator.comparingDouble(Expense::getAmount));
        return sorted;
    }

    /**
     * Returns transactions sorted by date (most recent first).
     */
    public List<Expense> sortByDateDesc() {
        List<Expense> sorted = new ArrayList<>(transactions);
        sorted.sort(Comparator.comparing(Expense::getDate).reversed());
        return sorted;
    }

    // ── Data Management ──────────────────────────────────────────────────────

    /**
     * Replaces the entire transaction list (used when loading from file).
     * Recalculates the nextId to avoid duplicate IDs.
     */
    public void setTransactions(ArrayList<Expense> loaded) {
        this.transactions = loaded;
        // Set nextId to max existing ID + 1
        this.nextId = loaded.stream()
                            .mapToInt(Expense::getId)
                            .max()
                            .orElse(0) + 1;
    }

    /**
     * Returns the raw list (used by FileManager to save data).
     */
    public ArrayList<Expense> getRawList() {
        return transactions;
    }

    /**
     * Returns total count of transactions.
     */
    public int getCount() {
        return transactions.size();
    }
}
