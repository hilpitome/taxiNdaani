package com.ndaani.taxi.taxindaani.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Lawrence on 10/12/16.
 */

public class PrefUtils {

    /**
     * Shared Preferences
     */
    private SharedPreferences sharedPreferencesCompat;

    /**
     * Editor for Shared preferences
     */
    private SharedPreferences.Editor editor;

    /**
     * Context
     */
    private Context mContext;

    /**
     * Shared pref mode
     */
    private int PRIVATE_MODE = 0;

    /**
     * Shared preferences file name
     */
    private static final String PREF_NAME = "taxiNdaniPrefs";

    /**
     *
     */
    private static final String KEY_IS_LOGGED_IN = "isUserLoggedIn";


    private static final String KEY_ACCESS_TOKEN = "token";

    private static final String KEY_USER_ID = "user_id";

    private static final String KEY_IS_WAITING_FOR_SMS = "IsUserWaitingForSms";

    private static final String KEY_IS_NEW_USER = "IsNewUser";

    private static final String KEY_NAME = "name";

    private static final String KEY_PHONE = "phone";



    public PrefUtils(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferencesCompat = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = this.sharedPreferencesCompat.edit();
    }


    public boolean isLoggedIn() {
        return sharedPreferencesCompat.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isWaitingForSms() {
        return sharedPreferencesCompat.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }


    public String getUserAccessToken() {
        return sharedPreferencesCompat.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setUserAccessToken(String token) {
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.commit();
    }

    public int getUserId() {
        return sharedPreferencesCompat.getInt(KEY_USER_ID, 0);
    }

    public void setUserId(int user_id) {
        editor.putInt(KEY_USER_ID, user_id);
        editor.commit();
    }



    public String getUserPhone() {
        return sharedPreferencesCompat.getString(KEY_PHONE, null);
    }


    public void storeUserDetails(String name, String phone) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE, phone);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", sharedPreferencesCompat.getString(KEY_NAME, null));
        profile.put("phone", sharedPreferencesCompat.getString(KEY_PHONE, null));
        return profile;
    }
}
