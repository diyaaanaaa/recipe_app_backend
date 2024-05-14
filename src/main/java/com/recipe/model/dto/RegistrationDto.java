package com.recipe.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class RegistrationDto{

    @NotBlank(message = "Name is required")
    @Size(min = 3, max=64, message = "Name size must be between 3 and 64 characters long")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*,-./:;<=>?@\\[\\]^_`{|}~\\\\])(?=\\S+$).{8,16}$",
            message = "Password doesn't meet required pattern")
    private String password;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*,-./:;<=>?@\\[\\]^_`{|}~\\\\])(?=\\S+$).{8,16}$",
    message = "Password Confirmation doesn't meet required pattern")
    private String confirmPassword;


}
