package com.example.meet.sos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Adapter{

    SOSDatabaseHelper dbhelper;
    SQLiteDatabase db;

    /*
    *
    * close
    * insert
    * delete
    * select
    * */
    DB_Adapter(Context context){
        dbhelper = new SOSDatabaseHelper(context);
    }

    //returns the same object just after opening writeable database
    /*
    DB_Adapter open(){
        db = dbhelper.getWritableDatabase();
        return this;
    }
    */

    //opens the connection to database
    void open(){
        db = dbhelper.getWritableDatabase();
    }

    //closes the database
    void close(){
        db.close();
    }

    void insert(String name, String number){
        db = dbhelper.getWritableDatabase();
        //set values in content values
        ContentValues cv = new ContentValues();
        cv.put("NAME", name);
        cv.put("CONTACT_NO", number);

        //insert values of cv in database
        db.insert("CONTACTS", null, cv);
    }

    int delete(int id){
        db = dbhelper.getWritableDatabase();
        return db.delete("CONTACTS","_id = ?",new String[]{Integer.toString(id)});
    }

    int delete(String name,String number){
        db = dbhelper.getWritableDatabase();
        return db.delete("CONTACTS","NAME = ? AND CONTACT_NO=?",new String[]{name,number});
    }

    Cursor select(){
        db = dbhelper.getWritableDatabase();
        Cursor c = db.query("CONTACTS",
                new String[]{"_id","NAME","CONTACT_NO"},
                null,null,
                null,null,null);
        return c;
    }

    public class SOSDatabaseHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "SOSDatabase";
        private static final int DB_VERSION = 1;
        private static final String tbName = "CONTACTS";

        //query for creating table
        private String createQuery = "CREATE TABLE " + tbName + "("
                +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"NAME TEXT NOT NULL,"
                +"CONTACT_NO TEXT NOT NULL);";

        public SOSDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            updateSOSDatabase(db,0,DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            updateSOSDatabase(db,oldVersion,newVersion);
        }

        private void updateSOSDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 2) {
                db.execSQL(createQuery);
            }

            /*
            ContentValues cv = new ContentValues();
            cv.put("NAME","meet");
            cv.put("CONTACT_NO","9913916628");
            db.insert("CONTACTS",null,cv);

            ContentValues cv2 = new ContentValues();
            cv.put("NAME","raj");
            cv.put("CONTACT_NO","1234567890");
            db.insert("CONTACTS",null,cv2);
            */
        }
    }
}
