package utils;

import model.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * InputValidator.java
 * Centralises all user-input reading and validation.
 * Demonstrates: Exception Handling, Input Validation, utility pattern
 */
public class InputValidator {

    private InputValidator() {}

    /**
     * Reads a non-empty string from the user.
     * Keeps prompting until valid input is given.
     */
    public static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(Colors.RED +
                "  ✗ Input cannot be empty. Please try again." + Colors.RESET);
        }
    }

    /**
     * Reads a positive double value from the user.
     * Rejects negative, zero, and non-numeric input.
     */
    public static double readPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println(Colors.RED +
                        "  ✗ Amount must be greater than zero." + Colors.RESET);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED +
                    "  ✗ Invalid number. Please enter a numeric value (e.g. 250.50)." +
                    Colors.RESET);
            }
        }
    }

    /**
     * Reads a positive integer from the user.
     */
    public static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println(Colors.RED +
                        "  ✗ Please enter a positive number." + Colors.RESET);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED +
                    "  ✗ Invalid input. Please enter a whole number." + Colors.RESET);
            }
        }
    }

    /**
     * Reads an integer within a specific range [min, max].
     */
    public static int readIntInRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println(Colors.RED +
                        "  ✗ Please enter a number between " + min +
                        " and " + max + "." + Colors.RESET);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED +
                    "  ✗ Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }

    /**
     * Reads a date in dd-MM-yyyy format.
     * Accepts "today" as a shortcut for the current date.
     */
    public static LocalDate readDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("today")) {
                return LocalDate.now();
            }

            try {
                return LocalDate.parse(input, Expense.DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println(Colors.RED +
                    "  ✗ Invalid date. Use format dd-MM-yyyy (e.g. 25-12-2024)" +
                    " or type 'today'." + Colors.RESET);
            }
        }
    }

    /**
     * Reads a transaction type — "EXPENSE" or "INCOME".
     * Accepts: e, expense, i, income (case-insensitive).
     */
    public static String readType(Scanner sc) {
        while (true) {
            System.out.print(Colors.CYAN +
                "  Type [E=Expense / I=Income]: " + Colors.RESET);
            String input = sc.nextLine().trim().toUpperCase();

            if (input.equals("E") || input.equals("EXPENSE")) return "EXPENSE";
            if (input.equals("I") || input.equals("INCOME"))  return "INCOME";

            System.out.println(Colors.RED +
                "  ✗ Invalid type. Enter E for Expense or I for Income." + Colors.RESET);
        }
    }

    /**
     * Reads a yes/no confirmation from the user.
     * @return true if user enters y/yes, false otherwise
     */
    public static boolean readConfirmation(Scanner sc, String prompt) {
        System.out.print(prompt + Colors.CYAN + " (y/n): " + Colors.RESET);
        String input = sc.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
}
