package com.recipe.service;

import com.recipe.model.dao.User;
import com.recipe.model.dto.*;
import com.recipe.util.exceptions.BadRequestException;
import com.recipe.util.exceptions.DuplicateDataException;
import com.recipe.util.exceptions.NotFoundException;


public interface UserService {

    void register(RegistrationDto requestDto) throws NotFoundException, DuplicateDataException, BadRequestException;

    void verify(VerificationDto verificationDto) throws NotFoundException;

    void resendVerificationCode(EmailDto emailDto) throws NotFoundException;

    void recoverPassword(EmailDto passwordRecoveryDto) throws NotFoundException;

    void checkRecoveryCode(VerificationDto verificationDto) throws NotFoundException;

    void updatePassword(PasswordChangeDto passwordChangeDto) throws NotFoundException, BadRequestException;

    User getById(long userId);

    void addToSaved(long userId, int recipeId)  throws DuplicateDataException, NotFoundException;

    void addMyRecipe(long userId, MyRecipeRequestDto myRecipeRequestDto);

    void updateMyRecipe(long userId, int myRecipeId, MyRecipeRequestDto myRecipeRequestDto) throws NotFoundException;

    void deleteMyRecipe(long userId, int myRecipeId) throws NotFoundException;
}
