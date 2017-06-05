package com.baecon.rockpaperscissorsapp.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.db.DatabaseHandler;
import com.baecon.rockpaperscissorsapp.model.Beacon;
import com.baecon.rockpaperscissorsapp.model.ReturnedErrorMessage;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProximityManager proximityManager;
    private static final String APIKEY = "ok";
    //TODO Refactoring id_beacon, id_player, player_name,... -> clean code
    private String playerName;
    private String id_beacon = "4LKv";


    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseHandler db = new DatabaseHandler(this);

        db.deleteInvalidBeacons();
        Log.d(TAG,"Beacons Table existiert: " + db.checkIfTableExists("beacons"));

        sharedPrefs = getSharedPreferences("userstats", MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.putBoolean("sawBeaconOverlay", false);
        editor.commit();
        playerName = sharedPrefs.getString("playername",null);
        if (playerName != null){
            TextView active_player = (TextView) findViewById(R.id.main_active_player);
            active_player.setText(playerName);
            active_player.setAlpha(1f);
        }
        TextView history = (TextView) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
               startActivity(historyIntent);
           }
       });

        TextView options = (TextView) findViewById(R.id.options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent optionsIntent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(optionsIntent);
            }
        });

        ImageView logo = (ImageView) findViewById(R.id.background);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(gameIntent);
            }
        });

        // TODO remove Kontakt.io but use public ibeacon listener
        KontaktSDK.initialize(APIKEY);

        proximityManager = ProximityManagerFactory.create(this);
        proximityManager.setIBeaconListener(createIBeaconListener());

    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                //Check if emulator is running
                String fingerprint = Build.FINGERPRINT;
                boolean isEmulator = false;
                if (fingerprint != null) {
                    isEmulator = fingerprint.contains("vbox") || fingerprint.contains("generic");
                }
                Log.d(TAG, "Running in Emulator: " + isEmulator);
                isValidBeacon(isEmulator == true ? id_beacon : String.valueOf(ibeacon.getUniqueId()));

                }
        };
    }

    private void isValidBeacon(final String id_beacon){
        final DatabaseHandler db = new DatabaseHandler(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.isvalidbeacon(id_beacon);
            Log.d(TAG,"returning True for UUID: " + id_beacon);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        ReturnedErrorMessage errorMessage;

                        try {
                            errorMessage = gson.fromJson(response.errorBody().string(), ReturnedErrorMessage.class);
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("something went wrong. Got: " + errorMessage.getErrorCode() + " with message " + errorMessage.getErrorMessage())
                                    .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ImageView beaconIconVisible = (ImageView) findViewById(R.id.beaconFound);
                        beaconIconVisible.setAlpha(1f);

                        boolean sawBeaconOverlay = getSharedPreferences("userstats", MODE_PRIVATE).getBoolean("sawBeaconOverlay", false);

                        String isValid = response.body();
                        editor.putString("id_beacon", id_beacon);
                        editor.commit();

                        db.addBeacon(new Beacon(id_beacon));



                        //TODO if push is clicked only opens fight menu without option to go back to main and change player
                        if (isValid.equals("true")) {
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(MainActivity.this)
                                            .setSmallIcon(R.drawable.battleicon)
                                            .setContentTitle("Yo")
                                            .setContentText("wanna fight?")
                                            .setAutoCancel(true);
                            Intent notificationIntent = new Intent(MainActivity.this, GameActivity.class);
                            PendingIntent notificationPendingIntent =
                                    PendingIntent.getActivity(
                                            MainActivity.this,
                                            0,
                                            notificationIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            mBuilder.setContentIntent(notificationPendingIntent);

                            int mNotificationId = 001;
                            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());

                            if (!sawBeaconOverlay){
                                editor.putBoolean("sawBeaconOverlay",true);
                                editor.commit();

                                new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.drawable.battleicon)
                                        .setMessage("Beacon gefunden. Want to play a game?")
                                        .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                                                startActivity(gameIntent);
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG,t.toString());
                    call.cancel();
                }
            });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "wird auch ausgeführt oder?");
        sharedPrefs = getSharedPreferences("userstats", MODE_PRIVATE);
        playerName = sharedPrefs.getString("playername",null);
        Log.d(TAG, "onRestart: " + playerName);
        if (playerName != null){
            TextView active_player = (TextView) findViewById(R.id.main_active_player);
            active_player.setText(playerName);
            active_player.setAlpha(1f);
        } else {
            TextView active_player = (TextView) findViewById(R.id.main_active_player);
            active_player.setAlpha(0f);
        }

    }

}
