/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Programming 2
 * Assignment     : Machine Problem 20 - Convert CSV Dataset to JSON
 * Date           : 2026-04-08
 * Description    : This program asks the user for the CSV file path, reads and
 *                  parses each data record, then converts the entire dataset into
 *                  a formatted JSON structure and prints it to the console.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MP20 {

    // Column names that correspond to each field position in the CSV data rows
    static final String[] KEYS = {
        "candidate", "type", "column1", "exam", "language",
        "examDate", "score", "result", "timeUsed"
    };

    // Parses a single CSV line while correctly handling comma-containing quoted fields
    static String[] parseCSVLine(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }

    // Escapes special characters inside a string so it is safe for JSON output
    static String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    // Converts one parsed CSV row into a JSON object string using the defined keys
    static String rowToJson(String[] fields, String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("{\n");
        for (int i = 0; i < KEYS.length; i++) {
            String value = (i < fields.length) ? escapeJson(fields[i]) : "";
            sb.append(indent).append("  \"").append(KEYS[i]).append("\": \"").append(value).append("\"");
            if (i < KEYS.length - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append(indent).append("}");
        return sb.toString();
    }

    public static void main(String[] args) {

        // Scanner to read the file path entered by the user
        Scanner scanner = new Scanner(System.in);

        // Ask the user to provide the path to the CSV dataset
        System.out.print("Enter the path to the CSV dataset file: ");
        String filePath = scanner.nextLine().trim();

        // List to store each parsed data row from the CSV file
        ArrayList<String[]> records = new ArrayList<>();

        // Flag used to skip metadata lines above the actual data section
        boolean dataStarted = false;

        System.out.println("\n===================================================");
        System.out.println("  MP20 - Convert CSV Dataset to JSON");
        System.out.println("  Student: Tanchingco, John Matthew R.");
        System.out.println("===================================================\n");

        // Read the CSV file and parse each valid data row
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("Candidate") && line.contains("Exam")) {
                    dataStarted = true;
                    continue;
                }
                if (dataStarted && !line.trim().isEmpty() && !line.trim().equals(",,,,,,,,,,,")) {
                    records.add(parseCSVLine(line));
                }
            }

            // Build and print the JSON output for all collected records
            System.out.println("[");
            for (int i = 0; i < records.size(); i++) {
                System.out.print(rowToJson(records.get(i), "  "));
                if (i < records.size() - 1) System.out.print(",");
                System.out.println();
            }
            System.out.println("]");
            System.out.println("\n  Total records converted: " + records.size());

        } catch (IOException e) {
            System.out.println("  ERROR: Could not read the file.");
            System.out.println("  Details: " + e.getMessage());
        }

        System.out.println("===================================================");
        scanner.close();
    }
}
