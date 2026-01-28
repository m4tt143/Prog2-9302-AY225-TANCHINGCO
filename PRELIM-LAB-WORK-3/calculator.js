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
const TOTAL_CLASSES = 5;

/**
 * Restore the original results HTML structure
 */
function restoreResultsStructure() {
    const resultsDiv = document.getElementById('results');
    resultsDiv.innerHTML = `
        <h2>Your Results</h2>
        
        <div class="result-section">
            <h3>Your Current Grades</h3>
            <div class="result-item">
                <span class="result-label">Attendance</span>
                <span class="result-value" id="result-attendance"></span>
            </div>
            <div class="result-item">
                <span class="result-label">Lab Work 1</span>
                <span class="result-value" id="result-lab1"></span>
            </div>
            <div class="result-item">
                <span class="result-label">Lab Work 2</span>
                <span class="result-value" id="result-lab2"></span>
            </div>
            <div class="result-item">
                <span class="result-label">Lab Work 3</span>
                <span class="result-value" id="result-lab3"></span>
            </div>
            <div class="result-item">
                <span class="result-label">Lab Work Average</span>
                <span class="result-value" id="result-labavg"></span>
            </div>
            <div class="result-item">
                <span class="result-label">Class Standing</span>
                <span class="result-value" id="result-standing"></span>
            </div>
        </div>
        
        <div class="result-section">
            <h3>What You Need on Prelim Exam</h3>
            <div class="target-scores">
                <div class="score-card">
                    <h4>To Pass (75)</h4>
                    <div class="score" id="result-pass"></div>
                </div>
                <div class="score-card">
                    <h4>For Excellent (100)</h4>
                    <div class="score" id="result-excellent"></div>
                </div>
            </div>
        </div>
        
        <div class="result-section">
            <h3>Explanation</h3>
            <div class="remarks" id="remarks"></div>
        </div>
    `;
}

/**
 * Toggle verification checkbox based on excused absences
 */
function toggleVerificationCheckbox() {
    const excused = parseFloat(document.getElementById('excused').value) || 0;
    const checkbox = document.getElementById('verified');
    
    if (excused > 0) {
        checkbox.disabled = false;
    } else {
        checkbox.disabled = true;
        checkbox.checked = false;
    }
}

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
    // Restore original results structure in case it was modified by auto-fail
    restoreResultsStructure();
    
    // Clear previous errors
    clearErrors();
    
    // Get input values
    const attendance = parseFloat(document.getElementById('attendance').value);
    const excusedAbsences = parseFloat(document.getElementById('excused').value);
    const lab1 = parseFloat(document.getElementById('lab1').value);
    const lab2 = parseFloat(document.getElementById('lab2').value);
    const lab3 = parseFloat(document.getElementById('lab3').value);
    
    // Validate inputs
    let hasError = false;
    
    if (!validateInput(attendance, 0, TOTAL_CLASSES)) {
        showError('attendance', `Please enter a valid attendance between 0 and ${TOTAL_CLASSES}`);
        hasError = true;
    }
    
    if (!validateInput(excusedAbsences, 0, TOTAL_CLASSES)) {
        showError('excused', `Please enter valid excused absences between 0 and ${TOTAL_CLASSES}`);
        hasError = true;
    }
    
    if (attendance + excusedAbsences > TOTAL_CLASSES) {
        showError('excused', `Total attendance + excused absences cannot exceed ${TOTAL_CLASSES}`);
        hasError = true;
    }
    
    // Check if excused absences need verification
    const verified = document.getElementById('verified').checked;
    if (excusedAbsences > 0 && !verified) {
        alert('⚠️ Verification Required\n\nYou must confirm that excused absences are verified by Sir Val.\n\nPlease check the verification box if your excuse letter has been approved.');
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
    
    // Calculate total classes that count (excused absences don't count against you)
    const totalClassesThatCount = TOTAL_CLASSES - excusedAbsences;
    const unexcusedAbsences = totalClassesThatCount - attendance;
    
    // Check if student has too many unexcused absences (4 or more)
    if (unexcusedAbsences >= 4) {
        // Display automatic failure message
        const resultsDiv = document.getElementById('results');
        resultsDiv.innerHTML = `
            <h2>Results</h2>
            <div style="padding: 20px; text-align: center;">
                <div style="background: #fee2e2; border: 2px solid #dc2626; border-radius: 8px; padding: 30px; text-align: left;">
                    <h3 style="color: #dc2626; font-size: 20px; margin-bottom: 16px;">⚠️ AUTOMATIC FAILURE</h3>
                    <p style="font-size: 16px; font-weight: 600; color: #991b1b; margin-bottom: 16px;">
                        ❌ YOU HAVE BEEN AUTOMATICALLY FAILED
                    </p>
                    <p style="font-size: 15px; color: #7f1d1d; margin-bottom: 12px;">
                        Classes held: <strong>${totalClassesThatCount}</strong>
                    </p>
                    <p style="font-size: 15px; color: #7f1d1d; margin-bottom: 12px;">
                        You attended: <strong>${attendance}</strong>
                    </p>
                    <p style="font-size: 15px; color: #7f1d1d; margin-bottom: 12px;">
                        Unexcused absences: <strong>${unexcusedAbsences}</strong>
                    </p>
                    <p style="font-size: 15px; color: #7f1d1d; margin-bottom: 12px;">
                        Having 4 or more unexcused absences results in automatic failure.
                    </p>
                    <p style="font-size: 14px; color: #7f1d1d; font-style: italic;">
                        Note: Excused absences do not count against you.
                    </p>
                </div>
            </div>
        `;
        resultsDiv.classList.add('show');
        resultsDiv.scrollIntoView({ 
            behavior: 'smooth', 
            block: 'nearest' 
        });
        return;
    }
    
    // Calculate attendance score (based on classes that count)
    const attendanceScore = totalClassesThatCount > 0 ? (attendance / totalClassesThatCount) * 100 : 100.0;
    
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
        excusedAbsences,
        totalClassesThatCount,
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
    excusedAbsences,
    totalClassesThatCount,
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
        `${attendance} / ${totalClassesThatCount} (${attendanceScore.toFixed(2)}%) | Excused: ${excusedAbsences}`;
    document.getElementById('result-lab1').textContent = lab1.toFixed(2);
    document.getElementById('result-lab2').textContent = lab2.toFixed(2);
    document.getElementById('result-lab3').textContent = lab3.toFixed(2);
    
    // Display computed values
    document.getElementById('result-labavg').textContent = labWorkAverage.toFixed(2);
    document.getElementById('result-standing').textContent = 
        `${classStanding.toFixed(2)} (70% of final grade)`;
    
    // Display required scores
    document.getElementById('result-pass').textContent = requiredForPassing.toFixed(2);
    document.getElementById('result-excellent').textContent = requiredForExcellent.toFixed(2);
    
    // Generate and display remarks
    const remarks = generateRemarks(requiredForPassing, requiredForExcellent, classStanding, attendanceScore, labWorkAverage);
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
function generateRemarks(requiredForPassing, requiredForExcellent, classStanding, attendanceScore, labWorkAverage) {
    let remarksClass = 'remarks';
    let remarks = '';
    
    // Determine overall status
    if (requiredForPassing > 100) {
        remarksClass = 'remarks warning-remarks';
    } else if (requiredForPassing <= 0) {
        remarksClass = 'remarks success-remarks';
    }
    
    // Update remarks container class
    const remarksDiv = document.getElementById('remarks');
    remarksDiv.className = remarksClass;
    
    // Calculate current points from class standing
    const pointsFromClassStanding = (classStanding * CLASS_STANDING_WEIGHT).toFixed(2);
    
    // Explanation of how grading works
    remarks += `
        <p style="margin-bottom: 16px; padding-bottom: 16px; border-bottom: 2px solid #e5e7eb;">
            <strong>How Your Final Grade is Calculated:</strong><br>
            Final Grade = (Prelim Exam × 30%) + (Class Standing × 70%)
        </p>
    `;
    
    remarks += `
        <p style="margin-bottom: 16px;">
            <strong>Your Current Status:</strong><br>
            With a Class Standing of <strong>${classStanding.toFixed(2)}</strong>, you already have 
            <strong>${pointsFromClassStanding} points</strong> secured (${classStanding.toFixed(2)} × 70%).
        </p>
    `;
    
    // Passing grade remarks
    if (requiredForPassing > 100) {
        const pointsNeeded = (PASSING_GRADE - pointsFromClassStanding).toFixed(2);
        
        // Determine what needs improvement
        let advice = '';
        if (attendanceScore < 80 && labWorkAverage >= 90) {
            advice = 'Focus on improving your attendance.';
        } else if (labWorkAverage < 80 && attendanceScore >= 90) {
            advice = 'Focus on improving your lab work grades.';
        } else if (attendanceScore < 80 && labWorkAverage < 80) {
            advice = 'Focus on improving both attendance and lab work.';
        } else {
            advice = 'Focus on improving attendance and lab work.';
        }
        
        remarks += `
            <p style="margin-bottom: 16px;">
                <strong>To Pass (75):</strong><br>
                ❌ Impossible. You need ${pointsNeeded} more points, but the Prelim Exam can only 
                give you 30 points maximum (100 × 30%). ${advice}
            </p>
        `;
    } else if (requiredForPassing <= 0) {
        remarks += `
            <p style="margin-bottom: 16px;">
                <strong>To Pass (75):</strong><br>
                ✓ You're already guaranteed to pass! Your ${pointsFromClassStanding} points are already 
                above 75. Even with 0 on the Prelim Exam, you'll pass!
            </p>
        `;
    } else {
        const pointsNeeded = (PASSING_GRADE - pointsFromClassStanding).toFixed(2);
        remarks += `
            <p style="margin-bottom: 16px;">
                <strong>To Pass (75):</strong><br>
                You need <strong>${pointsNeeded} more points</strong> to reach 75.<br>
                Since the Prelim Exam is worth 30% of your grade, you need to score:<br>
                <strong>${requiredForPassing.toFixed(2)}/100</strong> on the Prelim Exam<br>
                <em>(${pointsNeeded} ÷ 0.30 = ${requiredForPassing.toFixed(2)})</em>
            </p>
        `;
    }
    
    // Excellent grade remarks
    if (requiredForExcellent > 100) {
        const pointsNeeded = (EXCELLENT_GRADE - pointsFromClassStanding).toFixed(2);
        remarks += `
            <p>
                <strong>For Excellent (100):</strong><br>
                ❌ Not achievable. You would need ${pointsNeeded} more points, but the Prelim Exam 
                can only provide 30 points maximum.
            </p>
        `;
    } else if (requiredForExcellent <= 0) {
        remarks += `
            <p>
                <strong>For Excellent (100):</strong><br>
                ⭐ Already guaranteed! You've achieved excellent standing!
            </p>
        `;
    } else {
        const pointsNeeded = (EXCELLENT_GRADE - pointsFromClassStanding).toFixed(2);
        remarks += `
            <p>
                <strong>For Excellent (100):</strong><br>
                You need <strong>${pointsNeeded} more points</strong> to reach 100.<br>
                You must score: <strong>${requiredForExcellent.toFixed(2)}/100</strong> on the Prelim Exam
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
    document.getElementById('excused').value = '0';
    document.getElementById('verified').checked = false;
    document.getElementById('verified').disabled = true;
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
