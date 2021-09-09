package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.ChatAcceptedPatientReceiveParams;
import com.example.doctor360.model.ChatAccptedDoctorReceiveParams;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConnectionDetector connectionDetector;
    String doctorID, doctorName, doctorPhoto;
    TextView txtToolbarDoctorName;
    CircleImageView doctorImage;
    private static final String TAG = "PatientChatActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);

        toolbar = findViewById(R.id.doctorChatToolbar);
        setSupportActionBar(toolbar);
        txtToolbarDoctorName = findViewById(R.id.doctorNameChat);
        doctorImage = findViewById(R.id.doctorImageChat);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        doctorID = intent.getStringExtra("doc_id");
        doctorName = intent.getStringExtra("doc_name");
        doctorPhoto = intent.getStringExtra("doc_photo");

        txtToolbarDoctorName.setText(doctorName);

        if(doctorPhoto!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = doctorPhoto;
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            doctorImage.setImageBitmap(decodedImage);
        } else {
            doctorImage.setImageResource(R.drawable.noimage);
        }

        connectionDetector = new ConnectionDetector(PatientChatActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(PatientChatActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PatientChatActivity.this, PatientDashboardActivity.class);
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
            Intent intent=new Intent(PatientChatActivity.this, PatientDashboardActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
