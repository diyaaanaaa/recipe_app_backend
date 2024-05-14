package com.recipe.model.dao;

import com.recipe.model.dto.RegistrationDto;
import com.recipe.model.enums.UserStatus;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 64)
    private String name;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @JsonIgnore
    @NotBlank
    private String password;

    private long createdAt;

    private Long updatedAt;

    @Size(min = 5, max = 5)
    @Column(name = "verification_code")
    private String verificationCode;

    @Size(min = 5, max = 5)
    @Column(name = "recovery_code")
    private String recoveryCode;

    @Column(name = "recover_code_date")
    private Long recoveryCodeDate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_saved",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"))
    private Set<Recipe> recipes;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserRecipe> userRecipes;


    public static User createUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setStatus(UserStatus.UNVERIFIED);
        user.setCreatedAt(System.currentTimeMillis());

        return user;
    }

    @JsonGetter("password")
    public String getHiddenPassword(){
        return "[PROTECTED]";
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", password='" + getHiddenPassword() + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", resetPasswordToken='" + recoveryCode + '\'' +
                ", resetPasswordDate=" + recoveryCodeDate +
                ", roles=" + roles +
                '}';
    }
}
