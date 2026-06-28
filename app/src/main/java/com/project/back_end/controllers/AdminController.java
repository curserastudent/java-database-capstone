package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.back_end.model.Admin;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("${api.path}" + "admin")
public class AdminController {

    private final Service service;

    public AdminController(Service service) {
        this.service = service;
    }

    /**
     * Admin login endpoint.
     *
     * @param admin Admin credentials
     * @return JWT token if authentication succeeds, otherwise an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return service.validateAdmin(admin);
    }
}