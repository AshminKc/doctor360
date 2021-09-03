package com.example.doctor360.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorProfileReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifiedDoctorDescriptionActivity extends AppCompatActivity {

    TextView nameDecTxt, mobileDesTxt,emailDesTxt, genderDesTxt, qualiDesText, specDesTxt, statusDesTxt, toolbarText;
    ImageView documentDesImage;
    VerifiedDoctorReceiveParams.DataBean verifiedReceiveParams;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    String strDoctorId, strDoctorName;
    ConnectionDetector connectionDetector;
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

        connectionDetector = new ConnectionDetector(VerifiedDoctorDescriptionActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(VerifiedDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }

        Intent intent = getIntent();
        strDoctorId = intent.getStringExtra("doctor_id_adapter");
        strDoctorName = intent.getStringExtra("doctor_name_adapter");

        toolbarText.setText("Details of Dr. " + strDoctorName);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(VerifiedDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            Call<DoctorProfileReceiveParams> call = networkClient.viewDoctorProfile(strDoctorId);

            final SweetAlertDialog pDialog = new SweetAlertDialog(VerifiedDoctorDescriptionActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                                    LayoutInflater factory = LayoutInflater.from(VerifiedDoctorDescriptionActivity.this);
                                    final View view1 = factory.inflate(R.layout.image_zoom_dailog, null);
                                    ImageView imageDocument = (ImageView) view1.findViewById(R.id.dialogDocImage);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    String imageString = receiveParams.getData().getDocumentImage();
                                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    imageDocument.setImageBitmap(decodedImage);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifiedDoctorDescriptionActivity.this);
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
                            new AestheticDialog.Builder(VerifiedDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                    .setTitle("Error")
                                    .setMessage("Couldn't fetch data at the moment.")
                                    .setCancelable(true)
                                    .setGravity(Gravity.BOTTOM)
                                    .setDuration(3000)
                                    .show();
                            pDialog.dismiss();
                        }

                    } else {
                        new AestheticDialog.Builder(VerifiedDoctorDescriptionActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(VerifiedDoctorDescriptionActivity.this,AdminDashboardActivity.class);
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
            Intent intent=new Intent(VerifiedDoctorDescriptionActivity.this,AdminDashboardActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
