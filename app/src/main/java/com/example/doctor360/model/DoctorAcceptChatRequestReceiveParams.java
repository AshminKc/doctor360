package com.example.doctor360.model;

public class DoctorAcceptChatRequestReceiveParams {

    /**
     * success : true
     * message : Appointment Confirmed
     */

    private String success;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
