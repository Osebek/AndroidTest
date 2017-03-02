package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nejcvesel on 09/12/16.
 */

public interface UserInterface {

    @GET("user/{id}/")
    Call<User> getUserByID(@Path("id") String id);
    @GET("users/")
    Call<List<User>> getAllUsers();
}
