package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.model.OtpToken;
import org.example.project.model.User;
import org.example.project.repository.OtpTokenRepository;
import org.example.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository repository;
    private final EmailService emailSenderService;
    private final UserRepository userRepository;

    public String generateAndSendOtp(String username) {
        String otp = String.format("%06d", new Random().nextInt(1_000_000));

        repository.save(OtpToken.builder()
                .username(username)
                .otp(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .build());

        String email = userRepository.findByUsername(username)
                .map(User::getEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        emailSenderService.sendOtpEmail(email, otp);

        return otp;
    }

    public boolean verify(String username, String otp) {
        return repository.findByUsernameAndOtp(username, otp)
                .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(token -> {
                    repository.delete(token);
                    return true;
                }).orElse(false);
    }
}
