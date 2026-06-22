## MySQL Database Design

### Table: patients
* id: INT, Primary Key, AUTO_INCREMENT
* first_name: VARCHAR(100), NOT NULL
* last_name: VARCHAR(100), NOT NULL
* email: VARCHAR(255), NOT NULL, UNIQUE
* phone: VARCHAR(20), NOT NULL
* date_of_birth: DATE, NOT NULL
* password: VARCHAR(255), NOT NULL
* created_at: DATETIME, NOT NULL
* updated_at: DATETIME, NOT NULL

### Comments
* Email addresses must be unique.
* Email and phone number formats should be validated in application code.
* Patient records should not be deleted frequently because appointment history should be preserved.

---

### Table: doctors
* id: INT, Primary Key, AUTO_INCREMENT
* first_name: VARCHAR(100), NOT NULL
* last_name: VARCHAR(100), NOT NULL
* specialization: VARCHAR(100), NOT NULL
* email: VARCHAR(255), NOT NULL, UNIQUE
* phone: VARCHAR(20), NOT NULL
* password: VARCHAR(255), NOT NULL
* status: TINYINT, NOT NULL DEFAULT 1

  * 1 = Active
  * 0 = Inactive
* created_at: DATETIME, NOT NULL
* updated_at: DATETIME, NOT NULL

### Comments
* Doctors should remain in the system even if they stop practicing, preserving historical data.
* Email addresses should be unique.

---

### Table: admin
* id: INT, Primary Key, AUTO_INCREMENT
* username: VARCHAR(100), NOT NULL, UNIQUE
* email: VARCHAR(255), NOT NULL, UNIQUE
* password: VARCHAR(255), NOT NULL
* created_at: DATETIME, NOT NULL
* updated_at: DATETIME, NOT NULL

### Comments
* Usernames and email addresses must be unique.
* Passwords should be stored as hashes.

---

### Table: appointments
* id: INT, Primary Key, AUTO_INCREMENT
* doctor_id: INT, NOT NULL, Foreign Key → doctors(id)
* patient_id: INT, NOT NULL, Foreign Key → patients(id)
* appointment_time: DATETIME, NOT NULL
* duration_minutes: INT, NOT NULL DEFAULT 60
* status: TINYINT, NOT NULL DEFAULT 0

  * 0 = Scheduled
  * 1 = Completed
  * 2 = Cancelled
* notes: TEXT
* created_at: DATETIME, NOT NULL
* updated_at: DATETIME, NOT NULL

### Comments
* Appointment records should never be deleted automatically.
* Historical appointments should be retained permanently.
* A doctor should not have overlapping appointments.
* Overlapping appointments should be prevented through application logic.

---

### Table: doctor_availability
* id: INT, Primary Key, AUTO_INCREMENT
* doctor_id: INT, NOT NULL, Foreign Key → doctors(id)
* available_date: DATE, NOT NULL
* start_time: TIME, NOT NULL
* end_time: TIME, NOT NULL
* created_at: DATETIME, NOT NULL

### Comments
* Each doctor manages their own available time slots.
* Patients can only book appointments within available hours.
* Time ranges should be validated to avoid overlaps.

---

### Table: prescriptions
* id: INT, Primary Key, AUTO_INCREMENT
* appointment_id: INT, NOT NULL, Foreign Key → appointments(id)
* doctor_id: INT, NOT NULL, Foreign Key → doctors(id)
* patient_id: INT, NOT NULL, Foreign Key → patients(id)
* diagnosis: TEXT, NOT NULL
* medication: TEXT, NOT NULL
* instructions: TEXT
* created_at: DATETIME, NOT NULL

### Comments
* A prescription must always belong to a specific appointment.
* Prescriptions cannot exist independently.
* Medical history should be retained permanently.

---

### Table: clinic_locations
* id: INT, Primary Key, AUTO_INCREMENT
* name: VARCHAR(100), NOT NULL
* address: VARCHAR(255), NOT NULL
* phone: VARCHAR(20)
* city: VARCHAR(100), NOT NULL
* created_at: DATETIME, NOT NULL

### Comments
* Supports multiple clinic branches.
* Phone numbers should be validated in the application.

---

### Table: payments
* id: INT, Primary Key, AUTO_INCREMENT
* appointment_id: INT, NOT NULL, Foreign Key → appointments(id)
* amount: DECIMAL(10,2), NOT NULL
* payment_date: DATETIME, NOT NULL
* payment_method: VARCHAR(50), NOT NULL
* status: TINYINT, NOT NULL

  * 0 = Pending
  * 1 = Paid
  * 2 = Refunded

### Comments
* Each payment is associated with one appointment.
* Payment records should be preserved for auditing purposes.

---

# Referential Integrity Decisions

## Patient Deletion
* Patients should not be physically deleted.
* Historical appointment and prescription information must remain available.
* A soft delete mechanism (status field) is recommended.

## Doctor Deletion
* Doctors should not be deleted.
* Historical records should remain intact.
* Inactive doctors can be marked with status = 0.

## Appointment Overlap

* Doctors should not be allowed to have overlapping appointments.
* Validation should be implemented in application code before creating appointments.

## Appointment History
* Appointment history should be retained indefinitely.
* Completed and cancelled appointments should remain in the database.

## Prescription Relationship
* Prescriptions are tied to a specific appointment.
* They cannot exist independently.

## Future Enhancements
Possible additional tables:

### medical_records
Stores allergies, chronic conditions, and medical history.

### notifications
Stores email or SMS reminders sent to patients.

### reviews
Allows patients to rate doctors after appointments.

### audit_logs
Tracks important actions performed by administrators and users.



## MongoDB Collection Design

## Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "appointmentId": 51,
  "patientId": 12,
  "doctorId": 4,

  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500 mg",
      "frequency": "Every 6 hours",
      "durationDays": 5
    },
    {
      "name": "Ibuprofen",
      "dosage": "200 mg",
      "frequency": "Twice a day",
      "durationDays": 3
    }
  ],

  "diagnosis": "Seasonal flu",

  "doctorNotes": "Patient should rest and drink plenty of fluids. Return if symptoms persist for more than one week.",

  "tags": [
    "flu",
    "fever",
    "follow-up-required"
  ],

  "refillCount": 2,

  "attachments": [
    {
      "fileName": "blood_test_results.pdf",
      "fileType": "application/pdf",
      "fileUrl": "/documents/blood_test_results.pdf"
    },
    {
      "fileName": "xray_image.jpg",
      "fileType": "image/jpeg",
      "fileUrl": "/images/xray_image.jpg"
    }
  ],

  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  },

  "metadata": {
    "createdBy": "doctor",
    "version": 1,
    "lastUpdated": "2026-06-22T15:30:00Z"
  },

  "createdAt": "2026-06-22T15:00:00Z"
}
```

---

## Design Decisions

### Why use MongoDB for prescriptions?

* Doctor notes are free-form and may vary greatly between appointments.
* Prescriptions may contain different numbers of medications.
* File attachments and metadata are optional.
* New fields can be added later without altering the schema.

### References vs Embedded Documents

The document stores only:

* `appointmentId`
* `patientId`
* `doctorId`

instead of embedding the complete patient and doctor objects.

This avoids data duplication and ensures that changes to patient or doctor information are maintained in MySQL.

### Schema Evolution

The design supports future additions such as:

* Allergies
* Lab results
* Voice notes
* AI-generated recommendations
* Insurance information
* Prescription expiration dates

without requiring migration of existing documents.

---

## Alternative Collection: messages

MongoDB is also suitable for doctor-patient chat messages.

```json
{
  "_id": "ObjectId('64abc789012')",
  "conversationId": "conv_1001",
  "appointmentId": 51,
  "senderType": "patient",
  "senderId": 12,
  "receiverId": 4,

  "message": "Can I take the medication after meals?",

  "attachments": [
    {
      "fileName": "rash_photo.jpg",
      "fileType": "image/jpeg",
      "fileUrl": "/uploads/rash_photo.jpg"
    }
  ],

  "status": "read",

  "createdAt": "2026-06-22T16:45:00Z"
}
```

### Benefits of MongoDB for Messages

* Messages have flexible structures.
* Attachments are optional.
* Conversation history can grow without affecting relational tables.
* Additional fields such as reactions, edited messages, or voice notes can be added easily.
* Embedded arrays and nested documents simplify the storage of message metadata.

