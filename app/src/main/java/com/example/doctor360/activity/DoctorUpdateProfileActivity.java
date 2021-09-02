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
import com.example.doctor360.model.DoctorUpdateProfileReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.SquareImageView;
import com.jaredrummler.materialspinner.MaterialSpinner;
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

import static com.example.doctor360.utils.Constants.REQUEST_CAMERA_CODE;
import static com.example.doctor360.utils.Constants.REQUEST_GALLERY_CODE;

public class DoctorUpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Bitmap bitmap, bitmap1;
    ConnectionDetector connectionDetector;
    CircleImageView imageDoctorProfile;
    SquareImageView imageDoctorDocument;
    TextView txtCurrentSpec, txtCurrentQuali;
    AppCompatEditText edtUpdateName, edtUpdateEmail, edtUpdateMobile, edtUpdateGender;
    MaterialSpinner spinnerUpdateSpec, spinnerUpdateQuali;
    String strDoctorId, strUpdateName, strUpdateEmail, strUpdateMobile, strUpdateGender, strUpdateSpec,
            strUpdateQuali, strUpdatePP, strUpdateDocumentPic, encodedImage, encodedImage1;
    Button btnUpdateDetails, btnUploadProfilePic, btnUploadDocumentPic;
    String namePattern = "^[A-Za-z\\s]+$";
    String mobilePattern = "^[0-9]{10}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "DoctorUpdateProfileActi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_doctor_update_profile);

        toolbar = findViewById(R.id.doctorUpdateProfileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageDoctorProfile = findViewById(R.id.imageUpdateDoctorImage);
        btnUploadProfilePic = findViewById(R.id.btnUploadDoctorProfilePic);
        imageDoctorDocument = findViewById(R.id.imageUpdateDoctorDocument);
        btnUploadDocumentPic = findViewById(R.id.btnUploadDoctorDocumentPic);
        edtUpdateName = findViewById(R.id.edtUpdateDoctorName);
        edtUpdateEmail = findViewById(R.id.edtUpdateDoctorEmail);
        edtUpdateMobile = findViewById(R.id.edtUpdateDoctorMobile);
        edtUpdateGender = findViewById(R.id.edtUpdateDoctorGender);
        spinnerUpdateQuali = findViewById(R.id.spinnerUpdateDoctorQuali);
        spinnerUpdateSpec = findViewById(R.id.spinnerUpdateDoctorSpec);
        btnUpdateDetails = findViewById(R.id.btnUpdateDoctorDetails);
        txtCurrentQuali = findViewById(R.id.txtCurrentQualiValue);
        txtCurrentSpec = findViewById(R.id.txtCurrentSpecValue);

        Intent intent = getIntent();
        strDoctorId = intent.getStringExtra("doctor_update_id");
        strUpdateName = intent.getStringExtra("doctor_update_name");
        strUpdateMobile = intent.getStringExtra("doctor_update_mobile");
        strUpdateEmail = intent.getStringExtra("doctor_update_email");
        strUpdateGender = intent.getStringExtra("doctor_update_gender");
        strUpdateQuali = intent.getStringExtra("doctor_update_quali");
        strUpdateSpec = intent.getStringExtra("doctor_update_spec");
        strUpdatePP = intent.getStringExtra("doctor_update_image");
        strUpdateDocumentPic = intent.getStringExtra("doctor_update_document");

        edtUpdateName.setText(strUpdateName);
        edtUpdateMobile.setText(strUpdateMobile);
        edtUpdateEmail.setText(strUpdateEmail);
        edtUpdateGender.setText(strUpdateGender);
        txtCurrentQuali.setText(strUpdateQuali);
        txtCurrentSpec.setText(strUpdateSpec);

        if(strUpdateDocumentPic!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(strUpdateDocumentPic, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageDoctorDocument.setImageBitmap(decodedImage);
        } else {
            imageDoctorDocument.setImageResource(R.drawable.noimage);
        }

        if(strUpdatePP!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(strUpdatePP, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageDoctorProfile.setImageBitmap(decodedImage);
        } else {
            imageDoctorProfile.setImageResource(R.drawable.noimage);
        }

        imageDoctorProfile.setImageResource(R.drawable.noimage);

        spinnerUpdateQuali.setItems("Select", "Bachelor of Medicine (MBBS, BMBS, MBChB, MBBCh)","Bachelor of Surgery (MBBS, BMBS, MBChB, MBBCh)",
                "Bachelor of Medicine (B.Med)","Bachelor of Surgery (B.S)/(B.Surg)","Doctor of Medicine (MD, Dr.MuD, Dr.Med)",
                "Doctor of Osteopathic Medicine (DO)","Doctor of Medicine by research MD(Res), DM","Master of Clinical Medicine (MCM)",
                "Master of Medical Science (MMSc, MMedSc)","Master of Public Health (MPH)","Master of Medicine (MM, MMed)",
                "Master of Philosophy (MPhil)","Master of Philosophy in Ophthalmology (MPhO)","Master of Public Health and Ophthalmology (MPHO)",
                "Master of Surgery (MS, MSurg, MChir, MCh, ChM, CM)","Master of Science in Medicine or Surgery (MSc)","Doctor of Clinical Medicine (DCM)",
                "Doctor of Clinical Surgery (DClinSurg)","Doctor of Medical Science (DMSc, DMedSc)","Doctor of Surgery (DS, DSurg)");

        spinnerUpdateQuali.setHint("Select your qualification");
        spinnerUpdateQuali.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerUpdateQuali.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strUpdateQuali = item.toString();

            }
        });

        spinnerUpdateQuali.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
            }
        });

        spinnerUpdateSpec.setItems("Select", "Allergists/Immunologists","Anesthesiologists","Cardiologists","Colon and Rectal Surgeons",
                "Critical Care Medicine Specialists","Dermatologists","Endocrinologists","Emergency Medicine Specialists","Family Physicians",
                "Gastroenterologists","Geriatric Medicine Specialists","Hematologists","Hospice and Palliative Medicine Specialists",
                "Infectious Disease Specialists","Internists","Medical Geneticists","Nephrologists","Neurologists","Obstetricians and Gynecologists",
                "Oncologists","Ophthalmologists","Osteopaths","Otolaryngologists","Pathologists","Pediatricians","Physiatrists",
                "Plastic Surgeons","Podiatrists","Preventive Medicine Specialists","Psychiatrists","Pulmonologists","Radiologists",
                "Rheumatologists","Sleep Medicine Specialists","Sports Medicine Specialists","General Surgeons","Urologists");

        spinnerUpdateSpec.setHint("Select your specialization");
        spinnerUpdateSpec.setBackground(getDrawable(R.drawable.spinner_bg));
        spinnerUpdateSpec.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strUpdateSpec = item.toString();

            }
        });

        spinnerUpdateSpec.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
            }
        });

        connectionDetector = new ConnectionDetector(DoctorUpdateProfileActivity.this);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(DoctorUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }

        btnUploadProfilePic.setOnClickListener(this);
        btnUploadDocumentPic.setOnClickListener(this);
        btnUpdateDetails.setOnClickListener(this);
    }

    public void checkFields(){
        strUpdateName = edtUpdateName.getText().toString();
        strUpdateEmail = edtUpdateEmail.getText().toString();
        strUpdateMobile = edtUpdateMobile.getText().toString();
        strUpdateQuali = spinnerUpdateQuali.getText().toString();
        strUpdateGender = edtUpdateGender.getText().toString();
        strUpdateSpec = spinnerUpdateSpec.getText().toString();


        if(strUpdateName.isEmpty()){
            Toasty.error(DoctorUpdateProfileActivity.this,"Please Enter Full Name",300).show();
        } else if(!strUpdateName.matches(namePattern)){
            Toasty.error(DoctorUpdateProfileActivity.this,"Only Alphabet are allowed",300).show();
        } else if(strUpdateEmail.isEmpty()){
            Toasty.error(DoctorUpdateProfileActivity.this,"Please Enter Email",300).show();
        } else if(!strUpdateEmail.matches(emailPattern)){
            Toasty.error(DoctorUpdateProfileActivity.this,"Invalid Email",300).show();
        } else if(strUpdateMobile.isEmpty()){
            Toasty.error(DoctorUpdateProfileActivity.this,"Please Enter Mobile No",300).show();
        } else if(!strUpdateMobile.matches(mobilePattern)){
            Toasty.error(DoctorUpdateProfileActivity.this,"Invalid Mobile No",300).show();
        } else if(imageDoctorProfile.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.noimage).getConstantState()){
            Toasty.error(DoctorUpdateProfileActivity.this,"Image is required (jpg, jepg, png)",300).show();
        } else{
            updateDoctorProfile();
        }
    }

    public void updateDoctorProfile(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);

        String id = strDoctorId;
        strUpdateName = edtUpdateName.getText().toString();
        strUpdateEmail = edtUpdateEmail.getText().toString();
        strUpdateMobile = edtUpdateMobile.getText().toString();
        strUpdateQuali = spinnerUpdateQuali.getText().toString();
        strUpdateGender = edtUpdateGender.getText().toString();
        strUpdateSpec = spinnerUpdateSpec.getText().toString();

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

        if(bitmap1!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            encodedImage1 = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        } else {
            byte[] decodedString = Base64.decode(strUpdatePP, Base64.DEFAULT);
            bitmap1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            encodedImage1 = Base64.encodeToString(decodedString, Base64.DEFAULT);
        }

        if(strUpdateQuali.matches("Select"))
            strUpdateQuali = txtCurrentQuali.getText().toString();
        else
            strUpdateQuali = spinnerUpdateQuali.getText().toString();

        if(strUpdateSpec.matches("Select"))
            strUpdateSpec = txtCurrentSpec.getText().toString();
        else
            strUpdateSpec = spinnerUpdateSpec.getText().toString();

        final SweetAlertDialog pDialog = new SweetAlertDialog(DoctorUpdateProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Submitting Data....");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<DoctorUpdateProfileReceiveParams> call = networkClient.updateDoctorProfile(id, strUpdateName, strUpdateEmail,
                strUpdateMobile, strUpdateGender, strUpdateQuali, strUpdateSpec, encodedImage, encodedImage1);
        call.enqueue(new Callback<DoctorUpdateProfileReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorUpdateProfileReceiveParams> call, Response<DoctorUpdateProfileReceiveParams> response) {
                DoctorUpdateProfileReceiveParams receiveParams = response.body();

                if(response.body()!=null){

                    String status = receiveParams.getSuccess();

                    if(status.matches("true")){
                        Toasty.success(getApplicationContext(),"Updated Successfully. Re-login to see the changes.", 300).show();
                        Intent intent = new Intent(DoctorUpdateProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        pDialog.dismiss();

                    } else {
                        new AestheticDialog.Builder(DoctorUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }

                } else {
                    new AestheticDialog.Builder(DoctorUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<DoctorUpdateProfileReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.toString());
                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });

    }

    private void selectProfileImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_GALLERY_CODE);
    }

    private void selectDocumentImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CAMERA_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(DoctorUpdateProfileActivity.this, DoctorDashboardActivity.class);
        intent.putExtra("from_profile_id", strDoctorId);
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
            Intent intent=new Intent(DoctorUpdateProfileActivity.this, DoctorDashboardActivity.class);
            intent.putExtra("from_profile_id", strDoctorId);
            intent.putExtra("from_profile_name", strUpdateName);
            intent.putExtra("from_profile_image", strUpdatePP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {

                Uri path = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    imageDoctorProfile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == REQUEST_CAMERA_CODE && data!=null) {

                Uri path = data.getData();
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    imageDoctorDocument.setImageBitmap(bitmap1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnUpdateDoctorDetails:
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(DoctorUpdateProfileActivity.this, DialogStyle.RAINBOW, DialogType.ERROR)
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

            case R.id.btnUploadDoctorProfilePic:
                selectProfileImage();
                break;

            case R.id.btnUploadDoctorDocumentPic:
                selectDocumentImage();
                break;
        }
    }
}
