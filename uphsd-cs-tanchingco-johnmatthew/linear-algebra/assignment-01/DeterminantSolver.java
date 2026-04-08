/*
 * Student Name   : Tanchingco, John Matthew R.
 * Course Code    : Math 101 – Linear Algebra
 * Campus         : UPHSD Molino Campus
 * Assignment     : Assignment 01 – 3×3 Matrix Determinant Solver
 * Date           : 2026-04-08
 * Description    : This program declares a 3×3 matrix assigned to the student,
 *                  computes its determinant using cofactor expansion along the
 *                  first row, and prints every intermediate step to the console.
 */

public class DeterminantSolver {

    // ---------------------------------------------------------------
    // Computes the determinant of a 2×2 matrix given its four elements
    // arranged as: | a  b |
    //              | c  d |
    // Formula: (a × d) − (b × c)
    // ---------------------------------------------------------------
    public static int computeMinor(int a, int b, int c, int d) {
        return (a * d) - (b * c);
    }

    // ---------------------------------------------------------------
    // Prints the 3×3 matrix to the console in a bordered format
    // ---------------------------------------------------------------
    public static void printMatrix(int[][] m) {
        for (int[] row : m) {
            System.out.printf("  | %2d  %2d  %2d |%n", row[0], row[1], row[2]);
        }
    }

    // ---------------------------------------------------------------
    // Performs the full cofactor expansion along Row 1,
    // prints each step, and returns the final determinant value
    // ---------------------------------------------------------------
    public static int solveDeterminant(int[][] m) {

        // --- Step 1: Compute each 2×2 minor ---
        int minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
        int minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
        int minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);

        // --- Print the minor calculation steps ---
        System.out.println("\nExpanding along Row 1 (cofactor expansion):\n");

        System.out.printf(
            "  Step 1 - Minor M11: det([%d,%d],[%d,%d]) = (%d*%d) - (%d*%d) = %d - %d = %d%n",
            m[1][1], m[1][2], m[2][1], m[2][2],
            m[1][1], m[2][2], m[1][2], m[2][1],
            m[1][1] * m[2][2], m[1][2] * m[2][1],
            minor11
        );

        System.out.printf(
            "  Step 2 - Minor M12: det([%d,%d],[%d,%d]) = (%d*%d) - (%d*%d) = %d - %d = %d%n",
            m[1][0], m[1][2], m[2][0], m[2][2],
            m[1][0], m[2][2], m[1][2], m[2][0],
            m[1][0] * m[2][2], m[1][2] * m[2][0],
            minor12
        );

        System.out.printf(
            "  Step 3 - Minor M13: det([%d,%d],[%d,%d]) = (%d*%d) - (%d*%d) = %d - %d = %d%n",
            m[1][0], m[1][1], m[2][0], m[2][1],
            m[1][0], m[2][1], m[1][1], m[2][0],
            m[1][0] * m[2][1], m[1][1] * m[2][0],
            minor13
        );

        // --- Compute each signed cofactor term ---
        int c11 = +1 * m[0][0] * minor11;   // (+1) sign for column 1
        int c12 = -1 * m[0][1] * minor12;   // (-1) sign for column 2
        int c13 = +1 * m[0][2] * minor13;   // (+1) sign for column 3

        // --- Print the cofactor terms ---
        System.out.println();
        System.out.printf("  Cofactor C11 = (+1) x %d x %d = %d%n",  m[0][0], minor11, c11);
        System.out.printf("  Cofactor C12 = (-1) x %d x %d = %d%n",  m[0][1], minor12, c12);
        System.out.printf("  Cofactor C13 = (+1) x %d x %d = %d%n",  m[0][2], minor13, c13);

        // --- Show the final summation expression ---
        int det = c11 + c12 + c13;
        System.out.printf("%n  det(M) = %d + (%d) + %d%n", c11, c12, c13);

        return det;
    }

    // ---------------------------------------------------------------
    // Entry point: declares the assigned matrix and drives the solver
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        // Declare the student-assigned 3×3 matrix (hardcoded, no user input)
        int[][] matrix = {
            { 3, 5, 4 },
            { 6, 2, 1 },
            { 4, 3, 5 }
        };

        // Print the program header with student information
        System.out.println("===================================================");
        System.out.println("  3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("  Student: Tanchingco, John Matthew R.");
        System.out.println("  Assigned Matrix:");
        System.out.println("===================================================");
        printMatrix(matrix);
        System.out.println("===================================================");

        // Run the full step-by-step determinant computation
        int determinant = solveDeterminant(matrix);

        // Print the final result
        System.out.println("\n===================================================");
        if (determinant == 0) {
            System.out.println("  DETERMINANT = 0");
            System.out.println("  The matrix is SINGULAR - it has no inverse.");
        } else {
            System.out.printf("  DETERMINANT = %d%n", determinant);
        }
        System.out.println("===================================================");
    }
}
