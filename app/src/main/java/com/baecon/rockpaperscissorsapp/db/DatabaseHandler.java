package com.baecon.rockpaperscissorsapp.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baecon.rockpaperscissorsapp.model.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "stats";
    private static final String TABLE_PLAYERS = "players";
    private static final String TABLE_BEACONS = "beacons";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ACTIVE = "is_active";
    private static final String FIELD_VALID = "is_valid";

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
        String CREATE_BEACON_TABLE = "CREATE TABLE " + TABLE_BEACONS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_VALID + " INTEGER"
                + ")";

        db.execSQL(CREATE_PLAYER_TABLE);
        db.execSQL(CREATE_BEACON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading db fto Version" + DATABASE_VERSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS);

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

    public User getPlayer(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + FIELD_NAME + " = \"" + name + "\"" ;

        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(0)));
        user.setName(cursor.getString(1));

        return user;
    }

    public void deletePlayer(String playername){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYERS, FIELD_NAME + " = ?", new String[] {playername});
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

    public void addBeacon(int id_beacon){

    }

    public boolean checkIfValueExists(String value){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT count(*) FROM " + TABLE_PLAYERS + " WHERE " + FIELD_NAME + " = \"" + value + "\"" ;
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        Integer exists = Integer.parseInt(cursor.getString(0));
        return exists != 0;
    }



}
