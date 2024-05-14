package com.recipe.service;

import com.recipe.model.dto.AuthRequestDto;
import com.recipe.model.dto.AuthResponseDto;

import com.recipe.util.exceptions.AccessDeniedException;

import com.recipe.util.exceptions.NotFoundException;

public interface AuthService {

    AuthResponseDto login(AuthRequestDto authRequestDto) throws NotFoundException, AccessDeniedException;

}
