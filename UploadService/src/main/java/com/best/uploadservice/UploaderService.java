package com.best.uploadservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UploaderService {

    public UploaderService(Context context) {
        Intent intent = new Intent("android.intent.action.START_BACKGROUND_SERVICE");
        intent.setComponent(new ComponentName(context, BackgroundService.class));
        context.startService(intent);
    }

    public void upload(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}