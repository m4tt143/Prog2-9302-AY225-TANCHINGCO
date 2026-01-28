import java.util.Scanner;

/**
 * Prelim Grade Calculator
 * This program computes the required Prelim Exam score a student needs
 * to achieve either a passing grade (75) or excellent grade (100).
 * 
 * Grading Breakdown:
 * - Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
 * - Class Standing = (Attendance × 0.40) + (Lab Work Average × 0.60)
 * - Lab Work Average = (Lab1 + Lab2 + Lab3) / 3
 */
public class PrelimGradeCalculator {
    
    // Constants for grading weights
    private static final double PRELIM_EXAM_WEIGHT = 0.30;
    private static final double CLASS_STANDING_WEIGHT = 0.70;
    private static final double ATTENDANCE_WEIGHT = 0.40;
    private static final double LAB_WORK_WEIGHT = 0.60;
    private static final double PASSING_GRADE = 75.0;
    private static final double EXCELLENT_GRADE = 100.0;
    private static final int TOTAL_CLASSES = 15; // Total class sessions
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Display program header
        displayHeader();
        
        // Get user inputs
        int attendances = getAttendance(scanner);
        double lab1 = getLabGrade(scanner, 1);
        double lab2 = getLabGrade(scanner, 2);
        double lab3 = getLabGrade(scanner, 3);
        
        // Compute attendance percentage
        double attendanceScore = (double) attendances / TOTAL_CLASSES * 100;
        
        // Compute Lab Work Average
        double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
        
        // Compute Class Standing
        double classStanding = (attendanceScore * ATTENDANCE_WEIGHT) + 
                              (labWorkAverage * LAB_WORK_WEIGHT);
        
        // Compute required Prelim Exam scores
        double requiredPrelimForPassing = computeRequiredPrelimScore(
            classStanding, PASSING_GRADE);
        double requiredPrelimForExcellent = computeRequiredPrelimScore(
            classStanding, EXCELLENT_GRADE);
        
        // Display results
        displayResults(attendances, attendanceScore, lab1, lab2, lab3, 
                      labWorkAverage, classStanding, 
                      requiredPrelimForPassing, requiredPrelimForExcellent);
        
        scanner.close();
    }
    
    /**
     * Display program header
     */
    private static void displayHeader() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("            PRELIM GRADE CALCULATOR");
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }
    
    /**
     * Get and validate attendance input
     */
    private static int getAttendance(Scanner scanner) {
        int attendance = -1;
        while (attendance < 0 || attendance > TOTAL_CLASSES) {
            System.out.print("Enter number of attendances (0-" + TOTAL_CLASSES + "): ");
            if (scanner.hasNextInt()) {
                attendance = scanner.nextInt();
                if (attendance < 0 || attendance > TOTAL_CLASSES) {
                    System.out.println("❌ Invalid input! Attendance must be between 0 and " 
                                     + TOTAL_CLASSES + ".");
                }
            } else {
                System.out.println("❌ Invalid input! Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
        return attendance;
    }
    
    /**
     * Get and validate lab grade input
     */
    private static double getLabGrade(Scanner scanner, int labNumber) {
        double grade = -1;
        while (grade < 0 || grade > 100) {
            System.out.print("Enter Lab Work " + labNumber + " grade (0-100): ");
            if (scanner.hasNextDouble()) {
                grade = scanner.nextDouble();
                if (grade < 0 || grade > 100) {
                    System.out.println("❌ Invalid input! Grade must be between 0 and 100.");
                }
            } else {
                System.out.println("❌ Invalid input! Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
        return grade;
    }
    
    /**
     * Compute required Prelim Exam score to achieve target grade
     * Formula: Prelim Exam = (Target Grade - Class Standing × 0.70) / 0.30
     */
    private static double computeRequiredPrelimScore(double classStanding, 
                                                     double targetGrade) {
        return (targetGrade - (classStanding * CLASS_STANDING_WEIGHT)) 
               / PRELIM_EXAM_WEIGHT;
    }
    
    /**
     * Display all computed results
     */
    private static void displayResults(int attendances, double attendanceScore,
                                      double lab1, double lab2, double lab3,
                                      double labWorkAverage, double classStanding,
                                      double requiredForPassing, 
                                      double requiredForExcellent) {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("                   RESULTS");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Display input summary
        System.out.println("\nINPUT SUMMARY");
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.printf("  Attendances:       %d / %d (%.2f%%)%n", 
                         attendances, TOTAL_CLASSES, attendanceScore);
        System.out.printf("  Lab Work 1:        %.2f%n", lab1);
        System.out.printf("  Lab Work 2:        %.2f%n", lab2);
        System.out.printf("  Lab Work 3:        %.2f%n", lab3);
        
        // Display computed values
        System.out.println("\nCOMPUTED VALUES");
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.printf("  Lab Work Average:  %.2f%n", labWorkAverage);
        System.out.printf("  Class Standing:    %.2f%n", classStanding);
        
        // Display required Prelim Exam scores
        System.out.println("\nREQUIRED PRELIM EXAM SCORES");
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.printf("  To Pass (75):      %.2f%n", requiredForPassing);
        System.out.printf("  For Excellent:     %.2f%n", requiredForExcellent);
        
        // Display remarks
        System.out.println("\nREMARKS");
        System.out.println("───────────────────────────────────────────────────────────");
        displayRemarks(requiredForPassing, requiredForExcellent);
        
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }
    
    /**
     * Display remarks based on required scores
     */
    private static void displayRemarks(double requiredForPassing, 
                                       double requiredForExcellent) {
        // Passing grade remarks
        if (requiredForPassing > 100) {
            System.out.println("  Unfortunately, it is IMPOSSIBLE to pass.");
            System.out.printf("  You would need %.2f in the Prelim Exam (exceeds 100).%n", 
                             requiredForPassing);
            System.out.println("  Improve your attendance and lab work scores.");
        } else if (requiredForPassing < 0) {
            System.out.println("  You have already PASSED the Prelim period!");
            System.out.println("  Even with 0 on the exam, you will pass.");
        } else {
            System.out.printf("  You need %.2f or higher in the Prelim Exam to PASS.%n", 
                             requiredForPassing);
        }
        
        System.out.println();
        
        // Excellent grade remarks
        if (requiredForExcellent > 100) {
            System.out.println("  Excellent grade (100) is not achievable.");
            System.out.printf("  You would need %.2f in the Prelim Exam (exceeds 100).%n", 
                             requiredForExcellent);
        } else if (requiredForExcellent < 0) {
            System.out.println("  You have already achieved EXCELLENT standing!");
        } else {
            System.out.printf("  You need %.2f in the Prelim Exam for EXCELLENT standing.%n", 
                             requiredForExcellent);
        }
    }
}
