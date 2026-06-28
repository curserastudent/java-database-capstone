package com.project.back_end.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.model.Prescription;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    /**
     * Finds all prescriptions associated with a specific appointment.
     *
     * @param appointmentId the appointment ID
     * @return a list of prescriptions for the appointment
     */
    List<Prescription> findByAppointmentId(Long appointmentId);

}