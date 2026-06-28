package com.project.back_end.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /**
     * Creates a new patient.
     */
    public int createPatient(Patient patient) {

        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns all appointments for a patient.
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            Long id,
            String token) {

        Map<String, Object> response = new HashMap<>();

        try {

            String email = tokenService.extractIdentifier(token);

            Patient patient = patientRepository.findByEmail(email);

            if (patient == null || !patient.getId().equals(id)) {
                response.put("message", "Unauthorized.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            List<AppointmentDTO> appointments =
                    appointmentRepository.findByPatientId(id)
                            .stream()
                            .map(this::toDTO)
                            .collect(Collectors.toList());

            response.put("appointments", appointments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Filters appointments by condition.
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByCondition(
            String condition,
            Long id) {

        Map<String, Object> response = new HashMap<>();

        try {

            int status;

            if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else {
                response.put("message", "Invalid condition.");
                return ResponseEntity.badRequest().body(response);
            }

            List<AppointmentDTO> appointments =
                    appointmentRepository
                            .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status)
                            .stream()
                            .map(this::toDTO)
                            .collect(Collectors.toList());

            response.put("appointments", appointments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Filters appointments by doctor.
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctor(
            String name,
            Long patientId) {

        Map<String, Object> response = new HashMap<>();

        try {

            List<AppointmentDTO> appointments =
                    appointmentRepository
                            .filterByDoctorNameAndPatientId(name, patientId)
                            .stream()
                            .map(this::toDTO)
                            .collect(Collectors.toList());

            response.put("appointments", appointments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Filters appointments by doctor and condition.
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(
            String condition,
            String name,
            long patientId) {

        Map<String, Object> response = new HashMap<>();

        try {

            int status;

            if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else {
                response.put("message", "Invalid condition.");
                return ResponseEntity.badRequest().body(response);
            }

            List<AppointmentDTO> appointments =
                    appointmentRepository
                            .filterByDoctorNameAndPatientIdAndStatus(
                                    name,
                                    patientId,
                                    status)
                            .stream()
                            .map(this::toDTO)
                            .collect(Collectors.toList());

            response.put("appointments", appointments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Returns patient details.
     */
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {

        Map<String, Object> response = new HashMap<>();

        try {

            String email = tokenService.extractIdentifier(token);

            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                response.put("message", "Patient not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("patient", patient);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Converts Appointment to AppointmentDTO.
     */
    private AppointmentDTO toDTO(Appointment appointment) {

        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getPatient().getEmail(),
                appointment.getPatient().getPhone(),
                appointment.getPatient().getAddress(),
                appointment.getAppointmentTime(),
                appointment.getStatus());
    }
}