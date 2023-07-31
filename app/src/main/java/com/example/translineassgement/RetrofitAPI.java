package com.example.translineassgement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("/api:BS3FIkjh/attendance")
    Call<DataModal> createPost(@Body DataModal dataModal);

}