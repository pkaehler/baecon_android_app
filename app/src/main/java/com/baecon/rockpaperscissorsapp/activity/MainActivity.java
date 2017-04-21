package com.baecon.rockpaperscissorsapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences("userstats", MODE_PRIVATE);

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

        // FLiegt raus
        //TODO: Suche Beacon
        // Wenn gefunden, dann validiere
        // Wenn TRUE dann rufe Spiele Bildschirm auf
        // Code aus GameActivity
        ImageView background =  (ImageView) findViewById(R.id.background);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bluetoothIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(bluetoothIntent);
            }
        });
    }

}
