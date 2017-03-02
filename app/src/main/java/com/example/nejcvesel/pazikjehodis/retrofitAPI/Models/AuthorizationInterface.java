package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by nejcvesel on 23/02/17.
 */

public interface AuthorizationInterface {
    @FormUrlEncoded
    @POST("auth/convert-token/")
    Call<BackendToken> convertToken(
            @Field("grant_type") String grant,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("backend") String backend,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("auth/token/")
    Call<BackendToken> refreshToken(
            @Field("grant_type") String grant,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("refresh_token") String refresh_token);

}
