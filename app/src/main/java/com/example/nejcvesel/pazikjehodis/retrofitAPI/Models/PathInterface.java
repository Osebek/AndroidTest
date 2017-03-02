package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by nejcvesel on 11/02/17.
 */

public interface PathInterface {


        @GET("pathGetSpecific/{id}/")
        Call<com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path> getSpecificPath(@Path("id") String id);
        @GET("pathGetAll/")
        Call<List<com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path>> getAllPaths();
        @POST("pathList/upload")
        Call<com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path> uploadPath(@Body com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path path);



}
