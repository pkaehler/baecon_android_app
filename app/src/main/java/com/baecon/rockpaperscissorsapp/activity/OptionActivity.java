package com.baecon.rockpaperscissorsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.User;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionActivity extends AppCompatActivity {
    private static final String TAG = OptionActivity.class.getSimpleName();
    private String playerName;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        sharedPrefs = getSharedPreferences("userstats",MODE_PRIVATE);
        editor = sharedPrefs.edit();

        Button buttonPlayername = (Button) findViewById(R.id.savePlayername);
        final EditText playerNameEditText = (EditText) findViewById(R.id.inputPlayername);
        buttonPlayername.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerName = playerNameEditText.getText().toString();
                        if (!sharedPrefs.contains("playername")){
                            editor.putString("playername",playerName);
                            editor.commit();
                            createPlayer(playerName);

                        } else {
                            new AlertDialog.Builder(OptionActivity.this)
                                    .setTitle("Jo")
                                    .setMessage("a player already exists: " + sharedPrefs.getString("playername",null))
                                    .setNegativeButton("KK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    }
                }
        );

        TextView delete = (TextView) findViewById(R.id.delete_player);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OptionActivity.this)
                        .setTitle("Jo")
                        .setMessage("r u sure? Delete " + sharedPrefs.getString("playername",null))
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.clear();
                                editor.commit();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    public void createPlayer(String name){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, ApiClient.BASE_URL);


        Call<User> call = apiService.createPlayer(name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                User newUser = (User) response.body();
                editor.putInt("id", newUser.getId());
                editor.putString("playername", newUser.getName());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }
        });
    }
}
