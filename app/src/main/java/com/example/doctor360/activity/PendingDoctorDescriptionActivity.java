package com.example.doctor360.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorProfileReceiveParams;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.model.RejectDoctorReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.model.VerifyDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.Constants;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingDoctorDescriptionActivity extends AppCompatActivity {

    TextView nameDecTxt, mobileDesTxt,emailDesTxt, genderDesTxt, qualiDesText, specDesTxt, statusDesTxt, toolbarText;
    ImageView documentDesImage;
    Button btnVerify, btnReject;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    String strDoctorId, strDoctorName, encodedImage;
    ConnectionDetector connectionDetector;
    private static final String TAG = "PendingDoctorDescriptio";

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
        strDoctorId = intent.getStringExtra("doctor_id_adapter");
        strDoctorName = intent.getStringExtra("doctor_name_adapter");

        toolbarText.setText("Details of Dr. " +strDoctorName);

        connectionDetector = new ConnectionDetector(PendingDoctorDescriptionActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            Call<DoctorProfileReceiveParams> call = networkClient.viewDoctorProfile(strDoctorId);

            final SweetAlertDialog pDialog = new SweetAlertDialog(PendingDoctorDescriptionActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetching Data....");
            pDialog.setCancelable(false);
            pDialog.show();

            call.enqueue(new Callback<DoctorProfileReceiveParams>() {
                @Override
                public void onResponse(Call<DoctorProfileReceiveParams> call, Response<DoctorProfileReceiveParams> response) {
                    pDialog.dismiss();
                    final DoctorProfileReceiveParams receiveParams = response.body();

                    if(response.body()!=null){
                        String success = receiveParams.getSuccess();
                        if(success.matches("true")){
                            nameDecTxt.setText("DR. "+receiveParams.getData().getName());
                            mobileDesTxt.setText(receiveParams.getData().getMobile());
                            emailDesTxt.setText(receiveParams.getData().getEmail());
                            genderDesTxt.setText(receiveParams.getData().getGender());
                            qualiDesText.setText(receiveParams.getData().getQualification());
                            specDesTxt.setText(receiveParams.getData().getSpecialization());

                            int status = receiveParams.getData().getStatus();
                            if(status == 0)
                                statusDesTxt.setText(R.string.unverified);
                            else
                                statusDesTxt.setText(R.string.verified);

                            if(receiveParams.getData().getDocumentImage()!=null){
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] imageBytes = baos.toByteArray();
                                String imageString = receiveParams.getData().getDocumentImage();
                                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                documentDesImage.setImageBitmap(decodedImage);
                            } else {
                                documentDesImage.setImageResource(R.drawable.noimage);
                            }

                            documentDesImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LayoutInflater factory = LayoutInflater.from(PendingDoctorDescriptionActivity.this);
                                    final View view1 = factory.inflate(R.layout.image_zoom_dailog, null);
                                    ImageView imageDocument = (ImageView) view1.findViewById(R.id.dialogDocImage);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    String imageString = receiveParams.getData().getDocumentImage();
                                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    imageDocument.setImageBitmap(decodedImage);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PendingDoctorDescriptionActivity.this);
                                    builder.setView(imageDocument);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.getWindow().setLayout(600,500);

                                    builder.setView(view1)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    builder.show();
                                }
                            });


                        } else {
                            new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Couldn't fetch data at the moment.")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                        }

                    } else {
                        new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Some Error occurred at Server end. Please try again.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<DoctorProfileReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+ t.toString());
                    if(pDialog!= null && pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                }
            });

        }


        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    verifyDoctor();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    rejectDoctor();
                }
            }
        });
    }

    private void verifyDoctor(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(PendingDoctorDescriptionActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<VerifyDoctorReceiveParams> call = networkClient.verifyDoctor(strDoctorId);
        call.enqueue(new Callback<VerifyDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<VerifyDoctorReceiveParams> call, Response<VerifyDoctorReceiveParams> response) {
                Log.d(TAG, "onResponse: Success");
                pDialog.dismiss();
                new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                        .setTitle("Success")
                        .setMessage("Doctor Verified Successfully")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
                sendVerifiedEmail();
                statusDesTxt.setText("Verified");
                btnVerify.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<VerifyDoctorReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                pDialog.dismiss();
                new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Some error occured. Couldn't Verify.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
            }
        });
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
            message.setText(Constants.VERIFIED_BODY);

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void rejectDoctor(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(PendingDoctorDescriptionActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<RejectDoctorReceiveParams> call = networkClient.rejectDoctor(strDoctorId);
        call.enqueue(new Callback<RejectDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<RejectDoctorReceiveParams> call, Response<RejectDoctorReceiveParams> response) {
                Log.d(TAG, "onResponse: Success");
                pDialog.dismiss();
                new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                        .setTitle("Success")
                        .setMessage("Request Rejected.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
                sendRejectedEmail();
                btnVerify.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RejectDoctorReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                pDialog.dismiss();
                new AestheticDialog.Builder(PendingDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Some error occured. Request rejected.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
            }
        });
    }

    private void sendRejectedEmail(){
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
            startActivity(intent);
        }
        return true;
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
