package com.example.nejcvesel.pazikjehodis.retrofitAPI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by nejcvesel on 06/12/16.
 */

public interface FileUploadService {
    @Multipart
    @POST("locationList/upload/")
    Call<ResponseBody> upload(
            @Part("latitude") RequestBody latitude,
            @Part("longtitude") RequestBody longtitude,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("title") RequestBody title,
            @Part("text") RequestBody text,
            @Part MultipartBody.Part file
    );
}
