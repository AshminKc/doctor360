package com.example.doctor360.activity;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView toolbarTitle, txtPatientLoginName, txtPatientViewProfile;
    CircleImageView patientLoginImage;
    ConnectionDetector connectionDetector;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private static final String TAG = "PatientDashboardActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        toolbarTitle = findViewById(R.id.toolbarPatientTitle);
        txtPatientLoginName = headerView.findViewById(R.id.patientLoginName);
        patientLoginImage = headerView.findViewById(R.id.patientProfileImage);
        txtPatientViewProfile = headerView.findViewById(R.id.patientViewProfile);
        toolbar = findViewById(R.id.toolbarPatient);
        setSupportActionBar(toolbar);

       // final FrameLayout frameLayout= findViewById(R.id.fragmentContainer1);
       // final ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();

        toolbarTitle.setText(getString(R.string.menu_home));

        final Intent intent = getIntent();
        String patientName =  intent.getStringExtra("patient_name");
        txtPatientLoginName.setText(patientName);

        txtPatientViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patientID =  intent.getStringExtra("patient_id");

                Intent intent1 = new Intent(PatientDashboardActivity.this, PatientProfileActivity.class);
                intent1.putExtra("patient_profile_id", patientID);
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
            new AestheticDialog.Builder(PatientDashboardActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(2500)
                    .show();
        }


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
        toolbarTitle.setText(getString(R.string.menu_home));

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_patient_home) {

        } else if (id == R.id.nav_doctor_request) {

        } else if (id == R.id.nav_patient_chat_room) {

        } else if (id == R.id.nav_doctors_list) {

        } else if (id == R.id.nav_patient_faq) {

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_privacy_policy) {

        } else if (id == R.id.nav_patient_logout) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void exitApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientDashboardActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_logout);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
