package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
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
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    Bitmap decodedImage;
    CircleImageView imgProfile;
    TextView txtName, txtAddress, txtMobile, txtEmail, txtGender, txtAge, txtBlood;
    Button btnUpdateProfile, btnChangePassword;
    ConnectionDetector connectionDetector;
    String patientEmail, patientId, patientName, idFromChangePassword, strPatientID, profileImage;
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

        final Intent intent = getIntent();
        patientId = intent.getStringExtra("patient_profile_id");
        patientName  = intent.getStringExtra("patient_profile_name");
        patientEmail = intent.getStringExtra("patient_profile_email");
        profileImage = intent.getStringExtra("patient_profile_image");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PatientProfileActivity.this, PatientPasswordChangeActivity.class);
                intent1.putExtra("patient_profile_check_id", patientId);
                intent1.putExtra("patient_profile_check_email", patientEmail);
                intent1.putExtra("patient_profile_check_name", patientName);
                intent1.putExtra("patient_profile_check_image", profileImage);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PatientProfileActivity.this, PatientUpdateProfileActivity.class);
                intent1.putExtra("patient_update_id", patientId);
                intent1.putExtra("patient_update_address", txtAddress.getText().toString());
                intent1.putExtra("patient_update_mobile", txtMobile.getText().toString());
                intent1.putExtra("patient_update_email", txtEmail.getText().toString());
                intent1.putExtra("patient_update_name", txtName.getText().toString());
                intent1.putExtra("patient_update_age", txtAge.getText().toString());
                intent1.putExtra("patient_update_gender", txtGender.getText().toString());
                intent1.putExtra("patient_update_blood", txtBlood.getText().toString());
                intent1.putExtra("patient_update_image", profileImage);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });

        Intent intent1 = getIntent();
        idFromChangePassword = intent1.getStringExtra("id_from_password_change");

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

        if(patientId!=null)
            strPatientID = patientId;
        else
            strPatientID = idFromChangePassword;

        if(idFromChangePassword!=null)
            strPatientID = idFromChangePassword;
        else
            strPatientID = patientId;

        Call<PatientProfileReceiveParams> call = networkClient.viewPatientProfile(strPatientID);
        call.enqueue(new Callback<PatientProfileReceiveParams>() {
            @Override
            public void onResponse(Call<PatientProfileReceiveParams> call, Response<PatientProfileReceiveParams> response) {
                pDialog.dismiss();

                PatientProfileReceiveParams receiveParams = response.body();

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

                        if(receiveParams.getData().getProfileImg().matches("null")){
                            Picasso.with(PatientProfileActivity.this)
                                    .load(R.drawable.noimage)
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.noimage)
                                    .into(imgProfile);
                        } else {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = receiveParams.getData().getProfileImg();
                            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                            decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            imgProfile.setImageBitmap(decodedImage);
                        }

                    } else {
                        new AestheticDialog.Builder(PatientProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't View Profile at the moment. Please try again.")
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
        Intent intent=new Intent(PatientProfileActivity.this, PatientDashboardActivity.class);
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        intent.putExtra("from_profile_id", patientId);
        intent.putExtra("from_profile_name", name);
        intent.putExtra("from_profile_email", email);
        intent.putExtra("from_profile_image", profileImage);
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
            Intent intent=new Intent(PatientProfileActivity.this, PatientDashboardActivity.class);
            String name = txtName.getText().toString();
            String email = txtEmail.getText().toString();
            intent.putExtra("from_profile_id", patientId);
            intent.putExtra("from_profile_name", name);
            intent.putExtra("from_profile_email", email);
            intent.putExtra("from_profile_image", profileImage);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
