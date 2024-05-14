package com.recipe.service.impl;

import com.recipe.model.dao.Role;
import com.recipe.repository.RoleRepository;
import com.recipe.service.RoleService;
import com.recipe.util.exceptions.NotFoundException;
import com.recipe.util.ErrorMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByRole(String role) throws NotFoundException {
        return roleRepository.findByName(role).orElseThrow(() -> new NotFoundException(ErrorMessages.ROLE_NOT_FOUND));
    }

}
