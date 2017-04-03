package com.baecon.rockpaperscissorsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.baecon.rockpaperscissorsapp.Adapter.ResultAdapter;
import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.Example;
import com.baecon.rockpaperscissorsapp.model.RestResponse;
import com.baecon.rockpaperscissorsapp.model.Result;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    String searchPhrase = "GER";
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        adapter = new ResultAdapter(this, new ArrayList<Result>());
        getLastGames(searchPhrase);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }


    public void getLastGames(String phrase){
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Example> call = apiService.getCountriesWithCode(phrase);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Example resource = response.body();
                RestResponse result = resource.getRestResponse();
                adapter.clear();
                adapter.addAll(result.getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }

        });
    }
}
