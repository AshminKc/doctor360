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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.doctor360.R;
import com.example.doctor360.adapter.AllChatRequestDoctorAdapter;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.ViewChatRequestDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orhanobut.hawk.Hawk;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestChatDoctorFragment extends Fragment {

    View rootView;
    ConnectionDetector connectionDetector;
    RecyclerView recyclerView;
    AllChatRequestDoctorAdapter allChatRequestDoctorAdapter;
    SwipeRefreshLayout refreshLayout;
    List<ViewChatRequestDoctorReceiveParams.DataBean> chatList = new ArrayList<>();
    Context context;
    private ShimmerFrameLayout mShimmerLayout;
    String doctorID;
    private static final String TAG = "RequestChatDoctorFragme";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_chat_request_doctor,container,false);

        recyclerView = rootView.findViewById(R.id.chatRequestFromPatientRecyclerView);
        mShimmerLayout = rootView.findViewById(R.id.chatRequestFromPatientShimmer);
        refreshLayout = rootView.findViewById(R.id.chatRequestFromPatientSwipeRefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.red, R.color.colorPrimary, R.color.cyan, R.color.purple);

        context= getContext();
        Hawk.init(context).build();
        doctorID = Hawk.get("request_doctor_id");

        connectionDetector = new ConnectionDetector(context);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(connectionDetector.isNetworkAvailable() || connectionDetector.isDataAvailable()) {
                            viewAllChatList(doctorID);
                        } else {
                            showNoInternetMessage();
                        }
                    }
                }, 3000);
            }
        });

        RecyclerView.LayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if(connectionDetector.isDataAvailable() || connectionDetector.isNetworkAvailable()){
            viewAllChatList(doctorID);
        }

        if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
            showNoInternetMessage();
        }

        return rootView;
    }

    private void viewAllChatList(String id){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<ViewChatRequestDoctorReceiveParams> call = networkClient.viewDoctorChatRequest(id);

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
                    Log.d(TAG, "onResponse: ");
                    chatList = new ArrayList<ViewChatRequestDoctorReceiveParams.DataBean>(receiveParams.getData());
                    allChatRequestDoctorAdapter = new AllChatRequestDoctorAdapter(context, chatList);
                    recyclerView.setAdapter(allChatRequestDoctorAdapter);
                    pDialog.dismiss();
                    refreshLayout.setRefreshing(false);
                    mShimmerLayout.stopShimmerAnimation();
                    mShimmerLayout.setVisibility(View.GONE);
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
                mShimmerLayout.stopShimmerAnimation();
                refreshLayout.setRefreshing(false);
                pDialog.dismiss();

                if (refreshLayout.isRefreshing() && refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }

                if(pDialog!= null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });
    }

    private void showNoInternetMessage(){
        refreshLayout.setRefreshing(false);
        mShimmerLayout.stopShimmerAnimation();
        mShimmerLayout.setVisibility(View.GONE);

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
