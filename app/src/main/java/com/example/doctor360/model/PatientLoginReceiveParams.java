package com.example.doctor360.model;

public class PatientLoginReceiveParams {


    /**
     * success : true
     * data : {"usertype":"Patient","_id":"60fd87ca0f1eea001535de82","name":"Test Patient","address":"Kathamndu","email":"doct@gmail.com","mobile":"9862565305","gender":"Male","bloodGroup":"A+","age":24,"password":"$2a$10$RIjABajLFFql48ZOMbitGuVbjoK6XNGvO1pC8Dze3N6QVikKfyrm.","__v":0}
     * message : Patient Successfully Logged In
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
         * usertype : Patient
         * _id : 60fd87ca0f1eea001535de82
         * name : Test Patient
         * address : Kathamndu
         * email : doct@gmail.com
         * mobile : 9862565305
         * gender : Male
         * bloodGroup : A+
         * age : 24
         * password : $2a$10$RIjABajLFFql48ZOMbitGuVbjoK6XNGvO1pC8Dze3N6QVikKfyrm.
         * __v : 0
         */

        private String usertype;
        private String profileImg;
        private String _id;
        private String name;
        private String address;
        private String email;
        private String mobile;
        private String gender;
        private String bloodGroup;
        private int age;
        private String password;
        private int __v;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
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
