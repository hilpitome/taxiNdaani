package com.ndaani.taxi.taxindaani.auth;


import com.google.firebase.auth.FirebaseUser;
import com.ndaani.taxi.taxindaani.model.AuthResponse;

/**
 * Created by Lawrence on 11/04/2017.
 */

public interface AuthView {

    void showWait();

    void removeWait();

    void onFailure(String message, String description, int code);

    void showDialog();

    void setVerificationId(String verificationId);

    void setAuthResponse(FirebaseUser user);
}
