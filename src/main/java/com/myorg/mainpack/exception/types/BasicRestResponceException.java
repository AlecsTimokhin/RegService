package com.myorg.mainpack.exception.types;

import com.myorg.mainpack.dto.RestResponce;


public class BasicRestResponceException extends RuntimeException {

    private RestResponce restResponce;


    public BasicRestResponceException(RestResponce restResponce) {
        //super(restResponse.getMessage());
        this.restResponce = restResponce;
    }


    public RestResponce getRestResponse() { return restResponce; }

    public void setRestResponse(RestResponce restResponse) { this.restResponce = restResponse; }

}