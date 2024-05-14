package com.recipe.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerificationDto {

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "verification code is required")
    @Size(min = 5, max = 5, message = "code size must be 5 characters long")
    private String verificationCode;
}
