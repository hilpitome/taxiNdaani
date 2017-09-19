package com.ndaani.taxi.taxindaani.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ndaani.taxi.taxindaani.utils.Constants;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by choks on 24/01/2017.
 */

public class NetworkService {
    private static String baseUrl = Constants.API_BASE_URL;
    private NetworkApi networkApi;
    private Retrofit retrofit;

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public NetworkService() {
        this(baseUrl);
    }

    public NetworkService(String baseUrl) {

        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(60, TimeUnit.SECONDS);
        b.readTimeout(60, TimeUnit.SECONDS);
        b.writeTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        b.addInterceptor(logging);

        OkHttpClient okHttpClient = b.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        networkApi = retrofit.create(NetworkApi.class);
    }

    public NetworkApi getAPI() {
        return networkApi;
    }

}
