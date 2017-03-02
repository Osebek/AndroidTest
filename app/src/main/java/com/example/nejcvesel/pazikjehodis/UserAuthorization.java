package com.example.nejcvesel.pazikjehodis;

/**
 * Created by nejcvesel on 06/12/16.
 */

public class UserAuthorization {
    private String token;
    private String userID;
    private String appID;
    UserAuthorization(String token, String userID, String appID)
    {
        this.token = token;
        this.userID = userID;
        this.appID = appID;

    }


}
