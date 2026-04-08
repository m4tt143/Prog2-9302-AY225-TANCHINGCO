/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Programming 2
 * Assignment     : Machine Problem 20 - Convert CSV Dataset to JSON
 * Date           : 2026-04-08
 * Description    : This Node.js script asks the user for the CSV file path,
 *                  reads and parses all data records, maps each row to a named
 *                  JSON object using column keys, and prints the full JSON output.
 */

"use strict";

const fs = require("fs");
const readline = require("readline");

// Set up the interface to read user input from the terminal
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

// Key names that correspond to each column position in the CSV data
const KEYS = [
  "candidate", "type", "column1", "exam", "language",
  "examDate", "score", "result", "timeUsed"
];

// Parses a single CSV line and handles fields that contain commas inside quotes
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

// Converts one parsed row array into a JavaScript object using the key names
const rowToObject = (fields) => {
  const obj = {};
  KEYS.forEach((key, i) => { obj[key] = fields[i] ?? ""; });
  return obj;
};

// Prompt the user to enter the path of the CSV dataset
rl.question("Enter the path to the CSV dataset file: ", (filePath) => {

  console.log("\n===================================================");
  console.log("  MP20 - Convert CSV Dataset to JSON");
  console.log("  Student: Tanchingco, John Matthew R.");
  console.log("===================================================\n");

  try {
    // Read the full content of the CSV file as a text string
    const fileContents = fs.readFileSync(filePath.trim(), "utf8");

    // Split the text into individual lines for processing
    const lines = fileContents.split("\n");

    // Array to hold the converted JSON objects for each record
    const jsonRecords = [];

    // Flag that becomes true once we reach the actual data rows
    let dataStarted = false;

    for (const line of lines) {
      if (line.includes("Candidate") && line.includes("Exam")) {
        dataStarted = true;
        continue;
      }
      if (dataStarted && line.trim() !== "" && line.trim() !== ",,,,,,,,,,,") {
        jsonRecords.push(rowToObject(parseCSVLine(line)));
      }
    }

    // Print the entire records array as a formatted JSON string
    console.log(JSON.stringify(jsonRecords, null, 2));
    console.log(`\n  Total records converted: ${jsonRecords.length}`);

  } catch (err) {
    console.log("  ERROR: Could not read the file.");
    console.log(`  Details: ${err.message}`);
  }

  console.log("===================================================");
  rl.close();
});
