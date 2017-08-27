package com.mqtt;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.provider.EazyExitContract;
import com.util.Util;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTTConnector {

    private Context context;

    public MqttAndroidClient connect (Context mContext, String broker, String clientid, final String subscriptionTopic) {
        context = mContext;
        final MqttAndroidClient   mqttClient = new MqttAndroidClient(mContext, broker, clientid);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttClient.setCallback(new MqttCallbackHandler());
        try {

            mqttClient.connect(mqttConnectOptions,null,new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic(mqttClient,subscriptionTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    exception.printStackTrace();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqttClient;

    }

    public void subscribeToTopic(MqttAndroidClient mqttClient,String subscriptionTopic){
        try {
            mqttClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTTConnector","connected now");
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("MQTTConnector","Failed to subscribe");
                }
            });
            mqttClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(topic.equals(Util.DISCOVERY_TOPIC) && message != null) {
                        String messageArr[] = message.toString().split(":");
                        if(messageArr !=null && messageArr.length >0) {
                            ContentValues value = Util.createContentValue("New Node",messageArr[0],
                                    "ON","PRIMARY","NEW","No Name Yet");
                            context.getContentResolver().insert
                                    (EazyExitContract.NodeEntry.CONTENT_URI, value);
                            value.clear();

                        }

                        Log.d("MQTTConnector","Message: " + topic + " : " + new String(message.getPayload()));
                    }

                }
            });

        } catch (MqttException ex){
            Log.d("MQTTConnector","Exception while subscribing");
            ex.printStackTrace();
        }
    }
}
