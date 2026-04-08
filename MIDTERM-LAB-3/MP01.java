/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Programming 2
 * Assignment     : Machine Problem 01 - Load Dataset and Display Total Records
 * Date           : 2026-04-08
 * Description    : This program asks the user for the CSV file path, reads the
 *                  dataset using BufferedReader, skips the header/metadata rows,
 *                  counts all valid data records, and displays the total count.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MP01 {

    public static void main(String[] args) {

        // Scanner to read user input from the console
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the path to the CSV dataset
        System.out.print("Enter the path to the CSV dataset file: ");
        String filePath = scanner.nextLine().trim();

        // List to store each valid data record from the CSV
        ArrayList<String> records = new ArrayList<>();

        // Flag to track when we have passed the header/metadata rows
        boolean dataStarted = false;

        System.out.println("\n===================================================");
        System.out.println("  MP01 - Load Dataset and Display Total Records");
        System.out.println("  Student: Tanchingco, John Matthew R.");
        System.out.println("===================================================\n");

        // Open and read the CSV file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            // Read each line of the file until end of file
            while ((line = br.readLine()) != null) {

                // The actual data rows begin after the header row containing "Candidate"
                if (line.contains("Candidate") && line.contains("Exam")) {
                    dataStarted = true;
                    continue; // skip the header row itself
                }

                // Once the data section has started, collect non-empty rows
                if (dataStarted && !line.trim().isEmpty() && !line.trim().equals(",,,,,,,,,,,")) {
                    records.add(line);
                }
            }

            // Display the total number of records found in the dataset
            System.out.println("  Dataset loaded successfully.");
            System.out.println("  File: " + filePath);
            System.out.println();
            System.out.println("  Total records in dataset: " + records.size());

        } catch (IOException e) {
            // Handle the case where the file cannot be found or read
            System.out.println("  ERROR: Could not read the file.");
            System.out.println("  Details: " + e.getMessage());
        }

        System.out.println("\n===================================================");

        // Close the scanner to free resources
        scanner.close();
    }
}
