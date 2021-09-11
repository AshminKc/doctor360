package com.example.doctor360.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.example.doctor360.R;
import com.example.doctor360.fragment.AboutUsFragment;
import com.example.doctor360.fragment.AllHospitalListFragment;
import com.example.doctor360.fragment.ChatAcceptedPatientFragment;
import com.example.doctor360.fragment.DoctorHomeFragment;
import com.example.doctor360.fragment.FAQFragment;
import com.example.doctor360.fragment.PatientChatListFragment;
import com.example.doctor360.fragment.PatientHomeFragment;
import com.example.doctor360.fragment.PrivacyPolicyFragment;
import com.example.doctor360.fragment.RequestAppointmentDoctorFragment;
import com.example.doctor360.fragment.RequestAppointmentPatientFragment;
import com.example.doctor360.fragment.RequestDoctorFragment;
import com.example.doctor360.fragment.ScheduledAppointmentPatientFragment;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.utils.OnDataPasser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class PatientDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnDataPasser {

    Context _context;
    TextView toolbarTitle, txtPatientLoginName, txtPatientViewProfile;
    CircleImageView patientLoginImage;
    ConnectionDetector connectionDetector;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    String patientName, patientID, patientEmail, patientImageView, nameFromProfile, IdFromProfile, emailFromProfile,
            imageFromProfile, patientAddress, patientAge, patientMobile, patientBlood, patientGender, strPatientID;
    private static final String TAG = "PatientDashboardActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        _context = getApplicationContext();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        toolbarTitle = findViewById(R.id.toolbarPatientTitle);
        txtPatientLoginName = headerView.findViewById(R.id.patientLoginName);
        patientLoginImage = headerView.findViewById(R.id.patientProfileImage);
        txtPatientViewProfile = headerView.findViewById(R.id.patientViewProfile);
        toolbar = findViewById(R.id.toolbarPatient);
        setSupportActionBar(toolbar);

        final FrameLayout frameLayout= findViewById(R.id.fragmentContainer1);
        final ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();

        toolbarTitle.setText(getResources().getString(R.string.menu_home));

        final Intent intent = getIntent();
        patientName =  intent.getStringExtra("patient_name");
        patientAddress = intent.getStringExtra("patient_address");
        patientMobile = intent.getStringExtra("patient_mobile");
        patientID =  intent.getStringExtra("patient_id");
        patientEmail = intent.getStringExtra("patient_email");
        patientImageView = intent.getStringExtra("patient_image");
        patientGender = intent.getStringExtra("patient_gender");
        patientAge = intent.getStringExtra("patient_age");
        patientBlood = intent.getStringExtra("patient_blood");

        Hawk.init(getApplicationContext()).build();
        Hawk.put("request_patient_id", patientID);

        final Intent intent1 = getIntent();
        IdFromProfile = intent1.getStringExtra("from_profile_id");
        nameFromProfile = intent1.getStringExtra("from_profile_name");
        emailFromProfile = intent1.getStringExtra("from_profile_email");
        imageFromProfile = intent1.getStringExtra("from_profile_image");

        if(IdFromProfile!= null)
           strPatientID  = IdFromProfile;
        else
            strPatientID = patientID;

        if(patientID!= null)
            strPatientID = patientID;
        else
            strPatientID = IdFromProfile;

        if(nameFromProfile!= null)
            txtPatientLoginName.setText(nameFromProfile);
        else
            txtPatientLoginName.setText(patientName);

        if(patientName!= null)
            txtPatientLoginName.setText(patientName);
        else
            txtPatientLoginName.setText(nameFromProfile);

        if(patientImageView!=null){
            byte[] decodedString = Base64.decode(patientImageView, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            patientLoginImage.setImageBitmap(bitmap);
        } else if(imageFromProfile!=null){
            byte[] decodedString = Base64.decode(imageFromProfile, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            patientLoginImage.setImageBitmap(bitmap);
        } else {
            patientLoginImage.setImageResource(R.drawable.noimage);
        }

        txtPatientViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PatientDashboardActivity.this, PatientProfileActivity.class);
                intent1.putExtra("patient_profile_id", strPatientID);
                intent1.putExtra("patient_profile_name", patientName);
                intent1.putExtra("patient_profile_email", patientEmail);
                if(patientImageView!=null){
                    intent1.putExtra("patient_profile_image", patientImageView);
                } else  {
                    intent1.putExtra("patient_profile_image", imageFromProfile);
                }
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer1, new PatientHomeFragment()).commit();

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

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: Count" +count);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count >= 1) {
            getSupportFragmentManager().popBackStack();
            try {
                navigationView.getMenu().getItem(0).setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        switch (id) {
            case R.id.nav_patient_home: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new PatientHomeFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_home));
                break;
            }

            case R.id.nav_request_doctor:{
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new RequestDoctorFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_request_doctor));
                break;
            }

            case R.id.nav_doctor_request_accepted:{
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new ChatAcceptedPatientFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_accepted_request));
                break;
            }

            case R.id.nav_appointment_request:{
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new RequestAppointmentPatientFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_request_appointment));
                break;
            }

            case R.id.nav_patient_chat_room: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new PatientChatListFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_chat_room));
                break;
            }

            case R.id.nav_scheduled_appointment: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new ScheduledAppointmentPatientFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_scheduled_appointment));
                break;
            }

            case R.id.nav_hospitals: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new AllHospitalListFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_hospitals));
                break;
            }

            case R.id.nav_patient_faq: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new FAQFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_faq));
                break;
            }

            case R.id.nav_about_us: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new AboutUsFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_about_us));
                break;
            }

            case R.id.nav_privacy_policy: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer1,new PrivacyPolicyFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_privacy_policy));
                break;
            }

            case R.id.nav_patient_logout: {
                Logout();
                break;
            }
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

    public void Logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientDashboardActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_logout);
        builder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onChangeToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void setCheckedNavigationItem(int item) {
        navigationView.getMenu().getItem(item).setChecked(true);
    }
}
