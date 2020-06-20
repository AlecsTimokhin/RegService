package com.myorg.mainpack.exception.types;

import com.myorg.mainpack.dto.RestResponce;


public class NotFoundException extends BasicRestResponceException {

    public NotFoundException(RestResponce restResponce) {
        super(restResponce);
    }

}