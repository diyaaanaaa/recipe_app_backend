package com.recipe.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordChangeDto {

    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*,-./:;<=>?@\\[\\]^_`{|}~\\\\])(?=\\S+$).{8,16}$",
            message = "Password doesn't meet required pattern")
    private String password;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*,-./:;<=>?@\\[\\]^_`{|}~\\\\])(?=\\S+$).{8,16}$",
            message = "Password Confirmation doesn't meet required pattern")
    private String confirmPassword;

    private String recoveryCode;
}
