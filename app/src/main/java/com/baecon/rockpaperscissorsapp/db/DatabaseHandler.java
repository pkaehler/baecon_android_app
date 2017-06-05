package com.baecon.rockpaperscissorsapp.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baecon.rockpaperscissorsapp.model.Beacon;
import com.baecon.rockpaperscissorsapp.model.User;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.Calendar.getInstance;

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private final int VALID_BEACON_TIME = 120000;

    private static final String DATABASE_NAME = "stats";
    private static final String TABLE_PLAYERS = "players";
    private static final String TABLE_BEACONS = "beacons";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ACTIVE = "is_active";
    private static final String FIELD_VALID_TO = "valid_to";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_NAME + " TEXT"
                + FIELD_ACTIVE + " INTEGER"
                + ")";

        db.execSQL(CREATE_PLAYER_TABLE);

        String CREATE_BEACON_TABLE = "CREATE TABLE " + TABLE_BEACONS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_NAME + " TEXT, "
                + FIELD_VALID_TO + " TEXT"
                + ")";

        db.execSQL(CREATE_BEACON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading db fto Version" + DATABASE_VERSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }

    public void addPlayer(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_ID, user.getId());
        values.put(FIELD_NAME, user.getName());

        db.insert(TABLE_PLAYERS, null, values);
        db.close();
    }

    public void addBeacon(Beacon beacon){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar valid_to = getInstance();
        valid_to.getTimeInMillis();
        valid_to.add(Calendar.MILLISECOND,VALID_BEACON_TIME);

        ContentValues values = new ContentValues();
        values.put(FIELD_NAME,beacon.getId_beacon());
        values.put(FIELD_VALID_TO, String.valueOf(valid_to));

        db.insert(TABLE_BEACONS,null,values);
        db.close();
    }

    public User getPlayer(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();

        ContentValues values = new ContentValues();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + FIELD_NAME + " = \"" + name + "\"" ;
        Log.d(TAG, "Running Query: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            Log.d(TAG, "Moving cursor to first");
            if  (cursor.moveToFirst()){
                Log.d(TAG, "Moved cursor to first");
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
            }
        } else {
            Log.d(TAG, "using else part");
            user.setId(0);
            user.setName("no User created");
        }
        db.close();
        Log.d(TAG,"User looks like " + user.getId() + " " + user.getName());
        return user;
    }

    public String getValidIdBeacon(){
        SQLiteDatabase db = this.getReadableDatabase();
        String id_beacon;
        String selectQuery = "SELECT " + FIELD_NAME + " FROM " + TABLE_BEACONS + " LIMIT 1";
        Log.d(TAG, "Running Query: " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            Log.d(TAG, "Moving cursor to first");
            id_beacon = null;
            if  (cursor.moveToFirst()){
                Log.d(TAG, "Moved cursor to first");
                id_beacon = cursor.getString(0);
            }
        } else {
            Log.d(TAG, "no valid beacon found");
            id_beacon = null;
        }
        db.close();
        Log.d(TAG,"id beacon selected from database: " + id_beacon);
        return id_beacon;
    }

    public void deletePlayer(String playername){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYERS, FIELD_NAME + " = ?", new String[] {playername});
        db.close();
    }

    public void deleteInvalidBeacons(){
        Calendar now = getInstance();
        now.getTimeInMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BEACONS, FIELD_VALID_TO + " < ?", new String[] {String.valueOf(now)});
        Log.d(TAG, "Loeschen erfolgreich? Vielleicht");
        db.close();
    }

    public List<User> getAllPlayer(){
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;

    }

    public boolean checkIfValueExists(String value){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT count(*) FROM " + TABLE_PLAYERS + " WHERE " + FIELD_NAME + " = \"" + value + "\"" ;
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        Integer exists = Integer.parseInt(cursor.getString(0));
        return exists != 0;
    }

    public boolean checkIfTableExists(String table_name){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT count(*) FROM sqlite_master WHERE name = \"" + table_name + "\"";
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        Integer exists = Integer.parseInt(cursor.getString(0));
        return exists != 0;
    }



}
