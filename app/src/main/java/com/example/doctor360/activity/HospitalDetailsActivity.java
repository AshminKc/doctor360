package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.HospitalListReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import retrofit2.Call;

public class HospitalDetailsActivity extends AppCompatActivity {

    String strHospitalName, strHospitalAddress, strHospitalPhone, strHospitalEmail, strHospitalWebsite, strHospitalContact, strHospitalContactMobile;
    ConnectionDetector connectionDetector;
    Toolbar toolbar;
    TextView toolbarTitle, hospitalName, hospitalAddress, hospitalPhone, hospitalEmail, hospitalWebsite, hospitalContact, hospitalContactMobile;
    private static final String TAG = "HospitalDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);

        toolbar = findViewById(R.id.hospitalDetailsToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarTitle = findViewById(R.id.hospitalDetailsToolbarTitle);
        hospitalName = findViewById(R.id.txtHospitalNameDetails);
        hospitalAddress = findViewById(R.id.txtHospitalAddressDetails);
        hospitalPhone = findViewById(R.id.txtHospitalPhoneDetails);
        hospitalEmail = findViewById(R.id.txtHospitalEmailDetails);
        hospitalWebsite = findViewById(R.id.txtHospitalWebsiteDetails);
        hospitalContact = findViewById(R.id.txtHospitalContactPersonDetails);
        hospitalContactMobile = findViewById(R.id.txtHospitalContactMobileDetails);

        toolbarTitle.setText("Hospital Details");
        connectionDetector = new ConnectionDetector(getApplicationContext());

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(HospitalDetailsActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            getHospitals();
        }
    }

    private void getHospitals(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(HospitalDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting...");
        pDialog.setCancelable(false);
        pDialog.show();

        Intent intent = getIntent();
        strHospitalName = intent.getStringExtra("hospital_name");
        strHospitalAddress = intent.getStringExtra("hospital_address");
        strHospitalPhone = intent.getStringExtra("hospital_phone");
        strHospitalEmail = intent.getStringExtra("hospital_email");
        strHospitalWebsite = intent.getStringExtra("hospital_website");
        strHospitalContact = intent.getStringExtra("hospital_contact_name");
        strHospitalContactMobile = intent.getStringExtra("hospital_contact_mobile");

        hospitalName.setText(strHospitalName);
        hospitalAddress.setText(strHospitalAddress);
        hospitalPhone.setText(strHospitalPhone);
        hospitalEmail.setText(strHospitalEmail);
        hospitalWebsite.setText(strHospitalWebsite);
        hospitalContact.setText(strHospitalContact);
        hospitalContactMobile.setText(strHospitalContactMobile);

        pDialog.dismiss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(HospitalDetailsActivity.this, PatientDashboardActivity.class);
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
            Intent intent=new Intent(HospitalDetailsActivity.this, PatientDashboardActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
