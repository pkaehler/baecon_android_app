package com.baecon.rockpaperscissorsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.baecon.rockpaperscissorsapp.adapter.ResultAdapter;
import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.ReturnedErrorMessage;
import com.baecon.rockpaperscissorsapp.model.Stats;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private ResultAdapter adapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        adapter = new ResultAdapter(this, new ArrayList<Stats>());
        sharedPreferences = getSharedPreferences("userstats",MODE_PRIVATE);

        int id_player = sharedPreferences.getInt("id",0);
        Log.d(TAG,"id player: " + id_player);
        if (id_player != 0 ){
            getLastGames(id_player);
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

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
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
//                  Log.d(TAG,resource.toString());
//                  Log.d(TAG,"Name in Ressource: " + resource.getUser().getName());

                adapter.clear();
                adapter.addAll(resource);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }
        });
    }
}
