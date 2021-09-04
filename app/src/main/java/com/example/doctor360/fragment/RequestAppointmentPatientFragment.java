package com.example.doctor360.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doctor360.R;
import com.example.doctor360.helper.ConnectionDetector;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class RequestAppointmentPatientFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    View rootView;
    String strName, strDcotorName, strAddress, strMobile, strGender, strEmail, strDate, strTime, strRemarks;
    ConnectionDetector connectionDetector;
    EditText edtDate, edtTime, edtRemarks;
    MaterialSpinner spnDoctorName;
    Button btnRequest;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    Context context;
    Calendar c;
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

        connectionDetector = new ConnectionDetector(getContext());

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
                    .setTitle("Error")
                    .setMessage("No Internet Connection!!")
                    .setCancelable(true)
                    .setGravity(Gravity.BOTTOM)
                    .setDuration(3000)
                    .show();
        }

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