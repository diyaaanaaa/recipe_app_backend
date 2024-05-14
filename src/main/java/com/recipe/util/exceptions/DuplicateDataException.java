package com.recipe.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateDataException extends Exception {


    public DuplicateDataException(String message) {
        super(message);
    }

}
