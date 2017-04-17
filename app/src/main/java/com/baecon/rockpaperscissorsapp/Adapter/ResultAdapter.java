package com.baecon.rockpaperscissorsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baecon.rockpaperscissorsapp.R;
import com.baecon.rockpaperscissorsapp.model.Stats;

import java.util.ArrayList;

public class ResultAdapter extends ArrayAdapter<Stats> {
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

        TextView textRock = (TextView) listItemView.findViewById(R.id.textTotalRocksPlayed);
        textRock.setText(currentResult.getRockCount());

        TextView textPaper = (TextView) listItemView.findViewById(R.id.textTotalPapersPlayed);
        textPaper.setText(currentResult.getPaperCount());

        TextView textScissors = (TextView) listItemView.findViewById(R.id.textTotalScissorsPlayed);
        textScissors.setText(currentResult.getScissorCount());
        return listItemView;
    }
}
