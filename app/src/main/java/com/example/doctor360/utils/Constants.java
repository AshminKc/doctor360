package com.example.doctor360.utils;

import java.util.PrimitiveIterator;

public class Constants {

    public static final String APP_KEY = "ff50a7f6-1514-4f66-bcc0-26a5a3b9974b";
    public static final String BASE_URL = "https://doctor360app.herokuapp.com/";
    public  static final int RequestPermissionCode  = 1 ;

    public static final  String EMAIL = "ashminkc27@gmail.com";
    public static final String PASSWORD = "bboyrxvs3j";
    public static final String SUBJECT = "Verification Request Pending - Doctor360";
    public static final String BODY = "Hello User,\nYou have registered to Doctor360 successfully.\nYour profile is under verification.\nYou will be notified via email when admin verifies you.\nYou can login after your are verified.\n\nWith Regards,\nDoctor360";

    public static final String PATIENT_LOGIN_SUBJECT= "Patient Registration Successful - Doctor360";
    public static final String PATIENT_LOGIN_BODY = "Hello User,\nYou have registered to Doctor360 successfully.\nYou can login and consult with doctor online.\n\nWith Regards,\nDoctor360";

    public static final String VERIFIED_SUBJECT = "Verification Request Verified - Doctor360";
    public static final String VERIFIED_BODY = "Hello User,\nYou request have been verified by Admin successfully.\nYou can now login with your login credentials and enjoy the features of Doctor360.\n\nWith Regards,\nDoctor360";
    public static final String REJECTED_SUBJECT = "Verification Request Rejected - Doctor360";
    public static final String REJECTED_BODY = "Hello User,\nYou request have been rejected by Admin due to some reasons.\nPlease try valid details and document to use Doctor360.\n\nWith Regards,\nDoctor360";

    public static final String PASSWORD_CHANGE_SUBJECT = "Password Changed Successfully - Doctor360";
    public static final String PASSWORD_CHANGE_BODY = "Hello User,\nYou password have been changed successfully.\nYou can now login with your new password.\n\nWith Regards,\nDoctor360";

    public static final String USER_TYPE1 = "Admin";
    public static final String USER_TYPE2 = "Doctor";
    public static final String USER_TYPE3 = "Patient";

    public static final String PREF_NAME = "DOCTOR360";
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final int PRIVATE_MODE = 0;

    public static final int LOGGED_OUT = 44;
    public static final int LOGGED_IN = 55;

    public static final int REQUEST_GALLERY_CODE = 100;
    public static final int REQUEST_CAMERA_CODE = 200;

}
