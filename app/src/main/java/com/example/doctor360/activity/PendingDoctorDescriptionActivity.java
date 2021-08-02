package com.example.doctor360.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.doctor360.R;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.utils.Constants;
import com.squareup.picasso.Picasso;
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

public class PendingDoctorDescriptionActivity extends AppCompatActivity {

    TextView nameDecTxt, mobileDesTxt,emailDesTxt, genderDesTxt, qualiDesText, specDesTxt, statusDesTxt, toolbarText;
    ImageView documentDesImage;
    Button btnVerify, btnReject;
    String doctorID;
    PendingDoctorReceiveParams.DataBean pendingReceiveParams;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_doctor_description);

        toolbar = findViewById(R.id.pendingDoctorToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText = findViewById(R.id.pendingDoctorToolbarTitle);
        nameDecTxt = findViewById(R.id.pendingDoctorName);
        mobileDesTxt = findViewById(R.id.pendingDoctorMobile);
        emailDesTxt = findViewById(R.id.pendingDoctorEmail);
        genderDesTxt = findViewById(R.id.pendingDoctorGender);
        qualiDesText = findViewById(R.id.pendingDoctorQualification);
        specDesTxt = findViewById(R.id.pendingDoctorSpecialization);
        documentDesImage = findViewById(R.id.pendingDoctorImage);
        statusDesTxt = findViewById(R.id.pendingDoctorStatus);
        coordinatorLayout = findViewById(R.id.pendingCoordinatorLayout);
        btnVerify = findViewById(R.id.btnVerifyPendingDoctor);
        btnReject = findViewById(R.id.btnRejectPendingDoctor);

        Intent intent = getIntent();
        pendingReceiveParams = (PendingDoctorReceiveParams.DataBean) intent.getSerializableExtra("obj");

        toolbarText.setText("Details of " + pendingReceiveParams.getName());
        nameDecTxt.setText(pendingReceiveParams.getName());
        mobileDesTxt.setText(pendingReceiveParams.getMobile());
        emailDesTxt.setText(pendingReceiveParams.getEmail());
        genderDesTxt.setText(pendingReceiveParams.getGender());
        qualiDesText.setText(pendingReceiveParams.getQualification());
        specDesTxt.setText(pendingReceiveParams.getSpecialization());

        scaleGestureDetector = new ScaleGestureDetector(PendingDoctorDescriptionActivity.this, new ScaleListener());

        int status = pendingReceiveParams.getStatus();
        if(status == 0)
            statusDesTxt.setText(R.string.unverified);
        else
            statusDesTxt.setText(R.string.verified);

        Picasso.with(PendingDoctorDescriptionActivity.this)
                .load(pendingReceiveParams.getDocumentImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(documentDesImage);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyDoctor();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Information")
                        .setMessage("Request Rejected.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();

                rejectDoctor();
            }
        });

    }

    private void verifyDoctor(){
        new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                .setTitle("Information")
                .setMessage("Request Rejected.")
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setDuration(3000)
                .show();

        sendVerifiedEmail();
    }

    private void sendVerifiedEmail(){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.EMAIL, Constants.PASSWORD);
            }
        });

        try {
            String toEmail = emailDesTxt.getText().toString().trim();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(Constants.VERIFIED_SUBJECT);
            message.setText(Constants.VERIFIED_SUBJECT);

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void rejectDoctor(){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.EMAIL, Constants.PASSWORD);
            }
        });

        try {
            String toEmail = emailDesTxt.getText().toString().trim();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(Constants.REJECTED_SUBJECT);
            message.setText(Constants.REJECTED_BODY);

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PendingDoctorDescriptionActivity.this,AdminDashboardActivity.class);
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
            Intent intent=new Intent(PendingDoctorDescriptionActivity.this,AdminDashboardActivity.class);
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

    private class SendMail extends AsyncTask<Message, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Message... messages) {
            try{
                Transport.send(messages[0]);
                return "Success";

            } catch (MessagingException e){
                e.printStackTrace();
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
