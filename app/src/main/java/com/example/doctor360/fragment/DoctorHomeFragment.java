package com.example.doctor360.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.activity.DoctorPasswordChangeActivity;
import com.example.doctor360.adapter.AppointRequestPatientAdapter;
import com.example.doctor360.adapter.ChatRequestPatientAdapter;
import com.example.doctor360.adapter.ImageSliderAdapter;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.DoctorRequestAppoitmentReceiveParams;
import com.example.doctor360.model.ViewChatRequestDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.example.doctor360.utils.OnDataPasser;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorHomeFragment extends Fragment {

    Bundle bundle;
    OnDataPasser dataPasser;
    View rootView;
    private long delayTime =  5000;
    ImageSliderAdapter imageSliderAdapter;
    private AutoScrollViewPager viewPager;
    private CircleIndicator pageIndicatorView;
    RecyclerView chatRecyclerView, appointRecyclerView;
    ChatRequestPatientAdapter chatRequestPatientAdapter;
    AppointRequestPatientAdapter appointRequestPatientAdapter;
    ConnectionDetector connectionDetector;
    String strDoctorID;
    TextView viewAllChat, viewAllAppoint;
    List<DoctorRequestAppoitmentReceiveParams.DataBean> requestAppointment = new ArrayList<>();
    List<ViewChatRequestDoctorReceiveParams.DataBean> chatRequestList = new ArrayList<>();
    Context context;
    private static final String TAG = "DoctorHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_doctor_home,container,false);

        viewPager = rootView.findViewById(R.id.sliderViewPager);
        pageIndicatorView = rootView.findViewById(R.id.pageIndicator);
        chatRecyclerView = rootView.findViewById(R.id.chatRequestRecyclerView);
        appointRecyclerView = rootView.findViewById(R.id.appointRequestRecyclerView);
        viewAllChat = rootView.findViewById(R.id.txtViewAllChatRequest);
        viewAllAppoint = rootView.findViewById(R.id.txtViewAllAppointRequest);

        viewPager.startAutoScroll();
        viewPager.setInterval(delayTime);
        viewPager.setStopScrollWhenTouch(true);

        imageSliderAdapter = new ImageSliderAdapter(getContext());
        viewPager.setAdapter(imageSliderAdapter);
        pageIndicatorView.setViewPager(viewPager);

        context = getContext();

        Hawk.init(context).build();
        strDoctorID = Hawk.get("request_doctor_id");

        connectionDetector = new ConnectionDetector(context);

        if (!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()) {
            showNoInternetMessage();
        } else {
            getAppointmentRequest(strDoctorID);
            getChatRequest(strDoctorID);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        appointRecyclerView.setLayoutManager(linearLayoutManager1);
        appointRecyclerView.setItemAnimator(new DefaultItemAnimator());

        viewAllChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                RequestChatDoctorFragment requestChatDoctorFragment = new RequestChatDoctorFragment();
                ft.replace(R.id.fragmentContainer2, requestChatDoctorFragment,"").addToBackStack("").commit();
                passDataToDoctor("Pending Requests",1);
            }
        });

        viewAllAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                RequestAppointmentDoctorFragment requestAppointmentDoctorFragment = new RequestAppointmentDoctorFragment();
                ft.replace(R.id.fragmentContainer2, requestAppointmentDoctorFragment,"").addToBackStack("").commit();
                passDataToDoctor("Pending Appointments", 3);
            }
        });

        return rootView;
    }

    private void getAppointmentRequest(String id){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<DoctorRequestAppoitmentReceiveParams> call = networkClient.viewAllPendingAppointments(id);

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<DoctorRequestAppoitmentReceiveParams>() {
            @Override
            public void onResponse(Call<DoctorRequestAppoitmentReceiveParams> call, Response<DoctorRequestAppoitmentReceiveParams> response) {
                Log.d(TAG, "onResponse: " + response.body()) ;
                if(response.body()!=null){
                    final DoctorRequestAppoitmentReceiveParams receiveParams = response.body();
                    requestAppointment = new ArrayList<DoctorRequestAppoitmentReceiveParams.DataBean>(receiveParams.getData());
                    appointRequestPatientAdapter = new AppointRequestPatientAdapter(requestAppointment, context);
                    appointRecyclerView.setAdapter(appointRequestPatientAdapter);
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
            public void onFailure(Call<DoctorRequestAppoitmentReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Doctor " + t.toString());

                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });
    }

    private void getChatRequest(String patientid){
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<ViewChatRequestDoctorReceiveParams> call = networkClient.viewDoctorChatRequest(patientid);

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<ViewChatRequestDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<ViewChatRequestDoctorReceiveParams> call, Response<ViewChatRequestDoctorReceiveParams> response) {
                if(response.body()!=null){
                    final ViewChatRequestDoctorReceiveParams receiveParams = response.body();
                    chatRequestList = new ArrayList<ViewChatRequestDoctorReceiveParams.DataBean>(receiveParams.getData());
                    chatRequestPatientAdapter = new ChatRequestPatientAdapter(chatRequestList, context);
                    chatRecyclerView.setAdapter(chatRequestPatientAdapter);
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
            public void onFailure(Call<ViewChatRequestDoctorReceiveParams> call, Throwable t) {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPasser) context;
    }

    public void passDataToDoctor(String data, int id) {
        dataPasser.onChangeToolbarTitle(data);
        dataPasser.setCheckedNavigationItem(id);
    }


}
