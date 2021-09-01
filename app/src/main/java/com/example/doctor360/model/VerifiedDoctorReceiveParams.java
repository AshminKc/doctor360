package com.example.doctor360.model;

import java.io.Serializable;
import java.util.List;

public class VerifiedDoctorReceiveParams {


    /**
     * success : true
     * data : [{"status":1,"usertype":"Doctor","documentImage":"no-photo.jpg","_id":"610028922f8c2b0015ba79e7","name":"Nepaltt","email":"nep1@gmail.com","mobile":"9814736985","gender":"Male","specialization":"Allergists/Immunologists","qualification":"Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)","password":"$2a$10$pkK6WwuFNwfSriEv8mBawekUDuB2jlsxq8KnqloIBLJyeMOpKCdTi","__v":0},{"status":1,"usertype":"Doctor","documentImage":"db1.jpg","_id":"6102d22adb61731a60600e27","name":"Anil","email":"anil@gmail.com","mobile":"918142422331","gender":"Male","specialization":"NeuroSurgeon","qualification":"MD in Neuro Surgery","password":"$2a$10$oEaIhZ6FQtCdXZubcSFs4e6r0MszqZLpRAnS3X/STfVmuQbXD90N.","__v":0}]
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
         * status : 1
         * usertype : Doctor
         * documentImage : no-photo.jpg
         * _id : 610028922f8c2b0015ba79e7
         * name : Nepaltt
         * email : nep1@gmail.com
         * mobile : 9814736985
         * gender : Male
         * specialization : Allergists/Immunologists
         * qualification : Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)
         * password : $2a$10$pkK6WwuFNwfSriEv8mBawekUDuB2jlsxq8KnqloIBLJyeMOpKCdTi
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
