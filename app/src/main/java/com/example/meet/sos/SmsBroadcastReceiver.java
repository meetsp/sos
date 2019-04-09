package com.example.meet.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    Context context;
    static String strNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Log.d("smstest", "BroadcastReciever activated");

        Bundle bundle = intent.getExtras();
        SmsMessage msgs[];
        String strMessage = "";
        //String strNumber = "";
        String format = bundle.getString("format");

        Object[] pdus = (Object[]) bundle.get("pdus");
        if(pdus!=null){
            msgs = new SmsMessage[pdus.length];
            for (int i=0;i<msgs.length;i++){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i],format);
                }else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                strNumber = msgs[i].getOriginatingAddress();
                strMessage = msgs[i].getMessageBody();
                //Toast.makeText(context,strMessage,Toast.LENGTH_LONG).show();
            }
        }

        if (isMessageFromEmergencyContacts(strNumber) && isRemoteLocationKeyword(strMessage)){
            Intent serviceIntent = new Intent(context,SendLocationService.class);
            context.startService(serviceIntent);
            Toast.makeText(context,"Your location is sent",Toast.LENGTH_LONG).show();
        }

    }

    boolean isRemoteLocationKeyword(String strMessage){
        SharedPreferences preferences = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        if (preferences.getString("remote_location_keyword",null).equals(strMessage)){
            return true;
        }
        return false;
    }

    boolean isMessageFromEmergencyContacts(String senderNumber){
        try {
            DB_Adapter adapter = new DB_Adapter(context);
            Cursor cursor = adapter.select();
            while (cursor.moveToNext()) {
                String number = cursor.getString(2);
                if(senderNumber.equals(number)){
                    return true;
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
