package com.example.doctor360.model;

public class DoctorLoginReceiveParams {


    /**
     * success : true
     * data : {"status":1,"usertype":"Doctor","documentImage":"no-photo.jpg","_id":"60fd9f1d8d733f0015c0a09e","name":"Ashmin KC","email":"prembasnet094@gmail.com","mobile":"9810325896","gender":"Male","specialization":"Allergists/Immunologists","qualification":"Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)","password":"$2a$10$iPhP1TMZfkRYaRToVcWHcu.bE56tjttsDGHuaSjvQlHvPjU7tiA0y","__v":0}
     * message : Successfully logged in
     */

    private String success;
    private DataBean data;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * status : 1
         * usertype : Doctor
         * documentImage : no-photo.jpg
         * _id : 60fd9f1d8d733f0015c0a09e
         * name : Ashmin KC
         * email : prembasnet094@gmail.com
         * mobile : 9810325896
         * gender : Male
         * specialization : Allergists/Immunologists
         * qualification : Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)
         * password : $2a$10$iPhP1TMZfkRYaRToVcWHcu.bE56tjttsDGHuaSjvQlHvPjU7tiA0y
         * __v : 0
         */

        private int status;
        private String profileImg;
        private String usertype;
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

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
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


