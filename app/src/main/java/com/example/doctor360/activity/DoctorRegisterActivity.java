package com.example.doctor360.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;

public class DoctorRegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    ConnectionDetector connectionDetector;
    ImageView imgDocument;
    Button btnRegister, btnClick, btnUpload;
    TextView moveToLogin;
    AppCompatEditText name, email, password, confirmPassword, specialization, qualification, mobile, gender;
    ArrayList<String> genderArray, sepcArray, qualiArray;
    String[] genderList = {"Male", "Female", "Others"};
    String [] specList = {"Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)",};
    String [] qualiList = {};
    String strName, strEmail, strMobile, strGender, strPassword, strConfPassword, strSpec, strQuali;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        name = findViewById(R.id.edtDoctorName);
        gender = findViewById(R.id.spinnerDoctorGender);
        specialization = findViewById(R.id.spinnerDoctorSpecialization);
        qualification = findViewById(R.id.spinnerDoctorQualification);

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            Toasty.error(DoctorRegisterActivity.this, "No Internet Connection!!", 200).show();
        }

        gender.setOnClickListener(this);
        gender.setOnFocusChangeListener(this);

        genderArray = new ArrayList<>();
        genderArray.addAll(Arrays.asList(genderList));

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.spinnerDoctorGender:
                specialization.setFocusable(false);
                specialization.setFocusableInTouchMode(false);
                qualification.setFocusable(false);
                qualification.setFocusableInTouchMode(false);
                break;


        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();

        switch (id) {
            case R.id.spinnerDoctorGender:
                specialization.setFocusable(false);
                specialization.setFocusableInTouchMode(false);
                qualification.setFocusable(false);
                qualification.setFocusableInTouchMode(false);
                break;
        }
    }
}
