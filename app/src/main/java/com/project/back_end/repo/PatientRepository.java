package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds a patient by email.
     *
     * @param email the patient's email address
     * @return the matching Patient, or null if not found
     */
    Patient findByEmail(String email);

    /**
     * Finds a patient by email or phone number.
     *
     * @param email the patient's email address
     * @param phone the patient's phone number
     * @return the matching Patient, or null if not found
     */
    Patient findByEmailOrPhone(String email, String phone);

}