package com.project.back_end.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DoctorService doctorService,
            PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    /**
     * Validates a JWT token.
     */
    public ResponseEntity<Map<String, String>> validateToken(
            String token,
            String user) {

        Map<String, String> response = new HashMap<>();

        if (!tokenService.validateToken(token, user)) {
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Validates admin login.
     */
    public ResponseEntity<Map<String, String>> validateAdmin(
            Admin receivedAdmin) {

        Map<String, String> response = new HashMap<>();

        try {

            Admin admin =
                    adminRepository.findByUsername(receivedAdmin.getUsername());

            if (admin == null ||
                !admin.getPassword().equals(receivedAdmin.getPassword())) {

                response.put("message", "Invalid username or password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            String token = tokenService.generateToken(admin.getUsername());

            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("message", "Internal server error.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * Filters doctors.
     */
    public Map<String, Object> filterDoctor(
            String name,
            String specialty,
            String time) {

        boolean hasName = name != null && !name.isBlank();
        boolean hasSpecialty = specialty != null && !specialty.isBlank();
        boolean hasTime = time != null && !time.isBlank();

        if (hasName && hasSpecialty && hasTime) {
            return doctorService.filterDoctorsByNameSpecilityandTime(
                    name,
                    specialty,
                    time);
        }

        if (hasName && hasSpecialty) {
            return doctorService.filterDoctorByNameAndSpecility(
                    name,
                    specialty);
        }

        if (hasName && hasTime) {
            return doctorService.filterDoctorByNameAndTime(
                    name,
                    time);
        }

        if (hasSpecialty && hasTime) {
            return doctorService.filterDoctorByTimeAndSpecility(
                    specialty,
                    time);
        }

        if (hasName) {
            return doctorService.findDoctorByName(name);
        }

        if (hasSpecialty) {
            return doctorService.filterDoctorBySpecility(specialty);
        }

        if (hasTime) {
            return doctorService.filterDoctorsByTime(time);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("doctors", doctorService.getDoctors());

        return map;
    }

    /**
     * Validates appointment availability.
     */
    public int validateAppointment(Appointment appointment) {

        Optional<Doctor> doctor =
                doctorRepository.findById(appointment.getDoctor().getId());

        if (doctor.isEmpty()) {
            return -1;
        }

        List<String> availableSlots =
                doctorService.getDoctorAvailability(
                        doctor.get().getId(),
                        appointment.getAppointmentDate());

        String requestedTime =
                appointment.getAppointmentTime()
                        .toLocalTime()
                        .toString();

        return availableSlots.contains(requestedTime) ? 1 : 0;
    }

    /**
     * Validates patient uniqueness.
     */
    public boolean validatePatient(Patient patient) {

        return patientRepository.findByEmailOrPhone(
                patient.getEmail(),
                patient.getPhone()) == null;
    }

    /**
     * Validates patient login.
     */
    public ResponseEntity<Map<String, String>> validatePatientLogin(
            Login login) {

        Map<String, String> response = new HashMap<>();

        try {

            Patient patient =
                    patientRepository.findByEmail(login.getIdentifier());

            if (patient == null ||
                !patient.getPassword().equals(login.getPassword())) {

                response.put("message", "Invalid email or password.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            String token = tokenService.generateToken(patient.getEmail());

            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("message", "Internal server error.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * Filters patient appointments.
     */
    public ResponseEntity<Map<String, Object>> filterPatient(
            String condition,
            String name,
            String token) {

        String email = tokenService.extractIdentifier(token);

        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Patient not found.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        boolean hasCondition =
                condition != null && !condition.isBlank();

        boolean hasName =
                name != null && !name.isBlank();

        if (hasCondition && hasName) {
            return patientService.filterByDoctorAndCondition(
                    condition,
                    name,
                    patient.getId());
        }

        if (hasCondition) {
            return patientService.filterByCondition(
                    condition,
                    patient.getId());
        }

        if (hasName) {
            return patientService.filterByDoctor(
                    name,
                    patient.getId());
        }

        return patientService.getPatientAppointment(
                patient.getId(),
                token);
    }

}