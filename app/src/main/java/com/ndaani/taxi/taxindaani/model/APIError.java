package com.ndaani.taxi.taxindaani.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lawrence on 10/17/16.
 */

public class APIError {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("description")
    private String description;

    public APIError() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
