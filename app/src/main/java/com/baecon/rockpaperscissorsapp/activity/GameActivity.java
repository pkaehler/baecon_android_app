package com.baecon.rockpaperscissorsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.Move;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = GameActivity.class.getSimpleName();
    private static String OPTION;
    SharedPreferences sharedPreferences;
    private static String id_beacon ="tobedefined";
    private static int id_player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferences = getSharedPreferences("userstats",MODE_PRIVATE);
//        id_beacon = sharedPreferences.getString("id_beacon",null);
        id_player = sharedPreferences.getInt("id",0);
        //TODO: lesen der shared pref (beaconid, playerid)
        //TODO: onclick listener und restcall rausflanken mit
//        setMove(@Field("beaconId") int beaconId, @Field("playerId") int playerId, @Field("option") String option);
        final ImageView rock = (ImageView) findViewById(R.id.rockBattleOption);
        final ImageView paper = (ImageView) findViewById(R.id.paperBattleOption);
        final ImageView scissors = (ImageView) findViewById(R.id.scissorsBattleOption);

        rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rock.setAlpha(1f);
                paper.setAlpha(0.2f);
                scissors.setAlpha(0.2f);
                OPTION = "ROCK";
            }
        });
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paper.setAlpha(1f);
                rock.setAlpha(0.2f);
                scissors.setAlpha(0.2f);
                OPTION = "PAPER";
            }
        });
        scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scissors.setAlpha(1f);
                rock.setAlpha(0.2f);
                paper.setAlpha(0.2f);
                OPTION = "SCISSOR";
            }
        });

        TextView fight = (TextView) findViewById(R.id.fightButton);
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Fight with following: " + id_beacon + " and player " + id_player + " and option " + OPTION);
                fight(id_beacon,id_player,OPTION);
            }
        });


    }

    private void fight(String id_beacon, int id_player, String option){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Move> call = apiService.setMove(123,id_player,option);
        call.enqueue(new Callback<Move>() {
            @Override
            public void onResponse(Call<Move> call, Response<Move> response) {
                if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ReturnedErrorMessage errorMessage;

                    try {
                        errorMessage = gson.fromJson(response.errorBody().string(),ReturnedErrorMessage.class);
                        new AlertDialog.Builder(GameActivity.this)
                                .setMessage("something went wrong. Got: " + errorMessage.getErrorCode() + " with message " + errorMessage.getErrorMessage())
                                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Move resource = response.body();
                if (resource.getSecondFigure() != null ){
                    new AlertDialog.Builder(GameActivity.this)
                            .setMessage("made move against: " + resource.getFirstUser().getName() + " with Option: " + resource.getFirstFigure())
                            .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(GameActivity.this)
                            .setMessage("you are the first player: " + resource.getFirstUser().getName() + " with Option: " + resource.getFirstFigure())
                            .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                //TODO save game result or show if secondUser already exists
                //TODO Backend: also returns who won (don't want to implement logic here)
                /**
                 * Repsonse example
                 * {
                 "id": 3,
                 "firstUser": {
                 "id": 1,
                 "name": "User 2"
                 },
                 "secondUser": null,
                 "firstFigure": "SCISSOR",
                 "secondFigure": null
                 }
                 */
            }

            @Override
            public void onFailure(Call<Move> call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }
        });
    }
}
