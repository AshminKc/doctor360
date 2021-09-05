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
import com.example.doctor360.adapter.AllHospitalListAdapter;
import com.example.doctor360.adapter.VerifiedDoctorListAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllHospitalListFragment extends Fragment {

    private View rootView;
    ConnectionDetector connectionDetector;
    RecyclerView recyclerView;
    AllHospitalListAdapter allHospitalListAdapter;
    SwipeRefreshLayout refreshLayout;
    List<HospitalListReceiveParams.DataBean> hospitalList = new ArrayList<>();
    Context context;
    TextView noInternet;
    private ShimmerFrameLayout mShimmerLayout;
    private static final String TAG = "AllHospitalListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_allhospital_list,container,false);

        noInternet = rootView.findViewById(R.id.allHospitalNoInternetTXT);
        recyclerView = rootView.findViewById(R.id.allHospitalListRecyclerView);
        mShimmerLayout = rootView.findViewById(R.id.allHospitalDoctorShimmer);
        refreshLayout = rootView.findViewById(R.id.allHospitalSwipeRefreshLayout);
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
                            getAllHospital();
                            noInternet.setVisibility(View.GONE);
                        } else {
                            noInternet.setVisibility(View.VISIBLE);
                            showNoInternetMessage();
                        }
                    }
                }, 3000);
            }
        });

        RecyclerView.LayoutManager linearLayoutManager1=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if(connectionDetector.isDataAvailable() || connectionDetector.isNetworkAvailable()){
            noInternet.setVisibility(View.GONE);
            getAllHospital();
        }

        if(!connectionDetector.isDataAvailable() || !connectionDetector.isNetworkAvailable()){
            noInternet.setVisibility(View.VISIBLE);
            showNoInternetMessage();
        }

        return rootView;

    }

    private void getAllHospital(){
        refreshLayout.setRefreshing(true);
        NetworkClient1 networkClient1 = ServiceGenerator1.createRequestGsonAPI(NetworkClient1.class);
        Call<HospitalListReceiveParams> call = networkClient1.getHospitalList();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data..");
        pDialog.setCancelable(false);
        pDialog.show();

        call.enqueue(new Callback<HospitalListReceiveParams>() {
            @Override
            public void onResponse(Call<HospitalListReceiveParams> call, Response<HospitalListReceiveParams> response) {
                if(response.body()!=null){
                    final HospitalListReceiveParams receiveParams = response.body();
                    hospitalList = new ArrayList<HospitalListReceiveParams.DataBean>(receiveParams.getData());
                    allHospitalListAdapter = new AllHospitalListAdapter(hospitalList, context);
                    recyclerView.setAdapter(allHospitalListAdapter);
                    refreshLayout.setRefreshing(false);
                    mShimmerLayout.stopShimmerAnimation();
                    mShimmerLayout.setVisibility(View.GONE);
                    pDialog.dismiss();
                } else {
                    new AestheticDialog.Builder(getActivity(), DialogStyle.RAINBOW, DialogType.ERROR)
                            .setTitle("Error")
                            .setMessage("Some Error occurred at Server end. Please try again.")
                            .setCancelable(true)
                            .setGravity(Gravity.BOTTOM)
                            .setDuration(3000)
                            .show();
                    refreshLayout.setRefreshing(false);
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<HospitalListReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Verified" + t.toString());

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
