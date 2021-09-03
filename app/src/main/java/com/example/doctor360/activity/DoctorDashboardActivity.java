package com.example.doctor360.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.doctor360.R;
import com.example.doctor360.fragment.DoctorHomeFragment;
import com.example.doctor360.fragment.FAQFragment;
import com.example.doctor360.helper.ConnectionDetector;
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
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DoctorDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView toolbarTitle, txtDoctorLoginName, txtDoctorViewProfile;
    CircleImageView doctorLoginImage;
    ConnectionDetector connectionDetector;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    String doctorName, doctorID, doctorEmail, doctorImageView, nameFromProfile, IdFromProfile, emailFromProfile,
            imageFromProfile, doctorSpec, doctorMobile, doctorQuali, doctorGender, doctorDocument, strDoctorID;
    private static final String TAG = "DoctorDashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        toolbarTitle = findViewById(R.id.toolbarDoctorTitle);
        txtDoctorLoginName = headerView.findViewById(R.id.doctorLoginName);
        doctorLoginImage = headerView.findViewById(R.id.doctorProfileImage);
        txtDoctorViewProfile = headerView.findViewById(R.id.doctorViewProfile);

        toolbarTitle.setText(getString(R.string.menu_home));

        final Intent intent = getIntent();
        doctorName =  intent.getStringExtra("doctor_name");
        doctorEmail = intent.getStringExtra("doctor_email");
        doctorMobile = intent.getStringExtra("doctor_mobile");
        doctorID =  intent.getStringExtra("doctor_id");
        doctorImageView = intent.getStringExtra("doctor_image");
        doctorGender = intent.getStringExtra("doctor_gender");
        doctorQuali = intent.getStringExtra("doctor_quali");
        doctorSpec = intent.getStringExtra("doctor_spec");
        doctorDocument = intent.getStringExtra("doctor_document");

        final Intent intent1 = getIntent();
        IdFromProfile = intent1.getStringExtra("from_profile_id");
        nameFromProfile = intent1.getStringExtra("from_profile_name");
        emailFromProfile = intent1.getStringExtra("from_profile_email");
        imageFromProfile = intent1.getStringExtra("from_profile_image");

        if(IdFromProfile!= null)
            strDoctorID = IdFromProfile;
        else
           strDoctorID = doctorID;

        if(doctorID!= null)
            strDoctorID = doctorID;
        else
           strDoctorID = IdFromProfile;

        if(nameFromProfile!= null)
            txtDoctorLoginName.setText("DR. "+nameFromProfile);
        else
            txtDoctorLoginName.setText("DR. "+doctorName);

        if(doctorName!= null)
            txtDoctorLoginName.setText("DR. "+doctorName);
        else
            txtDoctorLoginName.setText("DR. "+nameFromProfile);

        if(doctorImageView!=null){
            byte[] decodedString = Base64.decode(doctorImageView, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            doctorLoginImage.setImageBitmap(bitmap);
        } else if(imageFromProfile!=null) {
            byte[] decodedString = Base64.decode(imageFromProfile, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            doctorLoginImage.setImageBitmap(bitmap);
        } else {
            doctorLoginImage.setImageResource(R.drawable.noimage);
        }

        txtDoctorViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DoctorDashboardActivity.this, DoctorProfileActivity.class);
                intent1.putExtra("doctor_profile_id", strDoctorID);
                intent1.putExtra("doctor_profile_name", doctorName);
                intent1.putExtra("doctor_profile_email", doctorEmail);
                if(doctorImageView!=null){
                    intent1.putExtra("doctor_profile_image", doctorImageView);
                } else  {
                    intent1.putExtra("doctor_profile_image", imageFromProfile);
                }

              //  intent1.putExtra("doctor_document_image", doctorDocument);
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, new DoctorHomeFragment()).commit();

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
       // getMenuInflater().inflate(R.menu.doctor_dashboard, menu);
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
            case R.id.nav_doctor_chat_room: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer2,new DoctorHomeFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_home));
                break;
            }

            case R.id.nav_doctor_faq: {
                getSupportFragmentManager().popBackStackImmediate();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer2,new FAQFragment()).addToBackStack("").commit();
                toolbarTitle.setText(getString(R.string.menu_faq));
                break;
            }

            case R.id.nav_doctor_logout:
                Logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void exitApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorDashboardActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorDashboardActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_logout);
        builder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }
}
