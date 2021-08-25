package com.example.doctor360.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.utils.SquareImageView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView imgProfile;
    SquareImageView imgDocument;
    TextView txtName, txtMobile, txtEmail, txtGender, txtSpec, txtQuali;
    Button btnUpdateProfile, btnChangePassword;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        toolbar = findViewById(R.id.patientProfileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgProfile = findViewById(R.id.viewDoctorProfileImage);
        imgDocument = findViewById(R.id.viewDoctorProfileDocument);
        txtName = findViewById(R.id.viewDoctorProfileName);
        txtMobile = findViewById(R.id.viewDoctorProfileMobile);
        txtEmail = findViewById(R.id.viewDoctorProfileEmail);
        txtGender = findViewById(R.id.viewDoctorProfileGender);
        txtSpec = findViewById(R.id.viewDoctorProfileSpecialization);
        txtQuali = findViewById(R.id.viewDoctorProfileQualification);
        btnUpdateProfile = findViewById(R.id.buttonDoctorUpdateProfile);
        btnChangePassword = findViewById(R.id.buttonDoctorChangePassword);

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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(DoctorProfileActivity.this, DoctorDashboardActivity.class);
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
            Intent intent=new Intent(DoctorProfileActivity.this,DoctorDashboardActivity.class);
            overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
            startActivity(intent);
        }
        return true;
    }
}
