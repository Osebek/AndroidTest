package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.*;


/**
 * Created by nejcvesel on 06/12/16.
 */

public interface FormUploadEditInterface {
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
    @Multipart
    @PUT("updateLocation/{id}/")
    Call<ResponseBody> updateLocationAndChangePicture(
            @retrofit2.http.Path("id") String id,
            @Part("latitude") RequestBody latitude,
            @Part("longtitude") RequestBody longtitude,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("title") RequestBody title,
            @Part("text") RequestBody text,
            @Part MultipartBody.Part file
    );
    @Multipart
    @PATCH("updateLocation/{id}/")
    Call<ResponseBody> updateLocation(
            @retrofit2.http.Path("id") String id,
            @Part("latitude") RequestBody latitude,
            @Part("longtitude") RequestBody longtitude,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("title") RequestBody title,
            @Part("text") RequestBody text
    );
    @DELETE("updateLocation/{id}/")
    Call<ResponseBody> deleteLocation(
            @retrofit2.http.Path("id") String id
    );
}
