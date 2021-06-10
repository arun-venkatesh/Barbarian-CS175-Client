package com.pantrybuddy.activity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class appService extends Service {

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        SharedPreferences  sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if(!sharedPreferences.getBoolean("RememberMeCheckBox", false)){
            sharedPrefEditor.putBoolean("userLoggedIn", false);
            sharedPrefEditor.apply();
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy(){
        SharedPreferences  sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if(!sharedPreferences.getBoolean("RememberMeCheckBox", false)){
            sharedPrefEditor.putBoolean("userLoggedIn", false);
            sharedPrefEditor.apply();
        }
        super.onDestroy();
    }
}
