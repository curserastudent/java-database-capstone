/**
 * ==========================================================
 * File: doctorCard.js
 * Description:
 * Creates a doctor card dynamically based on the user's role.
 * ==========================================================
 */

/**
 * Creates a doctor card.
 *
 * @param {Object} doctor - Doctor information.
 * @param {string} doctor.name
 * @param {string} doctor.specialty
 * @param {string} doctor.email
 * @param {Array<string>} doctor.availability
 *
 * @returns {HTMLElement}
 */
async function createDoctorCard(doctor) {

    /**
     * Create the main card container.
     */
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    /**
     * Get the current user role.
     */
    const role = localStorage.getItem("userRole");

    /**
     * Create the doctor information section.
     */
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    /**
     * Doctor name.
     */
    const name = document.createElement("h3");
    name.textContent = doctor.name;

    /**
     * Doctor specialty.
     */
    const specialty = document.createElement("p");
    specialty.innerHTML = `<strong>Specialty:</strong> ${doctor.specialty}`;

    /**
     * Doctor email.
     */
    const email = document.createElement("p");
    email.innerHTML = `<strong>Email:</strong> ${doctor.email}`;

    /**
     * Doctor availability.
     */
    const availability = document.createElement("p");
    availability.innerHTML = `
        <strong>Availability:</strong>
        ${doctor.availability.join(", ")}
    `;

    /**
     * Append doctor information.
     */
    infoDiv.appendChild(name);
    infoDiv.appendChild(specialty);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    /**
     * Create the actions container.
     */
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    /**
     * Admin actions.
     */
    if (role === "admin") {

        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";

        removeBtn.addEventListener("click", async () => {

            const confirmed = confirm(
                "Are you sure you want to delete this doctor?"
            );

            if (!confirmed) {
                return;
            }

            const token = localStorage.getItem("token");

            try {

                /**
                 * Replace with your API endpoint.
                 */
                const response = await fetch(`/api/doctors/${doctor.id}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error("Unable to delete doctor.");
                }

                /**
                 * Remove the card from the page.
                 */
                card.remove();

            } catch (error) {

                console.error(error);
                alert("Failed to delete doctor.");

            }

        });

        actionsDiv.appendChild(removeBtn);

    }

    /**
     * Guest patient actions.
     */
    else if (role === "patient") {

        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";

        bookNow.addEventListener("click", () => {

            alert("Patient needs to login first.");

        });

        actionsDiv.appendChild(bookNow);

    }

    /**
     * Logged patient actions.
     */
    else if (role === "loggedPatient") {

        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";

        bookNow.addEventListener("click", async (event) => {

            try {

                const token = localStorage.getItem("token");

                const patientData =
                    await getPatientData(token);

                showBookingOverlay(
                    event,
                    doctor,
                    patientData
                );

            } catch (error) {

                console.error(error);

                alert(
                    "Unable to retrieve patient information."
                );

            }

        });

        actionsDiv.appendChild(bookNow);

    }

    /**
     * Assemble the card.
     */
    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    /**
     * Return the completed card.
     */
    return card;

}
