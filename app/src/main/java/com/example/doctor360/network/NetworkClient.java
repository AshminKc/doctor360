package com.example.doctor360.network;


import com.example.doctor360.model.AdminLoginReceiveParams;
import com.example.doctor360.model.AdminLoginSendParams;
import com.example.doctor360.model.DoctorLoginReceiveParams;
import com.example.doctor360.model.DoctorLoginSendParams;
import com.example.doctor360.model.DoctorRegistrationReceiveParams;
import com.example.doctor360.model.DoctorRegistrationSendParams;
import com.example.doctor360.model.PatientLoginReceiveParams;
import com.example.doctor360.model.PatientLoginSendParams;
import com.example.doctor360.model.PatientRegistrationReceiveParams;
import com.example.doctor360.model.PatientRegistrationSendParams;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface NetworkClient {

    @POST("doctor/register")
    Call<DoctorRegistrationReceiveParams> doctorRegister(@Body DoctorRegistrationSendParams doctorRegistrationSendParams);

    @POST("patient/register")
    Call<PatientRegistrationReceiveParams> patientRegister(@Body PatientRegistrationSendParams patientRegistrationSendParams);

    @POST("admin/login")
    Call<AdminLoginReceiveParams> adminLogin(@Body AdminLoginSendParams adminLoginSendParams);

    @POST("doctor/login")
    Call<DoctorLoginReceiveParams> doctorLogin(@Body DoctorLoginSendParams doctorLoginSendParams);

    @POST("patient/login")
    Call<PatientLoginReceiveParams> patientLogin(@Body PatientLoginSendParams patientLoginSendParams);

    @GET("admin/doctors/view/pending")
    Call<PendingDoctorReceiveParams> getPendingDoctorList();

    @GET("admin/doctors/view/verified")
    Call<VerifiedDoctorReceiveParams> getVerifiedDoctorList();

}
