package com.example.doctor360;

public class ChatModel {

    String patientId;
    String doctorId;
    String chatDate;
    String chatTime;
    String patientMessage;
    String doctorMessage;

    public ChatModel(String patientId, String doctorId, String chatDate, String chatTime, String patientMessage, String doctorMessage) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.chatDate = chatDate;
        this.chatTime = chatTime;
        this.patientMessage = patientMessage;
        this.doctorMessage = doctorMessage;
    }

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

    public String getChatDate() {
        return chatDate;
    }

    public void setChatDate(String chatDate) {
        this.chatDate = chatDate;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getPatientMessage() {
        return patientMessage;
    }

    public void setPatientMessage(String patientMessage) {
        this.patientMessage = patientMessage;
    }

    public String getDoctorMessage() {
        return doctorMessage;
    }

    public void setDoctorMessage(String doctorMessage) {
        this.doctorMessage = doctorMessage;
    }
}
