package com.example.translineassgement;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static  final String BASE_URL ="https://x8ki-letl-twmt.n7.xano.io/";
    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if (retrofit==null){
            // adding  logging interceptor

            HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client= new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();


            // using retrofit library
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();



        }
        return retrofit;
    }
}
