package com.project.back_end.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Appointment;

import jakarta.transaction.Transactional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Retrieves appointments for a doctor within a given time range,
     * including doctor and availability information.
     */
    @Query("""
        SELECT a
        FROM Appointment a
        LEFT JOIN FETCH a.doctor d
        LEFT JOIN FETCH d.availableTimes
        WHERE d.id = :doctorId
        AND a.appointmentTime BETWEEN :start AND :end
        ORDER BY a.appointmentTime
    """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Retrieves appointments for a doctor filtered by patient name
     * within a given time range.
     */
    @Query("""
        SELECT DISTINCT a
        FROM Appointment a
        LEFT JOIN FETCH a.doctor d
        LEFT JOIN FETCH d.availableTimes
        LEFT JOIN FETCH a.patient p
        WHERE d.id = :doctorId
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%'))
        AND a.appointmentTime BETWEEN :start AND :end
        ORDER BY a.appointmentTime ASC
    """)
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("patientName") String patientName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Deletes all appointments for a doctor.
     */
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    /**
     * Finds all appointments for a patient.
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Finds appointments for a patient by status,
     * ordered by appointment time.
     */
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
            Long patientId,
            int status);

    /**
     * Filters appointments by doctor's name and patient ID.
     */
    @Query("""
        SELECT a
        FROM Appointment a
        JOIN FETCH a.doctor d
        JOIN FETCH a.patient p
        WHERE p.id = :patientId
          AND LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
        ORDER BY a.appointmentTime ASC
    """)
    List<Appointment> filterByDoctorNameAndPatientId(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId);

    /**
     * Filters appointments by doctor's name, patient ID and status.
     */
    @Query("""
        SELECT a
        FROM Appointment a
        JOIN FETCH a.doctor d
        JOIN FETCH a.patient p
        WHERE p.id = :patientId
          AND a.status = :status
          AND LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
        ORDER BY a.appointmentTime ASC
    """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId,
            @Param("status") int status);

}