import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Prelim Grade Calculator - GUI Version
 * This program computes the required Prelim Exam score a student needs
 * to achieve either a passing grade (75) or excellent grade (100).
 * 
 * Grading Breakdown:
 * - Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
 * - Class Standing = (Attendance × 0.40) + (Lab Work Average × 0.60)
 * - Lab Work Average = (Lab1 + Lab2 + Lab3) / 3
 */
public class PrelimGradeCalculatorGUI extends JFrame {
    
    // Constants for grading weights
    private static final double PRELIM_EXAM_WEIGHT = 0.30;
    private static final double CLASS_STANDING_WEIGHT = 0.70;
    private static final double ATTENDANCE_WEIGHT = 0.40;
    private static final double LAB_WORK_WEIGHT = 0.60;
    private static final double PASSING_GRADE = 75.0;
    private static final double EXCELLENT_GRADE = 100.0;
    private static final int TOTAL_CLASSES = 5;
    
    // GUI Components
    private JTextField attendanceField;
    private JTextField excusedAbsencesField;
    private JCheckBox excuseVerifiedCheckbox;
    private JTextField lab1Field;
    private JTextField lab2Field;
    private JTextField lab3Field;
    private JTextArea resultArea;
    private JButton calculateButton;
    private JButton resetButton;
    
    public PrelimGradeCalculatorGUI() {
        setTitle("Prelim Grade Calculator");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Input Panel
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Result Panel
        JPanel resultPanel = createResultPanel();
        mainPanel.add(resultPanel);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(37, 99, 235));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Prelim Grade Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Calculate your required Prelim Exam score");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            "Input Information",
            0,
            0,
            new Font("Arial", Font.BOLD, 14),
            new Color(55, 65, 81)
        ));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Grid panel for main inputs
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(5, 2, 10, 15));
        gridPanel.setBackground(Color.WHITE);
        
        // Attendance
        JLabel attendanceLabel = new JLabel("Number of Attendances (0-5):");
        attendanceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        attendanceField = new JTextField();
        attendanceField.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Excused Absences
        JLabel excusedLabel = new JLabel("Excused Absences (0-5):");
        excusedLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        excusedAbsencesField = new JTextField("0");
        excusedAbsencesField.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Lab 1
        JLabel lab1Label = new JLabel("Lab Work 1 Grade (0-100):");
        lab1Label.setFont(new Font("Arial", Font.PLAIN, 13));
        lab1Field = new JTextField();
        lab1Field.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Lab 2
        JLabel lab2Label = new JLabel("Lab Work 2 Grade (0-100):");
        lab2Label.setFont(new Font("Arial", Font.PLAIN, 13));
        lab2Field = new JTextField();
        lab2Field.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Lab 3
        JLabel lab3Label = new JLabel("Lab Work 3 Grade (0-100):");
        lab3Label.setFont(new Font("Arial", Font.PLAIN, 13));
        lab3Field = new JTextField();
        lab3Field.setFont(new Font("Arial", Font.PLAIN, 13));
        
        gridPanel.add(attendanceLabel);
        gridPanel.add(attendanceField);
        gridPanel.add(excusedLabel);
        gridPanel.add(excusedAbsencesField);
        gridPanel.add(lab1Label);
        gridPanel.add(lab1Field);
        gridPanel.add(lab2Label);
        gridPanel.add(lab2Field);
        gridPanel.add(lab3Label);
        gridPanel.add(lab3Field);
        
        // Verification checkbox
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        checkboxPanel.setBackground(new Color(249, 250, 251));
        checkboxPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(251, 191, 36), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        excuseVerifiedCheckbox = new JCheckBox(
            "<html><b>✓ Excused absences verified by Sir Val</b><br>" +
            "<small>(Required if you have excused absences)</small></html>"
        );
        excuseVerifiedCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        excuseVerifiedCheckbox.setBackground(new Color(249, 250, 251));
        excuseVerifiedCheckbox.setEnabled(false);
        
        // Enable checkbox when excused absences > 0
        excusedAbsencesField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    int excused = Integer.parseInt(excusedAbsencesField.getText().trim());
                    excuseVerifiedCheckbox.setEnabled(excused > 0);
                    if (excused == 0) {
                        excuseVerifiedCheckbox.setSelected(false);
                    }
                } catch (NumberFormatException e) {
                    excuseVerifiedCheckbox.setEnabled(false);
                }
            }
        });
        
        checkboxPanel.add(excuseVerifiedCheckbox);
        
        panel.add(gridPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(checkboxPanel);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(Color.WHITE);
        
        calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setBackground(new Color(37, 99, 235));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorderPainted(false);
        calculateButton.setPreferredSize(new Dimension(150, 40));
        calculateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calculateButton.addActionListener(e -> calculateGrade());
        
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(243, 244, 246));
        resetButton.setForeground(new Color(55, 65, 81));
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setPreferredSize(new Dimension(150, 40));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(e -> resetForm());
        
        panel.add(calculateButton);
        panel.add(resetButton);
        
        return panel;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            "Results",
            0,
            0,
            new Font("Arial", Font.BOLD, 14),
            new Color(55, 65, 81)
        ));
        
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(249, 250, 251));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(540, 250));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void calculateGrade() {
        try {
            // Get and validate inputs
            int attendance = Integer.parseInt(attendanceField.getText().trim());
            int excusedAbsences = Integer.parseInt(excusedAbsencesField.getText().trim());
            double lab1 = Double.parseDouble(lab1Field.getText().trim());
            double lab2 = Double.parseDouble(lab2Field.getText().trim());
            double lab3 = Double.parseDouble(lab3Field.getText().trim());
            
            // Validate ranges
            if (attendance < 0 || attendance > TOTAL_CLASSES) {
                showError("Attendance must be between 0 and " + TOTAL_CLASSES);
                return;
            }
            
            if (excusedAbsences < 0 || excusedAbsences > TOTAL_CLASSES) {
                showError("Excused absences must be between 0 and " + TOTAL_CLASSES);
                return;
            }
            
            if (attendance + excusedAbsences > TOTAL_CLASSES) {
                showError("Total attendance + excused absences cannot exceed " + TOTAL_CLASSES);
                return;
            }
            
            // Check if excused absences need verification
            if (excusedAbsences > 0 && !excuseVerifiedCheckbox.isSelected()) {
                showError("You must confirm that excused absences are verified by Sir Val.\n" +
                         "Please check the verification box if your excuse letter has been approved.");
                return;
            }
            
            if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                showError("Lab grades must be between 0 and 100");
                return;
            }
            
            // Calculate total classes that count (excused absences don't count against you)
            int totalClassesThatCount = TOTAL_CLASSES - excusedAbsences;
            int unexcusedAbsences = totalClassesThatCount - attendance;
            
            // Check if student has too many unexcused absences (4 or more)
            if (unexcusedAbsences >= 4) {
                resultArea.setText("═══════════════════════════════════════════════════════════\n" +
                                 "                  AUTOMATIC FAILURE\n" +
                                 "═══════════════════════════════════════════════════════════\n\n" +
                                 "  ❌ YOU HAVE BEEN AUTOMATICALLY FAILED\n\n" +
                                 String.format("  Classes held: %d%n", totalClassesThatCount) +
                                 String.format("  You attended: %d%n", attendance) +
                                 String.format("  Unexcused absences: %d%n%n", unexcusedAbsences) +
                                 "  Having 4 or more unexcused absences results in automatic failure.\n\n" +
                                 "  Note: Excused absences do not count against you.\n" +
                                 "═══════════════════════════════════════════════════════════\n");
                return;
            }
            
            // Calculate attendance score (based on classes that count)
            double attendanceScore = totalClassesThatCount > 0 ? 
                                    (double) attendance / totalClassesThatCount * 100 : 100.0;
            
            // Calculate Lab Work Average
            double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
            
            // Calculate Class Standing
            double classStanding = (attendanceScore * ATTENDANCE_WEIGHT) + 
                                  (labWorkAverage * LAB_WORK_WEIGHT);
            
            // Calculate required Prelim Exam scores
            double requiredForPassing = computeRequiredPrelimScore(classStanding, PASSING_GRADE);
            double requiredForExcellent = computeRequiredPrelimScore(classStanding, EXCELLENT_GRADE);
            
            // Display results
            displayResults(attendance, excusedAbsences, totalClassesThatCount, attendanceScore, 
                          lab1, lab2, lab3, labWorkAverage, classStanding, 
                          requiredForPassing, requiredForExcellent);
            
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers in all fields");
        }
    }
    
    private double computeRequiredPrelimScore(double classStanding, double targetGrade) {
        return (targetGrade - (classStanding * CLASS_STANDING_WEIGHT)) / PRELIM_EXAM_WEIGHT;
    }
    
    private void displayResults(int attendance, int excusedAbsences, int totalClassesThatCount, 
                               double attendanceScore, double lab1, double lab2, double lab3,
                               double labWorkAverage, double classStanding,
                               double requiredForPassing, double requiredForExcellent) {
        StringBuilder result = new StringBuilder();
        
        result.append("═══════════════════════════════════════════════════════════\n");
        result.append("                    YOUR CURRENT GRADES\n");
        result.append("═══════════════════════════════════════════════════════════\n\n");
        
        result.append(String.format("  Total Classes:        %d%n", TOTAL_CLASSES));
        result.append(String.format("  Excused Absences:     %d%n", excusedAbsences));
        result.append(String.format("  Classes That Count:   %d%n", totalClassesThatCount));
        result.append(String.format("  Attendance:           %d/%d classes (%.2f%%)%n%n", 
                                   attendance, totalClassesThatCount, attendanceScore));
        result.append(String.format("  Lab Work 1:           %.2f%n", lab1));
        result.append(String.format("  Lab Work 2:           %.2f%n", lab2));
        result.append(String.format("  Lab Work 3:           %.2f%n%n", lab3));
        
        result.append(String.format("  Lab Work Average:     %.2f%n", labWorkAverage));
        result.append(String.format("  Class Standing:       %.2f (70%% of final grade)%n%n", classStanding));
        
        result.append("═══════════════════════════════════════════════════════════\n");
        result.append("              WHAT YOU NEED ON THE PRELIM EXAM\n");
        result.append("═══════════════════════════════════════════════════════════\n\n");
        
        // Passing grade explanation
        if (requiredForPassing > 100) {
            result.append("  TO PASS (Final Grade of 75):\n");
            result.append(String.format("  ❌ Impossible - would need %.2f/100%n%n", requiredForPassing));
            result.append("  Your current Class Standing is too low.\n");
            
            // Give specific advice based on what's low
            if (attendanceScore < 80 && labWorkAverage >= 90) {
                result.append("  Focus on improving your attendance.\n\n");
            } else if (labWorkAverage < 80 && attendanceScore >= 90) {
                result.append("  Focus on improving your lab work grades.\n\n");
            } else if (attendanceScore < 80 && labWorkAverage < 80) {
                result.append("  Focus on improving both attendance and lab work.\n\n");
            } else {
                result.append("  Focus on improving attendance and lab work.\n\n");
            }
        } else if (requiredForPassing <= 0) {
            result.append("  TO PASS (Final Grade of 75):\n");
            result.append("  ✓ Already Guaranteed!%n%n");
            result.append("  You can score 0 on the Prelim Exam and still pass!\n");
            result.append("  Your Class Standing alone ensures a passing grade.\n\n");
        } else {
            result.append("  TO PASS (Final Grade of 75):\n");
            result.append(String.format("  Need at least: %.2f/100%n%n", requiredForPassing));
        }
        
        // Excellent grade explanation
        if (requiredForExcellent > 100) {
            result.append("  FOR EXCELLENT (Final Grade of 100):\n");
            result.append(String.format("  ❌ Impossible - would need %.2f/100%n%n", requiredForExcellent));
        } else if (requiredForExcellent <= 0) {
            result.append("  FOR EXCELLENT (Final Grade of 100):\n");
            result.append("  ⭐ Already Guaranteed!%n%n");
        } else {
            result.append("  FOR EXCELLENT (Final Grade of 100):\n");
            result.append(String.format("  Need exactly: %.2f/100%n%n", requiredForExcellent));
        }
        
        result.append("═══════════════════════════════════════════════════════════\n");
        result.append("                       HOW IT WORKS\n");
        result.append("═══════════════════════════════════════════════════════════\n\n");
        result.append("  Final Grade = (Prelim Exam × 30%) + (Class Standing × 70%)\n\n");
        result.append(String.format("  With your Class Standing of %.2f:%n", classStanding));
        result.append(String.format("  • You already have %.2f points locked in (%.2f × 70%%)%n", 
                                   classStanding * 0.70, classStanding));
        result.append("  • The Prelim Exam can add up to 30 more points (100 × 30%)\n\n");
        
        if (requiredForPassing > 0 && requiredForPassing <= 100) {
            double pointsNeeded = PASSING_GRADE - (classStanding * CLASS_STANDING_WEIGHT);
            result.append(String.format("  To reach 75: You need %.2f more points%n", pointsNeeded));
            result.append(String.format("  Since exam is worth 30%%: %.2f ÷ 0.30 = %.2f%n", 
                                       pointsNeeded, requiredForPassing));
        }
        
        result.append("\n═══════════════════════════════════════════════════════════\n");
        
        resultArea.setText(result.toString());
        resultArea.setCaretPosition(0);
    }
    
    private void resetForm() {
        attendanceField.setText("");
        excusedAbsencesField.setText("0");
        excuseVerifiedCheckbox.setSelected(false);
        excuseVerifiedCheckbox.setEnabled(false);
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        resultArea.setText("");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PrelimGradeCalculatorGUI frame = new PrelimGradeCalculatorGUI();
            frame.setVisible(true);
        });
    }
}
