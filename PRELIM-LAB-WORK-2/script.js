// Predefined login credentials
const validUsername = "admin";
const validPassword = "password123";

// Create beep sound using Web Audio API (no external file needed)
function playBeep() {
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    oscillator.frequency.value = 800;
    oscillator.type = 'sine';
    
    gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
    
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.5);
}

// Get current timestamp
function getCurrentTimestamp() {
    const now = new Date();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const year = now.getFullYear();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    
    return `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
}

// Handle form submission
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageDiv = document.getElementById('message');
    const timestampSection = document.getElementById('timestampSection');
    
    // Validate credentials
    if (username === validUsername && password === validPassword) {
        // Successful login
        messageDiv.style.display = 'none';
        timestampSection.style.display = 'block';
        
        // Display timestamp
        const timestamp = getCurrentTimestamp();
        document.getElementById('timestamp').textContent = timestamp;
        document.getElementById('welcomeName').textContent = username;
        
        // Store login data for attendance
        const loginData = {
            username: username,
            timestamp: timestamp
        };
        
        // Setup download button
        document.getElementById('downloadBtn').onclick = function() {
            generateAttendanceFile(loginData);
        };
        
    } else {
        // Failed login - play beep and show error
        playBeep();
        
        messageDiv.textContent = '❌ Invalid username or password!';
        messageDiv.className = 'message error';
        messageDiv.style.display = 'block';
        timestampSection.style.display = 'none';
        
        // Clear password field
        document.getElementById('password').value = '';
    }
});

// Generate and download attendance summary
function generateAttendanceFile(loginData) {
    const attendanceData = `
=====================================
    ATTENDANCE SUMMARY
=====================================

Username: ${loginData.username}
Login Time: ${loginData.timestamp}
Status: Present

=====================================
Generated on: ${getCurrentTimestamp()}
=====================================
    `.trim();
    
    // Create blob and download
    const blob = new Blob([attendanceData], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = 'attendance_summary.txt';
    link.click();
    
    // Clean up
    window.URL.revokeObjectURL(link.href);
}