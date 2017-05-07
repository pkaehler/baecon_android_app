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
import com.baecon.rockpaperscissorsapp.model.User;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<User> {
    private static final String TAG = PlayerAdapter.class.getSimpleName();

    public PlayerAdapter(@NonNull Context context, @NonNull ArrayList<User> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_player, parent, false);
        }
        User currentUser = getItem(position);

        TextView playerName = (TextView) listItemView.findViewById(R.id.item_playername);
        playerName.setText(currentUser.getName());

        return listItemView;
    }
}
