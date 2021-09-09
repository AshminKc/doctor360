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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConnectionDetector connectionDetector;
    String patientID, patientName, patientPhoto;
    TextView txtToolbarPatientName;
    CircleImageView patientImage;
    EditText edtEnterText;
    ImageView imgSend;
    FrameLayout chatLayout;
    private static final String TAG = "DoctorChatActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chat);

        toolbar = findViewById(R.id.patientChatToolbar);
        setSupportActionBar(toolbar);

        patientImage = findViewById(R.id.patientImageChat);
        chatLayout = findViewById(R.id.chatFrameLayoutDoctor);
        txtToolbarPatientName = findViewById(R.id.patientNameChat);
        edtEnterText = findViewById(R.id.edtEnterChatTextDoctor);
        imgSend = findViewById(R.id.imgSendTextDoctor);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("pat_id");
        patientName = intent.getStringExtra("pat_name");
        patientPhoto = intent.getStringExtra("pat_photo");

        txtToolbarPatientName.setText(patientName);

        if(patientPhoto!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = patientPhoto;
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            patientImage.setImageBitmap(decodedImage);
        } else {
            patientImage.setImageResource(R.drawable.noimage);
        }

        connectionDetector = new ConnectionDetector(DoctorChatActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(DoctorChatActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }

        chatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(DoctorChatActivity.this, DoctorDashboardActivity.class);
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
            Intent intent=new Intent(DoctorChatActivity.this, DoctorDashboardActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
