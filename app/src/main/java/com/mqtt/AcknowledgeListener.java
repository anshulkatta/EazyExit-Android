package com.mqtt;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import com.provider.EazyExitContract;
import com.util.Util;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Anshul on 09-09-2017.
 */

public class AcknowledgeListener  implements IMqttMessageListener {

    private static MqttAndroidClient ackClient = null;
    private static MQTTConnector connector = null;
    private Context context;

    public AcknowledgeListener(Context context) {
        this.context = context;
    }

    class AsnycAcknowledgeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                if(ackClient == null) {
                    connector = new MQTTConnector(AcknowledgeListener.this);
                    ackClient = connector.getClient(context, Util.getBrokerURL(context),"ackClient");
                    connector.connect(Util.ON_OFF_ACK_TOPIC);
                } else if(!ackClient.isConnected()) {
                    connector.connect(Util.ON_OFF_ACK_TOPIC);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if(topic.equals(Util.ON_OFF_ACK_TOPIC)&& message.toString() != null) {
            String[] arr = message.toString().split(":");
            updateStatus(arr[0],arr[1]);
        }
    }

    public void updateStatus(String hash,String status) {
        ContentValues values = null;
        if(status != null && !status.isEmpty()) {
            values = new ContentValues();
            if(status.contains("OFF")) {
                values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "OFF");
            } else if(status.contains("ON")) {
                values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "ON");
            }
        }
        if(values!=null) {
            context.getContentResolver().update(EazyExitContract.NodeEntry.CONTENT_URI, values,
                    EazyExitContract.NodeEntry.COLUMN_HASH + " =?", new String[]{hash});
        }
    }

    public void startAcknowlegementClient() {
        AsnycAcknowledgeTask asnycAcknowledgeTask = new AsnycAcknowledgeTask();
        asnycAcknowledgeTask.execute();
    }

    public void disconnectClient() {
        if(ackClient != null) {
            ackClient.unregisterResources();
            ackClient.close();
        }
    }
}
