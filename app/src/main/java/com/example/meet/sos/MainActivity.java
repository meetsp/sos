package com.example.meet.sos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * fetch location
     * create & send distress message
     * **/

    public static final int PERMISSION_REQUEST_CODE_SMS_SEND = 1;
    public static final int PERMISSION_LOCATION_REQUEST_CODE = 2;
    public static final int PERMISSION_COARSE_LOCATION_REQUEST_CODE = 3;
    public static final int PERMISSION_REQUEST_CODE_SMS_READ = 4;

    public static final String TAG = "debug";

    ImageButton SOSBtn;
    //SmsManager smsManager;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolabar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(R.string.app_name);
        }

        SOSBtn = findViewById(R.id.sos_button);
        getSmsPermission();
        getLocationPermission();
        setSOSBtnEffect();
    }

    void sendMessageToAll() {
        try {
            DB_Adapter adapter = new DB_Adapter(this);
            Cursor cursor = adapter.select();
            while (cursor.moveToNext()) {
                String number = cursor.getString(2);
                sendMessage(number,getMessage());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //registerReceiver(new SmsBroadcastReceiver,new IntentFilter(android.provider.Telephony.Sms));
    }

    void sendMessage(String destination, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        //String destination = "+917046414036";
        String scAddress = null;
        PendingIntent sentIntent = null;
        PendingIntent receivedIntent = null;
        smsManager.sendTextMessage(destination, scAddress, message, sentIntent, receivedIntent);
    }

    String getMessage() {
        String finalMessage;
        String distressMessage;
        String location;
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        distressMessage = preferences.getString("distress_message", SettingsActivity.DEFAULT_DISTRESS_MESSAGE);
        location = getLocationString();
        finalMessage = distressMessage + "\n" + location;
        return finalMessage;
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

    @SuppressLint("MissingPermission")
    String getOnlyLocationString() {
        String finalLocationString = "location";

        LocationManager lm;
        Location location;
        double longitude = 0;
        double latitude = 0;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        finalLocationString = "https://www.google.com/maps/?q=" + latitude + "," + longitude;
        return finalLocationString;
    }

    void getSmsPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},PERMISSION_REQUEST_CODE_SMS_SEND);
        }
        //new code
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},PERMISSION_REQUEST_CODE_SMS_READ);
        }
    }

    boolean hasSmsPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

    void getLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION_REQUEST_CODE);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_COARSE_LOCATION_REQUEST_CODE);
        }
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

    public void onSOSBtnClick(View view) {
        /*
        if(hasSmsPermission()){
            sendMessage();
            Toast.makeText(MainActivity.this,"Message sent",Toast.LENGTH_SHORT).show();
        }else {
            getSmsPermission();
        }
        */
    }

    @SuppressLint("ClickableViewAccessibility")
    void setSOSBtnEffect(){
        SOSBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        SOSBtn.setBackgroundResource(R.mipmap.panic_pressed);
                        /**
                         * NEED TO EDIT
                         * - set sendMessage in onClickListener instead of here
                         * **/
                        if(hasSmsPermission()){
                            //sendMessage("+917046414036");
                            sendMessageToAll();
                            Toast.makeText(MainActivity.this,"Message sent",Toast.LENGTH_SHORT).show();
                        }else {
                            getSmsPermission();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        SOSBtn.setBackgroundResource(R.mipmap.panic_normal_round);
                        break;
                    default:
                        Log.d(TAG, "MainActivity -> setSOSBtnEffect -> default case");
                        return false;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
            default:
                Log.d(TAG, "default case executed in onOptionsItemSelected in MainActivity");
        }
        return super.onOptionsItemSelected(item);
    }

}
