package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.DTO.Login;
import com.project.back_end.model.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    public DoctorController(DoctorService doctorService,
                            Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    /**
     * Returns the availability of a doctor for a given date.
     */
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable LocalDate date,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, user);

        if (validation != null) {
            return validation;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("availability",
                doctorService.getDoctorAvailability(doctorId, date));

        return ResponseEntity.ok(response);
    }

    /**
     * Returns all doctors.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {

        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorService.getDoctors());

        return ResponseEntity.ok(response);
    }

    /**
     * Adds a new doctor.
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "admin");

        if (validation != null) {
            return validation;
        }

        int result = doctorService.saveDoctor(doctor);

        Map<String, String> response = new HashMap<>();

        switch (result) {

            case 1:
                response.put("message", "Doctor added to db");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);

            case -1:
                response.put("message", "Doctor already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

            default:
                response.put("message", "Some internal error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Doctor login.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(
            @RequestBody Login login) {

        return doctorService.validateDoctor(login);
    }

    /**
     * Updates doctor information.
     */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "admin");

        if (validation != null) {
            return validation;
        }

        int result = doctorService.updateDoctor(doctor);

        Map<String, String> response = new HashMap<>();

        switch (result) {

            case 1:
                response.put("message", "Doctor updated");
                return ResponseEntity.ok(response);

            case -1:
                response.put("message", "Doctor not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

            default:
                response.put("message", "Some internal error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Deletes a doctor.
     */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "admin");

        if (validation != null) {
            return validation;
        }

        int result = doctorService.deleteDoctor(id);

        Map<String, String> response = new HashMap<>();

        switch (result) {

            case 1:
                response.put("message", "Doctor deleted successfully");
                return ResponseEntity.ok(response);

            case -1:
                response.put("message", "Doctor not found with id");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

            default:
                response.put("message", "Some internal error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Filters doctors by name, specialty and available time.
     */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        return ResponseEntity.ok(
                service.filterDoctor(name, speciality, time));
    }
}