// patientDashboard.js

import { createDoctorCard } from "./components/doctorCard.js";
import { openModal, closeModal } from "./components/modals.js";
import {
    getDoctors,
    filterDoctors
} from "./services/doctorServices.js";

import {
    patientSignup,
    patientLogin
} from "./services/patientServices.js";

/**
 * Initialize page
 */
document.addEventListener("DOMContentLoaded", () => {

    initializeEventListeners();

    loadDoctorCards();

});

/**
 * Register all page event listeners
 */
function initializeEventListeners() {

    const signupBtn = document.getElementById("patientSignup");
    if (signupBtn) {
        signupBtn.addEventListener("click", () => {
            openModal("patientSignup");
        });
    }

    const loginBtn = document.getElementById("patientLogin");
    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
            openModal("patientLogin");
        });
    }

    const searchBar = document.getElementById("searchBar");
    if (searchBar) {
        searchBar.addEventListener("input", filterDoctorsOnChange);
    }

    const filterTime = document.getElementById("filterTime");
    if (filterTime) {
        filterTime.addEventListener("change", filterDoctorsOnChange);
    }

    const filterSpecialty = document.getElementById("filterSpecialty");
    if (filterSpecialty) {
        filterSpecialty.addEventListener("change", filterDoctorsOnChange);
    }

}

/**
 * Load all doctors
 */
async function loadDoctorCards() {

    const contentDiv = document.getElementById("content");

    if (!contentDiv) return;

    contentDiv.innerHTML = "";

    try {

        const doctors = await getDoctors();

        renderDoctorCards(doctors);

    } catch (error) {

        console.error("Failed to load doctors:", error);

        contentDiv.innerHTML =
            "<p>Unable to load doctors at this time.</p>";

    }

}

/**
 * Render doctor cards
 */
function renderDoctorCards(doctors) {

    const contentDiv = document.getElementById("content");

    if (!contentDiv) return;

    contentDiv.innerHTML = "";

    if (!doctors || doctors.length === 0) {

        contentDiv.innerHTML =
            "<p>No doctors found with the given filters.</p>";

        return;

    }

    doctors.forEach(doctor => {

        const card = createDoctorCard(doctor);

        contentDiv.appendChild(card);

    });

}

/**
 * Search and filter doctors
 */
async function filterDoctorsOnChange() {

    const name =
        document.getElementById("searchBar")?.value.trim() || "";

    const time =
        document.getElementById("filterTime")?.value || "";

    const specialty =
        document.getElementById("filterSpecialty")?.value || "";

    try {

        const response = await filterDoctors(
            name || null,
            time || null,
            specialty || null
        );

        // Compatible with both:
        // return doctors
        // return { doctors: [...] }

        const doctors = response.doctors ?? response;

        renderDoctorCards(doctors);

    } catch (error) {

        console.error("Failed to filter doctors:", error);

        const contentDiv = document.getElementById("content");

        if (contentDiv) {

            contentDiv.innerHTML =
                "<p>No doctors found with the given filters.</p>";

        }

    }

}

/**
 * Patient Signup
 */
window.signupPatient = async function () {

    try {

        const data = {

            name: document.getElementById("name").value.trim(),

            email: document.getElementById("email").value.trim(),

            password: document.getElementById("password").value,

            phone: document.getElementById("phone").value.trim(),

            address: document.getElementById("address").value.trim()

        };

        const { success, message } =
            await patientSignup(data);

        if (success) {

            alert(message);

            if (typeof closeModal === "function") {
                closeModal();
            } else {
                document.getElementById("modal").style.display = "none";
            }

            window.location.reload();

        } else {

            alert(message);

        }

    } catch (error) {

        console.error("Signup failed:", error);

        alert("An error occurred while signing up.");

    }

};

/**
 * Patient Login
 */
window.loginPatient = async function () {

    try {

        const credentials = {

            email: document.getElementById("email").value.trim(),

            password: document.getElementById("password").value

        };

        const response = await patientLogin(credentials);

        if (response.ok) {

            const result = await response.json();

            localStorage.setItem("token", result.token);

            localStorage.setItem(
                "userRole",
                "loggedPatient"
            );

            window.location.href =
                "/pages/loggedPatientDashboard.html";

        } else {

            const error =
                await response.json().catch(() => ({}));

            alert(
                error.message ||
                "Invalid email or password."
            );

        }

    } catch (error) {

        console.error("Login failed:", error);

        alert("Unable to login. Please try again.");

    }

};

export {
    loadDoctorCards,
    renderDoctorCards
};