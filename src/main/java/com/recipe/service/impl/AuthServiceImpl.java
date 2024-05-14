package com.recipe.service.impl;


import com.recipe.config.security.JwtTokenProvider;

import com.recipe.model.dao.Role;
import com.recipe.model.dao.User;


import com.recipe.model.dto.AuthRequestDto;
import com.recipe.model.dto.AuthResponseDto;
import com.recipe.model.enums.UserStatus;

import com.recipe.repository.UserRepository;

import com.recipe.service.AuthService;

import com.recipe.util.exceptions.AccessDeniedException;
import com.recipe.util.exceptions.NotFoundException;
import com.recipe.util.ErrorMessages;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) throws NotFoundException, AccessDeniedException {
        String username = authRequestDto.getUsername();

            User user = userRepository.findByEmailIgnoreCase(username)
                    .orElseThrow(()->new NotFoundException(ErrorMessages.USER_NOT_FOUND));

            if (!encoder.matches(authRequestDto.getPassword(), user.getPassword())) {
                throw new NotFoundException(ErrorMessages.INVALID_PASSWORD);
            }

            if (user.getStatus().equals(UserStatus.UNVERIFIED)) {
                throw new AccessDeniedException(ErrorMessages.UNVERIFIED_USER);
            }

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            String accessToken = jwtTokenProvider.createJwt(user.getId(), user.getName(),
                  user.getEmail(), roles);

            return new AuthResponseDto(accessToken);
    }

}



