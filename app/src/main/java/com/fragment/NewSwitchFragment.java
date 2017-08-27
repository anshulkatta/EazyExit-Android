package com.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mqtt.MQTTConnector;

import com.util.Util;


import org.eclipse.paho.android.service.MqttAndroidClient;

import test.com.eazyexit.NodePing;
import test.com.eazyexit.R;

/**
 * Created by Akshay on 14-08-2017.
 */

public class NewSwitchFragment extends Fragment{

    MqttAndroidClient discoveryclient = null;
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

                AsyncTask m = new AsyncDiscoveryTask(getContext());
                m.execute();


            }
        });
        return view;
    }

    class AsyncDiscoveryTask extends AsyncTask {

        private Context mContext;
        private MQTTConnector mqttConnector = null;

        AsyncDiscoveryTask(Context context) {
            this.mContext = context;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            mqttConnector = new MQTTConnector();
            MqttAndroidClient mqttAndroidClient = mqttConnector.connect(getContext(),
                    Util.getBrokerURL(getContext()), "discoverReceive",Util.DISCOVERY_TOPIC);
            discoveryclient = mqttAndroidClient;
            NodePing n = new NodePing();
            n.ping("IDENTIFY",Util.getBrokerURL(getContext()));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(getContext(),"Discovery is in progress",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onDestroy() {
        if(discoveryclient != null) {
            discoveryclient.unregisterResources();
            discoveryclient.close();
        }
        super.onDestroy();
    }
}
