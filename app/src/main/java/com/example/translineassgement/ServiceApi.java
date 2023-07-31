package com.example.translineassgement;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceApi {
  @Multipart
    @POST("/api:BS3FIkjh/upload/simpleimage")
  Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}
