package com.example.doctor360.network;

import com.example.doctor360.utils.Constants;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator1 {

    public static final String API = "https://corona.askbhunte.com/api/";

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createRequestGsonAPI(Class<S> serviceClass) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                                .create()
                ))
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }
}
