package com.baecon.rockpaperscissorsapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.baecon.rockpaperscissorsapp.Adapter.ResultAdapter;
import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.Stats;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;

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
        getLastGames(id_player);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }


    public void getLastGames(int id){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Stats> call = apiService.getStats(id);
        call.enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                Stats resource = response.body();
                int rocks = resource.getRockCount();
                int papers = resource.getPaperCount();
                int scissors = resource.getScissorCount();
                Log.d(TAG,resource.toString());
                Log.d(TAG,"Name in Ressource: " + resource.getUser().getName());

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
