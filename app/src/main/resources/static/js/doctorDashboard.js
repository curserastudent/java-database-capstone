/**
 * ==========================================================
 * File: doctorAppointments.js
 * Description:
 * Handles the Doctor Appointments page.
 * - Loads appointments
 * - Filters by patient name
 * - Filters by date
 * ==========================================================
 */

import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

/**
 * ==========================================================
 * Global Variables
 * ==========================================================
 */

/**
 * Table body where appointments will be rendered.
 */
const patientTableBody = document.getElementById("patientTableBody");

/**
 * Today's date (YYYY-MM-DD).
 */
const today = new Date().toISOString().split("T")[0];

/**
 * Current selected date.
 */
let selectedDate = today;

/**
 * Authentication token.
 */
const token = localStorage.getItem("token");

/**
 * Patient search filter.
 */
let patientName = null;

/**
 * ==========================================================
 * Initialize Page
 * ==========================================================
 */

document.addEventListener("DOMContentLoaded", () => {

    /**
     * Set today's date in the date picker.
     */
    const datePicker = document.getElementById("datePicker");

    if (datePicker) {
        datePicker.value = today;
    }

    /**
     * Bind UI events.
     */
    initializeEvents();

    /**
     * Optional page rendering.
     */
    if (typeof renderContent === "function") {
        renderContent();
    }

    /**
     * Load today's appointments.
     */
    loadAppointments();

});

/**
 * ==========================================================
 * Event Listeners
 * ==========================================================
 */

function initializeEvents() {

    /**
     * Search bar.
     */
    const searchBar = document.getElementById("searchBar");

    if (searchBar) {

        searchBar.addEventListener("input", () => {

            const value = searchBar.value.trim();

            patientName = value === "" ? "null" : value;

            loadAppointments();

        });

    }

    /**
     * Today's appointments button.
     */
    const todayButton = document.getElementById("todayButton");

    if (todayButton) {

        todayButton.addEventListener("click", () => {

            selectedDate = today;

            const datePicker =
                document.getElementById("datePicker");

            if (datePicker) {
                datePicker.value = today;
            }

            loadAppointments();

        });

    }

    /**
     * Date picker.
     */
    const datePicker = document.getElementById("datePicker");

    if (datePicker) {

        datePicker.addEventListener("change", () => {

            selectedDate = datePicker.value;

            loadAppointments();

        });

    }

}

/**
 * ==========================================================
 * Load Appointments
 * ==========================================================
 */

async function loadAppointments() {

    try {

        /**
         * Fetch appointments from backend.
         */
        const appointments =
            await getAllAppointments(
                selectedDate,
                patientName,
                token
            );

        /**
         * Clear current table.
         */
        patientTableBody.innerHTML = "";

        /**
         * No appointments found.
         */
        if (!appointments || appointments.length === 0) {

            patientTableBody.innerHTML = `
                <tr>
                    <td colspan="100%" class="text-center">
                        No Appointments found for today.
                    </td>
                </tr>
            `;

            return;

        }

        /**
         * Render appointment rows.
         */
        appointments.forEach((appointment) => {

            const row = createPatientRow(appointment);

            patientTableBody.appendChild(row);

        });

    } catch (error) {

        console.error("loadAppointments()", error);

        patientTableBody.innerHTML = `
            <tr>
                <td colspan="100%" class="text-center">
                    Unable to load appointments.
                </td>
            </tr>
        `;

    }

}