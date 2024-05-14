package com.recipe.service;

import com.recipe.model.dao.Role;
import com.recipe.util.exceptions.NotFoundException;


public interface RoleService {

    Role findByRole(String role) throws NotFoundException;

}
