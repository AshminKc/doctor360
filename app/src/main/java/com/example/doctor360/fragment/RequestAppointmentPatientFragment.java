package com.example.doctor360.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.MainModel;
import com.example.doctor360.R;
import com.example.doctor360.activity.LoginActivity;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.PatientRequestAppointmentReceiveParams;
import com.example.doctor360.model.PatientRequestAppointmentSendParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.model.VerifyDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAppointmentPatientFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    View rootView;
    Object dID, pID;
    String strDoctorID, strPatientID,defaultDoctorID, strDate, strTime, strRemarks;
    ConnectionDetector connectionDetector;
    EditText edtDate, edtTime, edtRemarks;
    MaterialSpinner spnDoctorName;
    Button btnRequest;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    List<VerifiedDoctorReceiveParams.DataBean> verifiedList;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    Context context;
    Calendar c;
    String[] idList, nameList;
    private static final String TAG = "RequestAppointmentPatie";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_request_appointment_patient,container,false);

        edtDate = rootView.findViewById(R.id.edtRequestPatientDate);
        edtTime = rootView.findViewById(R.id.edtRequestPatientTime);
        edtRemarks = rootView.findViewById(R.id.edtRequestPatientRemarks);
        spnDoctorName = rootView.findViewById(R.id.spinnerRequestDoctorName);
        btnRequest = rootView.findViewById(R.id.btnRequestDoctorAppoint);

        context = getContext();

        c = Calendar.getInstance();

        edtDate.setOnFocusChangeListener(this);
        edtDate.setOnClickListener(this);
        edtTime.setOnFocusChangeListener(this);
        edtTime.setOnClickListener(this);
        btnRequest.setOnClickListener(this);

        requestDatePicker();
        requestTimePicker();

        Hawk.init(context).build();
        strPatientID = Hawk.get("request_patient_id");

        connectionDetector = new ConnectionDetector(getContext());

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        } else {
            getVerifiedDoctorList();
        }

        spnDoctorName.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                strDoctorID =  idList[position];
            }
        });

        return rootView;
    }

    private void requestDatePicker(){
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentYear = year;
                currentMonth = month;
                currentDay = dayOfMonth;

                edtDate.setText(year + "/" + (month+1) + "/" + dayOfMonth);
                edtDate.setFocusable(false);
                edtDate.setFocusableInTouchMode(false);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
    }

    private void requestTimePicker(){
        final Calendar c = Calendar.getInstance();
        currentHour = c.get(Calendar.HOUR_OF_DAY);
        currentMinute = c.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                currentHour = hourOfDay;
                currentMinute = minute;

                String time = hourOfDay + ":" + minute;
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = fmt.parse(time );
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");
                String formattedTime=fmtOut.format(date);

                edtTime.setText(formattedTime);
                edtTime.setFocusable(false);
                edtTime.setFocusableInTouchMode(false);
            }
        }, currentHour, currentMinute, false);
    }

    private void getVerifiedDoctorList(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<VerifiedDoctorReceiveParams> call = networkClient.getVerifiedDoctorList();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Doctors...");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<VerifiedDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<VerifiedDoctorReceiveParams> call, Response<VerifiedDoctorReceiveParams> response) {
                Log.d(TAG, "onResponse: ");

                if(response.body()!=null){
                    final VerifiedDoctorReceiveParams receiveParams = response.body();
                    verifiedList = new ArrayList<>(receiveParams.getData());

                    if(verifiedList.size() > 0){
                        nameList = new String[verifiedList.size()];
                        idList = new String[verifiedList.size()];
                        for(int i=0;i<verifiedList.size();i++){
                            nameList[i] = verifiedList.get(i).getName();
                            idList[i] = verifiedList.get(i).get_id();
                            strDoctorID = idList[0];

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, nameList);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnDoctorName.setAdapter(spinnerArrayAdapter);
                        }

                        pDialog.dismiss();
                    } else {
                        new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage("No Doctors Found.")
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }
                } else {
                    new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<VerifiedDoctorReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Doctor " + t.toString());
                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });
    }

    private void checkFields(){
        strDate = edtDate.getText().toString();
        strTime = edtTime.getText().toString();
        strRemarks = edtRemarks.getText().toString();

        if(strDate.isEmpty()){
            Toasty.error(getContext(), "Date is required", 300).show();
        } else if(strTime.isEmpty()) {
            Toasty.error(getContext(), "Time is required", 300).show();
        } else {
            requestAppointment();
        }
    }

    private void requestAppointment(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        PatientRequestAppointmentSendParams sendParams = new PatientRequestAppointmentSendParams();

        strDate = edtDate.getText().toString();
        strTime = edtTime.getText().toString();
        strRemarks = edtRemarks.getText().toString();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sending Request...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PatientRequestAppointmentReceiveParams> call =networkClient.requestAppointment(sendParams);
        sendParams.setDoctorId(strDoctorID);
        sendParams.setPatientId(strPatientID);
        sendParams.setDate(strDate);
        sendParams.setTime(strTime);
        sendParams.setDescription(strRemarks);

        call.enqueue(new Callback<PatientRequestAppointmentReceiveParams>() {
            @Override
            public void onResponse(Call<PatientRequestAppointmentReceiveParams> call, Response<PatientRequestAppointmentReceiveParams> response) {
                if(response.body()!=null){
                    final PatientRequestAppointmentReceiveParams receiveParams = response.body();

                    String success = receiveParams.getSuccess();
                    if(success.matches("true")){
                        new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.SUCCESS)
                                .setTitle("Success")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    } else {
                        new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
                                .setTitle("Error")
                                .setMessage(receiveParams.getMessage())
                                .setCancelable(true)
                                .setGravity(Gravity.BOTTOM)
                                .setDuration(3000)
                                .show();
                        pDialog.dismiss();
                    }

                } else {
                    new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
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
            public void onFailure(Call<PatientRequestAppointmentReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.toString());

                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.edtRequestPatientDate:
                edtDate.setInputType(InputType.TYPE_NULL);
                edtDate.setShowSoftInputOnFocus(false);
                datePickerDialog.show();
                break;

            case R.id.edtRequestPatientTime:
                edtTime.setInputType(InputType.TYPE_NULL);
                edtTime.setShowSoftInputOnFocus(false);
                timePickerDialog.show();
                break;

            case R.id.btnRequestDoctorAppoint:
                if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
                    new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
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
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();

        switch (id){
            case R.id.edtRequestPatientDate:
                edtDate.setInputType(InputType.TYPE_NULL);
                edtDate.setShowSoftInputOnFocus(false);
                datePickerDialog.show();
                break;

            case R.id.edtRequestPatientTime:
                edtTime.setInputType(InputType.TYPE_NULL);
                edtTime.setShowSoftInputOnFocus(false);
                timePickerDialog.show();
                break;
        }
    }
}