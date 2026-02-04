/**
 * Student Record System - JavaScript Version
 * Programmer: [Your Full Name] - [Your Student ID]
 * Date: February 4, 2026
 * 
 * This script demonstrates DOM manipulation, array operations, and template literals
 * Features: Parse CSV data, Add/Delete records, Dynamic rendering
 */

// Hardcoded CSV data as a multi-line string
const csvData = `StudentID,first_name,last_name,LAB WORK 1,LAB WORK 2,LAB WORK 3,PRELIM EXAM,ATTENDANCE GRADE
073900438,Osbourne,Wakenshaw,69,5,52,12,78
114924014,Albie,Gierardi,58,92,16,57,97
111901632,Eleen,Pentony,43,81,34,36,16
084000084,Arie,Okenden,31,5,14,39,99
272471551,Alica,Muckley,49,66,97,3,95
104900721,Jo,Burleton,98,94,33,13,29
111924392,Cam,Akram,44,84,17,16,24
292970744,Celine,Brosoli,3,15,71,83,45
107004352,Alan,Belfit,31,51,36,70,48
071108313,Jeanette,Gilvear,4,78,15,69,69
042204932,Ethelin,MacCathay,48,36,23,1,11
111914218,Kakalina,Finnick,69,5,65,10,8
074906059,Mayer,Lorenzetti,36,30,100,41,92
091000080,Selia,Rosenstengel,15,42,85,68,28
055002480,Dalia,Tadd,84,86,13,91,22
063101111,Darryl,Doogood,36,3,78,13,100
071908827,Brier,Wace,69,92,23,75,40
322285668,Bucky,Udall,97,63,19,46,28
103006406,Haslett,Beaford,41,32,85,60,61
104913048,Shelley,Spring,84,73,63,59,3
051403517,Marius,Southway,28,75,29,88,92
021301869,Katharina,Storch,6,61,6,49,56
063115178,Hester,Menendez,70,46,73,40,56
084202442,Shaylynn,Scorthorne,50,80,81,96,83
275079882,Madonna,Willatt,23,12,17,83,5
071001041,Bancroft,Padfield,50,100,58,13,14
261170740,Rici,Everard,51,15,48,99,41
113105478,Lishe,Dashkovich,9,23,48,63,95
267089712,Alexandrina,Abate,34,54,79,44,71
041002203,Jordon,Ribbens,41,42,24,60,21
021308176,Jennette,Andrassy,63,13,100,67,4
122239937,Hamid,Chapell,90,92,44,43,47
021109935,Cordy,Crosetto,16,10,99,32,57
111026041,Tiphanie,Gwin,34,45,88,87,27
072408708,Leanor,Izachik,95,35,88,9,75
221370030,Lissy,Tuffley,90,30,84,60,86
104900048,Myrta,Mathieson,88,80,16,6,48
111311413,Cynthea,Fowles,82,59,13,97,23
091408695,Zacherie,Branch,67,6,8,78,10
231372183,Britney,Blackesland,78,79,36,23,83
263190634,Theda,Menco,50,13,7,11,8
021606580,Carin,Schrader,77,32,25,56,53
074902341,Shawn,Moston,64,91,6,95,21
107006088,Virge,Sinnat,20,1,78,44,92
091807254,Alano,Jotcham,66,35,99,48,83
011601029,Pietra,Roy,35,34,75,39,98
122240010,Orren,Danihelka,51,36,17,59,32
091400046,Angie,Grindell,51,54,55,59,61
071001630,Vachel,Swancock,41,31,88,24,24
061020977,Zane,Bellie,88,92,92,52,46`;

// Array to store student objects
let students = [];

/**
 * Parse CSV string into array of objects
 */
function parseCSV(csvString) {
    const lines = csvString.trim().split('\n');
    const headers = lines[0].split(',');
    
    const data = [];
    
    for (let i = 1; i < lines.length; i++) {
        const values = lines[i].split(',');
        
        const student = {
            studentId: values[0],
            firstName: values[1],
            lastName: values[2],
            lab1: values[3],
            lab2: values[4],
            lab3: values[5],
            prelim: values[6],
            attendance: values[7]
        };
        
        data.push(student);
    }
    
    return data;
}

/**
 * Render the table with current student data
 */
function render() {
    const tableBody = document.getElementById('tableBody');
    
    // Clear existing rows
    tableBody.innerHTML = '';
    
    // Generate rows using template literals
    students.forEach((student, index) => {
        const row = `
            <tr>
                <td>${student.studentId}</td>
                <td>${student.firstName}</td>
                <td>${student.lastName}</td>
                <td>${student.lab1}</td>
                <td>${student.lab2}</td>
                <td>${student.lab3}</td>
                <td>${student.prelim}</td>
                <td>${student.attendance}</td>
                <td>
                    <button class="btn-delete" onclick="deleteStudent(${index})">Delete</button>
                </td>
            </tr>
        `;
        
        tableBody.innerHTML += row;
    });
    
    // Update record count
    document.getElementById('recordCount').textContent = `Total Records: ${students.length}`;
}

/**
 * Add a new student to the array
 */
function addStudent() {
    // Get form values
    const studentId = document.getElementById('studentId').value.trim();
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();
    const lab1 = document.getElementById('lab1').value.trim();
    const lab2 = document.getElementById('lab2').value.trim();
    const lab3 = document.getElementById('lab3').value.trim();
    const prelim = document.getElementById('prelim').value.trim();
    const attendance = document.getElementById('attendance').value.trim();
    
    // Validation
    if (!studentId || !firstName || !lastName || !lab1 || !lab2 || !lab3 || !prelim || !attendance) {
        alert('All fields are required!');
        return;
    }
    
    // Validate grades are numbers between 0-100
    const grades = [lab1, lab2, lab3, prelim, attendance];
    for (let grade of grades) {
        const num = parseInt(grade);
        if (isNaN(num) || num < 0 || num > 100) {
            alert('Grades must be numbers between 0 and 100!');
            return;
        }
    }
    
    // Create new student object
    const newStudent = {
        studentId,
        firstName,
        lastName,
        lab1,
        lab2,
        lab3,
        prelim,
        attendance
    };
    
    // Add to array
    students.push(newStudent);
    
    // Clear form
    document.getElementById('studentId').value = '';
    document.getElementById('firstName').value = '';
    document.getElementById('lastName').value = '';
    document.getElementById('lab1').value = '';
    document.getElementById('lab2').value = '';
    document.getElementById('lab3').value = '';
    document.getElementById('prelim').value = '';
    document.getElementById('attendance').value = '';
    
    // Re-render table
    render();
    
    alert('Student added successfully!');
}

/**
 * Delete a student from the array by index
 */
function deleteStudent(index) {
    if (confirm('Are you sure you want to delete this record?')) {
        // Remove from array
        students.splice(index, 1);
        
        // Re-render table
        render();
        
        alert('Student deleted successfully!');
    }
}

/**
 * Initialize the application on page load
 */
document.addEventListener('DOMContentLoaded', function() {
    // Parse CSV data into students array
    students = parseCSV(csvData);
    
    // Initial render
    render();
    
    console.log('Student Record System loaded successfully!');
    console.log(`Total students loaded: ${students.length}`);
});
