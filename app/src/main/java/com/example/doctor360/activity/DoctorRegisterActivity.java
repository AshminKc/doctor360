package com.example.doctor360.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.utils.Constants;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;

import static com.example.doctor360.utils.Constants.RequestPermissionCode;

public class DoctorRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector connectionDetector;
    ImageView imgDocument;
    Button btnRegister, btnClick, btnUpload;
    TextView moveToLogin;
    AppCompatEditText name, email, password, confirmPassword, mobile;
    MaterialSpinner spinnerDoctorGender, spinnerQualification, spinnerSpecialization;
    String strName, strEmail, strMobile, strGender, strPassword, strConfPassword, strSpec, strQuali, strImage;
    String namePattern = "^[A-Za-z\\s]+$";
    String mobilePattern = "^[0-9]{10}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        name = findViewById(R.id.edtDoctorName);
        email = findViewById(R.id.edtDoctorEmail);
        mobile = findViewById(R.id.edtDoctorMobile);
        spinnerDoctorGender = findViewById(R.id.spinnerDoctorGender);
        spinnerSpecialization = findViewById(R.id.spinnerDoctorSpecialization);
        spinnerQualification = findViewById(R.id.spinnerDoctorQualification);
        password = findViewById(R.id.edtDoctorPassword);
        confirmPassword = findViewById(R.id.edtDoctorConfirmPassword);
        imgDocument = findViewById(R.id.imageDoctorDoc);
        btnRegister = findViewById(R.id.btnDoctorRegister);
        btnUpload = findViewById(R.id.btnUploadPhoto);
        moveToLogin = findViewById(R.id.loginDoctorText);

        EnableRuntimePermission();
        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            Toasty.error(DoctorRegisterActivity.this, "No Internet Connection!!", 200).show();
        }

        btnRegister.setOnClickListener(this);
        moveToLogin.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        spinnerDoctorGender.setItems("Male", "Female", "Others");
        spinnerDoctorGender.setHint("Select your gender");
        spinnerDoctorGender.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerDoctorGender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toasty.success(getApplicationContext(), "Clicked " + item,200).show();

            }
        });

        spinnerDoctorGender.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                String defaultGender = spinner.getText().toString().trim();
                Toasty.success(getApplicationContext(), "Default " + defaultGender, 200).show();
            }
        });

        spinnerQualification.setItems("Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)","Bachelor of Surgery (MBBS, BMBS, MBChB, MBBCh)",
                "Bachelor of Medicine (B.Med)","Bachelor of Surgery (B.S)/(B.Surg)","Doctor of Medicine (MD, Dr.MuD, Dr.Med)",
                "Doctor of Osteopathic Medicine (DO)","Doctor of Medicine by research MD(Res), DM","Master of Clinical Medicine (MCM)",
                "Master of Medical Science (MMSc, MMedSc)","Master of Public Health (MPH)","Master of Medicine (MM, MMed)",
                "Master of Philosophy (MPhil)","Master of Philosophy in Ophthalmology (MPhO)","Master of Public Health and Ophthalmology (MPHO)",
                "Master of Surgery (MS, MSurg, MChir, MCh, ChM, CM)","Master of Science in Medicine or Surgery (MSc)","Doctor of Clinical Medicine (DCM)",
                "Doctor of Clinical Surgery (DClinSurg)","Doctor of Medical Science (DMSc, DMedSc)","Doctor of Surgery (DS, DSurg)");
        spinnerQualification.setHint("Select your qualification");
        spinnerQualification.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerQualification.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toasty.success(getApplicationContext(), "Clicked " + item,200).show();

            }
        });

        spinnerQualification.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                String defaultQualification = spinner.getText().toString().trim();
                Toasty.success(getApplicationContext(), "Default " + defaultQualification, 200).show();
            }
        });

        spinnerSpecialization.setItems("Allergists/Immunologists","Anesthesiologists","Cardiologists","Colon and Rectal Surgeons",
                "Critical Care Medicine Specialists","Dermatologists","Endocrinologists","Emergency Medicine Specialists","Family Physicians",
                "Gastroenterologists","Geriatric Medicine Specialists","Hematologists","Hospice and Palliative Medicine Specialists",
                "Infectious Disease Specialists","Internists","Medical Geneticists","Nephrologists","Neurologists","Obstetricians and Gynecologists",
                "Oncologists","Ophthalmologists","Osteopaths","Otolaryngologists","Pathologists","Pediatricians","Physiatrists",
                "Plastic Surgeons","Podiatrists","Preventive Medicine Specialists","Psychiatrists","Pulmonologists","Radiologists",
                "Rheumatologists","Sleep Medicine Specialists","Sports Medicine Specialists","General Surgeons","Urologists");
        spinnerSpecialization.setHint("Select your specialization");
        spinnerSpecialization.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerSpecialization.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toasty.success(getApplicationContext(), "Clicked " + item,200).show();

            }
        });

        spinnerSpecialization.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                String defaultSpecialization = spinner.getText().toString().trim();
                Toasty.success(getApplicationContext(), "Default " + defaultSpecialization, 200).show();
            }
        });
    }

    public void checkFields(){
        strName = name.getText().toString();
        strEmail = email.getText().toString();
        strMobile = mobile.getText().toString();
        strQuali = spinnerQualification.getText().toString();
        strGender = spinnerDoctorGender.getText().toString();
        strSpec = spinnerSpecialization.getText().toString();
        boolean hasDrawable = (imgDocument.getDrawable() != null);
        strPassword = password.getText().toString();
        strConfPassword = confirmPassword.getText().toString();

        if(strName.isEmpty()){
            Toasty.error(DoctorRegisterActivity.this,"Please Enter Full Name",300).show();
        } else if(!strName.matches(namePattern)){
            Toasty.error(DoctorRegisterActivity.this,"Only Alphabet are allowed",300).show();
        } else if(strEmail.isEmpty()){
            Toasty.error(DoctorRegisterActivity.this,"Please Enter Email",300).show();
        } else if(!strEmail.matches(emailPattern)){
            Toasty.error(DoctorRegisterActivity.this,"Invalid Email",300).show();
        } else if(strMobile.isEmpty()){
            Toasty.error(DoctorRegisterActivity.this,"Please Enter Mobile No",300).show();
        } else if(!strMobile.matches(mobilePattern)){
            Toasty.error(DoctorRegisterActivity.this,"Invalid Mobile No",300).show();
        } else if(strPassword.isEmpty()){
            Toasty.error(DoctorRegisterActivity.this,"Please Enter Password",300).show();
        } else if (strPassword.length()<8) {
            Toasty.error(DoctorRegisterActivity.this,"Password must be at least 8 characters",300).show();
        } else if(strConfPassword.isEmpty()){
            Toasty.error(DoctorRegisterActivity.this,"Please Enter Confirm Password",300).show();
        } else if(!strConfPassword.matches(strPassword)){
            Toasty.error(DoctorRegisterActivity.this,"Password and Confirm Password doesn't match",300).show();
        } else if(imgDocument.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.noimage).getConstantState()){
            Toasty.error(DoctorRegisterActivity.this,"Image is required (jpg, jepg, png",300).show();
        } else{
            registerDoctor();
        }

    }

    public void registerDoctor(){
        Toasty.success(DoctorRegisterActivity.this, "Successfully registered", 200).show();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.SELECT_IMAGE);
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    String path = getPathFromURI(selectedImageUri);
                    Toasty.success(DoctorRegisterActivity.this, "File Path: " + path).show();
                    /*int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                    Uri filePathUri = Uri.parse(cursor.getString(column_index));
                    String file_name = filePathUri.getLastPathSegment().toString();
                    String file_path=filePathUri.getPath();
                    Toast.makeText(this,"File Name & PATH are:"+file_name+"\n"+file_path, Toast.LENGTH_LONG).show();*/
                    imgDocument.setImageURI(selectedImageUri);
                }
            }
        }
    }

        private void EnableRuntimePermission() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DoctorRegisterActivity.this, Manifest.permission.CAMERA)) {
                Toasty.success(DoctorRegisterActivity.this, "Camera allows you to access CAMERA", 2000).show();
            } else {
                ActivityCompat.requestPermissions(DoctorRegisterActivity.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
            }
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case RequestPermissionCode:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toasty.success(DoctorRegisterActivity.this, "Camera Permission Granted", 2000).show();
                    } else {
                        Toasty.success(DoctorRegisterActivity.this, "Camera Permission Denied", 2000).show();
                    }

            }
        }

        @Override
        public void onClick (View view){
            int id = view.getId();

            switch (id) {

                case R.id.btnDoctorRegister:
                    if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                        Toasty.error(DoctorRegisterActivity.this, "Failed to Submit Data!!", 200).show();
                    } else {
                        checkFields();
                    }
                    break;

                case R.id.loginDoctorText:
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    finish();
                    break;

                case R.id.btnUploadPhoto:
                    openImageChooser();
                    break;

            }
        }
    }
