package com.project.back_end.services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.back_end.model.Admin;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public TokenService(
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Returns the JWT signing key.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token.
     *
     * @param identifier username (Admin) or email (Doctor/Patient)
     * @return JWT token
     */
    public String generateToken(String identifier) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 7L * 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .subject(identifier)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the identifier (username/email) from a JWT token.
     *
     * @param token JWT token
     * @return identifier
     */
    public String extractIdentifier(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Validates a JWT token.
     *
     * @param token JWT token
     * @param user admin, doctor or patient
     * @return true if valid
     */
    public boolean validateToken(String token, String user) {

        try {

            String identifier = extractIdentifier(token);

            switch (user.toLowerCase()) {

                case "admin":
                    Admin admin = adminRepository.findByUsername(identifier);
                    return admin != null;

                case "doctor":
                    Doctor doctor = doctorRepository.findByEmail(identifier);
                    return doctor != null;

                case "patient":
                    Patient patient = patientRepository.findByEmail(identifier);
                    return patient != null;

                default:
                    return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
}
