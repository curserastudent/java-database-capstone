/**
 * ==========================================================
 * File: doctorServices.js
 * Description:
 * Service layer responsible for all Doctor API requests.
 * ==========================================================
 */

import { API_BASE_URL } from "../config/config.js";

/**
 * Doctor API Endpoint
 */
const DOCTOR_API = `${API_BASE_URL}/doctor`;

/**
 * ==========================================================
 * Get All Doctors
 * ==========================================================
 */

/**
 * Retrieves all doctors from the server.
 *
 * @returns {Promise<Array>}
 */
export async function getDoctors() {

    try {

        const response = await fetch(DOCTOR_API);

        if (!response.ok) {
            throw new Error("Unable to retrieve doctors.");
        }

        const doctors = await response.json();

        return doctors;

    } catch (error) {

        console.error("getDoctors()", error);

        return [];

    }

}

/**
 * ==========================================================
 * Delete Doctor
 * ==========================================================
 */

/**
 * Deletes a doctor.
 *
 * @param {number|string} id
 * @param {string} token
 * @returns {Promise<Object>}
 */
export async function deleteDoctor(id, token) {

    try {

        const response = await fetch(`${DOCTOR_API}/${id}`, {

            method: "DELETE",

            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }

        });

        const result = await response.json();

        return {
            success: response.ok,
            message: result.message || "Doctor deleted successfully."
        };

    } catch (error) {

        console.error("deleteDoctor()", error);

        return {
            success: false,
            message: "Unable to delete doctor."
        };

    }

}

/**
 * ==========================================================
 * Save Doctor
 * ==========================================================
 */

/**
 * Creates a new doctor.
 *
 * @param {Object} doctor
 * @param {string} token
 * @returns {Promise<Object>}
 */
export async function saveDoctor(doctor, token) {

    try {

        const response = await fetch(DOCTOR_API, {

            method: "POST",

            headers: {

                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`

            },

            body: JSON.stringify(doctor)

        });

        const result = await response.json();

        return {

            success: response.ok,
            message: result.message || "Doctor saved successfully."

        };

    } catch (error) {

        console.error("saveDoctor()", error);

        return {

            success: false,
            message: "Unable to save doctor."

        };

    }

}

/**
 * ==========================================================
 * Filter Doctors
 * ==========================================================
 */

/**
 * Filters doctors by name, time and specialty.
 *
 * @param {string|null} name
 * @param {string|null} time
 * @param {string|null} specialty
 *
 * @returns {Promise<Array>}
 */
export async function filterDoctors(
    name = "",
    time = "",
    specialty = ""
) {

    try {

        /**
         * Build query parameters dynamically.
         */
        const params = new URLSearchParams();

        if (name) {
            params.append("name", name);
        }

        if (time) {
            params.append("time", time);
        }

        if (specialty) {
            params.append("specialty", specialty);
        }

        const url = params.toString()
            ? `${DOCTOR_API}/filter?${params.toString()}`
            : DOCTOR_API;

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("Unable to filter doctors.");
        }

        const doctors = await response.json();

        return doctors;

    } catch (error) {

        console.error("filterDoctors()", error);

        alert("Unable to filter doctors.");

        return [];

    }

}