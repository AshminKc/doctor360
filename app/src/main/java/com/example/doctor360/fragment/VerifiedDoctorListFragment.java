package com.example.doctor360.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.doctor360.adapter.PendingDoctorListAdapter;
import com.example.doctor360.adapter.VerifiedDoctorListAdapter;
import com.example.doctor360.helper.ConnectionDetector;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.network.NetworkClient;
import com.example.doctor360.network.ServiceGenerator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifiedDoctorListFragment extends Fragment {

    private View rootView;
    ConnectionDetector connectionDetector;
    RecyclerView recyclerView;
    VerifiedDoctorListAdapter verifiedDoctorListAdapter;
    SwipeRefreshLayout refreshLayout;
    List<VerifiedDoctorReceiveParams.DataBean> verifiedList = new ArrayList<>();
    Context context;
    TextView noInternet;
    private ShimmerFrameLayout mShimmerLayout;
    private static final String TAG = "VerifiedDoctorListFragm";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_verified_doctor_list,container,false);

        noInternet = rootView.findViewById(R.id.verifiedDoctorNoInternetTXT);
        recyclerView = rootView.findViewById(R.id.verifiedDoctorListRecyclerView);
        mShimmerLayout = rootView.findViewById(R.id.verifiedDoctorShimmer);
        refreshLayout = rootView.findViewById(R.id.verifiedDoctorSwipeRefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.red, R.color.colorPrimary, R.color.cyan, R.color.purple);

        context= getContext();
        connectionDetector = new ConnectionDetector(context);
        Hawk.init(context).build();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(connectionDetector.isNetworkAvailable() || connectionDetector.isDataAvailable()) {
                            getVerifiedDoctors();
                            noInternet.setVisibility(View.GONE);
                        } else {
                            noInternet.setVisibility(View.VISIBLE);
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
            noInternet.setVisibility(View.GONE);
            getVerifiedDoctors();
        }

        if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
            noInternet.setVisibility(View.VISIBLE);
            showNoInternetMessage();
        }

        return rootView;

    }

    private void getVerifiedDoctors(){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<VerifiedDoctorReceiveParams> call = networkClient.getVerifiedDoctorList();

        call.enqueue(new Callback<VerifiedDoctorReceiveParams>() {
            @Override
            public void onResponse(Call<VerifiedDoctorReceiveParams> call, Response<VerifiedDoctorReceiveParams> response) {
                Log.d(TAG, "onResponse: Success");


                final VerifiedDoctorReceiveParams doctorReceiveParams = response.body();
                verifiedList = new ArrayList<VerifiedDoctorReceiveParams.DataBean>(doctorReceiveParams.getData());
                verifiedDoctorListAdapter = new VerifiedDoctorListAdapter(verifiedList, context);
                recyclerView.setAdapter(verifiedDoctorListAdapter);
                refreshLayout.setRefreshing(false);
                mShimmerLayout.stopShimmerAnimation();
                mShimmerLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<VerifiedDoctorReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verified" + t.toString());

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
