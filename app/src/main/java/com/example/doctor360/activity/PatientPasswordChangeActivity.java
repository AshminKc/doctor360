package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.PatientPasswordChangeReceiveParams;
import com.example.doctor360.model.PatientPasswordChangeSendParams;
import com.example.doctor360.model.PatientRegistrationSendParams;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.Constants;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

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

public class PatientPasswordChangeActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    AppCompatEditText edtNewPassword, edtOldPasssword, edtConfirmNewPass;
    Button btnResetPass;
    ConnectionDetector connectionDetector;
    String strNewPass, strOldPass, strConfirmNewPass, strPatientId, strPatientEmail, patientName, patientStringImage;
    private static final String TAG = "PatientPasswordChangeAc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_change_password);

        toolbar = findViewById(R.id.patientPasswordToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.patientPasswordCoordinatorLayout);
        edtOldPasssword = findViewById(R.id.edtPatientOldPassword);
        edtNewPassword = findViewById(R.id.edtPatientNewPassword);
        edtConfirmNewPass = findViewById(R.id.edtPatientConfirmNewPassword);
        btnResetPass = findViewById(R.id.buttonPatientResetPassword);

        Intent intent = getIntent();
        strPatientId = intent.getStringExtra("patient_profile_check_id");
        strPatientEmail = intent.getStringExtra("patient_profile_check_email");
        patientName = intent.getStringExtra("patient_profile_check_name");
        patientStringImage = intent.getStringExtra("patient_profile_check_image");

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(PatientPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        }

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(PatientPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(2500)
                            .show();
                }

                if(connectionDetector.isDataAvailable() || connectionDetector.isNetworkAvailable()){
                    checkFields();
                }
            }
        });


    }

    private void checkFields(){
        strOldPass = edtOldPasssword.getText().toString();
        strNewPass = edtNewPassword.getText().toString();
        strConfirmNewPass = edtConfirmNewPass.getText().toString();

        if(strOldPass.isEmpty()){
            Toasty.error(PatientPasswordChangeActivity.this,"Please Enter Current Password",300).show();
        } else if(strNewPass.isEmpty()){
            Toasty.error(PatientPasswordChangeActivity.this,"Please Enter New Password",300).show();
        } else if(strConfirmNewPass.isEmpty()){
            Toasty.error(PatientPasswordChangeActivity.this,"Please Enter Confirm Password",300).show();
        }  else if(!strConfirmNewPass.matches(strNewPass)){
            Toasty.error(PatientPasswordChangeActivity.this,"New Password and Confirm New Password doesn't match",300).show();
        } else {
            changePatientPassword();
        }
    }

    public void changePatientPassword(){
        strOldPass = edtOldPasssword.getText().toString();
        strNewPass = edtNewPassword.getText().toString();

        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        final SweetAlertDialog pDialog = new SweetAlertDialog(PatientPasswordChangeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        final PatientPasswordChangeSendParams patientPasswordChangeSendParams = new PatientPasswordChangeSendParams();
        patientPasswordChangeSendParams.setCurrentpassword(strOldPass);
        patientPasswordChangeSendParams.setPassword(strNewPass);

        Call<PatientPasswordChangeReceiveParams> call = networkClient.patientChangePassword(strPatientId, patientPasswordChangeSendParams);
        call.enqueue(new Callback<PatientPasswordChangeReceiveParams>() {
            @Override
            public void onResponse(Call<PatientPasswordChangeReceiveParams> call, Response<PatientPasswordChangeReceiveParams> response) {
                PatientPasswordChangeReceiveParams patientPasswordChangeReceiveParams = response.body();

                if(response.body()!=null){
                    String Status = patientPasswordChangeReceiveParams.getSuccess();

                    if(Status.matches("true")){
                        new AestheticDialog.Builder(PatientPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage(patientPasswordChangeReceiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    } else {
                        new AestheticDialog.Builder(PatientPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(patientPasswordChangeReceiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                } else {
                    new AestheticDialog.Builder(PatientPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some Error occured at Server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PatientPasswordChangeReceiveParams> call, Throwable t) {
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
        Intent intent = new Intent(getApplicationContext(), PatientDashboardActivity.class);
        intent.putExtra("from_profile_id", strPatientId);
        intent.putExtra("from_profile_name", patientName);
        intent.putExtra("from_profile_image", patientStringImage);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            Intent intent=new Intent(PatientPasswordChangeActivity.this, PatientDashboardActivity.class);
            intent.putExtra("from_profile_id", strPatientId);
            intent.putExtra("from_profile_name", patientName);
            intent.putExtra("from_profile_image", patientStringImage);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
