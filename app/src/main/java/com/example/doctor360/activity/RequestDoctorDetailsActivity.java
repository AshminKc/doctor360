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
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorProfileReceiveParams;
import com.example.doctor360.model.PatientChatRequestReceiveParams;
import com.example.doctor360.model.PatientChatRequestSendParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.SquareImageView;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDoctorDetailsActivity extends AppCompatActivity {

    TextView nameDecTxt, mobileDesTxt,emailDesTxt, genderDesTxt, qualiDesText, specDesTxt, statusDesTxt, toolbarText;
    SquareImageView documentDesImage;
    CircleImageView profileDesImage;
    VerifiedDoctorReceiveParams.DataBean verifiedReceiveParams;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    Button btnRequest, btnCancel;
    String strPatientID, strDoctorID, strDoctorName;
    ConnectionDetector connectionDetector;
    private static final String TAG = "RequestDoctorDetailsAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_doctor_details);

        toolbar = findViewById(R.id.requestDoctorToolbarDetails);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText = findViewById(R.id.requestDoctorToolbarTitleDetails);
        nameDecTxt = findViewById(R.id.requestDoctorNameDetails);
        mobileDesTxt = findViewById(R.id.requestDoctorMobileDetails);
        emailDesTxt = findViewById(R.id.requestDoctorEmailDetails);
        genderDesTxt = findViewById(R.id.requestDoctorGenderDetails);
        qualiDesText = findViewById(R.id.requestDoctorQualificationDetails);
        specDesTxt = findViewById(R.id.requestDoctorSpecializationDetails);
        documentDesImage = findViewById(R.id.requestDoctorDocumentImageDetails);
        profileDesImage = findViewById(R.id.requestDoctorProfileImageDetails);
        statusDesTxt = findViewById(R.id.requestDoctorStatusDetails);
        btnRequest = findViewById(R.id.btnSendRequestDoctorDetails);
        btnCancel = findViewById(R.id.btnCancelRequestDoctorDetails);

        coordinatorLayout = findViewById(R.id.requestDoctorCoordinatorLayout);

        Intent intent = getIntent();
        strDoctorID = intent.getStringExtra("request_doctor_id");
        strDoctorName = intent.getStringExtra("request_doctor_name");

        Hawk.init(getApplicationContext()).build();
        if(Hawk.contains("request_patient_id"))
            strPatientID = Hawk.get("request_patient_id");

        toolbarText.setText("Request to DR. "+ strDoctorName);

        connectionDetector = new ConnectionDetector(RequestDoctorDetailsActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            Call<DoctorProfileReceiveParams> call = networkClient.viewDoctorProfile(strDoctorID);

            final SweetAlertDialog pDialog = new SweetAlertDialog(RequestDoctorDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                            strDoctorID = receiveParams.getData().get_id();
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

                            if(receiveParams.getData().getProfileImg()!=null){
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] imageBytes = baos.toByteArray();
                                String imageString = receiveParams.getData().getProfileImg();
                                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                profileDesImage.setImageBitmap(decodedImage);
                            } else {
                                profileDesImage.setImageResource(R.drawable.noimage);
                            }

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
                                    LayoutInflater factory = LayoutInflater.from(RequestDoctorDetailsActivity.this);
                                    final View view1 = factory.inflate(R.layout.image_zoom_dailog, null);
                                    ImageView imageDocument = (ImageView) view1.findViewById(R.id.dialogDocImage);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    String imageString = receiveParams.getData().getDocumentImage();
                                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    imageDocument.setImageBitmap(decodedImage);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestDoctorDetailsActivity.this);
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
                            new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Couldn't fetch data at the moment.")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                        }

                    } else {
                        new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    sendRequestToDoctor(strPatientID, strDoctorID);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequestToDoctor();
            }
        });
    }

    public void sendRequestToDoctor(String pID, String dID){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        PatientChatRequestSendParams sendParams = new PatientChatRequestSendParams();

        final SweetAlertDialog pDialog = new SweetAlertDialog(RequestDoctorDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sending Request...");
        pDialog.setCancelable(false);
        pDialog.show();

        sendParams.setDoctorId(dID);
        sendParams.setPatientId(pID);
        Call<PatientChatRequestReceiveParams> call = networkClient.sendChatRequest(sendParams);
        call.enqueue(new Callback<PatientChatRequestReceiveParams>() {
            @Override
            public void onResponse(Call<PatientChatRequestReceiveParams> call, Response<PatientChatRequestReceiveParams> response) {
                if(response.body()!=null){
                    final PatientChatRequestReceiveParams receiveParams = response.body();
                    String success = receiveParams.getSuccess();

                    if(success.matches("true")){
                        new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage("Request Send for Chat")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                        btnRequest.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.GONE);
                    } else {
                        new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Couldn't sent request at the moment.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                } else {
                        new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("Some error occured at server end.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PatientChatRequestReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.toString());

                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });
    }

    public void cancelRequestToDoctor(){
        finish();
        Intent intent=new Intent(RequestDoctorDetailsActivity.this, PatientDashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(RequestDoctorDetailsActivity.this, PatientDashboardActivity.class);
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
            Intent intent=new Intent(RequestDoctorDetailsActivity.this, PatientDashboardActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
