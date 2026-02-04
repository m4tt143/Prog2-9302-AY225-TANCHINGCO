/**
 * Student Record System - Lab Exam
 * Programmer: Tanchingco, John Matthew R. - 23-0792-227
 * Date: February 4, 2026
 */

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class StudentRecordSystem extends JFrame {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, firstNameField, lastNameField;
    private JTextField lab1Field, lab2Field, lab3Field, prelimField, attendanceField;
    private JButton addButton, deleteButton;
    
    private final String[] columns = {
        "ID", "First Name", "Last Name", "Lab 1", "Lab 2", "Lab 3", "Prelim", "Attendance"
    };
    
    // Document Filter for Student ID (numbers and dashes only)
    private class StudentIDFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            if (string.matches("[0-9-]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            if (text.matches("[0-9-]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
    
    // Document Filter for numeric fields (grades)
    private class NumericFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            if (string.matches("[0-9]*")) {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (newStr.isEmpty() || Integer.parseInt(newStr) <= 100) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            if (text.matches("[0-9]*")) {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newStr = current.substring(0, offset) + text + current.substring(offset + length);
                if (newStr.isEmpty() || Integer.parseInt(newStr) <= 100) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
    }
    
    public StudentRecordSystem() {
        setTitle("Student Records - Tanchingco, John Matthew R. - 23-0792-227");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        loadCSVData();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Main panel with clean background
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(248, 249, 250));
        JLabel titleLabel = new JLabel("Student Records");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(26, 26, 26));
        headerPanel.add(titleLabel);
        
        // Input card
        JPanel inputCard = createInputPanel();
        
        // Table
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(new Color(249, 250, 251));
        table.setSelectionForeground(new Color(31, 41, 55));
        
        // Header styling
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(249, 250, 251));
        table.getTableHeader().setForeground(new Color(55, 65, 81));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Cell alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 3; i < 8; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        
        JLabel tableTitle = new JLabel("ALL RECORDS");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        tableTitle.setForeground(new Color(55, 65, 81));
        tableHeader.add(tableTitle, BorderLayout.WEST);
        
        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(inputCard, BorderLayout.NORTH);
        mainPanel.add(tableCard, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createInputPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("ADD NEW RECORD");
        title.setFont(new Font("SansSerif", Font.BOLD, 11));
        title.setForeground(new Color(55, 65, 81));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 12, 12));
        gridPanel.setBackground(Color.WHITE);
        
        // Create fields
        idField = createLabeledField(gridPanel, "Student ID");
        firstNameField = createLabeledField(gridPanel, "First Name");
        lastNameField = createLabeledField(gridPanel, "Last Name");
        lab1Field = createLabeledField(gridPanel, "Lab 1");
        lab2Field = createLabeledField(gridPanel, "Lab 2");
        lab3Field = createLabeledField(gridPanel, "Lab 3");
        prelimField = createLabeledField(gridPanel, "Prelim");
        attendanceField = createLabeledField(gridPanel, "Attendance");
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        addButton = createButton("Add Student", new Color(59, 130, 246));
        addButton.addActionListener(e -> addRecord());
        
        deleteButton = createButton("Delete Selected", new Color(239, 68, 68));
        deleteButton.addActionListener(e -> deleteRecord());
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        card.add(title, BorderLayout.NORTH);
        card.add(gridPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JTextField createLabeledField(JPanel parent, String labelText) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        label.setForeground(new Color(107, 114, 128));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(0, 36));
        
        // Apply appropriate filter based on field type
        if (labelText.equals("Student ID")) {
            ((AbstractDocument) field.getDocument()).setDocumentFilter(new StudentIDFilter());
        } else if (labelText.matches("Lab [123]|Prelim|Attendance")) {
            ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericFilter());
        }
        
        container.add(label);
        container.add(field);
        parent.add(container);
        
        return field;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void loadCSVData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Prog2-9302-AY225-TANCHINGCO/PRELIM-EXAM/Java/MOCK_DATA.csv"));
            String line;
            boolean firstLine = true;
            int count = 0;  
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length >= 8) {
                    tableModel.addRow(new Object[]{
                        data[0].trim(), data[1].trim(), data[2].trim(),
                        data[3].trim(), data[4].trim(), data[5].trim(),
                        data[6].trim(), data[7].trim()
                    });
                    count++;
                }
            }
            
            reader.close();
            JOptionPane.showMessageDialog(this, 
                count + " records loaded successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                "MOCK_DATA.csv not found!\nPlease place it in the same folder.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading file: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Prog2-9302-AY225-TANCHINGCO/PRELIM-EXAM/Java/MOCK_DATA.csv"));
            
            // Write header
            writer.write("StudentID,first_name,last_name,LAB WORK 1,LAB WORK 2,LAB WORK 3,PRELIM EXAM,ATTENDANCE GRADE");
            writer.newLine();
            
            // Write all rows from table
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    line.append(tableModel.getValueAt(i, j));
                    if (j < tableModel.getColumnCount() - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.newLine();
            }
            
            writer.close();
            System.out.println("Data saved to CSV");
            
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }
    
    private void addRecord() {
        String id = idField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String lab1 = lab1Field.getText().trim();
        String lab2 = lab2Field.getText().trim();
        String lab3 = lab3Field.getText().trim();
        String prelim = prelimField.getText().trim();
        String attendance = attendanceField.getText().trim();
        
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
            lab1.isEmpty() || lab2.isEmpty() || lab3.isEmpty() || 
            prelim.isEmpty() || attendance.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tableModel.addRow(new Object[]{id, firstName, lastName, lab1, lab2, lab3, prelim, attendance});
        
        // Auto-save to CSV
        saveToCSV();
        
        // Clear fields
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        prelimField.setText("");
        attendanceField.setText("");
        
        JOptionPane.showMessageDialog(this, "Record added successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete this record?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            
            // Auto-save to CSV
            saveToCSV();
            
            JOptionPane.showMessageDialog(this, "Record deleted!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new StudentRecordSystem());
    }
}
