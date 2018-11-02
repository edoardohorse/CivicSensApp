package com.civic.gercs.civicsense.Sender;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.civic.gercs.civicsense.EndPoint.END_POINT_LOCAL;

public class ServiceGenerator {
    public static String apiBaseUrl = END_POINT_LOCAL;
    private static Retrofit retrofit;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(apiBaseUrl);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    // No need to instantiate this class.
    private ServiceGenerator() {


    }

    private static void createBuild(){

        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiBaseUrl)
                .client(okHttpClient)
                .build();

    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        createBuild();

    }

    public static Service createService(){
        if(retrofit == null){
            createBuild();
        }
        Service service = retrofit.create(Service.class);
        return service;
    }


}
