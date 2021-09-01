package com.example.doctor360.model;

import java.io.Serializable;
import java.util.List;

public class PendingDoctorReceiveParams {


    /**
     * success : true
     * data : [{"status":0,"usertype":"Doctor","documentImage":"image.jpg","_id":"60fc299970bc200015a23697","name":"Test Docotor","email":"test3@gmail.com","mobile":"9815945844","gender":"Male","specialization":"Hello","qualification":"MBBS","password":"$2a$10$CNeFmOvR/fZGJKI/yAYIBOJpZp7U0Cs9SglPd9IVhvDxGQDSTDD2K","__v":0}]
     */

    private String success;
    private List<DataBean> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * status : 0
         * usertype : Doctor
         * documentImage : image.jpg
         * _id : 60fc299970bc200015a23697
         * name : Test Docotor
         * email : test3@gmail.com
         * mobile : 9815945844
         * gender : Male
         * specialization : Hello
         * qualification : MBBS
         * password : $2a$10$CNeFmOvR/fZGJKI/yAYIBOJpZp7U0Cs9SglPd9IVhvDxGQDSTDD2K
         * __v : 0
         */

        private int status;
        private String usertype;
        private String profileImg;
        private String documentImage;
        private String _id;
        private String name;
        private String email;
        private String mobile;
        private String gender;
        private String specialization;
        private String qualification;
        private String password;
        private int __v;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
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

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }
    }
}
