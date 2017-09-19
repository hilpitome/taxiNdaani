package com.ndaani.taxi.taxindaani.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hilary on 9/19/17.
 */

public class User {


    @SerializedName("name")
    String name;

    @SerializedName("phone")
    String phone;

    @SerializedName("email")
    String email;



    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
