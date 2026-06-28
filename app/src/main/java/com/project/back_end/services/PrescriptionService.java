package com.project.back_end.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.model.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Saves a prescription.
     *
     * @param prescription the prescription to save
     * @return response indicating the result
     */
    public ResponseEntity<Map<String, String>> savePrescription(
            Prescription prescription) {

        Map<String, String> response = new HashMap<>();

        try {

            List<Prescription> prescriptions =
                    prescriptionRepository.findByAppointmentId(
                            prescription.getAppointmentId());

            if (!prescriptions.isEmpty()) {
                response.put("message", "Prescription already exists.");
                return ResponseEntity.badRequest().body(response);
            }

            prescriptionRepository.save(prescription);

            response.put("message", "Prescription saved.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();

            response.put("message", "Internal server error.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * Retrieves prescriptions associated with an appointment.
     *
     * @param appointmentId appointment ID
     * @return response containing prescriptions
     */
    public ResponseEntity<Map<String, Object>> getPrescription(
            Long appointmentId) {

        Map<String, Object> response = new HashMap<>();

        try {

            List<Prescription> prescriptions =
                    prescriptionRepository.findByAppointmentId(appointmentId);

            response.put("prescriptions", prescriptions);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();

            response.put("message", "Internal server error.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
