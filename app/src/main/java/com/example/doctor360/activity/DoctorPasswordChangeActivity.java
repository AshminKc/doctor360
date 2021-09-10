package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Color;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.fragment.DoctorHomeFragment;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorChangePasswordReceiveParams;
import com.example.doctor360.model.DoctorChangePasswordSendParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorPasswordChangeActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    AppCompatEditText edtNewPassword, edtOldPasssword, edtConfirmNewPass;
    Button btnResetPass;
    ConnectionDetector connectionDetector;
    String strNewPass, strOldPass, strConfirmNewPass, strDoctorId, strDoctorEmail, doctorName, doctorStringImage, doctorID;
    private static final String TAG = "DoctorPasswordChangeAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_change_password);

        toolbar = findViewById(R.id.doctorPasswordToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.doctorPasswordCoordinatorLayout);
        edtOldPasssword = findViewById(R.id.edtDoctorOldPassword);
        edtNewPassword = findViewById(R.id.edtDoctorNewPassword);
        edtConfirmNewPass = findViewById(R.id.edtDoctorConfirmNewPassword);
        btnResetPass = findViewById(R.id.buttonDoctorResetPassword);

        Intent intent = getIntent();
        strDoctorId = intent.getStringExtra("doctor_profile_check_id");
        strDoctorEmail = intent.getStringExtra("doctor_profile_check_email");
        doctorName = intent.getStringExtra("doctor_profile_check_name");
        doctorStringImage = intent.getStringExtra("doctor_profile_check_image");

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(DoctorPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
                    new AestheticDialog.Builder(DoctorPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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

    public void checkFields(){
        strOldPass = edtOldPasssword.getText().toString();
        strNewPass = edtNewPassword.getText().toString();
        strConfirmNewPass = edtConfirmNewPass.getText().toString();

        if(strOldPass.isEmpty()){
            Toasty.error(DoctorPasswordChangeActivity.this,"Please Enter Current Password",300).show();
        } else if(strNewPass.isEmpty()){
            Toasty.error(DoctorPasswordChangeActivity.this,"Please Enter New Password",300).show();
        } else if(strConfirmNewPass.isEmpty()){
            Toasty.error(DoctorPasswordChangeActivity.this,"Please Enter Confirm Password",300).show();
        }  else if(!strConfirmNewPass.matches(strNewPass)){
            Toasty.error(DoctorPasswordChangeActivity.this,"New Password and Confirm New Password doesn't match",300).show();
        } else {
            changeDoctorPassword();
        }
    }

    public void changeDoctorPassword(){
        strOldPass = edtOldPasssword.getText().toString();
        strNewPass = edtNewPassword.getText().toString();

        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        final SweetAlertDialog pDialog = new SweetAlertDialog(DoctorPasswordChangeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        final DoctorChangePasswordSendParams sendParams = new DoctorChangePasswordSendParams();
        sendParams.setCurrentpassword(strOldPass);
        sendParams.setPassword(strNewPass);

        Call<DoctorChangePasswordReceiveParams> call = networkClient.doctorChangePassword(strDoctorId, sendParams);
        call.enqueue(new Callback<DoctorChangePasswordReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorChangePasswordReceiveParams> call, Response<DoctorChangePasswordReceiveParams> response) {
                DoctorChangePasswordReceiveParams receiveParams = response.body();

                if(response.body()!=null){
                    String status = receiveParams.getSuccess();

                    if(status.matches("true")){
                        new AestheticDialog.Builder(DoctorPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    } else {
                        new AestheticDialog.Builder(DoctorPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }

                } else {
                    new AestheticDialog.Builder(DoctorPasswordChangeActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<DoctorChangePasswordReceiveParams> call, Throwable t) {
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
        Intent intent = new Intent(DoctorPasswordChangeActivity.this, DoctorProfileActivity.class);
        intent.putExtra("doctor_profile_id", strDoctorId);
        intent.putExtra("from_profile_name", doctorName);
        intent.putExtra("doctor_profile_image", doctorStringImage);
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
            Intent intent=new Intent(DoctorPasswordChangeActivity.this, DoctorProfileActivity.class);
            intent.putExtra("doctor_profile_id", strDoctorId);
            intent.putExtra("from_profile_name", doctorName);
            intent.putExtra("doctor_profile_image", doctorStringImage);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
