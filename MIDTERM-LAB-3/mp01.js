/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Programming 2
 * Assignment     : Machine Problem 01 - Load Dataset and Display Total Records
 * Date           : 2026-04-08
 * Description    : This Node.js script asks the user for the CSV file path,
 *                  reads and parses the dataset, skips metadata/header rows,
 *                  and displays the total number of valid data records found.
 */

"use strict";

const fs = require("fs");
const readline = require("readline");

// Set up the interface for reading user input from the terminal
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

// Prompt the user to enter the CSV file path
rl.question("Enter the path to the CSV dataset file: ", (filePath) => {

  console.log("\n===================================================");
  console.log("  MP01 - Load Dataset and Display Total Records");
  console.log("  Student: Tanchingco, John Matthew R.");
  console.log("===================================================\n");

  try {
    // Read the entire file contents as a UTF-8 string
    const fileContents = fs.readFileSync(filePath.trim(), "utf8");

    // Split the file into individual lines for processing
    const lines = fileContents.split("\n");

    // Array to hold the valid data records after skipping metadata
    const records = [];

    // Flag to indicate when we have reached the actual data rows
    let dataStarted = false;

    // Loop through every line and collect valid records
    lines.forEach((line) => {

      // Detect the header row that marks the start of data
      if (line.includes("Candidate") && line.includes("Exam")) {
        dataStarted = true;
        return; // skip the header row itself
      }

      // After the header, add non-empty and non-filler lines to records
      if (dataStarted && line.trim() !== "" && line.trim() !== ",,,,,,,,,,,") {
        records.push(line.trim());
      }
    });

    // Display the results to the user
    console.log("  Dataset loaded successfully.");
    console.log(`  File: ${filePath.trim()}`);
    console.log();
    console.log(`  Total records in dataset: ${records.length}`);

  } catch (err) {
    // Handle file not found or unreadable errors
    console.log("  ERROR: Could not read the file.");
    console.log(`  Details: ${err.message}`);
  }

  console.log("\n===================================================");
  rl.close();
});
