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
import com.baecon.rockpaperscissorsapp.model.Result;

import java.util.ArrayList;

public class ResultAdapter extends ArrayAdapter<Result> {
    public ResultAdapter(@NonNull Context context, @NonNull ArrayList<Result> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_battle, parent, false);
        }
        Result currentResult = getItem(position);

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_icon);
        iconView.setImageResource(currentResult.getImageResourceId(currentResult.getAlpha2Code()));

        TextView headerText = (TextView) listItemView.findViewById(R.id.textHeader);
        headerText.setText(currentResult.getName());

        TextView footerText = (TextView) listItemView.findViewById(R.id.textFooter);
        footerText.setText(currentResult.getAlpha3Code());

        return listItemView;
    }
}
