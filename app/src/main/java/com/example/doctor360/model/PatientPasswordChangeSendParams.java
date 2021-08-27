package com.example.doctor360.model;

public class PatientPasswordChangeSendParams {


    /**
     * currentpassword : 1234567897
     * password : nepal12345
     */

    private String currentpassword;
    private String password;

    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
