package com.example.doctor360.model;

public class DoctorRejectChatRequestReceiveParams {

    /**
     * success : true
     * message : Rejected and Deleted from system
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
