package com.baecon.rockpaperscissorsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.baecon.rockpaperscissorsapp.adapter.LastGamesAdapter;
import com.baecon.rockpaperscissorsapp.adapter.ResultAdapter;
import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.db.DatabaseHandler;
import com.baecon.rockpaperscissorsapp.model.GameResult;
import com.baecon.rockpaperscissorsapp.model.ReturnedErrorMessage;
import com.baecon.rockpaperscissorsapp.model.Stats;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private ResultAdapter resultAdapter;
    private LastGamesAdapter lastGamesAdapter;
    private static int id_player;
    private static String playerName;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final DatabaseHandler db = new DatabaseHandler(this);
        Log.d(TAG, "Database created");

        resultAdapter = new ResultAdapter(this, new ArrayList<Stats>());
        lastGamesAdapter = new LastGamesAdapter(this, new ArrayList<GameResult>());
        sharedPreferences = getSharedPreferences("userstats",MODE_PRIVATE);
        playerName = sharedPreferences.getString("playername",null);
        Log.d(TAG,"Player in Pref: " + playerName);
        id_player = db.getPlayer(playerName).getId();

        Log.d(TAG,"id player: " + id_player);
        if (id_player != 0 ){
            getLastGames(id_player);
            getAllGamesForPlayer(id_player);
        } else{
            new AlertDialog.Builder(HistoryActivity.this)
                    .setMessage("No player found")
                    .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

        ListView listView = (ListView) findViewById(R.id.historyList);
        listView.setAdapter(resultAdapter);
        ListView lastGamesView = (ListView) findViewById(R.id.lastGamesList);
        lastGamesView.setAdapter(lastGamesAdapter);
    }

    private void getAllGamesForPlayer(int id_player){
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<GameResult>> call = apiService.getAllGamesForPlayer(id_player);
        call.enqueue(new Callback<List<GameResult>>() {
            @Override
            public void onResponse(Call<List<GameResult>> call, Response<List<GameResult>> response) {
                if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ReturnedErrorMessage errorMessage;

                    try {
                        errorMessage = gson.fromJson(response.errorBody().string(),ReturnedErrorMessage.class);
                        new AlertDialog.Builder(HistoryActivity.this)
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
                    List<GameResult> resource = response.body();
                    // if wird wahrscheinlich nicht benötigt
                    if (resource != null && resource.size() > 0){
                        lastGamesAdapter.clear();
                        lastGamesAdapter.addAll(resource);
                        lastGamesAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<GameResult>> call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }
        });
    }


    private void getLastGames(int id){
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Stats> call = apiService.getStats(id);
        call.enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ReturnedErrorMessage errorMessage;

                    try {
                        errorMessage = gson.fromJson(response.errorBody().string(),ReturnedErrorMessage.class);
                        new AlertDialog.Builder(HistoryActivity.this)
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
                Stats resource = response.body();

                resultAdapter.clear();
                resultAdapter.addAll(resource);
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }
        });
    }
}
