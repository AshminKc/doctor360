package com.example.doctor360.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.mail.Mail;
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
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    ProgressBar progressBar;
    MaterialSpinner spinnerUser;
    EditText Email,Password;
    Button buttonSubmit, btnRegisterDoctor, btnRegisterPatient;
    CheckBox rememberCheck;
    ConnectionDetector connectionDetector;
    String strEmail, strPassword, strUserType, strStatus;
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
        progressBar = findViewById(R.id.progress_loader);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        rememberCheck.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        btnRegisterDoctor.setOnClickListener(this);
        btnRegisterPatient.setOnClickListener(this);

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            Toasty.error(LoginActivity.this, "No Internet Connection!!", 200).show();
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

    public void checkFields(){
        strUserType = spinnerUser.getText().toString();
        strEmail = Email.getText().toString();
        strPassword = Password.getText().toString();

        if(strEmail.isEmpty()){
            Toasty.error(LoginActivity.this,"Please Enter Email",300).show();
        } else if(!strEmail.matches(emailPattern)){
            Toasty.error(LoginActivity.this,"Invalid Email",300).show();
        } else if(strPassword.isEmpty()){
            Toasty.error(LoginActivity.this,"Please Enter Password",300).show();
        } else {
            userLogin();
        }

    }

    public void userLogin(){
        String value = strUserType;
        if(value.matches("Admin")){
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            final AdminLoginSendParams adminLoginSendParams = new AdminLoginSendParams();

            strEmail = Email.getText().toString();
            strPassword = Password.getText().toString();

            Call<AdminLoginReceiveParams> call = networkClient.adminLogin(adminLoginSendParams);
            adminLoginSendParams.setEmail(strEmail);
            adminLoginSendParams.setPassword(strPassword);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(2000);

            call.enqueue(new Callback<AdminLoginReceiveParams>() {
                @Override
                public void onResponse(Call<AdminLoginReceiveParams> call, Response<AdminLoginReceiveParams> response) {
                    AdminLoginReceiveParams receiveParams = response.body();
                    String success = receiveParams.getSuccess();

                    if(success.equals("true")){
                        progressBar.setVisibility(View.GONE);
                        finish();
                        Intent intent=new Intent(getApplicationContext(), AdminDashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    } else {
                        Toasty.error(LoginActivity.this, "Invalid Login Credentials", 200).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<AdminLoginReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+ t.toString());
                    if(progressBar!= null){
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

        if(value.matches("Doctor")){
                NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
                final DoctorLoginSendParams doctorLoginSendParams = new DoctorLoginSendParams();

                strEmail = Email.getText().toString();
                strPassword = Password.getText().toString();

                Call<DoctorLoginReceiveParams> call = networkClient.doctorLogin(doctorLoginSendParams);
                doctorLoginSendParams.setEmail(strEmail);
                doctorLoginSendParams.setPassword(strPassword);

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(2000);

                call.enqueue(new Callback<DoctorLoginReceiveParams>() {
                    @Override
                    public void onResponse(Call<DoctorLoginReceiveParams> call, Response<DoctorLoginReceiveParams> response) {
                        DoctorLoginReceiveParams receiveParams = response.body();
                        String success = receiveParams.getSuccess();
                        int status = receiveParams.getData().getStatus();

                        if(success.equals("true")){
                            if(status == 1){
                                progressBar.setVisibility(View.GONE);
                                finish();
                                Intent intent = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                            } else {
                                Toasty.error(LoginActivity.this, "Your profile is not verified. You can login once you are verified. You will get notified via email for verification", 200).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toasty.error(LoginActivity.this, "Invalid Login Credentials", 200).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<DoctorLoginReceiveParams> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+ t.toString());
                        if(progressBar!= null){
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        }


        if(value.matches("Patient")){
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            final PatientLoginSendParams patientLoginSendParams = new PatientLoginSendParams();

            strEmail = Email.getText().toString();
            strPassword = Password.getText().toString();

            Call<PatientLoginReceiveParams> call = networkClient.patientLogin(patientLoginSendParams);
            patientLoginSendParams.setEmail(strEmail);
            patientLoginSendParams.setPassword(strPassword);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(2000);

            call.enqueue(new Callback<PatientLoginReceiveParams>() {
                @Override
                public void onResponse(Call<PatientLoginReceiveParams> call, Response<PatientLoginReceiveParams> response) {
                    PatientLoginReceiveParams receiveParams = response.body();
                    String success = receiveParams.getSuccess();

                    if(success.equals("true")){
                        progressBar.setVisibility(View.GONE);
                        finish();
                        Intent intent=new Intent(getApplicationContext(), PatientDashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toasty.error(LoginActivity.this, "Invalid Login Credentials", 200).show();
                    }
                }

                @Override
                public void onFailure(Call<PatientLoginReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+ t.toString());
                    if(progressBar!= null){
                        progressBar.setVisibility(View.GONE);
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

        switch (id){
            case R.id.checkBoxRemember:
                if(rememberCheck.isChecked()){
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
                if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
                    Toasty.error(LoginActivity.this,"Failed to Submit Data!!",200).show();
                } else {
                    checkFields();
                }
                break;

            case R.id.btnPatientRegister:
                //new SendMail().execute("");

                Intent intent=new Intent(getApplicationContext(),PatientRegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

            case R.id.btnDoctorRegister:
                Intent intent1=new Intent(getApplicationContext(),DoctorRegisterActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

        }
    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail(Constants.EMAIL, Constants.PASSWORD);

            String[] toArr = {"prembasnet094@gmail.com"};
            m.setTo(toArr);
            m.setFrom(Constants.EMAIL);
            m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
            m.setBody("Email body.");

            try {
                if (m.send()) {
                    Toast.makeText(LoginActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }
}


