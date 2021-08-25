package com.example.doctor360.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.app.MyApplication;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorRegistrationSendParams;
import com.example.doctor360.model.PatientRegistrationReceiveParams;
import com.example.doctor360.model.PatientRegistrationSendParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.Constants;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector connectionDetector;
    TextView moveToLogin;
    Button btnPatientRegister;
    MaterialSpinner spinnerGender, spinnerAge, spinnerBlood;
    AppCompatEditText name, email, age, password, confirmPassword, address, bloodGroup, mobile, gender;
    String strName, strEmail, strMobile, strGender, strPassword, strConfPassword, strBlood, strAddress, strAge;
    String namePattern = "^[A-Za-z\\s]+$";
    String mobilePattern = "^[0-9]{10}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "PatientRegisterActivity";


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
            new AestheticDialog.Builder(PatientRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        }

        moveToLogin.setOnClickListener(this);
        btnPatientRegister.setOnClickListener(this);

        spinnerGender.setItems("Male", "Female", "Others");
        spinnerGender.setHint("Select your gender");
        spinnerGender.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerGender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strGender = item.toString();

            }
        });

        spinnerGender.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                strGender = spinner.getText().toString().trim();
            }
        });

        spinnerBlood.setItems("A+","A-","B+","B-","O+","O-","AB+","AB-");
        spinnerBlood.setHint("Select your blood group");
        spinnerBlood.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerBlood.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strBlood = item.toString();

            }
        });

        spinnerBlood.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                strBlood = spinner.getText().toString().trim();
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
               strAge = item.toString();

            }
        });

        spinnerAge.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
             strAge = spinner.getText().toString().trim();
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
        } else if(strConfPassword.isEmpty()){
            Toasty.error(PatientRegisterActivity.this,"Please Enter Confirm Password",300).show();
        } else if(!strConfPassword.matches(strPassword)){
            Toasty.error(PatientRegisterActivity.this,"Password and Confirm Password doesn't match",300).show();
        } else{
            registerPatient();
        }

    }

    public void registerPatient(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final PatientRegistrationSendParams patientRegistrationSendParams = new PatientRegistrationSendParams();

        strName = name.getText().toString();
        strAddress = address.getText().toString();
        strEmail = email.getText().toString();
        strBlood = spinnerBlood.getText().toString();
        strMobile = mobile.getText().toString();
        strAge = spinnerAge.getText().toString();
        strGender = spinnerGender.getText().toString();
        strPassword = password.getText().toString();

        Call<PatientRegistrationReceiveParams> call = networkClient.patientRegister(patientRegistrationSendParams);
        patientRegistrationSendParams.setName(strName);
        patientRegistrationSendParams.setAddress(strAddress);
        patientRegistrationSendParams.setEmail(strEmail);
        patientRegistrationSendParams.setMobile(strMobile);
        patientRegistrationSendParams.setAge(strAge);
        patientRegistrationSendParams.setGender(strGender);
        patientRegistrationSendParams.setUserType(Constants.USER_TYPE3);
        patientRegistrationSendParams.setBloodGroup(strBlood);
        patientRegistrationSendParams.setPassword(strPassword);

        final SweetAlertDialog pDialog = new SweetAlertDialog(PatientRegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<PatientRegistrationReceiveParams>() {
            @Override
            public void onResponse(Call<PatientRegistrationReceiveParams> call, Response<PatientRegistrationReceiveParams> response) {
                PatientRegistrationReceiveParams receiveParams = response.body();
                String Status = receiveParams.getSuccess();

                if(response.body()!=null){
                    if(Status.matches("true")){
                        new AestheticDialog.Builder(PatientRegisterActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage("Successfully registered!!")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    } else {
                        new AestheticDialog.Builder(PatientRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                } else {
                    new AestheticDialog.Builder(PatientRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some Error Occured at Server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<PatientRegistrationReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.toString());
                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonPatientRegister:
                if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
                    new AestheticDialog.Builder(PatientRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Failed to submit data.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
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
