package com.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mqtt.DiscoverListener;
import com.mqtt.MQTTConnector;

import com.provider.EazyExitContract;
import com.util.Util;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import test.com.eazyexit.NodePing;
import test.com.eazyexit.R;

/**
 * Created by Akshay on 14-08-2017.
 */

public class NewSwitchFragment extends Fragment  {

    private static DiscoverListener discoverListener;
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
                initDiscoverClient();
            }
        });
        return view;
    }


    @Override
    public void onDestroy() {
        if(discoverListener != null){
            discoverListener.disconnectClient();
        }
        super.onDestroy();
    }

    private void initDiscoverClient() {
        if(discoverListener == null) {
            discoverListener = new DiscoverListener(getContext());
            discoverListener.startDiscoverClient();
        } else {
            discoverListener.startDiscoverClient();
        }

    }


}
