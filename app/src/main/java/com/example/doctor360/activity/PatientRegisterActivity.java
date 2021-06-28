package com.example.doctor360.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.doctor360.R;
import com.example.doctor360.app.MyApplication;
import com.example.doctor360.helper.ConnectionDetector;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class PatientRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector connectionDetector;
    TextView moveToLogin;
    Button btnPatientRegister;
    MaterialSpinner spinnerGender, spinnerAge, spinnerBlood;
    AppCompatEditText name, email, age, password, confirmPassword, address, bloodGroup, mobile, gender;
    ArrayList<String> genderArray, bloodArray, ageArray ;
    String[] genderList = {"Male", "Female", "Others"};
    String [] bloodList = {"A+","A-","B+","B-","O+","O-","AB+","AB-"};
    String [] ageList = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
    "21","22","23","24","25","26","27","28","29","30","31","32","33","35","36","37","38","39","40","41","42","43","44","45",
    "46","47","48","49","50"};
    String strName, strEmail, strMobile, strGender, strPassword, strConfPassword, strBlood, strAddress, strAge;
    String namePattern = "^[A-Za-z\\s]+$";
    String mobilePattern = "^[0-9]{10}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        name = findViewById(R.id.edtPatientName);
        address = findViewById(R.id.edtPatientAddress);
        email = findViewById(R.id.edtPatientEmail);
        spinnerAge = findViewById(R.id.spinnerPatientAge);
        spinnerBlood = findViewById(R.id.spinnerPatientBlood);
        mobile = findViewById(R.id.edtPatientMobile);
        spinnerGender = findViewById(R.id.spinnerPatientGender);
        password = findViewById(R.id.edtPatientPassword);
        confirmPassword = findViewById(R.id.edtPatientConfirmPassword);
        moveToLogin = findViewById(R.id.loginPatientText);
        btnPatientRegister = findViewById(R.id.buttonPatientRegister);


        connectionDetector = new ConnectionDetector(PatientRegisterActivity.this);
        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            Toasty.error(PatientRegisterActivity.this, "No Internet Connection!!", 200).show();
        }

        moveToLogin.setOnClickListener(this);
        btnPatientRegister.setOnClickListener(this);

        spinnerGender.setItems("Male", "Female", "Others");
        spinnerGender.setHint("Select your gender");
        spinnerGender.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerGender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toasty.success(getApplicationContext(), "Clicked " + item,200).show();

            }
        });

        spinnerGender.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                String defaultGender = spinner.getText().toString().trim();
                Toasty.success(getApplicationContext(), "Default " + defaultGender, 200).show();
            }
        });

        spinnerBlood.setItems("A+","A-","B+","B-","O+","O-","AB+","AB-");
        spinnerBlood.setHint("Select your blood group");
        spinnerBlood.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerBlood.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toasty.success(getApplicationContext(), "Clicked " + item,200).show();

            }
        });

        spinnerBlood.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                String defaultBlood = spinner.getText().toString().trim();
                Toasty.success(getApplicationContext(), "Default " + defaultBlood, 200).show();
            }
        });

        spinnerAge.setItems("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                "21","22","23","24","25","26","27","28","29","30","31","32","33","35","36","37","38","39","40","41","42","43","44","45",
                "46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70",
                "71","72","73","74","75","76","77","78","79","80","80","81","82","83","84","85","86","87","88","89","90","91","92","93",
                "94","95","96","97","98","99","100");
        spinnerAge.setHint("Select your age");
        spinnerAge.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerAge.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toasty.success(getApplicationContext(), "Clicked " + item,200).show();

            }
        });

        spinnerAge.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                String defaultAge = spinner.getText().toString().trim();
                Toasty.success(getApplicationContext(), "Default " + defaultAge, 200).show();
            }
        });

    }

    public void checkFields(){
        strName = name.getText().toString();
        strAddress = address.getText().toString();
        strEmail = email.getText().toString();
        strBlood = spinnerBlood.getText().toString();
        strMobile = mobile.getText().toString();
        strAge = spinnerAge.getText().toString();
        strGender = spinnerGender.getText().toString();
        strPassword = password.getText().toString();
        strConfPassword = confirmPassword.getText().toString();

        if(strName.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Full Name",300).show();
        } else if(!strName.matches(namePattern)){
            Toasty.error(PatientRegisterActivity.this,"Only Alphabet are allowed",300).show();
        } else if(strAddress.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Address",300).show();
        } else if(strEmail.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Email",300).show();
        } else if(!strEmail.matches(emailPattern)){
            Toasty.error(PatientRegisterActivity.this,"Invalid Email",300).show();
        } else if(strMobile.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Mobile No",300).show();
        } else if(!strMobile.matches(mobilePattern)){
            Toasty.error(PatientRegisterActivity.this,"Invalid Mobile No",300).show();
        }  else if(strPassword.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Password",300).show();
        } else if (strPassword.length()<8) {
            Toasty.error(PatientRegisterActivity.this,"Password must be at least 8 characters",300).show();
        } else if(strConfPassword.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Confirm Password",300).show();
        } else if(!strConfPassword.matches(strPassword)){
            Toasty.error(PatientRegisterActivity.this,"Password and Confirm Password doesn't match",300).show();
        } else{
            registerPatient();
        }

    }

    public void registerPatient(){
        Toasty.success(PatientRegisterActivity.this, "Successfully registered", 200).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonPatientRegister:
                if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
                    Toasty.error(PatientRegisterActivity.this,"Failed to Submit Data!!",200).show();
                } else {
                    checkFields();
                }
                break;

            case R.id.loginPatientText:
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

        }
    }

}
