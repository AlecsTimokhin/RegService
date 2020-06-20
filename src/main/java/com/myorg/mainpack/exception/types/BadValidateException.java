package com.myorg.mainpack.exception.types;

import com.myorg.mainpack.dto.RestResponce;


public class BadValidateException extends BasicRestResponceException {

    public BadValidateException(RestResponce restResponce) {
        super(restResponce);
    }

}