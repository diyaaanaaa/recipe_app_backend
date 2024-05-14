package com.recipe.util;

import com.recipe.util.exceptions.AuthorizationException;
import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {

    public static String generateString(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }


    public static String getCorrectToken(String bearerToken) throws AuthorizationException {
        if (bearerToken.startsWith("Bearer"))
            return bearerToken.replace("Bearer", "").trim();

        return bearerToken.trim();

    }

}
