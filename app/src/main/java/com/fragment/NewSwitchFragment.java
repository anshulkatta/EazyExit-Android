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

public class NewSwitchFragment extends Fragment implements IMqttMessageListener {

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
            if(discoveryclient == null) {
                MQTTConnector  connector = new MQTTConnector(NewSwitchFragment.this);
                discoveryclient = connector.getClient(getContext(),Util.getBrokerURL(getContext()),"receiveClient");
                connector.connect(discoveryclient,Util.DISCOVERY_TOPIC);
            }
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

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if(topic.equals(Util.DISCOVERY_TOPIC) && message != null) {
            String messageArr[] = message.toString().split(":");
            if(messageArr !=null && messageArr.length >0) {
                Cursor cursor = getContext().getContentResolver().query(EazyExitContract.NodeEntry.CONTENT_URI,
                        new String[]{EazyExitContract.NodeEntry.COLUMN_HASH},
                        EazyExitContract.NodeEntry.COLUMN_HASH+" =? ",new String[]{messageArr[0]},null);
                while (cursor!=null && cursor.moveToNext()){
                    String hash = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_HASH));
                    if(hash.equals(messageArr[0])){
                        ContentValues values = new ContentValues();
                        values.put(EazyExitContract.NodeEntry.COLUMN_IP, messageArr[1]);
                        getContext().getContentResolver().update(EazyExitContract.NodeEntry.CONTENT_URI,
                                values,EazyExitContract.NodeEntry.COLUMN_HASH + " = ?",new String[]{hash});
                    }
                    return;
                }
                ContentValues value = Util.createContentValue("New Node",messageArr[0],messageArr[1],
                        "ON","PRIMARY","NEW","No Name Yet");
                getContext().getContentResolver().insert
                        (EazyExitContract.NodeEntry.CONTENT_URI, value);
                value.clear();
            }
            Log.d("MQTTConnector","Message: " + topic + " : " + new String(message.getPayload()));
        }
    }
}
