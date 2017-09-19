package com.ndaani.taxi.taxindaani.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lawrence on 13/04/2017.
 */

public class AuthData {

    @SerializedName("phone")
    String phone;

    public AuthData(String phone) {
        this.phone = phone;
    }
}
