package com.example.doctor360.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Email,Password;
    Button buttonSubmit, btnRegisterDoctor, btnRegisterPatient;
    CheckBox rememberCheck;
    ProgressDialog progressDialog;
    ConnectionDetector connectionDetector;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^(?=.*[@$%&#_()=+?»«<>£§€{}\\[\\]-])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*(?<=.{8,})$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.edtLoginEmail);
        Password = findViewById(R.id.edtLoginPassword);
        rememberCheck = findViewById(R.id.checkBoxRemember);
        btnRegisterPatient = findViewById(R.id.btnPatientRegister);
        btnRegisterDoctor = findViewById(R.id.btnDoctorRegister);
        buttonSubmit = findViewById(R.id.btnLogin);

        rememberCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rememberCheck.isChecked()){
                    Toasty.success(getApplicationContext(),"Checked",300).show();
                } else {
                    Toasty.error(getApplicationContext(),"Unchecked",300).show();
                }
            }
        });

        buttonSubmit.setOnClickListener(this);
        btnRegisterDoctor.setOnClickListener(this);
        btnRegisterPatient.setOnClickListener(this);

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            Toasty.error(LoginActivity.this, "No Internet Connection!!", 200).show();
        }
    }

    public void checkFields(){
        String strEmail = Email.getText().toString();
        String strPass = Password.getText().toString();
        String strStatus = "active";

        if(strEmail.isEmpty()){
            Toasty.error(LoginActivity.this,"Please Enter Email",300).show();
        } else if(!strEmail.matches(emailPattern)){
            Toasty.error(LoginActivity.this,"Invalid Email",300).show();
        } else if(strPass.isEmpty()){
            Toasty.error(LoginActivity.this,"Please Enter Password",300).show();
        } else if(!strPass.matches(passwordPattern)){
            Toasty.error(LoginActivity.this,"Entered password is not as per format",300).show();
        } else if(strEmail.equals("admin@gmail.com") && strPass.equals("Admin@123")){
            userLogin();
        }  else {
            Toasty.error(LoginActivity.this,"Invalid Email or Password",300).show();
        }

    }

    public void userLogin(){
        this.finish();
        Intent intent=new Intent(getApplicationContext(), PatientDashboardActivity.class);
        //intent.putExtra("status",2);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSubmit) {
            if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
                Toasty.error(LoginActivity.this,"Failed to Submit Data!!",200).show();
            } else {
                checkFields();
            }
        }

        if(v == btnRegisterPatient){
            Intent intent=new Intent(getApplicationContext(),PatientRegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }

        if(v == btnRegisterDoctor){
            Intent intent=new Intent(getApplicationContext(),DoctorRegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }
    }
}

