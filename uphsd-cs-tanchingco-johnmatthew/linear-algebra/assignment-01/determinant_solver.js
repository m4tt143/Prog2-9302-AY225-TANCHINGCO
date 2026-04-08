/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Math 101 – Linear Algebra
 * Campus         : UPHSD Molino Campus
 * Assignment     : Assignment 01 – 3×3 Matrix Determinant Solver
 * Date           : 2026-04-08
 * Description    : This Node.js script declares the student-assigned 3×3 matrix,
 *                  computes its determinant through cofactor expansion along the
 *                  first row, and logs every intermediate step to the console.
 */

"use strict";

// ---------------------------------------------------------------
// Prints the 3×3 matrix to the console with a simple border
// ---------------------------------------------------------------
const printMatrix = (m) => {
  m.forEach(row => {
    const [a, b, c] = row.map(n => String(n).padStart(2));
    console.log(`  | ${a}  ${b}  ${c} |`);
  });
};

// ---------------------------------------------------------------
// Returns the determinant of a 2×2 sub-matrix using the formula
// (a × d) − (b × c)
// ---------------------------------------------------------------
const computeMinor = (a, b, c, d) => (a * d) - (b * c);

// ---------------------------------------------------------------
// Performs cofactor expansion along Row 1, prints every step,
// and returns the final determinant value
// ---------------------------------------------------------------
const solveDeterminant = (m) => {

  // --- Compute each 2×2 minor from the sub-matrices ---
  const minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
  const minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
  const minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);

  // --- Display the expansion header ---
  console.log("\nExpanding along Row 1 (cofactor expansion):\n");

  // --- Print each minor with its full arithmetic breakdown ---
  console.log(
    `  Step 1 - Minor M11: det([${m[1][1]},${m[1][2]}],[${m[2][1]},${m[2][2]}])` +
    ` = (${m[1][1]}*${m[2][2]}) - (${m[1][2]}*${m[2][1]})` +
    ` = ${m[1][1] * m[2][2]} - ${m[1][2] * m[2][1]} = ${minor11}`
  );

  console.log(
    `  Step 2 - Minor M12: det([${m[1][0]},${m[1][2]}],[${m[2][0]},${m[2][2]}])` +
    ` = (${m[1][0]}*${m[2][2]}) - (${m[1][2]}*${m[2][0]})` +
    ` = ${m[1][0] * m[2][2]} - ${m[1][2] * m[2][0]} = ${minor12}`
  );

  console.log(
    `  Step 3 - Minor M13: det([${m[1][0]},${m[1][1]}],[${m[2][0]},${m[2][1]}])` +
    ` = (${m[1][0]}*${m[2][1]}) - (${m[1][1]}*${m[2][0]})` +
    ` = ${m[1][0] * m[2][1]} - ${m[1][1] * m[2][0]} = ${minor13}`
  );

  // --- Calculate each signed cofactor term ---
  const c11 = +1 * m[0][0] * minor11;   // positive sign for first column
  const c12 = -1 * m[0][1] * minor12;   // negative sign for second column
  const c13 = +1 * m[0][2] * minor13;   // positive sign for third column

  // --- Display the cofactor values ---
  console.log();
  console.log(`  Cofactor C11 = (+1) x ${m[0][0]} x ${minor11} = ${c11}`);
  console.log(`  Cofactor C12 = (-1) x ${m[0][1]} x ${minor12} = ${c12}`);
  console.log(`  Cofactor C13 = (+1) x ${m[0][2]} x ${minor13} = ${c13}`);

  // --- Show the summation that yields the determinant ---
  const det = c11 + c12 + c13;
  console.log(`\n  det(M) = ${c11} + (${c12}) + ${c13}`);

  return det;
};

// ---------------------------------------------------------------
// Main execution block: declare the matrix and run the solver
// ---------------------------------------------------------------

// Hardcoded student-assigned 3×3 matrix — no user input required
const matrix = [
  [3, 5, 4],
  [6, 2, 1],
  [4, 3, 5]
];

// Print the program banner and the matrix
console.log("===================================================");
console.log("  3x3 MATRIX DETERMINANT SOLVER");
console.log("  Student: Tanchingco, John Matthew R.");
console.log("  Assigned Matrix:");
console.log("===================================================");
printMatrix(matrix);
console.log("===================================================");

// Run the step-by-step computation and capture the result
const determinant = solveDeterminant(matrix);

// Display the final determinant and flag singular matrices
console.log("\n===================================================");
if (determinant === 0) {
  console.log("  DETERMINANT = 0");
  console.log("  The matrix is SINGULAR - it has no inverse.");
} else {
  console.log(`  DETERMINANT = ${determinant}`);
}
console.log("===================================================");
