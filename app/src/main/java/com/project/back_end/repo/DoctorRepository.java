package com.project.back_end.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.back_end.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Finds a doctor by email.
     *
     * @param email the doctor's email
     * @return the matching Doctor, or null if not found
     */
    Doctor findByEmail(String email);

    /**
     * Finds doctors whose name contains the given text.
     */
    @Query("""
        SELECT d
        FROM Doctor d
        WHERE d.name LIKE CONCAT('%', :name, '%')
    """)
    List<Doctor> findByNameLike(@Param("name") String name);

    /**
     * Finds doctors whose name contains the given text
     * and whose specialty matches exactly (case-insensitive).
     */
    @Query("""
        SELECT d
        FROM Doctor d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
          AND LOWER(d.specialty) = LOWER(:specialty)
    """)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
            @Param("name") String name,
            @Param("specialty") String specialty);

    /**
     * Finds doctors by specialty (case-insensitive).
     *
     * @param specialty the doctor's specialty
     * @return matching doctors
     */
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

}