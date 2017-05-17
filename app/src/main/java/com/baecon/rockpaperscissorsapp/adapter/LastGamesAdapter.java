package com.baecon.rockpaperscissorsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.GameResult;

import java.util.ArrayList;

public class LastGamesAdapter extends ArrayAdapter<GameResult> {
    private static final String TAG = LastGamesAdapter.class.getSimpleName();
    public LastGamesAdapter(@NonNull Context context, @NonNull ArrayList<GameResult> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_last_games, parent, false);
        }
        GameResult currentResult = getItem(position);

        TextView optionPlayer = (TextView) listItemView.findViewById(R.id.option_played);
        optionPlayer.setText(currentResult.getOption());

        TextView wonLost = (TextView) listItemView.findViewById(R.id.result_game);
        String result = currentResult.getResult();
        switch (result) {
            case "WIN":
                wonLost.setText(R.string.won_against);
                break;
            case "LOOSE":
                wonLost.setText(R.string.lost_against);
                break;
            case "DRAWN":
                wonLost.setText(R.string.drawn_against);
                break;
        }

        return listItemView;
    }
}
