package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private Service service;

    /**
     * Admin Dashboard
     * Validates the admin token before rendering the dashboard.
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {

        Map<String, String> validation = service.validateToken(token, "admin");

        if (validation.isEmpty()) {
            return "admin/adminDashboard";
        }

        return "redirect:/";
    }

    /**
     * Doctor Dashboard
     * Validates the doctor token before rendering the dashboard.
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {

        Map<String, String> validation = service.validateToken(token, "doctor");

        if (validation.isEmpty()) {
            return "doctor/doctorDashboard";
        }

        return "redirect:/";
    }
}