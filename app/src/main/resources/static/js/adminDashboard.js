/**
 * ==========================================================
 * File: adminDashboard.js
 * Description:
 * Handles the Admin Dashboard functionality:
 *  - Load doctors
 *  - Search and filter doctors
 *  - Add new doctor
 * ==========================================================
 */

import { openModal } from "../components/modals.js";

import {
    getDoctors,
    filterDoctors,
    saveDoctor
} from "../services/doctorServices.js";

import { createDoctorCard } from "../components/doctorCard.js";

/**
 * ==========================================================
 * Page Initialization
 * ==========================================================
 */

document.addEventListener("DOMContentLoaded", () => {

    loadDoctorCards();

    initializeEventListeners();

});

/**
 * ==========================================================
 * Initialize Events
 * ==========================================================
 */

function initializeEventListeners() {

    /**
     * Add Doctor Button
     */
    const addDoctorButton = document.getElementById("addDocBtn");

    if (addDoctorButton) {

        addDoctorButton.addEventListener("click", () => {

            openModal("addDoctor");

        });

    }

    /**
     * Search Bar
     */
    const searchBar = document.getElementById("searchBar");

    if (searchBar) {

        searchBar.addEventListener(
            "input",
            filterDoctorsOnChange
        );

    }

    /**
     * Time Filter
     */
    const filterTime = document.getElementById("filterTime");

    if (filterTime) {

        filterTime.addEventListener(
            "change",
            filterDoctorsOnChange
        );

    }

    /**
     * Specialty Filter
     */
    const filterSpecialty =
        document.getElementById("filterSpecialty");

    if (filterSpecialty) {

        filterSpecialty.addEventListener(
            "change",
            filterDoctorsOnChange
        );

    }

    /**
     * Add Doctor Form
     */
    const doctorForm =
        document.getElementById("addDoctorForm");

    if (doctorForm) {

        doctorForm.addEventListener("submit", async (event) => {

            event.preventDefault();

            await adminAddDoctor();

        });

    }

}

/**
 * ==========================================================
 * Load Doctors
 * ==========================================================
 */

async function loadDoctorCards() {

    const doctors = await getDoctors();

    renderDoctorCards(doctors);

}

/**
 * ==========================================================
 * Render Doctor Cards
 * ==========================================================
 */

function renderDoctorCards(doctors) {

    const contentDiv = document.getElementById("content");

    contentDiv.innerHTML = "";

    if (!doctors || doctors.length === 0) {

        contentDiv.innerHTML =
            "<p>No doctors found.</p>";

        return;

    }

    doctors.forEach((doctor) => {

        const card = createDoctorCard(doctor);

        contentDiv.appendChild(card);

    });

}

/**
 * ==========================================================
 * Search / Filter Doctors
 * ==========================================================
 */

async function filterDoctorsOnChange() {

    const name =
        document.getElementById("searchBar")?.value || "";

    const time =
        document.getElementById("filterTime")?.value || "";

    const specialty =
        document.getElementById("filterSpecialty")?.value || "";

    const doctors =
        await filterDoctors(name, time, specialty);

    renderDoctorCards(doctors);

}

/**
 * ==========================================================
 * Add Doctor
 * ==========================================================
 */

async function adminAddDoctor() {

    /**
     * Verify admin token.
     */
    const token = localStorage.getItem("token");

    if (!token) {

        alert("Administrator session has expired.");

        return;

    }

    /**
     * Read form fields.
     */
    const name =
        document.getElementById("doctorName").value.trim();

    const specialty =
        document.getElementById("doctorSpecialty").value;

    const email =
        document.getElementById("doctorEmail").value.trim();

    const password =
        document.getElementById("doctorPassword").value;

    const mobile =
        document.getElementById("doctorMobile").value.trim();

    /**
     * Read availability checkboxes.
     */
    const availability = [];

    document
        .querySelectorAll(
            "input[name='availability']:checked"
        )
        .forEach((checkbox) => {

            availability.push(checkbox.value);

        });

    /**
     * Doctor object.
     */
    const doctor = {

        name,
        specialty,
        email,
        password,
        mobile,
        availability

    };

    /**
     * Save doctor.
     */
    const result = await saveDoctor(
        doctor,
        token
    );

    if (result.success) {

        alert(result.message);

        /**
         * Close modal.
         */
        document
            .getElementById("modalOverlay")
            ?.classList.remove("show");

        /**
         * Reload doctors.
         */
        await loadDoctorCards();

    } else {

        alert(result.message);

    }

}