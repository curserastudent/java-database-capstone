package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    /**
     * Books a new appointment.
     *
     * @param appointment appointment to save
     * @return 1 if successful, 0 otherwise
     */
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Updates an existing appointment.
     */
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {

        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existing =
                appointmentRepository.findById(appointment.getId());

        if (existing.isEmpty()) {
            response.put("message", "Appointment not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Example validation
        // Replace with validateAppointment(...) if your project has one.
        appointmentRepository.save(appointment);

        response.put("message", "Appointment updated successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * Cancels an appointment.
     */
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(
            long id,
            String token) {

        Map<String, String> response = new HashMap<>();

        Optional<Appointment> optional =
                appointmentRepository.findById(id);

        if (optional.isEmpty()) {
            response.put("message", "Appointment not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Appointment appointment = optional.get();

        // Validate ownership using TokenService if required.
        // Example:
        // Long patientId = tokenService.extractPatientId(token);

        appointmentRepository.delete(appointment);

        response.put("message", "Appointment cancelled successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves appointments for a doctor on a given day.
     */
    public Map<String, Object> getAppointment(
            String pname,
            LocalDate date,
            String token) {

        Map<String, Object> result = new HashMap<>();

        // Example:
        // Long doctorId = tokenService.extractDoctorId(token);
        Long doctorId = tokenService.getUserIdFromToken(token);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Appointment> appointments;

        if (pname == null || pname.isBlank()) {
            appointments =
                    appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                            doctorId, start, end);
        } else {
            appointments =
                    appointmentRepository
                            .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                                    doctorId,
                                    pname,
                                    start,
                                    end);
        }

        result.put("appointments", appointments);
        return result;
    }

    /**
     * Changes the appointment status.
     */
    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(
            Long appointmentId,
            int status) {

        Map<String, String> response = new HashMap<>();

        Optional<Appointment> optional =
                appointmentRepository.findById(appointmentId);

        if (optional.isEmpty()) {
            response.put("message", "Appointment not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Appointment appointment = optional.get();
        appointment.setStatus(status);

        appointmentRepository.save(appointment);

        response.put("message", "Appointment status updated successfully.");
        return ResponseEntity.ok(response);
    }
}