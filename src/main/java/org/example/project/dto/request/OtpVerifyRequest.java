package org.example.project.dto.request;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String username;
    private String otp;
}
