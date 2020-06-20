package com.myorg.mainpack.dto;


import java.util.Map;

public class RestResponce {

    String message;

    String status;

    int errorCode;

    Map<String, String> params;



    public RestResponce(){}

    public RestResponce(String message, String status){
        this.message = message;
        this.status = status;
    }

    public RestResponce(String message, String status, int errorCode){
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

    public RestResponce(String message, String status, Map<String, String> params){
        this.message = message;
        this.status = status;
        this.params = params;
    }



    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}