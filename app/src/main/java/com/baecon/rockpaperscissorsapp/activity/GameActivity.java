package com.baecon.rockpaperscissorsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;
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

import static java.lang.Boolean.FALSE;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = GameActivity.class.getSimpleName();
    private ProximityManager proximityManager;
    private static final String APIKEY = "xxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        // Key verstecken
        //TODO: key hier drin lassen (also dynamisch) oder im manifest lassen (statisch)
        KontaktSDK.initialize(APIKEY);

        proximityManager = ProximityManagerFactory.create(this);
        proximityManager.setIBeaconListener(createIBeaconListener());


    }

    //TODO auslagern in eigene Klasse ?

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
                Log.d(TAG, "Heureka IBeacon discovered: " + ibeacon.toString());
//                if (isValidBeacon(ibeacon.getUniqueId()) ){
                //TODO starte spiel
            }
        };
    }

    private boolean isValidBeacon(String beaconID){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<String> call = apiService.isValidBeacon(beaconID);
        try {
            return Boolean.valueOf(call.execute().body());
        } catch (IOException e) {
            return FALSE;
        }
    }
}
