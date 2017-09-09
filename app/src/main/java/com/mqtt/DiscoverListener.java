package com.mqtt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fragment.NewSwitchFragment;
import com.provider.EazyExitContract;
import com.util.Util;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import test.com.eazyexit.NodePing;

/**
 * Created by Anshul on 10-09-2017.
 */

public class DiscoverListener implements IMqttMessageListener {

    private Context context;
    private static MqttAndroidClient discoveryclient = null;
    private MQTTConnector mqttConnector = null;

    public DiscoverListener(Context context){
        this.context = context;
    }

    class AsyncDiscoveryTask extends AsyncTask {



        @Override
        protected Object doInBackground(Object[] params) {
            if(discoveryclient == null) {
                mqttConnector = new MQTTConnector(DiscoverListener.this);
                discoveryclient = mqttConnector.getClient(context,Util.getBrokerURL(context),"receiveClient");
                mqttConnector.connect(Util.DISCOVERY_TOPIC);
            }
            NodePing n = new NodePing();
            n.ping("IDENTIFY",Util.getBrokerURL(context));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(context,"Discovery is in progress",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if(topic.equals(Util.DISCOVERY_TOPIC) && message != null) {
            String messageArr[] = message.toString().split(":");
            if(messageArr !=null && messageArr.length >0) {
                Cursor cursor = context.getContentResolver().query(EazyExitContract.NodeEntry.CONTENT_URI,
                        new String[]{EazyExitContract.NodeEntry.COLUMN_HASH},
                        EazyExitContract.NodeEntry.COLUMN_HASH+" =? ",new String[]{messageArr[0]},null);
                while (cursor!=null && cursor.moveToNext()){
                    String hash = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_HASH));
                    if(hash.equals(messageArr[0])){
                        ContentValues values = new ContentValues();
                        values.put(EazyExitContract.NodeEntry.COLUMN_IP, messageArr[1]);
                        context.getContentResolver().update(EazyExitContract.NodeEntry.CONTENT_URI,
                                values,EazyExitContract.NodeEntry.COLUMN_HASH + " = ?",new String[]{hash});
                    }
                    return;
                }
                ContentValues value = Util.createContentValue("New Node",messageArr[0],messageArr[1],
                        "ON","PRIMARY","NEW","No Name Yet");
                context.getContentResolver().insert
                        (EazyExitContract.NodeEntry.CONTENT_URI, value);
                value.clear();
            }
            Log.d("MQTTConnector","Message: " + topic + " : " + new String(message.getPayload()));
        }
    }

    public void startDiscoverClient() {
        AsyncDiscoveryTask asyncDiscoveryTask = new AsyncDiscoveryTask();
        asyncDiscoveryTask.execute();
    }

    public void disconnectClient() {
        if(discoveryclient != null) {
            discoveryclient.unregisterResources();
            discoveryclient.close();
        }
    }
}
