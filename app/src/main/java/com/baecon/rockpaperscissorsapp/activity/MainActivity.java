package com.baecon.rockpaperscissorsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    Context context;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        sharedPrefs = context.getSharedPreferences("userstats", Context.MODE_PRIVATE);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"Testen der shared Prefs: " + sharedPrefs.getInt("id",3));
        Log.d(TAG,"Testen der shared Prefs: " + sharedPrefs.getString("playername",null));
    }
}
