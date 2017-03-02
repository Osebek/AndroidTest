package com.example.nejcvesel.pazikjehodis.retrofitAPI.Models;

import java.util.HashMap;
import java.util.Map;

public class BackendToken {

    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String refresh_token;
    private String scope;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String tokenType) {
        this.token_type = tokenType;
    }

    public Integer getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expires_in = expiresIn;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return String.format("%s %s",getAccessToken(), getRefreshToken());
    }
}
