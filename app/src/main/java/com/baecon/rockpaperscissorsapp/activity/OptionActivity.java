package com.baecon.rockpaperscissorsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.adapter.PlayerAdapter;
import com.baecon.rockpaperscissorsapp.db.DatabaseHandler;
import com.baecon.rockpaperscissorsapp.model.Beacon;
import com.baecon.rockpaperscissorsapp.model.ReturnedErrorMessage;
import com.baecon.rockpaperscissorsapp.model.User;
import com.baecon.rockpaperscissorsapp.rest.ApiClient;
import com.baecon.rockpaperscissorsapp.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = OptionActivity.class.getSimpleName();
    private String playerName;
    private PlayerAdapter adapter;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        final DatabaseHandler db = new DatabaseHandler(this);

        sharedPrefs = getSharedPreferences("userstats",MODE_PRIVATE);
        editor = sharedPrefs.edit();


        adapter = new PlayerAdapter(this, new ArrayList<User>());

        List<User> userList = db.getAllPlayer();

        adapter.clear();
        adapter.addAll(userList);
        adapter.notifyDataSetChanged();

        final ListView listView = (ListView) findViewById(R.id.list_players);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        Button buttonPlayername = (Button) findViewById(R.id.savePlayername);
        final EditText playerNameEditText = (EditText) findViewById(R.id.inputPlayername);
        buttonPlayername.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerName = playerNameEditText.getText().toString();
                        if (!db.checkIfValueExists(playerName)){
                            Log.d(TAG,"Player schon vorhanden? " + db.checkIfValueExists(playerName));
                            createPlayer(playerName);
                            editor.putString("playername",playerName);
                            editor.commit();
                        } else {
                            new AlertDialog.Builder(OptionActivity.this)
                                    .setTitle("Jo")
                                    .setMessage("a player already exists: " + playerName)
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
                final String active_player = sharedPrefs.getString("playername",null);
                new AlertDialog.Builder(OptionActivity.this)
                        .setMessage("Are u sure? Delete " + active_player)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.remove("playername");
                                editor.commit();
                                db.deletePlayer(active_player);
                                List<User> newUserList = db.getAllPlayer();
                                adapter.clear();
                                adapter.addAll(newUserList);
                                adapter.notifyDataSetChanged();

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
        final DatabaseHandler db = new DatabaseHandler(this);

        Call<User> call = apiService.createPlayer(name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ReturnedErrorMessage errorMessage;

                    try {
                        errorMessage = gson.fromJson(response.errorBody().string(), ReturnedErrorMessage.class);
                        new AlertDialog.Builder(OptionActivity.this)
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
                    Log.d(TAG, "Getting Response");
                    User newUser = (User) response.body();
                    Log.d(TAG, "Writing player to db: " + newUser.getId() + " " + newUser.getName());
                    db.addPlayer(newUser);
                    List<User> newUserList = db.getAllPlayer();
                    adapter.clear();
                    adapter.addAll(newUserList);
                    adapter.notifyDataSetChanged();

                    new AlertDialog.Builder(OptionActivity.this)
                            .setMessage("player was born: " + newUser.getId() + " " + newUser.getName())
                            .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG,t.toString());
                call.cancel();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = adapter.getItem(position).getName();
                editor.remove("playername");
                editor.putString("playername",item);
                editor.commit();

        new AlertDialog.Builder(OptionActivity.this)
                .setMessage("Aktiver Player " + item)
                .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
