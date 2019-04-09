package com.example.meet.sos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

public class SendLocationService extends Service {

    public SendLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //strNumber = intent.getStringExtra("strNumber");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * IMPROVEABLE:
         * - currently static variable strNumber is used in SmsBroadcastReceiver
         *      because we are unable to pass that data using intent
         *
        * */
        sendMessage(SmsBroadcastReceiver.strNumber,getLocationString());
    }

    void sendMessage(String destination, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        String scAddress = null;
        PendingIntent sentIntent = null;
        PendingIntent receivedIntent = null;
        smsManager.sendTextMessage(destination, scAddress, message, sentIntent, receivedIntent);
    }

    @SuppressLint("MissingPermission")
    String getLocationString() {
        String finalLocationString = "location";

        LocationManager lm;
        Location location;
        double longitude = 0;
        double latitude = 0;

        if (hasLocationPermission()) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        finalLocationString = "https://www.google.com/maps/?q=" + latitude + "," + longitude;
        return finalLocationString;
    }

    boolean hasLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

}
