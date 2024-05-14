package com.recipe.repository;

import com.recipe.model.dao.User;
import com.recipe.model.enums.UserStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmailIgnoreCaseAndStatus(String email, UserStatus userStatus);
    Optional<User> findByEmailIgnoreCaseAndStatusAndVerificationCode(String email, UserStatus userStatus, String verificationCode);
    Optional<User> findByEmailIgnoreCaseAndStatusAndRecoveryCode(String email, UserStatus userStatus, String recoveryCode);

}
