package com.example.doctor360.model;

public class DoctorUpdateProfileSendParams {


    /**
     * name : Test
     * email : Kathmandu
     * mobile : 9812300000
     * gender : Male
     * specialization : Test Spec
     * qualification : New Quali
     * profileImg : test.jpg
     * documentImage : new.jpg
     */

    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String specialization;
    private String qualification;
    private String profileImg;
    private String documentImage;

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

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }
}
