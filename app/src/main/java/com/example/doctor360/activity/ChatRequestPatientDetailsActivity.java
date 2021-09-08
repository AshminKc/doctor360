package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorAcceptChatRequestReceiveParams;
import com.example.doctor360.model.DoctorRejectChatRequestReceiveParams;
import com.example.doctor360.model.PatientProfileReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.Constants;
import com.orhanobut.hawk.Hawk;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRequestPatientDetailsActivity extends AppCompatActivity {

    CircleImageView imagePatientProfile;
    TextView txtToolbarTitle, txtPatientName, txtPatientAge, txtPatientAddress, txtPatientMobile, txtPatientEmail,
            txtPatientGender, txtPatientBlood, txtRequestStatus;
    String strPatientID, strPatientName, strChatID;
    ConnectionDetector connectionDetector;
    Button btnAccept, btnReject;
    Toolbar toolbar;
    int status;
    private static final String TAG = "ChatRequestPatientDetai";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_chat_patient_details);

        toolbar = findViewById(R.id.requestChatPatientToolbarDetails);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtToolbarTitle = findViewById(R.id.requestChatPatientToolbarTitleDetails);
        imagePatientProfile = findViewById(R.id.requestChatPatientProfileImageDetails);
        txtPatientName = findViewById(R.id.requestChatPatientNameDetails);
        txtPatientAddress = findViewById(R.id.requestChatPatientAddressDetails);
        txtPatientMobile = findViewById(R.id.requestChatPatientMobileDetails);
        txtPatientEmail = findViewById(R.id.requestChatPatientEmailDetails);
        txtPatientGender = findViewById(R.id.requestChatPatientGenderDetails);
        txtPatientBlood = findViewById(R.id.requestChatPatientBloodDetails);
        txtPatientAge = findViewById(R.id.requestChatPatientAgeDetails);
        txtRequestStatus = findViewById(R.id.requestChatPatientStatusDetails);
        btnAccept = findViewById(R.id.btnAcceptChatRequest);
        btnReject = findViewById(R.id.btnRejectChatRequest);

        Intent intent = getIntent();
        strPatientID = intent.getStringExtra("chat_patient_id");
        strPatientName = intent.getStringExtra("chat_patient_name");
        status = intent.getIntExtra("chat_status",0);
        strChatID = intent.getStringExtra("chat_id");

        Hawk.init(getApplicationContext()).build();

        txtToolbarTitle.setText("Request From "+strPatientName);

        connectionDetector = new ConnectionDetector(ChatRequestPatientDetailsActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            Call<PatientProfileReceiveParams> call = networkClient.viewPatientProfile(strPatientID);

            final SweetAlertDialog pDialog = new SweetAlertDialog(ChatRequestPatientDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetching Data....");
            pDialog.setCancelable(false);
            pDialog.show();

            call.enqueue(new Callback<PatientProfileReceiveParams>() {
                @Override
                public void onResponse(Call<PatientProfileReceiveParams> call, Response<PatientProfileReceiveParams> response) {
                    pDialog.dismiss();
                    final PatientProfileReceiveParams receiveParams = response.body();

                    if (response.body() != null) {
                        String success = receiveParams.getSuccess();
                        if (success.matches("true")) {
                            txtPatientName.setText(receiveParams.getData().getName());
                            txtPatientAddress.setText(receiveParams.getData().getAddress());
                            txtPatientMobile.setText(receiveParams.getData().getMobile());
                            txtPatientEmail.setText(receiveParams.getData().getEmail());
                            txtPatientGender.setText(receiveParams.getData().getGender());
                            txtPatientBlood.setText(receiveParams.getData().getBloodGroup());
                            txtPatientAge.setText(String.valueOf(receiveParams.getData().getAge()));;

                            if (status == 1)
                                txtRequestStatus.setText("Accepted");
                            else
                                txtRequestStatus.setText("Pending");

                            if (receiveParams.getData().getProfileImg() != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] imageBytes = baos.toByteArray();
                                String imageString = receiveParams.getData().getProfileImg();
                                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                imagePatientProfile.setImageBitmap(decodedImage);
                            } else {
                                imagePatientProfile.setImageResource(R.drawable.noimage);
                            }

                        } else {
                            new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Couldn't fetch data at the moment.")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                        }

                    } else {
                        new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
                public void onFailure(Call<PatientProfileReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    acceptChatRequestFromPatient(strChatID);
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    rejectChatRequestFromPatient(strChatID);
                }
            }
        });
    }

    private void acceptChatRequestFromPatient(String chatID){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        final SweetAlertDialog pDialog = new SweetAlertDialog(ChatRequestPatientDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<DoctorAcceptChatRequestReceiveParams> call = networkClient.acceptChatRequest(chatID);
        call.enqueue(new Callback<DoctorAcceptChatRequestReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorAcceptChatRequestReceiveParams> call, Response<DoctorAcceptChatRequestReceiveParams> response) {
                if(response.body()!=null){
                    final DoctorAcceptChatRequestReceiveParams receiveParams = response.body();
                    if(receiveParams.getSuccess().matches("true")){
                        pDialog.dismiss();
                        new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        sendAcceptedEmail();
                        btnAccept.setVisibility(View.GONE);
                        btnReject.setVisibility(View.GONE);
                    } else {
                        pDialog.dismiss();
                        new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't accept at the moment.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                    }
                } else {
                    pDialog.dismiss();
                    new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some error occured at server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<DoctorAcceptChatRequestReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                pDialog.dismiss();
                new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Some error occured. Couldn't Verify.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
            }
        });
    }

    private void rejectChatRequestFromPatient(String chatID){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        final SweetAlertDialog pDialog = new SweetAlertDialog(ChatRequestPatientDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<DoctorRejectChatRequestReceiveParams> call = networkClient.rejectChatRequest(chatID);
        call.enqueue(new Callback<DoctorRejectChatRequestReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorRejectChatRequestReceiveParams> call, Response<DoctorRejectChatRequestReceiveParams> response) {
                if(response.body()!=null){

                    final DoctorRejectChatRequestReceiveParams receiveParams = response.body();
                    if(receiveParams.getSuccess().matches("true")){
                        pDialog.dismiss();
                        new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        sendRejectedEmail();
                        btnAccept.setVisibility(View.GONE);
                        btnReject.setVisibility(View.GONE);
                    } else {
                        pDialog.dismiss();
                        new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't reject at the moment.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                    }

                } else {
                    pDialog.dismiss();
                    new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some error occured at server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<DoctorRejectChatRequestReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                pDialog.dismiss();
                new AestheticDialog.Builder(ChatRequestPatientDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Some error occured. Couldn't Verify.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
            }
        });

    }

    private void sendAcceptedEmail(){
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
            String toEmail = txtPatientEmail.getText().toString().trim();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Chat Request Accepted - Doctor360");
            message.setText("Dear User, \n Your chat request has been accepted.\n\nWith Regards,\nDoctor360");

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
            String toEmail = txtPatientEmail.getText().toString().trim();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Chat Request Rejected - Doctor360");
            message.setText("Dear User, \n Your chat request has been rejected.\n\nWith Regards,\nDoctor360");

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(ChatRequestPatientDetailsActivity.this, DoctorDashboardActivity.class);
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
            Intent intent=new Intent(ChatRequestPatientDetailsActivity.this, DoctorDashboardActivity.class);
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
