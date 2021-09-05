package com.example.doctor360.model;

public class PatientRequestAppointmentSendParams {


    /**
     * patientId : 612a6656a9649a00164b356d
     * doctorId : 612e73a3bac4920016be399e
     * description : test appointment
     * date : 2021/09/06
     * time : 12:00 PM
     */

    private String patientId;
    private String doctorId;
    private String description;
    private String date;
    private String time;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
