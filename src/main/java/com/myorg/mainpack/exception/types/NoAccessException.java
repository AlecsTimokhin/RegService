package com.myorg.mainpack.exception.types;

import com.myorg.mainpack.dto.RestResponce;


public class NoAccessException extends BasicRestResponceException {

    public NoAccessException(RestResponce restResponce) {
        super(restResponce);
    }

}