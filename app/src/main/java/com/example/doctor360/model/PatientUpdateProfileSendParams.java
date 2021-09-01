package com.example.doctor360.model;

public class PatientUpdateProfileSendParams {


    /**
     * name : Test
     * address : Kathmandu
     * mobile : 9812300000
     * email : ktm@gmail.com
     * gender : Male
     * age : 23
     * profileImg : test.jpg
     */

    private String name;
    private String address;
    private String mobile;
    private String email;
    private String gender;
    private String age;
    private String bloodGroup;
    private String profileImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
