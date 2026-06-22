## Admin User Stories

## User Story 1
**Title:**
*As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely.*
**Acceptance Criteria:**
1. The admin can enter a valid username and password.
2. Successful authentication grants access to the admin dashboard.
3. Invalid credentials display an error message.
**Priority:** High
**Story Points:** 3
**Notes:**
* Passwords should be stored securely.
* Account access should require valid credentials.

---

## User Story 2
**Title:**
*As an admin, I want to log out of the portal, so that I can protect system access.*
**Acceptance Criteria:**
1. The admin can click a logout option.
2. The current session is terminated.
3. Protected pages cannot be accessed after logout.
**Priority:** High
**Story Points:** 2
**Notes:**
* Redirect the user to the login page after logout.

---

## User Story 3
**Title:**
*As an admin, I want to add doctors to the portal, so that they can provide services to patients.*
**Acceptance Criteria:**
1. The admin can enter doctor information.
2. Required fields are validated before saving.
3. The doctor profile is successfully created.
**Priority:** High
**Story Points:** 5
**Notes:**
* Doctor information should include name, specialization, and contact details.

---

## User Story 4
**Title:**
*As an admin, I want to delete a doctor's profile from the portal, so that I can maintain accurate records.*
**Acceptance Criteria:**
1. The admin can select a doctor profile.
2. The system requests confirmation before deletion.
3. The doctor profile is permanently removed.
**Priority:** Medium
**Story Points:** 3
**Notes:**
* Prevent accidental deletions with a confirmation dialog.

---

## User Story 5
**Title:**
*As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics.*
**Acceptance Criteria:**
1. The stored procedure executes successfully.
2. Monthly appointment counts are returned.
3. Results can be reviewed by the admin.
**Priority:** Medium
**Story Points:** 3
**Notes:**
* The procedure should aggregate appointments by month.

## Patient User Stories

## User Story 1
**Title:**
*As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.*
**Acceptance Criteria:**
1. Patients can access the doctor list without authentication.
2. Doctor names and specializations are displayed.
3. The list is available to all visitors.
**Priority:** Medium
**Story Points:** 2
**Notes:**
* No personal information should be displayed.

---

## User Story 2
**Title:**
*As a patient, I want to sign up using my email and password, so that I can book appointments.*
**Acceptance Criteria:**
1. Patients can enter their email and password.
2. Required fields are validated.
3. A new account is created successfully.
**Priority:** High
**Story Points:** 3
**Notes:**
* Email addresses should be unique.

---

## User Story 3
**Title:**
*As a patient, I want to log into the portal, so that I can manage my bookings.*
**Acceptance Criteria:**
1. Patients can provide valid credentials.
2. Successful authentication grants access to their account.
3. Invalid credentials display an error message.
**Priority:** High
**Story Points:** 3
**Notes:**
* Sessions should expire after inactivity.

---

## User Story 4
**Title:**
*As a patient, I want to log out of the portal, so that I can secure my account.*
**Acceptance Criteria:**
1. Patients can select the logout option.
2. The active session is terminated.
3. Protected pages are inaccessible after logout.
**Priority:** High
**Story Points:** 2
**Notes:**
* Redirect users to the login page after logout.

---

## User Story 5
**Title:**
*As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor.*
**Acceptance Criteria:**
1. Patients can select an available time slot.
2. The appointment duration is one hour.
3. A confirmation message is displayed after booking.
**Priority:** High
**Story Points:** 5
**Notes:**
* Double booking should not be allowed.

---

## User Story 6
**Title:**
*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*
**Acceptance Criteria:**
1. Patients can access a list of upcoming appointments.
2. Appointment date and time are displayed.
3. Past appointments are excluded from the list.
**Priority:** Medium
**Story Points:** 3
**Notes:**
* Appointments should be ordered chronologically.

## Doctor User Stories

## User Story 1
**Title:**
*As a doctor, I want to log into the portal, so that I can manage my appointments.*
**Acceptance Criteria:**
1. Doctors can enter valid credentials.
2. Successful authentication grants access to the portal.
3. Invalid credentials display an error message.
**Priority:** High
**Story Points:** 3
**Notes:**
* Access should be restricted to authorized users.

---

## User Story 2
**Title:**
*As a doctor, I want to log out of the portal, so that I can protect my data.*
**Acceptance Criteria:**
1. Doctors can click the logout option.
2. The current session is terminated.
3. Protected pages are inaccessible after logout.
**Priority:** High
**Story Points:** 2
**Notes:**
* Users should be redirected to the login page.

---

## User Story 3
**Title:**
*As a doctor, I want to view my appointment calendar, so that I can stay organized.*
**Acceptance Criteria:**
1. Doctors can access their calendar.
2. Scheduled appointments are displayed.
3. Appointments are shown by date and time.
**Priority:** High
**Story Points:** 5
**Notes:**
* Calendar entries should be sorted chronologically.

---

## User Story 4
**Title:**
*As a doctor, I want to mark my unavailability, so that patients only see available slots.*
**Acceptance Criteria:**
1. Doctors can specify unavailable dates and times.
2. Unavailable slots are hidden from patients.
3. Changes are reflected immediately.
**Priority:** High
**Story Points:** 5
**Notes:**
* Existing appointments should not be affected.

---

## User Story 5
**Title:**
*As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information.*
**Acceptance Criteria:**
1. Doctors can edit profile information.
2. Changes are validated before saving.
3. Updated information is visible to patients.
**Priority:** Medium
**Story Points:** 3
**Notes:**
* Contact information should follow the required format.

---

## User Story 6
**Title:**
*As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared.*
**Acceptance Criteria:**
1. Doctors can access patient information for scheduled appointments.
2. Relevant details are displayed correctly.
3. Information is available only for upcoming appointments.
**Priority:** High
**Story Points:** 3
**Notes:**
* Access to patient data must comply with privacy requirements.
