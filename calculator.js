/**
 * Prelim Grade Calculator - JavaScript Version
 * 
 * This program computes the required Prelim Exam score a student needs
 * to achieve either a passing grade (75) or excellent grade (100).
 * 
 * Grading Breakdown:
 * - Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
 * - Class Standing = (Attendance × 0.40) + (Lab Work Average × 0.60)
 * - Lab Work Average = (Lab1 + Lab2 + Lab3) / 3
 */

// Constants for grading weights
const PRELIM_EXAM_WEIGHT = 0.30;
const CLASS_STANDING_WEIGHT = 0.70;
const ATTENDANCE_WEIGHT = 0.40;
const LAB_WORK_WEIGHT = 0.60;
const PASSING_GRADE = 75.0;
const EXCELLENT_GRADE = 100.0;
const TOTAL_CLASSES = 15;

/**
 * Validate input value
 * @param {number} value - The value to validate
 * @param {number} min - Minimum allowed value
 * @param {number} max - Maximum allowed value
 * @returns {boolean} - True if valid, false otherwise
 */
function validateInput(value, min, max) {
    return !isNaN(value) && value >= min && value <= max;
}

/**
 * Calculate required Prelim Exam score to achieve target grade
 * Formula: Prelim Exam = (Target Grade - Class Standing × 0.70) / 0.30
 * @param {number} classStanding - The computed class standing
 * @param {number} targetGrade - The target grade to achieve
 * @returns {number} - Required Prelim Exam score
 */
function computeRequiredPrelimScore(classStanding, targetGrade) {
    return (targetGrade - (classStanding * CLASS_STANDING_WEIGHT)) / PRELIM_EXAM_WEIGHT;
}

/**
 * Main calculation function
 */
function calculateGrade() {
    // Clear previous errors
    clearErrors();
    
    // Get input values
    const attendance = parseFloat(document.getElementById('attendance').value);
    const lab1 = parseFloat(document.getElementById('lab1').value);
    const lab2 = parseFloat(document.getElementById('lab2').value);
    const lab3 = parseFloat(document.getElementById('lab3').value);
    
    // Validate inputs
    let hasError = false;
    
    if (!validateInput(attendance, 0, TOTAL_CLASSES)) {
        showError('attendance', `Please enter a valid attendance between 0 and ${TOTAL_CLASSES}`);
        hasError = true;
    }
    
    if (!validateInput(lab1, 0, 100)) {
        showError('lab1', 'Please enter a valid grade between 0 and 100');
        hasError = true;
    }
    
    if (!validateInput(lab2, 0, 100)) {
        showError('lab2', 'Please enter a valid grade between 0 and 100');
        hasError = true;
    }
    
    if (!validateInput(lab3, 0, 100)) {
        showError('lab3', 'Please enter a valid grade between 0 and 100');
        hasError = true;
    }
    
    // Stop if there are validation errors
    if (hasError) {
        return;
    }
    
    // Calculate attendance score
    const attendanceScore = (attendance / TOTAL_CLASSES) * 100;
    
    // Calculate Lab Work Average
    const labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
    
    // Calculate Class Standing
    const classStanding = (attendanceScore * ATTENDANCE_WEIGHT) + 
                         (labWorkAverage * LAB_WORK_WEIGHT);
    
    // Calculate required Prelim Exam scores
    const requiredForPassing = computeRequiredPrelimScore(classStanding, PASSING_GRADE);
    const requiredForExcellent = computeRequiredPrelimScore(classStanding, EXCELLENT_GRADE);
    
    // Display results
    displayResults(
        attendance,
        attendanceScore,
        lab1,
        lab2,
        lab3,
        labWorkAverage,
        classStanding,
        requiredForPassing,
        requiredForExcellent
    );
}

/**
 * Display calculation results
 */
function displayResults(
    attendance,
    attendanceScore,
    lab1,
    lab2,
    lab3,
    labWorkAverage,
    classStanding,
    requiredForPassing,
    requiredForExcellent
) {
    // Display input summary
    document.getElementById('result-attendance').textContent = 
        `${attendance} / ${TOTAL_CLASSES} (${attendanceScore.toFixed(2)}%)`;
    document.getElementById('result-lab1').textContent = lab1.toFixed(2);
    document.getElementById('result-lab2').textContent = lab2.toFixed(2);
    document.getElementById('result-lab3').textContent = lab3.toFixed(2);
    
    // Display computed values
    document.getElementById('result-labavg').textContent = labWorkAverage.toFixed(2);
    document.getElementById('result-standing').textContent = classStanding.toFixed(2);
    
    // Display required scores
    document.getElementById('result-pass').textContent = requiredForPassing.toFixed(2);
    document.getElementById('result-excellent').textContent = requiredForExcellent.toFixed(2);
    
    // Generate and display remarks
    const remarks = generateRemarks(requiredForPassing, requiredForExcellent);
    document.getElementById('remarks').innerHTML = remarks;
    
    // Show results section
    document.getElementById('results').classList.add('show');
    
    // Scroll to results
    document.getElementById('results').scrollIntoView({ 
        behavior: 'smooth', 
        block: 'nearest' 
    });
}

/**
 * Generate remarks based on required scores
 */
function generateRemarks(requiredForPassing, requiredForExcellent) {
    let remarksClass = 'remarks';
    let remarks = '';
    
    // Determine overall status
    if (requiredForPassing > 100) {
        remarksClass = 'remarks warning-remarks';
    } else if (requiredForPassing < 0) {
        remarksClass = 'remarks success-remarks';
    }
    
    // Update remarks container class
    const remarksDiv = document.getElementById('remarks');
    remarksDiv.className = remarksClass;
    
    // Passing grade remarks
    if (requiredForPassing > 100) {
        remarks += `
            <p>
                <strong>Passing Status:</strong> Unfortunately, it is impossible to pass the Prelim period. 
                You would need ${requiredForPassing.toFixed(2)} in the Prelim Exam, which exceeds 100. 
                Consider improving your attendance and lab work scores.
            </p>
        `;
    } else if (requiredForPassing < 0) {
        remarks += `
            <p>
                <strong>Passing Status:</strong> You have already passed the Prelim period! 
                Even with a score of 0 in the Prelim Exam, you will pass.
            </p>
        `;
    } else {
        remarks += `
            <p>
                <strong>Passing Status:</strong> You need ${requiredForPassing.toFixed(2)} or higher 
                in the Prelim Exam to pass.
            </p>
        `;
    }
    
    // Excellent grade remarks
    if (requiredForExcellent > 100) {
        remarks += `
            <p>
                <strong>Excellent Status:</strong> Achieving an excellent grade (100) is not achievable. 
                You would need ${requiredForExcellent.toFixed(2)} in the Prelim Exam, which exceeds 100.
            </p>
        `;
    } else if (requiredForExcellent < 0) {
        remarks += `
            <p>
                <strong>Excellent Status:</strong> You have already achieved excellent standing!
            </p>
        `;
    } else {
        remarks += `
            <p>
                <strong>Excellent Status:</strong> You need ${requiredForExcellent.toFixed(2)} 
                in the Prelim Exam for excellent standing.
            </p>
        `;
    }
    
    return remarks;
}

/**
 * Show validation error for a field
 */
function showError(fieldId, message) {
    const input = document.getElementById(fieldId);
    const error = document.getElementById(`${fieldId}-error`);
    
    input.classList.add('error');
    error.textContent = message;
    error.style.display = 'block';
}

/**
 * Clear all validation errors
 */
function clearErrors() {
    const inputs = document.querySelectorAll('input');
    const errors = document.querySelectorAll('.error-message');
    
    inputs.forEach(input => input.classList.remove('error'));
    errors.forEach(error => error.style.display = 'none');
}

/**
 * Reset the form and hide results
 */
function resetForm() {
    // Clear all input fields
    document.getElementById('attendance').value = '';
    document.getElementById('lab1').value = '';
    document.getElementById('lab2').value = '';
    document.getElementById('lab3').value = '';
    
    // Clear errors
    clearErrors();
    
    // Hide results
    document.getElementById('results').classList.remove('show');
    
    // Scroll to top
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

/**
 * Allow Enter key to trigger calculation
 */
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input');
    
    inputs.forEach(input => {
        input.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                calculateGrade();
            }
        });
    });
});
