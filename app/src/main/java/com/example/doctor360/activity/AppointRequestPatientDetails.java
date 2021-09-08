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
import androidx.constraintlayout.solver.widgets.HelperWidget;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorAcceptAppointmentReceiveParams;
import com.example.doctor360.model.DoctorProfileReceiveParams;
import com.example.doctor360.model.DoctorRejectAppointmentReceiveParams;
import com.example.doctor360.model.DoctorRequestAppoitmentReceiveParams;
import com.example.doctor360.model.PatientProfileReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.Constants;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import org.w3c.dom.Text;

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

public class AppointRequestPatientDetails extends AppCompatActivity {

    CircleImageView imagePatientProfile;
    TextView txtToolbarTitle, txtPatientName, txtPatientAge, txtPatientAddress, txtPatientMobile, txtPatientEmail, txtPatientGender, txtPatientBlood,
    txtRequestDate, txtRequestTime, txtRequestRemarks, txtRequestStatus;
    String strAppointId, strDate, strTime, strRemarks, strPatientID, strPatientName, strDoctorId;
    ConnectionDetector connectionDetector;
    Button btnAccept, btnReject;
    Toolbar toolbar;
    int status;
    private static final String TAG = "AppointRequestPatientDe";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_appointment_patient_details);

        toolbar = findViewById(R.id.requestFromPatientToolbarDetails);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtToolbarTitle = findViewById(R.id.requestFromPatientToolbarTitleDetails);
        imagePatientProfile = findViewById(R.id.requestFromPatientProfileImageDetails);
        txtPatientName = findViewById(R.id.requestFromPatientNameDetails);
        txtPatientAddress = findViewById(R.id.requestFromPatientAddressDetails);
        txtPatientMobile = findViewById(R.id.requestFromPatientMobileDetails);
        txtPatientEmail = findViewById(R.id.requestFromPatientEmailDetails);
        txtPatientGender = findViewById(R.id.requestFromPatientGenderDetails);
        txtPatientBlood = findViewById(R.id.requestFromPatientBloodDetails);
        txtPatientAge = findViewById(R.id.requestFromPatientAgeDetails);
        txtRequestStatus = findViewById(R.id.requestFromPatientStatusDetails);
        txtRequestRemarks = findViewById(R.id.requestFromPatientRemarksDetails);
        txtRequestDate = findViewById(R.id.requestFromPatientDateDetails);
        txtRequestTime = findViewById(R.id.requestFromPatientTimeDetails);
        btnAccept = findViewById(R.id.btnAcceptAppointmentRequest);
        btnReject = findViewById(R.id.btnCancelAppointmentRequest);

        Intent intent = getIntent();
        strAppointId = intent.getStringExtra("appoint_id");
        Log.d(TAG, "onCreate: Appint id" + strAppointId);
        strPatientID = intent.getStringExtra("appoint_patient_id");
        strPatientName = intent.getStringExtra("appoint_patient_name");
        strDate = intent.getStringExtra("appoint_date");
        strTime = intent.getStringExtra("appoint_time");
        strRemarks = intent.getStringExtra("appoint_remarks");
        status = intent.getIntExtra("appoint_status",0);
      //  strDoctorId = intent.getStringExtra("appoint_doctor_id");

        Hawk.init(getApplicationContext()).build();

        txtToolbarTitle.setText("Request From "+strPatientName);

        connectionDetector = new ConnectionDetector(AppointRequestPatientDetails.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            Call<PatientProfileReceiveParams> call = networkClient.viewPatientProfile(strPatientID);

            final SweetAlertDialog pDialog = new SweetAlertDialog(AppointRequestPatientDetails.this, SweetAlertDialog.PROGRESS_TYPE);
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
                            txtPatientAge.setText(String.valueOf(receiveParams.getData().getAge()));
                            txtRequestDate.setText(strDate);
                            txtRequestTime.setText(strTime);
                            txtRequestRemarks.setText(strRemarks);

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
                            new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Couldn't fetch data at the moment.")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                        }

                    } else {
                        new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
                    new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    acceptAppointment(strAppointId);
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    rejectAppointment(strAppointId);
                }
            }
        });
    }

    private void acceptAppointment(String appointID){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        final SweetAlertDialog pDialog = new SweetAlertDialog(AppointRequestPatientDetails.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<DoctorAcceptAppointmentReceiveParams> call = networkClient.acceptAppointment(appointID);
        call.enqueue(new Callback<DoctorAcceptAppointmentReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorAcceptAppointmentReceiveParams> call, Response<DoctorAcceptAppointmentReceiveParams> response) {
                if (response.body() != null) {
                    final DoctorAcceptAppointmentReceiveParams receiveParams = response.body();

                    if (receiveParams.getSuccess().matches("true")) {
                        pDialog.dismiss();
                        new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
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
                        new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't accept at the moment.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                    }
                } else {
                    pDialog.dismiss();
                    new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some error occured at server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<DoctorAcceptAppointmentReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                pDialog.dismiss();
                new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Some error occured. Couldn't Verify.")
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setDuration(3000)
                        .show();
            }
        });
    }

    private void rejectAppointment(String appointID){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        final SweetAlertDialog pDialog = new SweetAlertDialog(AppointRequestPatientDetails.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<DoctorRejectAppointmentReceiveParams> call = networkClient.rejectAppointment(appointID);
        call.enqueue(new Callback<DoctorRejectAppointmentReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorRejectAppointmentReceiveParams> call, Response<DoctorRejectAppointmentReceiveParams> response) {
                if (response.body() != null) {
                    final DoctorRejectAppointmentReceiveParams receiveParams = response.body();

                    if (receiveParams.getSuccess().matches("true")) {
                        pDialog.dismiss();
                        new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage("Appointment Request Rejected.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        sendRejectedEmail();
                        txtRequestStatus.setText("Rejected");
                        btnAccept.setVisibility(View.GONE);
                        btnReject.setVisibility(View.GONE);
                    } else {
                        pDialog.dismiss();
                        new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't reject at the moment.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                    }
                } else {
                    pDialog.dismiss();
                    new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some error occured at server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<DoctorRejectAppointmentReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verify " + t.toString());
                pDialog.dismiss();
                new AestheticDialog.Builder(AppointRequestPatientDetails.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
            String toDate = txtRequestDate.getText().toString().trim();
            String toTime = txtRequestTime.getText().toString().trim();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Appointment Confirmed - Doctor360");
            message.setText("Dear User, \n Your appointment has been scheduled on "+toDate+" "+toTime+ ".\n Please consult with your preferred doctor.\n\nWith Regards,\nDoctor360");

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
            message.setSubject("Appointment Rejected - Doctor360");
            message.setText("Dear User, \n Your appointment has been rejected.\n\nWith Regards,\nDoctor360");

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(AppointRequestPatientDetails.this, DoctorDashboardActivity.class);
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
            Intent intent=new Intent(AppointRequestPatientDetails.this, DoctorDashboardActivity.class);
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
