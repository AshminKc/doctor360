package com.example.doctor360;

public class MainModel {

    Integer image;
    String name;
    String mobile;

    public MainModel(Integer image, String name, String mobile) {
        this.image = image;
        this.name = name;
        this.mobile = mobile;
    }

    public Integer getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
