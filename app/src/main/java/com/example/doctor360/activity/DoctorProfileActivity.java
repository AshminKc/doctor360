package com.example.doctor360.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorProfileReceiveParams;
import com.example.doctor360.model.DoctorUpdateProfileReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.SquareImageView;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    Bitmap decodedImage, decodedImage1;
    CircleImageView imgDoctorProfile;
    TextView txtName, txtMobile, txtEmail, txtGender, txtSpec, txtQuali;
    Button btnUpdateProfile, btnChangePassword;
    ConnectionDetector connectionDetector;
    String doctorEmail, doctorId, doctorName, idFromChangePassword, strDoctorID, profileImage, documentImage, strDisplayName;
    private static final String TAG = "DoctorProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        toolbar = findViewById(R.id.doctorProfileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgDoctorProfile = findViewById(R.id.viewDoctorProfileImage);
        txtName = findViewById(R.id.viewDoctorProfileName);
        txtMobile = findViewById(R.id.viewDoctorProfileMobile);
        txtEmail = findViewById(R.id.viewDoctorProfileEmail);
        txtGender = findViewById(R.id.viewDoctorProfileGender);
        txtSpec = findViewById(R.id.viewDoctorProfileSpecialization);
        txtQuali = findViewById(R.id.viewDoctorProfileQualification);
        btnUpdateProfile = findViewById(R.id.buttonDoctorUpdateProfile);
        btnChangePassword = findViewById(R.id.buttonDoctorChangePassword);

        final Intent intent = getIntent();
        doctorId = intent.getStringExtra("doctor_profile_id");
        doctorName  = intent.getStringExtra("doctor_profile_name");
        doctorEmail = intent.getStringExtra("doctor_profile_email");
        profileImage = intent.getStringExtra("doctor_profile_image");
        documentImage = intent.getStringExtra("doctor_document_image");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DoctorProfileActivity.this, DoctorPasswordChangeActivity.class);
                intent1.putExtra("doctor_profile_check_id", doctorId);
                intent1.putExtra("doctor_profile_check_email", doctorEmail);
                intent1.putExtra("doctor_profile_check_name", doctorName);
                intent1.putExtra("doctor_profile_check_image", profileImage);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DoctorProfileActivity.this, DoctorUpdateProfileActivity.class);
                intent1.putExtra("doctor_update_id", doctorId);
                intent1.putExtra("doctor_update_name", strDisplayName);
                intent1.putExtra("doctor_update_mobile", txtMobile.getText().toString());
                intent1.putExtra("doctor_update_email", txtEmail.getText().toString());
                intent1.putExtra("doctor_update_spec", txtSpec.getText().toString());
                intent1.putExtra("doctor_update_gender", txtGender.getText().toString());
                intent1.putExtra("doctor_update_quali", txtQuali.getText().toString());
                intent1.putExtra("doctor_update_image", profileImage);
                intent1.putExtra("doctor_update_document", documentImage);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });

        Intent intent1 = getIntent();
        idFromChangePassword = intent1.getStringExtra("id_from_password_change");

        connectionDetector = new ConnectionDetector(DoctorProfileActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(DoctorProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        } else {
            viewDoctorProfile();
        }
    }

    private void viewDoctorProfile(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(DoctorProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Profile..");
        pDialog.setCancelable(false);
        pDialog.show();

        if(doctorId!=null)
            strDoctorID = doctorId;
        else
            strDoctorID = idFromChangePassword;

        if(idFromChangePassword!=null)
            strDoctorID = idFromChangePassword;
        else
            strDoctorID = doctorId;

        Call<DoctorProfileReceiveParams> call = networkClient.viewDoctorProfile(strDoctorID);
        call.enqueue(new Callback<DoctorProfileReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorProfileReceiveParams> call, Response<DoctorProfileReceiveParams> response) {
                pDialog.dismiss();

                final DoctorProfileReceiveParams receiveParams = response.body();

                if(response.body()!=null){
                    String status = receiveParams.getSuccess();

                    if(status.matches("true")){

                        strDisplayName = receiveParams.getData().getName();
                        txtName.setText("DR ."+strDisplayName);
                        txtEmail.setText(receiveParams.getData().getEmail());
                        txtMobile.setText(receiveParams.getData().getMobile());
                        txtGender.setText(receiveParams.getData().getGender());
                        txtSpec.setText(receiveParams.getData().getSpecialization());
                        txtQuali.setText(receiveParams.getData().getQualification());

                        if(receiveParams.getData().getProfileImg().matches("null")){
                            Picasso.with(DoctorProfileActivity.this)
                                    .load(R.drawable.noimage)
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.noimage)
                                    .into(imgDoctorProfile);
                        } else {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = receiveParams.getData().getProfileImg();
                            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                            decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            imgDoctorProfile.setImageBitmap(decodedImage);
                        }

                    } else {
                        new AestheticDialog.Builder(DoctorProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't View Profile at the moment. Please try again.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }

                } else {
                    new AestheticDialog.Builder(DoctorProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<DoctorProfileReceiveParams> call, Throwable t) {
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
        Intent intent=new Intent(DoctorProfileActivity.this, DoctorDashboardActivity.class);
        String email = txtEmail.getText().toString();
        intent.putExtra("from_profile_id", doctorId);
        intent.putExtra("from_profile_name", strDisplayName);
        intent.putExtra("from_profile_email", email);
        intent.putExtra("from_profile_image", profileImage);
        startActivity(intent);
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
            Intent intent=new Intent(DoctorProfileActivity.this,DoctorDashboardActivity.class);
            String email = txtEmail.getText().toString();
            intent.putExtra("from_profile_id", doctorId);
            intent.putExtra("from_profile_name", strDisplayName);
            intent.putExtra("from_profile_email", email);
            intent.putExtra("from_profile_image", profileImage);
            startActivity(intent);
        }
        return true;
    }
}
