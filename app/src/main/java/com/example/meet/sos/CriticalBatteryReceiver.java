package com.example.meet.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class CriticalBatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");

        Toast.makeText(context,"CriticalBatteryReceiver is working...!",Toast.LENGTH_SHORT).show();
        Log.d("CBR", "CriticalBatteryReceiver is working...!");
    }
}
