package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Finds an administrator by username.
     *
     * @param username the administrator's username
     * @return the matching Admin, or null if not found
     */
    Admin findByUsername(String username);

}