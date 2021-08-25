package com.example.doctor360.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView toolbarTitle, txtDoctorLoginName, txtDoctorViewProfile;
    CircleImageView doctorLoginImage;
    ConnectionDetector connectionDetector;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private static final String TAG = "DoctorDashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        toolbarTitle = findViewById(R.id.toolbarDoctorTitle);
        txtDoctorLoginName = headerView.findViewById(R.id.doctorLoginName);
        doctorLoginImage = headerView.findViewById(R.id.doctorProfileImage);
        txtDoctorViewProfile = headerView.findViewById(R.id.doctorViewProfile);

        toolbarTitle.setText(getString(R.string.menu_home));

        final Intent intent = getIntent();
        String doctorName =  intent.getStringExtra("doctor_name");
        txtDoctorLoginName.setText(doctorName);

        txtDoctorViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patientID =  intent.getStringExtra("patient_id");

                Intent intent1 = new Intent(DoctorDashboardActivity.this, PatientProfileActivity.class);
                intent1.putExtra("doctor_profile_id", patientID);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        connectionDetector = new ConnectionDetector(this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(DoctorDashboardActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        }

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        try{
            NavigationMenuView navigationMenuView=(NavigationMenuView) navigationView.getChildAt(0);
        }catch (Exception e){
            e.printStackTrace();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    int count = getSupportFragmentManager().getBackStackEntryCount();
                    Log.d("OnBackChange", "onBackStackChanged: " + count);

                    if (count == 0) {
                        toolbarTitle.setText(getString(R.string.menu_home));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_doctor_home) {
            // Handle the camera action
        } else if (id == R.id.nav_patient_request) {

        } else if (id == R.id.nav_doctor_chat_room) {

        } else if (id == R.id.nav_doctor_faq) {

        } else if (id == R.id.nav_doctor_logout) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
