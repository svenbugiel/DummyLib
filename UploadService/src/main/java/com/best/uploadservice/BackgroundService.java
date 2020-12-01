package com.best.uploadservice;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class BackgroundService extends IntentService {
    public BackgroundService(String name) {
        super(name);
    }

    public BackgroundService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}