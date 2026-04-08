/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Programming 2
 * Assignment     : Machine Problem 02 - Display First 10 Rows of Dataset
 * Date           : 2026-04-08
 * Description    : This Node.js script asks the user for the CSV file path,
 *                  reads and parses the dataset, then displays the first 10
 *                  valid data records in a clean, formatted table in the console.
 */

"use strict";

const fs = require("fs");
const readline = require("readline");

// Set up terminal input interface to prompt the user
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

// Parses one CSV line, correctly handling fields wrapped in double quotes
const parseCSVLine = (line) => {
  const fields = [];
  let current = "";
  let inQuotes = false;

  for (const char of line) {
    if (char === '"') {
      inQuotes = !inQuotes;
    } else if (char === "," && !inQuotes) {
      fields.push(current.trim());
      current = "";
    } else {
      current += char;
    }
  }
  fields.push(current.trim());
  return fields;
};

// Pads a string to a fixed length for aligned table output
const pad = (str, len) => String(str ?? "").padEnd(len).substring(0, len);

// Prompt the user to enter the path to the CSV dataset
rl.question("Enter the path to the CSV dataset file: ", (filePath) => {

  console.log("\n===================================================");
  console.log("  MP02 - First 10 Rows of Dataset");
  console.log("  Student: Tanchingco, John Matthew R.");
  console.log("===================================================\n");

  try {
    // Read the entire file as a string
    const fileContents = fs.readFileSync(filePath.trim(), "utf8");

    // Split the file content into individual lines
    const lines = fileContents.split("\n");

    // Array to hold the first 10 parsed data rows
    const records = [];

    // Flag that turns true once we reach the data section
    let dataStarted = false;

    // Loop through lines and collect up to 10 valid data records
    for (const line of lines) {
      if (line.includes("Candidate") && line.includes("Exam")) {
        dataStarted = true;
        continue;
      }
      if (dataStarted && line.trim() !== "" && line.trim() !== ",,,,,,,,,,," && records.length < 10) {
        records.push(parseCSVLine(line));
      }
    }

    // Print the table header with fixed-width columns
    console.log(
      "  " + pad("Candidate", 25) + pad("Type", 10) +
      pad("Exam", 45) + pad("Score", 7) + pad("Result", 6)
    );
    console.log("  " + "-".repeat(88));

    // Print each of the 10 records in formatted columns
    records.forEach((row) => {
      console.log(
        "  " + pad(row[0], 25) + pad(row[1], 10) +
        pad(row[3], 45) + pad(row[6], 7) + pad(row[7], 6)
      );
    });

    console.log(`\n  Showing ${records.length} of first 10 records.`);

  } catch (err) {
    console.log("  ERROR: Could not read the file.");
    console.log(`  Details: ${err.message}`);
  }

  console.log("\n===================================================");
  rl.close();
});
