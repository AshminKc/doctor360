package com.example.doctor360.fragment;

import android.content.Context;
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
import com.example.doctor360.adapter.RequestDoctorListAdapter;
import com.example.doctor360.adapter.VerifiedDoctorListAdapter;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDoctorFragment extends Fragment {

    private View rootView;
    ConnectionDetector connectionDetector;
    RecyclerView recyclerView;
    RequestDoctorListAdapter requestDoctorListAdapter;
    SwipeRefreshLayout refreshLayout;
    List<VerifiedDoctorReceiveParams.DataBean> verifiedList = new ArrayList<>();
    Context context;
    private ShimmerFrameLayout mShimmerLayout;
    private static final String TAG = "RequestDoctorFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_request_doctor,container,false);

        recyclerView = rootView.findViewById(R.id.requestDoctorListRecyclerView);
        mShimmerLayout = rootView.findViewById(R.id.requestDoctorShimmer);
        refreshLayout = rootView.findViewById(R.id.requestDoctorSwipeRefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.red, R.color.colorPrimary, R.color.cyan, R.color.purple);

        context= getContext();
        connectionDetector = new ConnectionDetector(context);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(connectionDetector.isNetworkAvailable() || connectionDetector.isDataAvailable()) {
                            viewVerifiedDoctors();
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
            viewVerifiedDoctors();
        }

        if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
            showNoInternetMessage();
        }

        return rootView;
    }

    public void viewVerifiedDoctors(){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<VerifiedDoctorReceiveParams> call = networkClient.getVerifiedDoctorList();

        call.enqueue(new Callback<VerifiedDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<VerifiedDoctorReceiveParams> call, Response<VerifiedDoctorReceiveParams> response) {
                if(response.body()!=null){
                    final VerifiedDoctorReceiveParams doctorReceiveParams = response.body();
                    verifiedList = new ArrayList<VerifiedDoctorReceiveParams.DataBean>(doctorReceiveParams.getData());
                    requestDoctorListAdapter = new RequestDoctorListAdapter(verifiedList, context);
                    recyclerView.setAdapter(requestDoctorListAdapter);
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
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<VerifiedDoctorReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Request" + t.toString());

                mShimmerLayout.stopShimmerAnimation();
                refreshLayout.setRefreshing(false);

                if (refreshLayout.isRefreshing() && refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
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
