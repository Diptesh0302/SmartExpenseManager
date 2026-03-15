package utils;

/**
 * Colors.java
 * ANSI escape codes for colored terminal output.
 * Demonstrates: Constants, utility class pattern
 *
 * Works on: Linux, macOS, Windows Terminal, VS Code terminal
 * Note: May not work on older Windows CMD — falls back gracefully
 */
public class Colors {

    // Prevent instantiation — this is a pure constants class
    private Colors() {}

    // ── Reset ────────────────────────────────────────────────────────────────
    public static final String RESET   = "\u001B[0m";

    // ── Text Colors ──────────────────────────────────────────────────────────
    public static final String BLACK   = "\u001B[30m";
    public static final String RED     = "\u001B[31m";
    public static final String GREEN   = "\u001B[32m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String BLUE    = "\u001B[34m";
    public static final String PURPLE  = "\u001B[35m";
    public static final String CYAN    = "\u001B[36m";
    public static final String WHITE   = "\u001B[37m";

    // ── Bold ─────────────────────────────────────────────────────────────────
    public static final String BOLD        = "\u001B[1m";
    public static final String BOLD_GREEN  = "\u001B[1;32m";
    public static final String BOLD_RED    = "\u001B[1;31m";
    public static final String BOLD_CYAN   = "\u001B[1;36m";
    public static final String BOLD_YELLOW = "\u001B[1;33m";
    public static final String BOLD_WHITE  = "\u001B[1;37m";

    // ── Background Colors ────────────────────────────────────────────────────
    public static final String BG_GREEN   = "\u001B[42m";
    public static final String BG_RED     = "\u001B[41m";
    public static final String BG_BLUE    = "\u001B[44m";
    public static final String BG_YELLOW  = "\u001B[43m";

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Wraps text in green color */
    public static String green(String text)  { return GREEN + text + RESET; }

    /** Wraps text in red color */
    public static String red(String text)    { return RED + text + RESET; }

    /** Wraps text in yellow color */
    public static String yellow(String text) { return YELLOW + text + RESET; }

    /** Wraps text in cyan color */
    public static String cyan(String text)   { return CYAN + text + RESET; }

    /** Wraps text in bold white */
    public static String bold(String text)   { return BOLD_WHITE + text + RESET; }
}
