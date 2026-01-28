/**
 * ===========================================================================
 * ATTENDANCE TRACKER - Modern Clean Design
 * ===========================================================================
 * 
 * HOW TO RUN IN VS CODE:
 * ---------------------
 * 1. Press Ctrl + ` (backtick) to open terminal
 * 2. Type: javac AttendanceTrackerClean.java
 * 3. Type: java AttendanceTrackerClean
 * 4. The attendance window will appear!
 * 
 * @author Tanchingco, John Matthew R.
 * @version 3.0 - Modern Clean Design
 * @date January 2026
 * ===========================================================================
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.io.*;
import java.util.ArrayList;

public class AttendanceTrackerClean {
    
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Modern blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);    // Light blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);      // Green
    private static final Color DANGER_COLOR = new Color(231, 76, 60);        // Red
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Light gray
    private static final Color CARD_COLOR = Color.WHITE;                     // White
    private static final Color TEXT_COLOR = new Color(44, 62, 80);           // Dark gray
    private static final Color BORDER_COLOR = new Color(189, 195, 199);      // Gray border
    
    // UI Components
    private JFrame frame;
    private JTextField nameField;
    private JTextField courseField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JLabel recordCountLabel;
    
    // Data
    private ArrayList<AttendanceRecord> attendanceList;
    private static final String ATTENDANCE_FILE = "attendance_records.txt";
    
    /**
     * Inner class for attendance records
     */
    class AttendanceRecord {
        String name, course, timeIn, signature;
        
        public AttendanceRecord(String name, String course, String timeIn, String signature) {
            this.name = name;
            this.course = course;
            this.timeIn = timeIn;
            this.signature = signature;
        }
        
        @Override
        public String toString() {
            return name + "|" + course + "|" + timeIn + "|" + signature;
        }
    }
    
    /**
     * Constructor
     */
    public AttendanceTrackerClean() {
        attendanceList = new ArrayList<>();
        loadAttendanceRecords();
        initializeGUI();
    }
    
    /**
     * Initialize the modern GUI
     */
    private void initializeGUI() {
        frame = new JFrame("Attendance Tracker");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Add components
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(createMainContentPanel(), BorderLayout.CENTER);
        frame.add(createFooterPanel(), BorderLayout.SOUTH);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Create modern header panel
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(frame.getWidth(), 80));
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Title
        JLabel titleLabel = new JLabel("Attendance Tracking System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        // Record count badge
        recordCountLabel = new JLabel(attendanceList.size() + " Records");
        recordCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recordCountLabel.setForeground(Color.WHITE);
        recordCountLabel.setOpaque(true);
        recordCountLabel.setBackground(SECONDARY_COLOR);
        recordCountLabel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        header.add(recordCountLabel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Create main content with cards
     */
    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(createFormCard());
        mainPanel.add(createTableCard());
        
        return mainPanel;
    }
    
    /**
     * Create form card with modern styling
     */
    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 20));
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());
        
        // Card header
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(CARD_COLOR);
        cardHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 25));
        
        JLabel cardTitle = new JLabel("New Entry");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        cardTitle.setForeground(TEXT_COLOR);
        cardHeader.add(cardTitle);
        
        card.add(cardHeader, BorderLayout.NORTH);
        
        // Form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));
        
        // Name field
        formPanel.add(createFormField("Full Name", nameField = createStyledTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Course field
        formPanel.add(createFormField("Course / Year", courseField = createStyledTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Time In field
        timeInField = createStyledTextField();
        timeInField.setEditable(false);
        timeInField.setBackground(new Color(245, 245, 245));
        timeInField.setText(getCurrentDateTime());
        formPanel.add(createFormField("Time In", timeInField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // E-Signature field
        eSignatureField = createStyledTextField();
        eSignatureField.setEditable(false);
        eSignatureField.setBackground(new Color(245, 245, 245));
        eSignatureField.setFont(new Font("Courier New", Font.PLAIN, 11));
        eSignatureField.setText(generateESignature());
        formPanel.add(createFormField("E-Signature", eSignatureField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(CARD_COLOR);
        
        JButton submitBtn = createModernButton("Submit Attendance", SUCCESS_COLOR);
        submitBtn.addActionListener(e -> submitAttendance());
        buttonPanel.add(submitBtn);
        
        JButton refreshBtn = createModernButton("Refresh Time & Signature", SECONDARY_COLOR);
        refreshBtn.addActionListener(e -> refreshFields());
        buttonPanel.add(refreshBtn);
        
        formPanel.add(buttonPanel);
        
        card.add(formPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create table card with modern styling
     */
    private JPanel createTableCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 20));
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());
        
        // Card header
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(CARD_COLOR);
        cardHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 25));
        
        JLabel cardTitle = new JLabel("Attendance History");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        cardTitle.setForeground(TEXT_COLOR);
        cardHeader.add(cardTitle);
        
        card.add(cardHeader, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Name", "Course", "Time In", "Signature"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attendanceTable.setRowHeight(35);
        attendanceTable.setShowGrid(false);
        attendanceTable.setIntercellSpacing(new Dimension(0, 0));
        attendanceTable.setSelectionBackground(new Color(52, 152, 219, 50));
        attendanceTable.setSelectionForeground(TEXT_COLOR);
        
        // Header styling
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        attendanceTable.getTableHeader().setBackground(new Color(236, 240, 241));
        attendanceTable.getTableHeader().setForeground(TEXT_COLOR);
        attendanceTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        attendanceTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Column widths
        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        attendanceTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        attendanceTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        attendanceTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        attendanceTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        
        loadTableData();
        
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create footer with action buttons
     */
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        JButton exportBtn = createSmallButton("Export Records", SUCCESS_COLOR);
        exportBtn.addActionListener(e -> exportRecords());
        footer.add(exportBtn);
        
        JButton clearBtn = createSmallButton("Clear All", DANGER_COLOR);
        clearBtn.addActionListener(e -> clearAllRecords());
        footer.add(clearBtn);
        
        return footer;
    }
    
    /**
     * Create styled text field
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }
    
    /**
     * Create form field with label
     */
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(field);
        
        return panel;
    }
    
    /**
     * Create modern button
     */
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 45));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Create small button for footer
     */
    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        return button;
    }
    
    /**
     * Create card border
     */
    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }
    
    // ===========================================================================
    // UTILITY METHODS
    // ===========================================================================
    
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    private String generateESignature() {
        return UUID.randomUUID().toString();
    }
    
    private void refreshFields() {
        timeInField.setText(getCurrentDateTime());
        eSignatureField.setText(generateESignature());
    }
    
    // ===========================================================================
    // ATTENDANCE OPERATIONS
    // ===========================================================================
    
    private void submitAttendance() {
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String timeIn = timeInField.getText();
        String signature = eSignatureField.getText();
        
        if (name.isEmpty() || course.isEmpty()) {
            showStyledMessage("Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        AttendanceRecord record = new AttendanceRecord(name, course, timeIn, signature);
        attendanceList.add(record);
        tableModel.addRow(new Object[]{name, course, timeIn, signature});
        saveAttendanceRecords();
        
        nameField.setText("");
        courseField.setText("");
        refreshFields();
        updateRecordCount();
        
        showStyledMessage("Attendance recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateRecordCount() {
        recordCountLabel.setText(attendanceList.size() + " Records");
    }
    
    private void showStyledMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(frame, message, title, type);
    }
    
    // ===========================================================================
    // FILE OPERATIONS
    // ===========================================================================
    
    private void loadAttendanceRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    attendanceList.add(new AttendanceRecord(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (FileNotFoundException e) {
            // First run - no file yet
        } catch (IOException e) {
            System.err.println("Error loading: " + e.getMessage());
        }
    }
    
    private void saveAttendanceRecords() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (AttendanceRecord record : attendanceList) {
                writer.println(record.toString());
            }
        } catch (IOException e) {
            showStyledMessage("Error saving records", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTableData() {
        for (AttendanceRecord record : attendanceList) {
            tableModel.addRow(new Object[]{record.name, record.course, record.timeIn, record.signature});
        }
    }
    
    private void clearAllRecords() {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Delete all attendance records? This cannot be undone!",
            "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            attendanceList.clear();
            tableModel.setRowCount(0);
            saveAttendanceRecords();
            updateRecordCount();
            showStyledMessage("All records cleared", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportRecords() {
        if (attendanceList.isEmpty()) {
            showStyledMessage("No records to export", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String filename = "attendance_export_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ATTENDANCE RECORDS EXPORT");
            writer.println("Export Date: " + getCurrentDateTime());
            writer.println("Total Records: " + attendanceList.size());
            writer.println("=".repeat(60));
            writer.println();
            
            for (int i = 0; i < attendanceList.size(); i++) {
                AttendanceRecord r = attendanceList.get(i);
                writer.println("Record #" + (i + 1));
                writer.println("  Name: " + r.name);
                writer.println("  Course: " + r.course);
                writer.println("  Time: " + r.timeIn);
                writer.println("  Signature: " + r.signature);
                writer.println();
            }
            
            showStyledMessage("Exported to: " + filename, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showStyledMessage("Error exporting", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===========================================================================
    // MAIN METHOD
    // ===========================================================================
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceTrackerClean());
    }
}
