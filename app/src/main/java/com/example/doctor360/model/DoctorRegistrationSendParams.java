package com.example.doctor360.model;

import java.io.File;

public class DoctorRegistrationSendParams {


    /**
     * name : Test Docoto5r
     * email : test5@gmail.com
     * mobile : 9815945634
     * gender : Male
     * userType : Doctor
     * specialization : Hello
     * qualification : MBBS
     * documentImage : image.jpg
     * password : 123456
     */

    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String userType;
    private String specialization;
    private String qualification;
    //private String documentImage;
    private File documentImage;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public File getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(File documentImage) {
        this.documentImage = documentImage;
    }

    /*public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
