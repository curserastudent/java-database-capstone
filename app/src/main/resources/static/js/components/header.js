/**
 * ==========================================================
 * File: header.js
 * Description:
 * Dynamically renders the application header based on the
 * current user's role and authentication status.
 * ==========================================================
 */

/**
 * Renders the application header.
 */
function renderHeader() {

    const headerDiv = document.getElementById("header");

    if (!headerDiv) {
        return;
    }

    /**
     * If the user is on the landing page,
     * clear the selected role and render only the logo.
     */
    if (window.location.pathname.endsWith("/") ||
        window.location.pathname.endsWith("/index.html")) {

        localStorage.removeItem("userRole");

        headerDiv.innerHTML = `
            <header class="header">
                <div class="logo-section">
                    <img
                        src="../assets/images/logo/Logo.png"
                        alt="Hospital CMS Logo"
                        class="logo-img">

                    <span class="logo-title">
                        Hospital CMS
                    </span>
                </div>
            </header>
        `;

        return;
    }

    /**
     * Retrieve session data.
     */
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    /**
     * Session validation.
     */
    if (
        (role === "loggedPatient" ||
            role === "admin" ||
            role === "doctor") &&
        !token
    ) {

        localStorage.removeItem("userRole");

        alert("Session expired or invalid login. Please log in again.");

        window.location.href = "/";

        return;
    }

    /**
     * Base header.
     */
    let headerContent = `
        <header class="header">

            <div class="logo-section">

                <img
                    src="../assets/images/logo/Logo.png"
                    alt="Hospital CMS Logo"
                    class="logo-img">

                <span class="logo-title">
                    Hospital CMS
                </span>

            </div>

            <nav class="header-nav">
    `;

    /**
     * Admin
     */
    if (role === "admin") {

        headerContent += `
            <button
                id="addDocBtn"
                class="adminBtn"
                onclick="openModal('addDoctor')">

                Add Doctor

            </button>

            <a href="#" onclick="logout()">
                Logout
            </a>
        `;
    }

    /**
     * Doctor
     */
    else if (role === "doctor") {

        headerContent += `
            <button
                id="doctorHome"
                class="adminBtn"
                onclick="selectRole('doctor')">

                Home

            </button>

            <a href="#" onclick="logout()">
                Logout
            </a>
        `;
    }

    /**
     * Guest Patient
     */
    else if (role === "patient") {

        headerContent += `
            <button
                id="patientLogin"
                class="adminBtn">

                Login

            </button>

            <button
                id="patientSignup"
                class="adminBtn">

                Sign Up

            </button>
        `;
    }

    /**
     * Logged Patient
     */
    else if (role === "loggedPatient") {

        headerContent += `
            <button
                id="home"
                class="adminBtn"
                onclick="window.location.href='/pages/loggedPatientDashboard.html'">

                Home

            </button>

            <button
                id="patientAppointments"
                class="adminBtn"
                onclick="window.location.href='/pages/patientAppointments.html'">

                Appointments

            </button>

            <a href="#" onclick="logoutPatient()">
                Logout
            </a>
        `;
    }

    /**
     * Default (Landing Page)
     */
    else {

        headerContent += `
            <button
                id="selectRoleBtn"
                class="adminBtn"
                onclick="window.location.href='../index.html'">

                Select Role

            </button>
        `;
    }

    /**
     * Close header.
     */
    headerContent += `
            </nav>
        </header>
    `;

    /**
     * Render header.
     */
    headerDiv.innerHTML = headerContent;

    /**
     * Register button events.
     */
    attachHeaderButtonListeners();
}

/**
 * Registers listeners for dynamically created buttons.
 */
function attachHeaderButtonListeners() {

    const patientLogin =
        document.getElementById("patientLogin");

    if (patientLogin) {

        patientLogin.addEventListener("click", () => {

            if (typeof openModal === "function") {
                openModal("patientLogin");
            }

        });
    }

    const patientSignup =
        document.getElementById("patientSignup");

    if (patientSignup) {

        patientSignup.addEventListener("click", () => {

            if (typeof openModal === "function") {
                openModal("patientSignup");
            }

        });
    }

    const addDoctor =
        document.getElementById("addDocBtn");

    if (addDoctor) {

        addDoctor.addEventListener("click", () => {

            if (typeof openModal === "function") {
                openModal("addDoctor");
            }

        });
    }
}

/**
 * Logs out an administrator or doctor.
 */
function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("userRole");

    window.location.href = "/";
}

/**
 * Logs out a patient.
 */
function logoutPatient() {

    localStorage.removeItem("token");
    localStorage.removeItem("userRole");

    window.location.href = "/pages/index.html";
}

/**
 * Make functions globally accessible.
 */
window.renderHeader = renderHeader;
window.logout = logout;
window.logoutPatient = logoutPatient;

/**
 * Render the header immediately.
 */
document.addEventListener("DOMContentLoaded", renderHeader);