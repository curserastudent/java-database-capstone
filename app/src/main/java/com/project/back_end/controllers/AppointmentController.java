package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.model.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    public AppointmentController(AppointmentService appointmentService,
                                 Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    /**
     * Retrieves appointments for a doctor on a given date.
     */
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable LocalDate date,
            @PathVariable String patientName,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "doctor");

        if (validation != null) {
            return validation;
        }

        return ResponseEntity.ok(
                appointmentService.getAppointment(patientName, date, token));
    }

    /**
     * Books a new appointment.
     */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "patient");

        if (validation != null) {
            return validation;
        }

        int appointmentValidation = service.validateAppointment(appointment);

        Map<String, String> response = new HashMap<>();

        switch (appointmentValidation) {

            case -1:
                response.put("message", "Doctor not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

            case 0:
                response.put("message", "Appointment time is unavailable.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

            default:
                int result = appointmentService.bookAppointment(appointment);

                if (result == 1) {
                    response.put("message", "Appointment booked successfully.");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }

                response.put("message", "Unable to book appointment.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Updates an existing appointment.
     */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "patient");

        if (validation != null) {
            return validation;
        }

        return appointmentService.updateAppointment(appointment);
    }

    /**
     * Cancels an appointment.
     */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation =
                service.validateToken(token, "patient");

        if (validation != null) {
            return validation;
        }

        return appointmentService.cancelAppointment(id, token);
    }
}