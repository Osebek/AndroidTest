package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by nejcvesel on 09/12/16.
 */

public interface LocationInterface {

    @GET("locationGetSpecific/{id}/")
    Call<Location> getSpecificLocation(@Path("id") String id);
    @GET("locationGetAll/")
    Call<List<Location>> getAllLocations();
    @GET("getUserLocations/")
    Call<List<Location>> getUserLocations();


}
