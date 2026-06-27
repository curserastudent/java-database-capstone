/**
 * ==========================================================
 * File: index.js
 * Description:
 * Handles Admin and Doctor authentication from the
 * application's landing page.
 * ==========================================================
 */

import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { selectRole } from "../render.js";

/**
 * API Endpoints
 */
const ADMIN_API = `${API_BASE_URL}/admin`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

/**
 * Register button events after the page loads.
 */
window.onload = function () {

    /**
     * Admin Login Button
     */
    const adminBtn = document.getElementById("adminLogin");

    if (adminBtn) {

        adminBtn.addEventListener("click", () => {

            openModal("adminLogin");

        });

    }

    /**
     * Doctor Login Button
     */
    const doctorBtn = document.getElementById("doctorLogin");

    if (doctorBtn) {

        doctorBtn.addEventListener("click", () => {

            openModal("doctorLogin");

        });

    }

};

/**
 * Handles administrator authentication.
 */
async function adminLoginHandler() {

    const username =
        document.getElementById("adminUsername").value.trim();

    const password =
        document.getElementById("adminPassword").value;

    const admin = {
        username,
        password
    };

    try {

        const response = await fetch(ADMIN_API, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(admin)

        });

        if (!response.ok) {

            alert("Invalid credentials!");
            return;

        }

        const data = await response.json();

        localStorage.setItem("token", data.token);

        selectRole("admin");

    } catch (error) {

        console.error(error);

        alert("An unexpected error occurred.");

    }

}

/**
 * Handles doctor authentication.
 */
async function doctorLoginHandler() {

    const email =
        document.getElementById("doctorEmail").value.trim();

    const password =
        document.getElementById("doctorPassword").value;

    const doctor = {
        email,
        password
    };

    try {

        const response = await fetch(DOCTOR_API, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(doctor)

        });

        if (!response.ok) {

            alert("Invalid credentials!");
            return;

        }

        const data = await response.json();

        localStorage.setItem("token", data.token);

        selectRole("doctor");

    } catch (error) {

        console.error(error);

        alert("An unexpected error occurred.");

    }

}

/**
 * Expose handlers globally so they can be invoked
 * from modal buttons.
 */
window.adminLoginHandler = adminLoginHandler;
window.doctorLoginHandler = doctorLoginHandler;