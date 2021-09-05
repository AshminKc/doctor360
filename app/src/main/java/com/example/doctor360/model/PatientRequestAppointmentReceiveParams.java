package com.example.doctor360.model;

public class PatientRequestAppointmentReceiveParams {


    /**
     * success : true
     * message : Appointment Request Sent
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
