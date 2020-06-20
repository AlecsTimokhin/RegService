package com.myorg.mainpack.util.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.HashMap;


public class CommonValidationUtil {

    CommonValidationUtil(){}


    public static boolean validFields(BindingResult bindingResult, HashMap<String, String> params){

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                params.put(error.getField() + "Error", error.getDefaultMessage());
            }
            return false;
        }
        else{
            return  true;
        }

    }

}