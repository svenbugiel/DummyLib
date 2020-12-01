package com.best.uploadservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends IntentService {
    public BackgroundService(String name) {
        super(name);
    }

    public BackgroundService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("COVFEFE", "SystemTime: " + SystemClock.elapsedRealtime());
            }
        }, 0, 5000);
    }
}