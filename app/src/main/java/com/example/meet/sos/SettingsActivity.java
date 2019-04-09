package com.example.meet.sos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] options = {
            "Contacts",
            "Distress message",
            "Remote location keyword",
            "Critical battery level",
            "About us"};
    private static final int[] imageId = {
            R.drawable.contact,
            R.drawable.message,
            R.drawable.voice,
            R.drawable.battery,
            R.drawable.about_us};

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    public static final String DEFAULT_DISTRESS_MESSAGE = "Help! I am in danger.";
    public static final String DEFAULT_REMOTE_LOCATION_KEYWORD = "get location";
    public static final int DEFAULT_CRITICAL_BATTERT_LEVEL = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }

        ListView listView = findViewById(R.id.settings_list_view);
        SettingsListAdapter adapter = new SettingsListAdapter(this,options,imageId);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(SettingsActivity.this, ContactsActivity.class));
                        break;
                    case 1:
                        setDistressMessage();
                        break;
                    case 2:
                        setRemoteLocationKeyword();
                        break;
                    case 3:
                        setCriticalBatteryLevel();
                        break;
                    case 4:
                        startActivity(new Intent(SettingsActivity.this, AboutUsActivity.class));
                        break;
                    default:
                }
            }
        });

        //sets all shared preferences when user opens the app for first time
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        prefEditor = preferences.edit();
        if (preferences.getInt("critical_battery_level",-1) == -1){
            prefEditor.putInt("critical_battery_level",DEFAULT_CRITICAL_BATTERT_LEVEL);
        }
        if (preferences.getString("remote_location_keyword","FIRST_TIME").equals("FIRST_TIME")){
            prefEditor.putString("remote_location_keyword",DEFAULT_REMOTE_LOCATION_KEYWORD);
        }
        if (preferences.getString("distress_message","FIRST_TIME").equals("FIRST_TIME")){
            prefEditor.putString("distress_message",DEFAULT_DISTRESS_MESSAGE);
        }
        prefEditor.apply();
    }

    void setDistressMessage(){
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        prefEditor = preferences.edit();

        final EditText editText = new EditText(getApplicationContext());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("SOS");
        alert.setMessage("Set distress message");
        alert.setView(editText);

        editText.setText(preferences.getString("distress_message",DEFAULT_DISTRESS_MESSAGE));
        editText.setSelectAllOnFocus(true);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newDistressMessage = editText.getText().toString();
                prefEditor.putString("distress_message",newDistressMessage);
                prefEditor.apply();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    void setRemoteLocationKeyword(){
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        prefEditor = preferences.edit();

        final EditText editText = new EditText(getApplicationContext());
        editText.setText(preferences.getString("remote_location_keyword",DEFAULT_REMOTE_LOCATION_KEYWORD));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("SOS");
        alert.setMessage("Set Remote Location Keyword");
        alert.setView(editText);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newRemoteLocationKeyword = editText.getText().toString();
                prefEditor.putString("remote_location_keyword",newRemoteLocationKeyword);
                prefEditor.apply();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    void setCriticalBatteryLevel(){
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        prefEditor = preferences.edit();

        int oldLevel = preferences.getInt("critical_battery_level",DEFAULT_CRITICAL_BATTERT_LEVEL);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("SOS");
        alert.setMessage("Set critical battery level (" + oldLevel + "%)");

        //with SeekBar
        final SeekBar seekBar = new SeekBar(getApplicationContext());
        seekBar.setProgress(oldLevel);
        alert.setView(seekBar);

        /*with EditText
        final EditText editText = new EditText(getApplicationContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(editText);
        */

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newCriticalBatteryLevel = seekBar.getProgress();
                prefEditor.putInt("critical_battery_level",newCriticalBatteryLevel);
                prefEditor.apply();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }
}
