package com.example.nejcvesel.pazikjehodis;

import android.content.SharedPreferences;

import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;

import java.net.URL;

/**
 * Created by brani on 3/2/2017.
 */

public class UserProfile {
    String loginType = "";
    String userName = "";
    String userToken = "";
    String refreshToken = "";
    String backendAccessToken = "";
    String firstName = "";
    String lastName = "";
    private SharedPreferences sharedPrefs;
    UserProfile(SharedPreferences sharedPrefs)
    {
        this.sharedPrefs = sharedPrefs;
    }

    UserProfile()
    {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isLogIn(){
        return this.userToken != null;
    }

    public void setBackendAccessToken(String accessToken) {
        this.backendAccessToken = accessToken;
    }

    public String getBackendAccessToken(){
        return this.backendAccessToken;
    }

    public boolean clearAll(){
        this.loginType = "";
        this.userName = "";
        this.userToken = "";
        this.refreshToken = "";
        this.backendAccessToken = "";
        this.firstName = "";
        this.lastName = "";
        return true;
    }
}
