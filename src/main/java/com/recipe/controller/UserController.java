package com.recipe.controller;


import com.recipe.config.security.JwtTokenProvider;
import com.recipe.model.dao.Recipe;
import com.recipe.model.dao.UserRecipe;
import com.recipe.model.dto.*;
import com.recipe.service.UserService;

import com.recipe.util.exceptions.BadRequestException;
import com.recipe.util.exceptions.DuplicateDataException;
import com.recipe.util.exceptions.NotFoundException;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/account")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/registration")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationDto registrationDto)
            throws DuplicateDataException, NotFoundException,  BadRequestException {

        userService.register(registrationDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/verify")
    public ResponseEntity<Void> verify(@RequestBody @Valid VerificationDto verificationCode)
            throws NotFoundException {

        userService.verify(verificationCode);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/resend-verification-code")
    public ResponseEntity<Void> resendVerificationCode(@RequestBody @Valid EmailDto emailDto)
            throws NotFoundException {

        userService.resendVerificationCode(emailDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/forgot-password")
    public ResponseEntity<Void> forgotPasswordByEmail(@RequestBody @Valid EmailDto passwordRecoveryDto)
            throws NotFoundException{

        userService.recoverPassword(passwordRecoveryDto);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/validate-recovery-code")
    public ResponseEntity<Void> validateRecoverCode(@RequestBody @Valid VerificationDto verificationDto)
            throws NotFoundException{

        userService.checkRecoveryCode(verificationDto);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto)
            throws NotFoundException, BadRequestException{

        userService.updatePassword(passwordChangeDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/saved")
    public ResponseEntity<Set<Recipe>> getSaved(HttpServletRequest request){

        String token = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(token).get("userId").toString());

        Set<Recipe> saved = userService.getById(userId).getRecipes();
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/save/{recipeId}")
    public ResponseEntity<Void> save(HttpServletRequest request, @PathVariable int recipeId)
            throws DuplicateDataException, NotFoundException {

        String token = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(token).get("userId").toString());

        userService.addToSaved(userId, recipeId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/my-recipes")
    public ResponseEntity<Set<UserRecipe>> getMyRecipes(HttpServletRequest request){

        String token = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(token).get("userId").toString());

        Set<UserRecipe> userRecipes = userService.getById(userId).getUserRecipes();
        return ResponseEntity.ok(userRecipes);
    }

    @PostMapping("/my-recipes")
    public ResponseEntity<Void> addMyRecipe(HttpServletRequest request, @RequestBody @Valid MyRecipeRequestDto
                                            myRecipeRequestDto){

        String token = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(token).get("userId").toString());

        userService.addMyRecipe(userId, myRecipeRequestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/my-recipes/{myRecipeId}")
    public ResponseEntity<Void> updateMyRecipe(HttpServletRequest request, @PathVariable int myRecipeId,
                                               @RequestBody @Valid MyRecipeRequestDto myRecipeRequestDto) throws NotFoundException{

        String token = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(token).get("userId").toString());

        userService.updateMyRecipe(userId, myRecipeId, myRecipeRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my-recipes/{myRecipeId}")
    public ResponseEntity<Void> updateMyRecipe(HttpServletRequest request, @PathVariable int myRecipeId) throws NotFoundException{

        String token = jwtTokenProvider.resolveToken(request);
        Long userId = Long.valueOf(jwtTokenProvider.getClaims(token).get("userId").toString());

        userService.deleteMyRecipe(userId, myRecipeId);
        return ResponseEntity.ok().build();
    }

}
