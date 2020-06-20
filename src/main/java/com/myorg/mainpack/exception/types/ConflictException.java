package com.myorg.mainpack.exception.types;

import com.myorg.mainpack.dto.RestResponce;


public class ConflictException extends BasicRestResponceException {

    public ConflictException(RestResponce restResponce) {
        super(restResponce);
    }

}