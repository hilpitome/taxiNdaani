package com.ndaani.taxi.taxindaani;

import android.app.Application;
import android.content.Context;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.ndaani.taxi.taxindaani.networking.NetworkService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by hilary on 9/11/17.
 */

public class MainApplication extends Application {
    private static Context context;
    private NetworkService networkService;

    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize NetworkService
        networkService = new NetworkService();
        context = getApplicationContext();

        // Initialize Realm
        Realm.init(this);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
//                .schemaVersion(2) // Must be bumped when the schema changes
//                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);

        // Fr
        Fresco.initialize(this);

        // Firebase
    }

    public NetworkService getNetworkService() {
        return networkService;
    }
}




