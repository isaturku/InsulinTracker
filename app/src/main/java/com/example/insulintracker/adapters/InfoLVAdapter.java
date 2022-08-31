package com.example.insulintracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.insulintracker.R;

public class InfoLVAdapter extends ArrayAdapter {
    Activity ctx;

    public InfoLVAdapter(@NonNull Activity context) {
        super(context, R.layout.info_item);
    }
}
