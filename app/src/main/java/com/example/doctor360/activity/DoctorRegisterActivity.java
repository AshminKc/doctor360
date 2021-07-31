package com.example.doctor360.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import com.balsikandar.crashreporter.CrashReporter;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorRegistrationReceiveParams;
import com.example.doctor360.model.DoctorRegistrationSendParams;
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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.doctor360.utils.Constants.RequestPermissionCode;

public class DoctorRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector connectionDetector;
    ImageView imgDocument;
    Button btnRegister, btnUpload;
    TextView moveToLogin;
    AppCompatEditText name, email, password, confirmPassword, mobile;
    MaterialSpinner spinnerDoctorGender, spinnerQualification, spinnerSpecialization;
    String strName, strEmail, strMobile, strGender, strPassword, strConfPassword, strSpec, strQuali, strImage;
    String namePattern = "^[A-Za-z\\s]+$";
    String mobilePattern = "^[0-9]{10}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "DoctorRegisterActivity";

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

      //  EnableRuntimePermission();
        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(DoctorRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
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
                strGender = item.toString();

            }
        });

        spinnerDoctorGender.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                strGender = spinner.getText().toString().trim();
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
                strQuali = item.toString();

            }
        });

        spinnerQualification.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                strQuali = spinner.getText().toString().trim();
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
                strSpec = item.toString();

            }
        });

        spinnerSpecialization.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                strSpec = spinner.getText().toString().trim();
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
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final DoctorRegistrationSendParams registrationSendParams = new DoctorRegistrationSendParams();

        strName = name.getText().toString();
        strEmail = email.getText().toString();
        strMobile = mobile.getText().toString();
        strQuali = spinnerQualification.getText().toString();
        strGender = spinnerDoctorGender.getText().toString();
        strSpec = spinnerSpecialization.getText().toString();
        strPassword = password.getText().toString();

        Call<DoctorRegistrationReceiveParams> call = networkClient.doctorRegister(registrationSendParams);
        registrationSendParams.setName(strName);
        registrationSendParams.setEmail(strEmail);
        registrationSendParams.setMobile(strMobile);
        registrationSendParams.setQualification(strQuali);
        registrationSendParams.setGender(strGender);
        registrationSendParams.setSpecialization(strSpec);
        registrationSendParams.setUserType(Constants.USER_TYPE2);
        registrationSendParams.setPassword(strPassword);

        final SweetAlertDialog pDialog = new SweetAlertDialog(DoctorRegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<DoctorRegistrationReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorRegistrationReceiveParams> call, Response<DoctorRegistrationReceiveParams> response) {
                DoctorRegistrationReceiveParams receiveParams = response.body();
                String Status = receiveParams.getMessage();
                if(Status.matches("true")){
                    verificationEmail();
                    new AestheticDialog.Builder(DoctorRegisterActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                            .setTitle("Success")
                            .setMessage("Successfully registered. Your profile is under verification. Please visit email for more info")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                    pDialog.dismiss();

                } else {
                    new AestheticDialog.Builder(DoctorRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some error occurred. Please try again!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DoctorRegistrationReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.toString());
                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });

    }

    public void verificationEmail(){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.EMAIL, Constants.PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(strEmail));
            message.setSubject(Constants.SUBJECT);
            message.setText(Constants.BODY);

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
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
                        new AestheticDialog.Builder(DoctorRegisterActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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

    private class SendMail extends AsyncTask<Message, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Message... messages) {
            try{
                Transport.send(messages[0]);
                return "Success";

            } catch (MessagingException e){
                e.printStackTrace();
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
