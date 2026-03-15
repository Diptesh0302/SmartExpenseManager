package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Expense.java
 * Model class representing a single financial transaction (Expense or Income).
 * Demonstrates: OOP, Encapsulation, Java Date/Time API
 */
public class Expense {

    // ── Fields ──────────────────────────────────────────────────────────────
    private int id;
    private String title;
    private String category;
    private double amount;
    private String type;       // "EXPENSE" or "INCOME"
    private LocalDate date;

    // Shared date formatter used for display and file I/O
    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ── Constructor ──────────────────────────────────────────────────────────
    public Expense(int id, String title, String category,
                   double amount, String type, LocalDate date) {
        this.id       = id;
        this.title    = title;
        this.category = category;
        this.amount   = amount;
        this.type     = type.toUpperCase();
        this.date     = date;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getTitle()            { return title; }
    public void setTitle(String title)  { this.title = title; }

    public String getCategory()                 { return category; }
    public void setCategory(String category)    { this.category = category; }

    public double getAmount()               { return amount; }
    public void setAmount(double amount)    { this.amount = amount; }

    public String getType()             { return type; }
    public void setType(String type)    { this.type = type.toUpperCase(); }

    public LocalDate getDate()              { return date; }
    public void setDate(LocalDate date)     { this.date = date; }

    // ── toString ─────────────────────────────────────────────────────────────
    /**
     * Human-readable representation used when printing transactions.
     */
    @Override
    public String toString() {
        String sign = type.equals("INCOME") ? "+" : "-";
        return String.format(
            "[%d] %-20s | %-15s | %s%-8.2f | %-7s | %s",
            id,
            title,
            category,
            sign,
            amount,
            type,
            date.format(DATE_FORMAT)
        );
    }

    /**
     * Converts the transaction to a CSV line for file storage.
     * Format: id,title,category,amount,type,date
     */
    public String toCsvString() {
        return id + "," +
               title + "," +
               category + "," +
               amount + "," +
               type + "," +
               date.format(DATE_FORMAT);
    }

    /**
     * Creates an Expense object from a CSV line read from file.
     * @param csvLine a single line from the data file
     * @return Expense object, or null if the line is malformed
     */
    public static Expense fromCsvString(String csvLine) {
        try {
            String[] parts = csvLine.split(",", 6);
            if (parts.length != 6) return null;

            int        id       = Integer.parseInt(parts[0].trim());
            String     title    = parts[1].trim();
            String     category = parts[2].trim();
            double     amount   = Double.parseDouble(parts[3].trim());
            String     type     = parts[4].trim();
            LocalDate  date     = LocalDate.parse(parts[5].trim(), DATE_FORMAT);

            return new Expense(id, title, category, amount, type, date);
        } catch (Exception e) {
            // Return null if line is corrupt; caller handles it
            return null;
        }
    }
}
