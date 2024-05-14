package com.recipe.controller;


import com.recipe.model.dto.AuthRequestDto;


import com.recipe.service.AuthService;
import com.recipe.util.exceptions.AccessDeniedException;
import com.recipe.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto authRequestDto) throws AccessDeniedException, NotFoundException{
        return ResponseEntity.ok(authService.login(authRequestDto));
    }


    @PostMapping("/validate/token")
    public ResponseEntity<Boolean> validateToken() {
        return ResponseEntity.ok(true);
    }
    



}
