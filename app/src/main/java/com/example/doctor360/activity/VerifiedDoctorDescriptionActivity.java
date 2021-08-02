package com.example.doctor360.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.doctor360.R;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.squareup.picasso.Picasso;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class VerifiedDoctorDescriptionActivity extends AppCompatActivity {

    TextView nameDecTxt, mobileDesTxt,emailDesTxt, genderDesTxt, qualiDesText, specDesTxt, statusDesTxt, toolbarText;
    ImageView documentDesImage;
    VerifiedDoctorReceiveParams.DataBean verifiedReceiveParams;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private static final String TAG = "VerifiedDoctorDescripti";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_doctor_description);

        toolbar = findViewById(R.id.verifiedDoctorToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText = findViewById(R.id.verifiedDoctorToolbarTitle);
        nameDecTxt = findViewById(R.id.verifiedDoctorName);
        mobileDesTxt = findViewById(R.id.verifiedDoctorMobile);
        emailDesTxt = findViewById(R.id.verifiedDoctorEmail);
        genderDesTxt = findViewById(R.id.verifiedDoctorGender);
        qualiDesText = findViewById(R.id.verifiedDoctorQualification);
        specDesTxt = findViewById(R.id.verifiedDoctorSpecialization);
        documentDesImage = findViewById(R.id.verifiedDoctorImage);
        statusDesTxt = findViewById(R.id.verifiedDoctorStatus);
        coordinatorLayout = findViewById(R.id.verifiedCoordinatorLayout);

        Intent intent = getIntent();
        verifiedReceiveParams = (VerifiedDoctorReceiveParams.DataBean) intent.getSerializableExtra("obj");

        toolbarText.setText("Details of " + verifiedReceiveParams.getName());
        nameDecTxt.setText(verifiedReceiveParams.getName());
        mobileDesTxt.setText(verifiedReceiveParams.getMobile());
        emailDesTxt.setText(verifiedReceiveParams.getEmail());
        genderDesTxt.setText(verifiedReceiveParams.getGender());
        qualiDesText.setText(verifiedReceiveParams.getQualification());
        specDesTxt.setText(verifiedReceiveParams.getSpecialization());

        scaleGestureDetector = new ScaleGestureDetector(VerifiedDoctorDescriptionActivity.this, new ScaleListener());

        int status = verifiedReceiveParams.getStatus();
        if(status == 0)
            statusDesTxt.setText(R.string.unverified);
        else
            statusDesTxt.setText(R.string.verified);

        Picasso.with(VerifiedDoctorDescriptionActivity.this)
                .load(verifiedReceiveParams.getDocumentImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(documentDesImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(VerifiedDoctorDescriptionActivity.this,AdminDashboardActivity.class);
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
            Intent intent=new Intent(VerifiedDoctorDescriptionActivity.this,AdminDashboardActivity.class);
            overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            documentDesImage.setScaleX(mScaleFactor);
            documentDesImage.setScaleY(mScaleFactor);
            return true;
        }
    }
    }
