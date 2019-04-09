package com.example.meet.sos;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity {

    /*
    * fetches contacts from database
    * displays contacts
    *
    * displays alert on click on add FAB
    * and adds that contact to database and displays it in list
    * */

    ListView listView;

    private static final String TAG = "debug_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.contacts);
        }

        listView = findViewById(R.id.contacts_list_view);
        displayContacts();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);
                alert.setMessage(R.string.delete_contact_request);

                final String cName = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                final String cNumber = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();

                //Log.d(TAG, "name : " + cName);
                //Log.d(TAG, "number : " + cNumber);

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContact(cName,cNumber);
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();

                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                //create alert dialog
                AlertDialog.Builder addContactAlertDialog = new AlertDialog.Builder(ContactsActivity.this);
                addContactAlertDialog.setTitle("ADD CONTACT");

                //height and width of a liner layout
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                //EditText for name
                final EditText inputNameET = new EditText(ContactsActivity.this);
                inputNameET.setLayoutParams(lp);
                inputNameET.setHint("Name");
                inputNameET.setHintTextColor(Color.LTGRAY);

                //EditText for contact number
                final EditText inputNumberET = new EditText(ContactsActivity.this);
                inputNumberET.setLayoutParams(lp);
                inputNumberET.setHint("Contact Number");
                inputNumberET.setHintTextColor(Color.LTGRAY);

                //linear layout - container for name and contact number edittexts
                LinearLayout container = new LinearLayout(ContactsActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);
                container.addView(inputNameET);
                container.addView(inputNumberET);

                //set view of alert as a linear layout
                addContactAlertDialog.setView(container);

                //task to perform for positive response
                addContactAlertDialog.setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //get values from edit texts and store in Strings
                                String newContactName =  inputNameET.getText().toString();
                                String newContactNumber = inputNumberET.getText().toString();
                                addContact(newContactName,newContactNumber);
                            }
                        });

                //actions to perform for negative response from user
                addContactAlertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                //show alert dialog
                addContactAlertDialog.show();
            }
        });
    }

    void addContact(String name, String number){
        try {
            DB_Adapter db_adapter = new DB_Adapter(this);
            db_adapter.insert(name,number);
            db_adapter.close();
            displayContacts();
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //probably needs to edit input arguments
    void deleteContact(String name, String number){
        try{
            DB_Adapter db_adapter = new DB_Adapter(ContactsActivity.this);
            db_adapter.delete(name,number);
            displayContacts();
            db_adapter.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void displayContacts(){
        try {
            DB_Adapter adapter = new DB_Adapter(this);
            Cursor c = adapter.select();

            c.moveToFirst();
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    c,
                    new String[]{"NAME","CONTACT_NO"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(cursorAdapter);

            /*
            //////insert        *****working*****
            adapter.insert("meet","9913916628");
            adapter.insert("raj","123456");
            adapter.insert("vikalp","123789654");
            adapter.insert("manish","7891230546");
            //////delete        *****working*****
            adapter.delete(11);
            adapter.delete(12);

            ///displays in logcat for testing
            Cursor c = adapter.select();
            Log.d(TAG, "Contacts: ");
            while (c.moveToNext()){
                Log.d(TAG,"_id : " + c.getInt(0) +  "  name : " + c.getString(1) + "  number : " + c.getInt(2));
            }
            */
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
