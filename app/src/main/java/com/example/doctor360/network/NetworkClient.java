package com.example.doctor360.network;


import com.example.doctor360.model.AdminLoginReceiveParams;
import com.example.doctor360.model.AdminLoginSendParams;
import com.example.doctor360.model.DoctorChangePasswordReceiveParams;
import com.example.doctor360.model.DoctorChangePasswordSendParams;
import com.example.doctor360.model.DoctorLoginReceiveParams;
import com.example.doctor360.model.DoctorLoginSendParams;
import com.example.doctor360.model.DoctorProfileReceiveParams;
import com.example.doctor360.model.DoctorRegistrationReceiveParams;
import com.example.doctor360.model.DoctorRegistrationSendParams;
import com.example.doctor360.model.DoctorUpdateProfileReceiveParams;
import com.example.doctor360.model.PatientLoginReceiveParams;
import com.example.doctor360.model.PatientLoginSendParams;
import com.example.doctor360.model.PatientPasswordChangeReceiveParams;
import com.example.doctor360.model.PatientPasswordChangeSendParams;
import com.example.doctor360.model.PatientProfileReceiveParams;
import com.example.doctor360.model.PatientRegistrationReceiveParams;
import com.example.doctor360.model.PatientRegistrationSendParams;
import com.example.doctor360.model.PatientRequestAppointmentReceiveParams;
import com.example.doctor360.model.PatientRequestAppointmentSendParams;
import com.example.doctor360.model.PatientUpdateProfileReceiveParams;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.model.RejectDoctorReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.model.VerifyDoctorReceiveParams;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface NetworkClient {

    @FormUrlEncoded
    @POST("doctor/register")
    Call<DoctorRegistrationReceiveParams> doctorRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("gender") String gender,
            @Field("specialization") String specialization,
            @Field("qualification") String qualification,
            @Field("password") String password,
            @Field("documentImage") String encodedImage
            );

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

    @PUT("admin/doctors/update/{id}")
    Call<VerifyDoctorReceiveParams> verifyDoctor(@Path("id") String id);

    @DELETE("admin/doctors/reject/{id}")
    Call<RejectDoctorReceiveParams> rejectDoctor(@Path("id") String id);

    @GET("patient/view/{id}")
    Call<PatientProfileReceiveParams> viewPatientProfile(@Path("id") String id);

    @PUT("patient/changepassword/{id}")
    Call<PatientPasswordChangeReceiveParams> patientChangePassword(@Path("id") String id, @Body PatientPasswordChangeSendParams passwordChangeSendParams);

    @FormUrlEncoded
    @PUT("patient/updateprofile/{id}")
    Call<PatientUpdateProfileReceiveParams> updatePatientProfile(
            @Path("id") String id,
            @Field("name") String name,
            @Field("address") String address,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("gender") String gender,
            @Field("age") String age,
            @Field("bloodGroup") String blood,
            @Field("profileImg") String encodedImage
    );

    @GET("doctor/view/{id}")
    Call<DoctorProfileReceiveParams> viewDoctorProfile(@Path("id") String id);

    @PUT("doctor/changepassword/{id}")
    Call<DoctorChangePasswordReceiveParams> doctorChangePassword(@Path("id") String id, @Body DoctorChangePasswordSendParams passwordChangeSendParams);

    @FormUrlEncoded
    @PUT("doctor/updateprofile/{id}")
    Call<DoctorUpdateProfileReceiveParams> updateDoctorProfile(
            @Path("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("gender") String gender,
            @Field("specialization") String specialization,
            @Field("qualification") String qualification,
            @Field("profileImg") String encodedImage,
            @Field("documentImage") String encodedImage1
    );

    @POST("patient/appointment")
    Call<PatientRequestAppointmentReceiveParams> requestAppointment (@Body PatientRequestAppointmentSendParams sendParams);
}
