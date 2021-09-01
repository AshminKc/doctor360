package com.example.doctor360.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorRegistrationReceiveParams;
import com.example.doctor360.model.PatientUpdateProfileReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.doctor360.utils.Constants.REQUEST_GALLERY_CODE;

public class PatientUpdateProfileActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Bitmap bitmap;
    ConnectionDetector connectionDetector;
    CircleImageView imagePatientProfile;
    TextView txtCurrentAge, txtCurrentBlood;
    AppCompatEditText edtUpdateName, edtUpdateAddress, edtUpdateEmail, edtUpdateMobile, edtUpdateGender;
    MaterialSpinner spinnerUpdateAge, spinnerUpdateBlood;
    String strPatientId, strUpdateName, strUpdateAddress, strUpdateEmail, strUpdateMobile, strUpdateGender, strUpdateAge, strUpdateBlood, strUpdatePP, encodedImage;
    Button btnUpdateDetails, btnUploadProfilePic;
    String namePattern = "^[A-Za-z\\s]+$";
    String mobilePattern = "^[0-9]{10}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "PatientUpdateProfileAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_update_profile);

        toolbar = findViewById(R.id.patientUpdateProfileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagePatientProfile = findViewById(R.id.imageUpdatePatientImage);
        btnUploadProfilePic = findViewById(R.id.btnUploadPatientProfilePic);
        edtUpdateName = findViewById(R.id.edtUpdatePatientName);
        edtUpdateAddress = findViewById(R.id.edtUpdatePatientAddress);
        edtUpdateEmail = findViewById(R.id.edtUpdatePatientEmail);
        edtUpdateMobile = findViewById(R.id.edtUpdatePatientMobile);
        edtUpdateGender = findViewById(R.id.edtUpdatePatientGender);
        spinnerUpdateAge = findViewById(R.id.spinnerUpdatePatientAge);
        spinnerUpdateBlood = findViewById(R.id.spinnerUpdatePatientBlood);
        btnUpdateDetails = findViewById(R.id.btnUpdatePatientDetails);
        txtCurrentAge = findViewById(R.id.txtCurrentAgeValue);
        txtCurrentBlood = findViewById(R.id.txtCurrentBloodValue);

        Intent intent = getIntent();
        strPatientId = intent.getStringExtra("patient_update_id");
        strUpdateName = intent.getStringExtra("patient_update_name");
        strUpdateAddress = intent.getStringExtra("patient_update_address");
        strUpdateMobile = intent.getStringExtra("patient_update_mobile");
        strUpdateEmail = intent.getStringExtra("patient_update_email");
        strUpdateGender = intent.getStringExtra("patient_update_gender");
        strUpdateAge = intent.getStringExtra("patient_update_age");
        strUpdateBlood = intent.getStringExtra("patient_update_blood");
        strUpdatePP = intent.getStringExtra("patient_update_image");

        edtUpdateName.setText(strUpdateName);
        edtUpdateAddress.setText(strUpdateAddress);
        edtUpdateMobile.setText(strUpdateMobile);
        edtUpdateEmail.setText(strUpdateEmail);
        edtUpdateGender.setText(strUpdateGender);
        txtCurrentAge.setText(strUpdateAge);
        txtCurrentBlood.setText(strUpdateBlood);

        if(strUpdatePP!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(strUpdatePP, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imagePatientProfile.setImageBitmap(decodedImage);
        } else {
            imagePatientProfile.setImageResource(R.drawable.noimage);
        }

        spinnerUpdateBlood.setItems("Select", "A+","A-","B+","B-","O+","O-","AB+","AB-");
        spinnerUpdateBlood.setHint("Select your blood group");
        spinnerUpdateBlood.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerUpdateBlood.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strUpdateBlood = item.toString();

            }
        });

        spinnerUpdateBlood.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {

            }
        });

        spinnerUpdateAge.setItems("Select","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                "21","22","23","24","25","26","27","28","29","30","31","32","33","35","36","37","38","39","40","41","42","43","44","45",
                "46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70",
                "71","72","73","74","75","76","77","78","79","80","80","81","82","83","84","85","86","87","88","89","90","91","92","93",
                "94","95","96","97","98","99","100");
        spinnerUpdateAge.setHint("Select your age");
        spinnerUpdateAge.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerUpdateAge.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strUpdateAge = item.toString();

            }
        });

        spinnerUpdateAge.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {

            }
        });

        connectionDetector = new ConnectionDetector(PatientUpdateProfileActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(PatientUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }

        btnUploadProfilePic.setOnClickListener(this);
        btnUpdateDetails.setOnClickListener(this);

    }

    public void checkFields(){
        strUpdateName = edtUpdateName.getText().toString();
        strUpdateAddress = edtUpdateAddress.getText().toString();
        strUpdateEmail = edtUpdateEmail.getText().toString();
        strUpdateMobile = edtUpdateMobile.getText().toString();
        strUpdateAge = spinnerUpdateAge.getText().toString();
        strUpdateGender = edtUpdateGender.getText().toString();
        strUpdateBlood = spinnerUpdateBlood.getText().toString();


        if(strUpdateName.isEmpty()){
            Toasty.error(PatientUpdateProfileActivity.this,"Please Enter Full Name",300).show();
        } else if(!strUpdateName.matches(namePattern)){
            Toasty.error(PatientUpdateProfileActivity.this,"Only Alphabet are allowed",300).show();
        } else if(strUpdateAddress.isEmpty()){
            Toasty.error(PatientUpdateProfileActivity.this,"Please Enter Address",300).show();
        } else if(strUpdateEmail.isEmpty()){
            Toasty.error(PatientUpdateProfileActivity.this,"Please Enter Email",300).show();
        } else if(!strUpdateEmail.matches(emailPattern)){
            Toasty.error(PatientUpdateProfileActivity.this,"Invalid Email",300).show();
        } else if(strUpdateMobile.isEmpty()){
            Toasty.error(PatientUpdateProfileActivity.this,"Please Enter Mobile No",300).show();
        } else if(!strUpdateMobile.matches(mobilePattern)){
            Toasty.error(PatientUpdateProfileActivity.this,"Invalid Mobile No",300).show();
        } else if(imagePatientProfile.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.noimage).getConstantState()){
            Toasty.error(PatientUpdateProfileActivity.this,"Image is required (jpg, jepg, png)",300).show();
        } else{
            updatePatientProfile();
        }
    }

    public void updatePatientProfile(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        String id = strPatientId;
        strUpdateName = edtUpdateName.getText().toString();
        strUpdateAddress = edtUpdateAddress.getText().toString();
        strUpdateEmail = edtUpdateEmail.getText().toString();
        strUpdateMobile = edtUpdateMobile.getText().toString();
        strUpdateAge = spinnerUpdateAge.getText().toString();
        strUpdateGender = edtUpdateGender.getText().toString();
        strUpdateBlood = spinnerUpdateBlood.getText().toString();

        if(bitmap!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        } else {
            byte[] decodedString = Base64.decode(strUpdatePP, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            encodedImage = Base64.encodeToString(decodedString, Base64.DEFAULT);
        }

        if(strUpdateAge.matches("Select"))
            strUpdateAge = txtCurrentAge.getText().toString();
        else
            strUpdateAge = spinnerUpdateAge.getText().toString();

        if(strUpdateBlood.matches("Select"))
            strUpdateBlood = txtCurrentBlood.getText().toString();
        else
            strUpdateBlood = spinnerUpdateBlood.getText().toString();

        final SweetAlertDialog pDialog = new SweetAlertDialog(PatientUpdateProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PatientUpdateProfileReceiveParams> call = networkClient.updatePatientProfile(id, strUpdateName, strUpdateAddress,
                strUpdateEmail, strUpdateMobile, strUpdateGender, strUpdateAge, strUpdateBlood, encodedImage);
        call.enqueue(new Callback<PatientUpdateProfileReceiveParams>() {
            @Override
            public void onResponse(Call<PatientUpdateProfileReceiveParams> call, Response<PatientUpdateProfileReceiveParams> response) {
                PatientUpdateProfileReceiveParams receiveParams = response.body();

                if(response.body()!=null){
                    String status = receiveParams.getSuccess();

                    if(status.matches("true")){
                        Toasty.success(getApplicationContext(),"Updated Successfully. Re-login to see the changes.", 300).show();
                        Intent intent = new Intent(PatientUpdateProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        pDialog.dismiss();

                    } else {
                        new AestheticDialog.Builder(PatientUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }

                } else {
                    new AestheticDialog.Builder(PatientUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<PatientUpdateProfileReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.toString());
                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });

    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_GALLERY_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PatientUpdateProfileActivity.this, PatientDashboardActivity.class);
        intent.putExtra("from_profile_id", strPatientId);
        intent.putExtra("from_profile_name", strUpdateName);
        intent.putExtra("from_profile_image", strUpdatePP);
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
            Intent intent=new Intent(PatientUpdateProfileActivity.this, PatientDashboardActivity.class);
            intent.putExtra("from_profile_id", strPatientId);
            intent.putExtra("from_profile_name", strUpdateName);
            intent.putExtra("from_profile_image", strUpdatePP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY_CODE && data!=null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imagePatientProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnUpdatePatientDetails:
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(PatientUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("No Internet Connection!!")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                } else {
                    checkFields();
                }
                break;

            case R.id.btnUploadPatientProfilePic:
                selectImage();
                break;
        }
    }
}
