package storage;

import model.Expense;
import utils.Colors;

import java.io.*;
import java.util.ArrayList;

/**
 * FileManager.java
 * Handles all file I/O for persisting transactions between sessions.
 * Demonstrates: File Handling, BufferedReader/Writer, Exception Handling
 *
 * Data is stored in CSV format:
 * id,title,category,amount,type,date
 */
public class FileManager {

    // File stored in the project root directory
    private static final String FILE_PATH = "expenses.csv";

    // Header line written at the top of the CSV file
    private static final String CSV_HEADER = "id,title,category,amount,type,date";

    // ── Save ─────────────────────────────────────────────────────────────────

    /**
     * Saves all transactions to the CSV file.
     * Overwrites existing file content each time.
     *
     * @param expenses list of transactions to save
     * @throws IOException if the file cannot be written
     */
    public static void saveExpensesToFile(ArrayList<Expense> expenses) throws IOException {
        // BufferedWriter for efficient file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            // Write CSV header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write each transaction as a CSV row
            for (Expense e : expenses) {
                writer.write(e.toCsvString());
                writer.newLine();
            }

        }
        // try-with-resources automatically closes the writer
    }

    // ── Load ─────────────────────────────────────────────────────────────────

    /**
     * Loads all transactions from the CSV file.
     * Returns an empty list if the file does not exist yet.
     *
     * @return ArrayList of Expense objects loaded from file
     * @throws IOException if the file exists but cannot be read
     */
    public static ArrayList<Expense> loadExpensesFromFile() throws IOException {
        ArrayList<Expense> expenses = new ArrayList<>();

        File file = new File(FILE_PATH);

        // If file doesn't exist yet, return empty list (first run)
        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Skip blank lines
                if (line.trim().isEmpty()) continue;

                // Parse each CSV line into an Expense object
                Expense expense = Expense.fromCsvString(line);

                if (expense != null) {
                    expenses.add(expense);
                } else {
                    // Log corrupt lines but continue loading
                    System.out.println(Colors.YELLOW +
                        "[WARNING] Skipped corrupt data line: " + line +
                        Colors.RESET);
                }
            }
        }

        return expenses;
    }

    /**
     * Returns the file path being used for storage.
     */
    public static String getFilePath() {
        return new File(FILE_PATH).getAbsolutePath();
    }

    /**
     * Checks if a saved data file exists.
     */
    public static boolean dataFileExists() {
        return new File(FILE_PATH).exists();
    }
}
