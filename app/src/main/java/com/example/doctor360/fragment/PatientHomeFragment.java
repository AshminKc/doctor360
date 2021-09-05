package com.example.doctor360.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.MainModel;
import com.example.doctor360.R;
import com.example.doctor360.activity.DoctorDashboardActivity;
import com.example.doctor360.activity.PatientDashboardActivity;
import com.example.doctor360.activity.PatientProfileActivity;
import com.example.doctor360.adapter.AppointRequestPatientAdapter;
import com.example.doctor360.adapter.ChatRequestPatientAdapter;
import com.example.doctor360.adapter.DoctorListAdapter;
import com.example.doctor360.adapter.HospitalListAdapter;
import com.example.doctor360.adapter.ImageSliderAdapter;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.HospitalListReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.NetworkClient1;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.network.ServiceGenerator1;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientHomeFragment extends Fragment {

    View rootView;
    private long delayTime =  5000;
    ImageSliderAdapter imageSliderAdapter;
    HospitalListAdapter hospitalListAdapter;
    private AutoScrollViewPager viewPager;
    private CircleIndicator pageIndicatorView;
    TextView viewAllDoctorTXT, viewAllHospitalTXT, txtRequest, txtViewAppoint;
    RecyclerView doctorRecyclerView, hospitalRecyclerView;
    DoctorListAdapter doctorListAdapter;
    ConnectionDetector connectionDetector;
    List<HospitalListReceiveParams.DataBean> hospitalList;
    List<VerifiedDoctorReceiveParams.DataBean> verifiedList;
    Context context;
    private static final String TAG = "PatientHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_patient_home,container,false);

        viewPager = rootView.findViewById(R.id.sliderViewPager);
        pageIndicatorView = rootView.findViewById(R.id.pageIndicator);
        viewAllDoctorTXT = rootView.findViewById(R.id.txtViewAllDoctor);
        viewAllHospitalTXT = rootView.findViewById(R.id.txtViewAllHospitals);
        txtRequest = rootView.findViewById(R.id.txtRequestForAppointment);
        txtViewAppoint = rootView.findViewById(R.id.txtViewAllAppointment);
        doctorRecyclerView = rootView.findViewById(R.id.allDoctorRecyclerView);
        hospitalRecyclerView = rootView.findViewById(R.id.allHospitalRecyclerView);

        viewPager.startAutoScroll();
        viewPager.setInterval(delayTime);
        viewPager.setStopScrollWhenTouch(true);

        imageSliderAdapter = new ImageSliderAdapter(getContext());
        viewPager.setAdapter(imageSliderAdapter);
        pageIndicatorView.setViewPager(viewPager);

        context = getContext();

        connectionDetector = new ConnectionDetector(context);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            showNoInternetMessage();
        } else {
            getHospitalList();
            getVerifiedDoctors();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        doctorRecyclerView.setLayoutManager(linearLayoutManager);
        doctorRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        hospitalRecyclerView.setLayoutManager(linearLayoutManager1);
        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());

        viewAllDoctorTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                RequestDoctorFragment requestDoctorFragment = new RequestDoctorFragment();
                ft.replace(R.id.fragmentContainer1, requestDoctorFragment,"requestDoctorFragment").addToBackStack("requestDoctorFragment").commit();
                ((PatientDashboardActivity) getActivity()).setToolbarAndNavViewData(getContext(),1, getResources().getString(R.string.menu_request_doctor));
            }
        });

        viewAllHospitalTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                AllHospitalListFragment allHospitalListFragment = new AllHospitalListFragment();
                ft.replace(R.id.fragmentContainer1, allHospitalListFragment,"allHospitalFragment").addToBackStack("allHospitalFragment").commit();
                ((PatientDashboardActivity) getActivity()).setToolbarAndNavViewData1(getContext(),7, getResources().getString(R.string.menu_hospitals));
            }
        });

        txtRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                RequestAppointmentPatientFragment requestAppointmentPatientFragment = new RequestAppointmentPatientFragment();
                ft.replace(R.id.fragmentContainer1, requestAppointmentPatientFragment,"requestAppointmentFragment").addToBackStack("requestAppointmentFragment").commit();
                ((PatientDashboardActivity) getActivity()).setToolbarAndNavViewData2(getContext(),3, getResources().getString(R.string.menu_request_appointment));
            }
        });

        return rootView;
    }

    private void getVerifiedDoctors(){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<VerifiedDoctorReceiveParams> call = networkClient.getVerifiedDoctorList();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<VerifiedDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<VerifiedDoctorReceiveParams> call, Response<VerifiedDoctorReceiveParams> response) {
                if(response.body()!=null){
                    final VerifiedDoctorReceiveParams receiveParams = response.body();
                    verifiedList = new ArrayList<VerifiedDoctorReceiveParams.DataBean>(receiveParams.getData());
                    doctorListAdapter = new DoctorListAdapter(verifiedList, context);
                    doctorRecyclerView.setAdapter(doctorListAdapter);
                    pDialog.dismiss();
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

    private void getHospitalList(){
        NetworkClient1 networkClient1 = ServiceGenerator1.createRequestGsonAPI(NetworkClient1.class);
        Call<HospitalListReceiveParams> call = networkClient1.getHospitalList();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<HospitalListReceiveParams>() {
            @Override
            public void onResponse(Call<HospitalListReceiveParams> call, Response<HospitalListReceiveParams> response) {
                if(response.body()!=null){
                    final HospitalListReceiveParams receiveParams = response.body();
                    hospitalList = new ArrayList<HospitalListReceiveParams.DataBean>(receiveParams.getData());
                    hospitalListAdapter = new HospitalListAdapter(hospitalList, context);
                    hospitalRecyclerView.setAdapter(hospitalListAdapter);
                    pDialog.dismiss();
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
            public void onFailure(Call<HospitalListReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Doctor " + t.toString());

                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });
    }

    private void showNoInternetMessage(){
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("No Internet Connection")
                .setContentText("Please check your connection and try again.")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
