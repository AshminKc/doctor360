package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
        Intent intent = getIntent();
        String patientID = intent.getStringExtra("patient_profile_id");

        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(PatientProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Profile..");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PatientProfileReceiveParams> call = networkClient.viewPatientProfile(patientID);
        call.enqueue(new Callback<PatientProfileReceiveParams>() {
            @Override
            public void onResponse(Call<PatientProfileReceiveParams> call, Response<PatientProfileReceiveParams> response) {
                Log.d(TAG, "onResponse: Success");
                pDialog.dismiss();

                PatientProfileReceiveParams receiveParams = response.body();
                if(response.body()!=null){
                    String success = receiveParams.getSuccess();

                    if(success.matches("true")){
                        txtName.setText(receiveParams.getData().getName());
                        txtAddress.setText(receiveParams.getData().getAddress());
                        txtMobile.setText(receiveParams.getData().getAddress());
                        txtEmail.setText(receiveParams.getData().getEmail());
                        txtAge.setText(receiveParams.getData().getAge());
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
                            .setMessage("Some Error Occured at Server end. Please try again.")
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
                pDialog.dismiss();
                new AestheticDialog.Builder(PatientProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Some error occured. Couldn't Verify.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PatientProfileActivity.this,PatientDashboardActivity.class);
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
            Intent intent=new Intent(PatientProfileActivity.this,PatientDashboardActivity.class);
            overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
            startActivity(intent);
        }
        return true;
    }
}
