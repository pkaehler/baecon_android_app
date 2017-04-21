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
import com.baecon.rockpaperscissorsapp.model.Stats;

import java.util.ArrayList;

public class ResultAdapter extends ArrayAdapter<Stats> {
    private static final String TAG = ResultAdapter.class.getSimpleName();
    public ResultAdapter(@NonNull Context context, @NonNull ArrayList<Stats> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_battle, parent, false);
        }
        Stats currentResult = getItem(position);

        TextView rockHeadline = (TextView) listItemView.findViewById(R.id.totalRocksPlayed);
        rockHeadline.setText("Rocks played:");
        TextView textRock = (TextView) listItemView.findViewById(R.id.textTotalRocksPlayed);
        textRock.setText(currentResult.getRockCount().toString());
        TextView rockWins = (TextView) listItemView.findViewById(R.id.rockWins);
        rockWins.setText("Rocks won:");
        TextView textRockWins= (TextView) listItemView.findViewById(R.id.textRockWins);
        textRockWins.setText(currentResult.getRockWinCount().toString());


        TextView paperHeadline = (TextView) listItemView.findViewById(R.id.totalPapersPlayed);
        paperHeadline.setText("Papers played:");
        TextView textPaper = (TextView) listItemView.findViewById(R.id.textTotalPapersPlayed);
        textPaper.setText(currentResult.getPaperCount().toString());
        TextView paperWins = (TextView) listItemView.findViewById(R.id.paperWins);
        paperWins.setText("Papers won:");
        TextView textPaperWins= (TextView) listItemView.findViewById(R.id.textPaperWins);
        textPaperWins.setText(currentResult.getPaperWinCount().toString());

        TextView scissorsHeadline = (TextView) listItemView.findViewById(R.id.totalScissorsPlayed);
        scissorsHeadline.setText("Scissors played:");
        TextView textScissors = (TextView) listItemView.findViewById(R.id.textTotalScissorsPlayed);
        textScissors.setText(currentResult.getScissorCount().toString());
        TextView scissorsWins = (TextView) listItemView.findViewById(R.id.scissorsWins);
        scissorsWins.setText("Scissors won:");
        TextView textScissorsWins= (TextView) listItemView.findViewById(R.id.textScissorsWins);
        textScissorsWins.setText(currentResult.getScissorWinCount().toString());

        return listItemView;
    }
}
