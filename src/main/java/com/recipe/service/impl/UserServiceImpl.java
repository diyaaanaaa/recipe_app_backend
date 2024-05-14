package com.recipe.service.impl;


import com.recipe.model.dao.*;


import com.recipe.model.enums.UserStatus;
import com.recipe.repository.RecipeRepository;
import com.recipe.repository.UserRecipeRepository;
import com.recipe.repository.UserRepository;
import com.recipe.service.RoleService;
import com.recipe.service.UserService;
import com.recipe.util.ErrorMessages;
import com.recipe.util.StringUtils;

import com.recipe.util.exceptions.BadRequestException;
import com.recipe.util.exceptions.DuplicateDataException;
import com.recipe.util.exceptions.NotFoundException;
import com.recipe.util.helpers.ExceptionChecker;
import com.recipe.util.mail.EmailUtils;
import com.recipe.model.dto.*;
import com.recipe.util.AppKeys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final UserRecipeRepository userRecipeRepository;

    private final PasswordEncoder encoder;

    @Value("${verification.time}")
    private long verificationTimeMs;

    private final MessageSource messageSource;

    private final EmailUtils emailUtils;

    @Override
    public void register(RegistrationDto requestDto) throws NotFoundException, DuplicateDataException, BadRequestException {

        checkCreationInfo(requestDto.getEmail(), requestDto.getPassword(), requestDto.getConfirmPassword());

        User user = User.createUser(requestDto);
        user.setPassword(encoder.encode(requestDto.getPassword()));

        Role role = roleService.findByRole("ROLE_CUSTOMER"); //by default , only role
        user.setRoles(Collections.singleton(role));

        user.setVerificationCode(StringUtils.generateString(5, false, true));

        userRepository.save(user);

        sendCodeByEmail(requestDto.getEmail(),  "en", AppKeys.EMAIL_VERIFICATION_MS, AppKeys.EMAIL_VERIFICATION_SBJ,
                user.getVerificationCode()); /*english only*/
    }


    @Override
    @Transactional
    public void verify(VerificationDto verificationDto) throws NotFoundException {
        User user = userRepository.findByEmailIgnoreCaseAndStatusAndVerificationCode(verificationDto.getEmail(),
                UserStatus.UNVERIFIED, verificationDto.getVerificationCode())
                .orElseThrow(()->new NotFoundException((ErrorMessages.USER_NOT_FOUND)));

        /*@TODO
        * check creation time to validate verification code lifetime
        * */

        user.setVerificationCode(null);
        user.setUpdatedAt(System.currentTimeMillis());
        user.setStatus(UserStatus.VERIFIED);
    }

    @Override
    @Transactional
    public void resendVerificationCode(EmailDto emailDto) throws NotFoundException {

        User user = userRepository.findByEmailIgnoreCaseAndStatus(emailDto.getEmail(),
                        UserStatus.UNVERIFIED)
                .orElseThrow(()->new NotFoundException((ErrorMessages.USER_NOT_FOUND)));

        String verificationCode = StringUtils.generateString(5, false, true);

        user.setVerificationCode(verificationCode);
        user.setUpdatedAt(System.currentTimeMillis());

        sendCodeByEmail(emailDto.getEmail(),  "en", AppKeys.EMAIL_VERIFICATION_MS, AppKeys.EMAIL_VERIFICATION_SBJ,
                verificationCode); /*english only*/
    }

    @Override
    @Transactional
    public void recoverPassword(EmailDto passwordRecoveryDto) throws NotFoundException {
        User user = userRepository.findByEmailIgnoreCaseAndStatus(passwordRecoveryDto.getEmail(),
                        UserStatus.VERIFIED)
                .orElseThrow(()->new NotFoundException((ErrorMessages.USER_NOT_FOUND)));

        String recoveryCode = StringUtils.generateString(5, false, true);

        user.setRecoveryCode(recoveryCode);
        user.setUpdatedAt(System.currentTimeMillis());

        sendCodeByEmail(user.getEmail(),"en", AppKeys.PASSWORD_RECOVERY_MS,
                AppKeys.PASSWORD_RECOVERY_SUBJECT, recoveryCode);
    }

    @Override
    public void checkRecoveryCode(VerificationDto verificationDto) throws NotFoundException {
        userRepository.findByEmailIgnoreCaseAndStatusAndRecoveryCode(verificationDto.getEmail(),
                        UserStatus.VERIFIED, verificationDto.getVerificationCode())
                .orElseThrow(()->new NotFoundException((ErrorMessages.DATA_NOT_FOUND)));

        /*@TODO
         * check requested code creation time to validate recovery code lifetime
         * */
    }

    @Override
    @Transactional
    public void updatePassword(PasswordChangeDto passwordChangeDto) throws NotFoundException, BadRequestException {
        User user = userRepository.findByEmailIgnoreCaseAndStatusAndRecoveryCode(passwordChangeDto.getEmail(),
                        UserStatus.VERIFIED, passwordChangeDto.getRecoveryCode())
                .orElseThrow(()->new NotFoundException((ErrorMessages.DATA_NOT_FOUND)));

        ExceptionChecker.badRequestException(!passwordChangeDto.getPassword()
                .equals(passwordChangeDto.getConfirmPassword()), ErrorMessages.PASSWORD_NOT_EQUAL);

        user.setPassword(encoder.encode(passwordChangeDto.getPassword()));
        user.setRecoveryCode(null);
        user.setUpdatedAt(System.currentTimeMillis());
    }


    @Override
    public User getById(long userId){
        return userRepository.findById(userId).get();
    }

    @Transactional
    @Override
    public void addToSaved(long userId, int recipeId) throws DuplicateDataException, NotFoundException{
        User user = userRepository.findById(userId).get();
        Set<Recipe> saved = user.getRecipes();

        if (saved.stream().anyMatch(recipe -> recipe.getId()==recipeId)){
            throw new DuplicateDataException(ErrorMessages.ALREADY_LIKED);
        }
        else{
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(()->new NotFoundException(ErrorMessages.RECIPE_NOT_FOUND));
            saved.add(recipe);;
        }
    }

    @Override
    @Transactional
    public void addMyRecipe(long userId, MyRecipeRequestDto myRecipeRequestDto) {
        User user = userRepository.findById(userId).get();

        UserRecipe userRecipe = new UserRecipe();
        userRecipe.setUser(user);
        userRecipe.setTitle(myRecipeRequestDto.getTitle());
        userRecipe.setDescription(myRecipeRequestDto.getDescription());

        userRecipeRepository.save(userRecipe);
    }

    @Override
    @Transactional
    public void updateMyRecipe(long userId, int myRecipeId, MyRecipeRequestDto myRecipeRequestDto) throws NotFoundException{
        UserRecipe userRecipe = userRecipeRepository.findByIdAndUser_Id(myRecipeId, userId).orElseThrow(
                ()->new NotFoundException(ErrorMessages.USER_RECIPE_NOT_FOUND));

        userRecipe.setTitle(myRecipeRequestDto.getTitle());
        userRecipe.setDescription(myRecipeRequestDto.getDescription());
    }

    @Override
    public void deleteMyRecipe(long userId, int myRecipeId) throws NotFoundException {
        UserRecipe userRecipe = userRecipeRepository.findByIdAndUser_Id(myRecipeId, userId).orElseThrow(
                ()->new NotFoundException(ErrorMessages.USER_RECIPE_NOT_FOUND));

        userRecipeRepository.delete(userRecipe);
    }


    @Async
    public void sendCodeByEmail(String email, String locale, String message, String subject, String code) {

        String emailText = messageSource.getMessage(message,
                new String[]{code},
                Locale.forLanguageTag(locale));
        emailUtils.sendHtmlMail(email,
                messageSource.getMessage(subject, null, Locale.forLanguageTag(locale)),
                emailText);
    }

    private void checkCreationInfo(String email, String password, String confirmPassword) throws DuplicateDataException,
            BadRequestException {

        Optional<User> user = userRepository.findByEmailIgnoreCaseAndStatus(email, UserStatus.VERIFIED);
        ExceptionChecker.duplicateDataException(user.isPresent(), ErrorMessages.DUPLICATE_EMAIL);

        ExceptionChecker.badRequestException(!password.equals(confirmPassword), ErrorMessages.PASSWORD_NOT_EQUAL);
    }

}
