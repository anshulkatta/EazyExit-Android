package com.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.provider.EazyExitContract;

import test.com.eazyexit.R;

/**
 * Created by Akshay on 14-08-2017.
 */

public class NewSwitchFragment extends Fragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_switch,container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();

                values.put(EazyExitContract.NodeEntry.COLUMN_NAME, "yyyyy");
                values.put(EazyExitContract.NodeEntry.COLUMN_SSID, "2223");
                values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "ON");
                values.put(EazyExitContract.NodeEntry.COLUMN_LEVEL, "VITAL");
                values.put(EazyExitContract.NodeEntry.COLUMN_LOCATION, "NEW");
                values.put(EazyExitContract.NodeEntry.COLUMN_TYPE, "LIGHT");
                getContext().getContentResolver().insert(EazyExitContract.NodeEntry.CONTENT_URI, values);
            }
        });
        return view;
    }
}
