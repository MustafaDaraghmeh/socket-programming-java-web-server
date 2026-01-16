/**
 * Main JavaScript for Java Web Server Demo
 * Adds interactivity and demonstrates client-side JavaScript
 */

// Initialize the application when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    initializeNavigation();
    initializeStats();
    highlightCurrentPage();
});

/**
 * Initialize navigation functionality
 */
function initializeNavigation() {
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // The href will be handled by the browser normally
            // This just adds smooth interaction
            this.style.opacity = '0.7';
            setTimeout(() => {
                this.style.opacity = '1';
            }, 200);
        });
    });
}

/**
 * Highlight the current page in navigation
 */
function highlightCurrentPage() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        
        if (currentPath === href || 
            (currentPath === '/' && href === '/') ||
            (href !== '/' && currentPath.includes(href))) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

/**
 * Initialize and animate statistics
 */
function initializeStats() {
    const statCards = document.querySelectorAll('.stat-card');
    
    if (statCards.length === 0) return;
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '0';
                entry.target.style.transform = 'translateY(20px)';
                
                setTimeout(() => {
                    entry.target.style.transition = 'all 0.6s ease';
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }, 100);
                
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });
    
    statCards.forEach(card => observer.observe(card));
}

/**
 * Utility function to fetch and display request information
 * Demonstrates how the server is processing your request
 */
function displayRequestInfo() {
    const info = {
        'User Agent': navigator.userAgent,
        'Page URL': window.location.href,
        'Page Title': document.title,
        'Local Time': new Date().toLocaleString(),
        'Server Response': 'Successfully served via Java WebServer'
    };
    
    console.log('=== Web Server Request Info ===');
    Object.entries(info).forEach(([key, value]) => {
        console.log(`${key}: ${value}`);
    });
    console.log('=== End Info ===');
}

/**
 * Log server statistics
 */
function logServerStats() {
    console.log('Java Web Server - Socket Programming Demo');
    console.log('=========================================');
    console.log('This page was served by:');
    console.log('  • ServerSocket: Listening for incoming connections');
    console.log('  • Socket: Connected to your browser');
    console.log('  • Thread: Processing your request independently');
    console.log('  • HTTP Protocol: Response sent with proper headers');
    console.log('=========================================');
}

// Log information when page loads
window.addEventListener('load', function() {
    displayRequestInfo();
    logServerStats();
});

/**
 * Add smooth scrolling for anchor links
 */
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        const href = this.getAttribute('href');
        if (href !== '#') {
            e.preventDefault();
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({ behavior: 'smooth' });
            }
        }
    });
});

/**
 * Example: Function to demonstrate server communication
 * Students can use this to understand request/response cycle
 */
function checkServerStatus() {
    console.log('Checking server status...');
    console.log('If you can see this and the page loaded, the server is running!');
    console.log('The server processed your HTTP request and sent this page via socket.');
}

// Make functions available in console for educational purposes
window.serverUtils = {
    displayRequestInfo,
    logServerStats,
    checkServerStatus
};

console.log('Server utilities available: window.serverUtils.displayRequestInfo(), logServerStats(), checkServerStatus()');
