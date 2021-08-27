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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.PatientLoginReceiveParams;
import com.example.doctor360.model.PatientProfileReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView imgProfile;
    TextView txtName, txtAddress, txtMobile, txtEmail, txtGender, txtAge, txtBlood;
    Button btnUpdateProfile, btnChangePassword;
    ConnectionDetector connectionDetector;
    String patientEmail, patientId, patientName;
    private static final String TAG = "PatientProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        toolbar = findViewById(R.id.patientProfileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgProfile = findViewById(R.id.viewPatientProfileImage);
        txtName = findViewById(R.id.viewPatientProfileName);
        txtAddress = findViewById(R.id.viewPatientProfileAddress);
        txtMobile = findViewById(R.id.viewPatientProfileMobile);
        txtEmail = findViewById(R.id.viewPatientProfileEmail);
        txtGender = findViewById(R.id.viewPatientProfileGender);
        txtAge = findViewById(R.id.viewPatientProfileAge);
        txtBlood = findViewById(R.id.viewPatientProfileBlood);
        btnUpdateProfile = findViewById(R.id.buttonPatientUpdateProfile);
        btnChangePassword = findViewById(R.id.buttonPatientChangePassword);

        Intent intent = getIntent();
        patientId = intent.getStringExtra("patient_profile_id");
        patientEmail = intent.getStringExtra("patient_profile_email");
        Log.d(TAG, "onCreate: Patient id" + patientId);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PatientProfileActivity.this, PatientPasswordChangeActivity.class);
                intent1.putExtra("patient_profile_check_id", patientId);
                intent1.putExtra("patient_profile_check_email", patientEmail);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });

        connectionDetector = new ConnectionDetector(PatientProfileActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(PatientProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        } else {
            viewPatientProfile();
        }

    }

    private void viewPatientProfile(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(PatientProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Profile..");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PatientProfileReceiveParams> call = networkClient.viewPatientProfile(patientId);
        call.enqueue(new Callback<PatientProfileReceiveParams>() {
            @Override
            public void onResponse(Call<PatientProfileReceiveParams> call, Response<PatientProfileReceiveParams> response) {
                pDialog.dismiss();

                PatientProfileReceiveParams receiveParams = response.body();
                Log.d(TAG, "onResponse: Success " + receiveParams.getData().getAge());
                if(response.body()!=null){
                    String success = receiveParams.getSuccess();

                    if(success.matches("true")){
                        txtName.setText(receiveParams.getData().getName());
                        txtAddress.setText(receiveParams.getData().getAddress());
                        txtMobile.setText(receiveParams.getData().getMobile());
                        txtEmail.setText(receiveParams.getData().getEmail());
                        txtAge.setText(String.valueOf(receiveParams.getData().getAge()));
                        txtGender.setText(receiveParams.getData().getGender());
                        txtBlood.setText(receiveParams.getData().getBloodGroup());
                      /*  Picasso.with(PatientProfileActivity.this)
                                .load(receiveParams.getData().get)
                                .placeholder(R.drawable.noimage)
                                .error(R.drawable.noimage)
                                .into(imgProfile);*/

                    } else {
                        new AestheticDialog.Builder(PatientProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                } else {
                    new AestheticDialog.Builder(PatientProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<PatientProfileReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PatientProfileActivity.this, PatientDashboardActivity.class);
        String sendName = txtEmail.getText().toString();
        intent.putExtra("to_dashboard", sendName);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
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
            finish();
            Intent intent=new Intent(PatientProfileActivity.this, PatientDashboardActivity.class);
            String sendName = txtEmail.getText().toString();
            intent.putExtra("to_dashboard", sendName);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
        }
        return true;
    }
}
