package com.moises.quedadaseventos.retrofit;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://actively-glad-roughy.ngrok-free.app/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofitWithout = null;

    public static Retrofit getClientWithInterceptor(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenAuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientWithoutInterceptor(Context context) {
        if (retrofitWithout == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            retrofitWithout = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitWithout;
    }

}