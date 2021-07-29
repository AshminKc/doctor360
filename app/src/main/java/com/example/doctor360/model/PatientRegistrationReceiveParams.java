package com.example.doctor360.model;

public class PatientRegistrationReceiveParams {

    /**
     * success : true
     * message : Successfully Registered
     */

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
