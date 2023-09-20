package com.fabridev.webservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class customAdapter extends ArrayAdapter<String> {
    public customAdapter(Context context, List<String> data) {
        super(context, android.R.layout.simple_list_item_1, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(getItem(position));
        return view;
    }
}
