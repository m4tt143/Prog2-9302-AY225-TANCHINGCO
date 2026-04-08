/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Programming 2
 * Assignment     : Machine Problem 02 - Display First 10 Rows of Dataset
 * Date           : 2026-04-08
 * Description    : This program asks the user for the CSV file path, reads and
 *                  parses the dataset, then displays the first 10 data records
 *                  in a formatted, readable table layout in the console.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MP02 {

    // Prints a single row of data with fixed-width columns for readability
    static void printRow(String[] fields) {
        System.out.printf("  %-25s", fields.length > 0 ? fields[0] : "");
        System.out.printf("%-10s", fields.length > 1 ? fields[1] : "");
        System.out.printf("%-45s", fields.length > 3 ? fields[3] : "");
        System.out.printf("%-7s", fields.length > 6 ? fields[6] : "");
        System.out.printf("%-6s%n", fields.length > 7 ? fields[7] : "");
    }

    // Parses a CSV line, correctly handling quoted fields that contain commas
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

    public static void main(String[] args) {

        // Scanner to capture user input from the terminal
        Scanner scanner = new Scanner(System.in);

        // Ask the user for the location of the CSV file
        System.out.print("Enter the path to the CSV dataset file: ");
        String filePath = scanner.nextLine().trim();

        // List to store up to 10 parsed data records
        ArrayList<String[]> records = new ArrayList<>();

        // Flag to know when the actual data rows have begun
        boolean dataStarted = false;

        System.out.println("\n===================================================");
        System.out.println("  MP02 - First 10 Rows of Dataset");
        System.out.println("  Student: Tanchingco, John Matthew R.");
        System.out.println("===================================================\n");

        // Read the CSV file and collect the first 10 data records
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                // Identify the header row to mark the start of data
                if (line.contains("Candidate") && line.contains("Exam")) {
                    dataStarted = true;
                    continue;
                }

                // Collect valid data rows until we have 10
                if (dataStarted && !line.trim().isEmpty()
                        && !line.trim().equals(",,,,,,,,,,,")
                        && records.size() < 10) {
                    records.add(parseCSVLine(line));
                }
            }

            // Print the column header row
            System.out.printf("  %-25s%-10s%-45s%-7s%-6s%n",
                "Candidate", "Type", "Exam", "Score", "Result");
            System.out.println("  " + "-".repeat(88));

            // Print each of the first 10 records
            for (String[] row : records) {
                printRow(row);
            }

            System.out.println("\n  Showing " + records.size() + " of first 10 records.");

        } catch (IOException e) {
            System.out.println("  ERROR: Could not read the file.");
            System.out.println("  Details: " + e.getMessage());
        }

        System.out.println("\n===================================================");
        scanner.close();
    }
}
