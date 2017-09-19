package com.ndaani.taxi.taxindaani.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lawrence on 13/04/2017.
 */

public class AuthResponse {

    @SerializedName("token_type")
    String token_type;

    @SerializedName("access_token")
    String access_token;

    @SerializedName("user")
    User user;

    public String getTokenType() {
        return token_type;
    }

    public String getAccessToken() {
        return access_token;
    }

    public User getUser() {
        return user;
    }
}
