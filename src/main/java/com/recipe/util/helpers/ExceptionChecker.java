package com.recipe.util.helpers;


import com.recipe.util.exceptions.BadRequestException;
import com.recipe.util.exceptions.DuplicateDataException;
import com.recipe.util.exceptions.NotFoundException;

public class ExceptionChecker {


    public static void duplicateDataException(boolean expresion, String message) throws DuplicateDataException {

        if (expresion)
            throw new DuplicateDataException(message);
    }

    public static void badRequestException(boolean expresion, String message) throws BadRequestException {

        if (expresion)
            throw new BadRequestException(message);
    }


}
