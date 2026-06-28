package com.project.back_end.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

import jakarta.transaction.Transactional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves the available appointment slots for a doctor on a given date.
     *
     * @param doctorId the doctor's ID
     * @param date the appointment date
     * @return list of available time slots
     */
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDateTime date) {

        LocalDateTime start = date.toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusNanos(1);

        // Fetch booked appointments for the doctor
        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        start,
                        end);

        // Retrieve doctor information
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null) {
            return new ArrayList<>();
        }

        /*
         * Replace this with the actual way your Doctor entity stores
         * available time slots.
         *
         * Example:
         * List<String> availableSlots = new ArrayList<>(doctor.getAvailability());
         */
        List<String> availableSlots = new ArrayList<>();

        // Remove booked slots
        for (Appointment appointment : appointments) {
            availableSlots.remove(
                    appointment.getAppointmentTime().toLocalTime().toString());
        }

        return availableSlots;
    }

    /**
     * Saves a new doctor.
     *
     * @param doctor the doctor to save
     * @return 1 if successful, -1 if the doctor already exists, 0 if an error occurs
     */
    @Transactional
    public int saveDoctor(Doctor doctor) {

        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Updates an existing doctor.
     *
     * @param doctor the doctor with updated information
     * @return 1 if successful, -1 if the doctor does not exist, 0 if an error occurs
     */
    @Transactional
    public int updateDoctor(Doctor doctor) {

        try {
            if (!doctorRepository.existsById(doctor.getId())) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retrieves all doctors.
     *
     * @return list of all doctors
     */
    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Deletes a doctor and all associated appointments.
     *
     * @param id the doctor's ID
     * @return 1 if successful, -1 if the doctor does not exist, 0 if an error occurs
     */
    @Transactional
    public int deleteDoctor(long id) {

        try {
            if (!doctorRepository.existsById(id)) {
                return -1;
            }

            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);

            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Validates a doctor's login credentials.
     *
     * @param login the login request
     * @return a response containing a JWT token or an error message
     */
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {

        Map<String, String> response = new HashMap<>();

        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());

        if (doctor == null) {
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (!doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateToken(doctor.getEmail());

        response.put("token", token);
        response.put("message", "Login successful.");

        return ResponseEntity.ok(response);
    }

    /**
     * Finds doctors by name.
     *
     * @param name doctor's name
     * @return map containing the list of matching doctors
     */
    @Transactional
    public Map<String, Object> findDoctorByName(String name) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findByNameLike(name);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters doctors by name, specialty and availability.
     *
     * @param name doctor's name
     * @param specialty doctor's specialty
     * @param amOrPm AM or PM
     * @return map containing filtered doctors
     */
    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String amOrPm) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name,
                        specialty);

        doctors = filterDoctorByTime(doctors, amOrPm);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters doctors by name and availability.
     *
     * @param name doctor's name
     * @param amOrPm AM or PM
     * @return map containing filtered doctors
     */
    @Transactional
    public Map<String, Object> filterDoctorByNameAndTime(
            String name,
            String amOrPm) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findByNameLike(name);

        doctors = filterDoctorByTime(doctors, amOrPm);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters doctors by name and specialty.
     *
     * @param name doctor's name
     * @param specialty doctor's specialty
     * @return map containing filtered doctors
     */
    @Transactional
    public Map<String, Object> filterDoctorByNameAndSpecility(
            String name,
            String specialty) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name,
                        specialty);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters doctors by specialty and availability (AM/PM).
     *
     * @param specialty doctor's specialty
     * @param amOrPm AM or PM
     * @return map containing filtered doctors
     */
    @Transactional
    public Map<String, Object> filterDoctorByTimeAndSpecility(
            String specialty,
            String amOrPm) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findBySpecialtyIgnoreCase(specialty);

        doctors = filterDoctorByTime(doctors, amOrPm);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters doctors by specialty.
     *
     * @param specialty doctor's specialty
     * @return map containing filtered doctors
     */
    @Transactional
    public Map<String, Object> filterDoctorBySpecility(
            String specialty) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors =
                doctorRepository.findBySpecialtyIgnoreCase(specialty);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters all doctors by availability (AM/PM).
     *
     * @param amOrPm AM or PM
     * @return map containing filtered doctors
     */
    @Transactional
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {

        Map<String, Object> result = new HashMap<>();

        List<Doctor> doctors = doctorRepository.findAll();

        doctors = filterDoctorByTime(doctors, amOrPm);

        result.put("doctors", doctors);

        return result;
    }

    /**
     * Filters a list of doctors by available time (AM/PM).
     *
     * @param doctors list of doctors
     * @param amOrPm AM or PM
     * @return filtered list
     */
    private List<Doctor> filterDoctorByTime(
            List<Doctor> doctors,
            String amOrPm) {

        List<Doctor> filteredDoctors = new ArrayList<>();

        for (Doctor doctor : doctors) {

            // Replace getAvailability() with the actual method in your entity.
            for (String slot : doctor.getAvailableTimes()) {

                int hour = Integer.parseInt(slot.substring(0, 2));

                if ("AM".equalsIgnoreCase(amOrPm) && hour < 12) {
                    filteredDoctors.add(doctor);
                    break;
                }

                if ("PM".equalsIgnoreCase(amOrPm) && hour >= 12) {
                    filteredDoctors.add(doctor);
                    break;
                }
            }
        }

        return filteredDoctors;
    }

}