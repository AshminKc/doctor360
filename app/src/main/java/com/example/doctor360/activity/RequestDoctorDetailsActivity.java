package com.example.doctor360.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.utils.SquareImageView;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RequestDoctorDetailsActivity extends AppCompatActivity {

    TextView nameDecTxt, mobileDesTxt,emailDesTxt, genderDesTxt, qualiDesText, specDesTxt, statusDesTxt, toolbarText;
    SquareImageView documentDesImage;
    CircleImageView profileDesImage;
    VerifiedDoctorReceiveParams.DataBean verifiedReceiveParams;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    Button btnRequest, btnCancel;
    String strPatientID, strDoctorID;
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
        verifiedReceiveParams = (VerifiedDoctorReceiveParams.DataBean) intent.getSerializableExtra("obj1");

        strDoctorID = verifiedReceiveParams.get_id();

        Hawk.init(getApplicationContext()).build();
        if(Hawk.contains("request_patient_id"))
            strPatientID = Hawk.get("request_patient_id");

        Toasty.success(getApplicationContext(),"Doctor ID" + strDoctorID, 300).show();
        Toasty.success(getApplicationContext(),"Patient ID" + strPatientID, 300).show();

        toolbarText.setText("Request to DR. "+ verifiedReceiveParams.getName());
        nameDecTxt.setText("DR. "+ verifiedReceiveParams.getName());
        mobileDesTxt.setText(verifiedReceiveParams.getMobile());
        emailDesTxt.setText(verifiedReceiveParams.getEmail());
        genderDesTxt.setText(verifiedReceiveParams.getGender());
        qualiDesText.setText(verifiedReceiveParams.getQualification());
        specDesTxt.setText(verifiedReceiveParams.getSpecialization());

        int status = verifiedReceiveParams.getStatus();
        if(status == 0)
            statusDesTxt.setText(R.string.unverified);
        else
            statusDesTxt.setText(R.string.verified);

        if(verifiedReceiveParams.getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = verifiedReceiveParams.getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileDesImage.setImageBitmap(decodedImage);
        } else {
            profileDesImage.setImageResource(R.drawable.noimage);
        }

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = verifiedReceiveParams.getDocumentImage();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            documentDesImage.setImageBitmap(decodedImage);

            documentDesImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater factory = LayoutInflater.from(RequestDoctorDetailsActivity.this);
                    final View view1 = factory.inflate(R.layout.image_zoom_dailog, null);
                    ImageView imageDocument = (ImageView) view1.findViewById(R.id.dialogDocImage);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = verifiedReceiveParams.getDocumentImage();
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
        } catch (IllegalArgumentException e) {
            Toasty.error(getApplicationContext(),"Couldn't process image", 300).show();
        }

        connectionDetector = new ConnectionDetector(RequestDoctorDetailsActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(RequestDoctorDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
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
                    sendRequestToDoctor();
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

    public void sendRequestToDoctor(){

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
