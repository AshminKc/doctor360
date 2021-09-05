package com.example.doctor360.network;

import com.example.doctor360.model.HospitalListReceiveParams;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkClient1 {

    @GET("v1/hospitals")
    Call<HospitalListReceiveParams> getHospitalList();

}
