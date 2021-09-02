package com.example.doctor360.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.AdminLoginReceiveParams;
import com.example.doctor360.model.AdminLoginSendParams;
import com.example.doctor360.model.DoctorLoginReceiveParams;
import com.example.doctor360.model.DoctorLoginSendParams;
import com.example.doctor360.model.DoctorRegistrationReceiveParams;
import com.example.doctor360.model.DoctorRegistrationSendParams;
import com.example.doctor360.model.PatientLoginReceiveParams;
import com.example.doctor360.model.PatientLoginSendParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.Constants;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap bitmap;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    MaterialSpinner spinnerUser;
    EditText Email, Password;
    Button buttonSubmit, btnRegisterDoctor, btnRegisterPatient;
    CheckBox rememberCheck;
    ConnectionDetector connectionDetector;
    String strEmail, strPassword, strUserType, strProfileImage, encodedImage, encodedImageDocProfile, strDoctorImage, strDoctorDocument, encodedImageDocument;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.edtLoginEmail);
        Password = findViewById(R.id.edtLoginPassword);
        rememberCheck = findViewById(R.id.checkBoxRemember);
        btnRegisterPatient = findViewById(R.id.btnPatientRegister);
        btnRegisterDoctor = findViewById(R.id.btnDoctorRegister);
        spinnerUser = findViewById(R.id.spinnerUserType);
        buttonSubmit = findViewById(R.id.btnLogin);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        rememberCheck.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        btnRegisterDoctor.setOnClickListener(this);
        btnRegisterPatient.setOnClickListener(this);

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        }

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            spinnerUser.setText(loginPreferences.getString("userType", ""));
            Email.setText(loginPreferences.getString("email", ""));
            Password.setText(loginPreferences.getString("password", ""));
            rememberCheck.setChecked(true);
        }

        spinnerUser.setItems("Admin", "Doctor", "Patient");
        spinnerUser.setHint("Select user type");
        spinnerUser.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerUser.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strUserType = item.toString();

            }
        });

        spinnerUser.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                strUserType = spinner.getText().toString().trim();
            }
        });
    }

    public void checkFields() {
        strUserType = spinnerUser.getText().toString();
        strEmail = Email.getText().toString();
        strPassword = Password.getText().toString();

        if (strEmail.isEmpty()) {
            Toasty.error(LoginActivity.this, "Please Enter Email", 300).show();
        } else if (!strEmail.matches(emailPattern)) {
            Toasty.error(LoginActivity.this, "Invalid Email", 300).show();
        } else if (strPassword.isEmpty()) {
            Toasty.error(LoginActivity.this, "Please Enter Password", 300).show();
        } else {
            userLogin();
        }

    }

    public void userLogin() {
        String value = strUserType;
        if (value.matches("Admin")) {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            final AdminLoginSendParams adminLoginSendParams = new AdminLoginSendParams();

            strEmail = Email.getText().toString();
            strPassword = Password.getText().toString();

            Call<AdminLoginReceiveParams> call = networkClient.adminLogin(adminLoginSendParams);
            adminLoginSendParams.setEmail(strEmail);
            adminLoginSendParams.setPassword(strPassword);

            final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Signing in");
            pDialog.setCancelable(false);
            pDialog.show();

            call.enqueue(new Callback<AdminLoginReceiveParams>() {
                @Override
                public void onResponse(Call<AdminLoginReceiveParams> call, Response<AdminLoginReceiveParams> response) {
                    AdminLoginReceiveParams receiveParams = response.body();

                    if(response.body()!=null){
                        String success = receiveParams.getSuccess();

                        if (success.equals("true")) {
                            pDialog.dismiss();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        } else {
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Invalid Login Credentials")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                        }
                    } else {
                        new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Some error occcured at server end. Please try again.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<AdminLoginReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        }

        if (value.matches("Doctor")) {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            final DoctorLoginSendParams doctorLoginSendParams = new DoctorLoginSendParams();

            strEmail = Email.getText().toString();
            strPassword = Password.getText().toString();

            Call<DoctorLoginReceiveParams> call = networkClient.doctorLogin(doctorLoginSendParams);
            doctorLoginSendParams.setEmail(strEmail);
            doctorLoginSendParams.setPassword(strPassword);

            final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Signing in");
            pDialog.setCancelable(false);
            pDialog.show();

            call.enqueue(new Callback<DoctorLoginReceiveParams>() {
                @Override
                public void onResponse(Call<DoctorLoginReceiveParams> call, Response<DoctorLoginReceiveParams> response) {
                    DoctorLoginReceiveParams receiveParams = response.body();

                    if(response.body()!=null){
                        String success = receiveParams.getSuccess();

                        if (success.equals("true")) {
                            int status = receiveParams.getData().getStatus();
                            if (status == 1) {
                                pDialog.dismiss();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), DoctorDashboardActivity.class);
                                intent.putExtra("doctor_id", receiveParams.getData().get_id());
                                intent.putExtra("doctor_name", receiveParams.getData().getName());
                                intent.putExtra("doctor_email", receiveParams.getData().getEmail());
                                intent.putExtra("doctor_mobile", receiveParams.getData().getMobile());
                                intent.putExtra("doctor_image", receiveParams.getData().getProfileImg());
                                intent.putExtra("doctor_document", receiveParams.getData().getDocumentImage());
                                intent.putExtra("doctor_quali", receiveParams.getData().getQualification());
                                intent.putExtra("doctor_spec", receiveParams.getData().getSpecialization());
                                intent.putExtra("doctor_gender", receiveParams.getData().getGender());
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                            } else {
                                new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                        .setTitle("Info")
                                        .setMessage("Your profile is not verified. You can login once you are verified. You will get notified via email for verification")
                                        .setCancelable(true)
                                        .setGravity(Gravity.BOTTOM)
                                        .setDuration(3000)
                                        .show();
                                pDialog.dismiss();
                            }
                        } else {
                                new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                        .setTitle("Error")
                                        .setMessage("Invalid Login Credentials")
                                        .setCancelable(true)
                                        .setGravity(Gravity.BOTTOM)
                                        .setDuration(3000)
                                        .show();
                                pDialog.dismiss();
                        }
                    } else {
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Some error occured at server end. Please try again.")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<DoctorLoginReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        }


        if (value.matches("Patient")) {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            final PatientLoginSendParams patientLoginSendParams = new PatientLoginSendParams();

            strEmail = Email.getText().toString();
            strPassword = Password.getText().toString();

            Call<PatientLoginReceiveParams> call = networkClient.patientLogin(patientLoginSendParams);
            patientLoginSendParams.setEmail(strEmail);
            patientLoginSendParams.setPassword(strPassword);

            final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Signing in");
            pDialog.setCancelable(false);
            pDialog.show();

            call.enqueue(new Callback<PatientLoginReceiveParams>() {
                @Override
                public void onResponse(Call<PatientLoginReceiveParams> call, Response<PatientLoginReceiveParams> response) {
                    PatientLoginReceiveParams receiveParams = response.body();

                    if(response.body()!=null){
                        String success = receiveParams.getSuccess();

                        if (success.equals("true")) {
                            pDialog.dismiss();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                            intent.putExtra("patient_id", receiveParams.getData().get_id());
                            intent.putExtra("patient_name", receiveParams.getData().getName());
                            intent.putExtra("patient_email", receiveParams.getData().getEmail());
                            intent.putExtra("patient_image", receiveParams.getData().getProfileImg());
                            intent.putExtra("patient_address", receiveParams.getData().getAddress());
                            intent.putExtra("patient_mobile", receiveParams.getData().getMobile());
                            intent.putExtra("patient_age", receiveParams.getData().getAge());
                            intent.putExtra("patient_blood", receiveParams.getData().getBloodGroup());
                            intent.putExtra("patient_gender", receiveParams.getData().getGender());
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        } else {
                            pDialog.dismiss();
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Invalid Login Credentials")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                        }
                    } else {
                        new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Some error occcured. Please try again.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<PatientLoginReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        String userType = spinnerUser.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        switch (id) {
            case R.id.checkBoxRemember:
                if (rememberCheck.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("userType", userType);
                    loginPrefsEditor.putString("email", email);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                break;

            case R.id.btnLogin:
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(LoginActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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

            case R.id.btnPatientRegister:
                Intent intent = new Intent(getApplicationContext(), PatientRegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

            case R.id.btnDoctorRegister:
                Intent intent1 = new Intent(getApplicationContext(), DoctorRegisterActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

        }
    }
}



