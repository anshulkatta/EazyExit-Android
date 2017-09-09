package com.mqtt;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.fragment.NewSwitchFragment;
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


public class MQTTConnector implements IMqttActionListener{

    private Context context;
    private MqttAndroidClient mqttClient;
    private IMqttMessageListener listener;
    private String subscriptionTopic;

    public MQTTConnector(IMqttMessageListener listener) {
        this.listener = listener;
    }

    public MqttAndroidClient getClient(Context mContext, String broker, String clientid) {
        context = mContext;
        mqttClient = new MqttAndroidClient(mContext, broker, clientid);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttClient.setCallback(new MqttCallbackHandler());
        return mqttClient;
    }

    public void connect(String subscriptionTopic) {
        try {
            this.mqttClient.connect(null, this);
            this.subscriptionTopic = subscriptionTopic;
        }catch (Exception e) {
            System.out.print("Not connected ..");
        }
    }

    private DisconnectedBufferOptions getDisconnectedbufferoptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }


        public void onSuccess(IMqttToken asyncActionToken) {
            mqttClient.setBufferOpts(getDisconnectedbufferoptions());
            subscribeToTopic(mqttClient, subscriptionTopic);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          System.out.println("Not Connected yet");
        }



    private void subscribeToTopic(MqttAndroidClient mqttClient,String subscriptionTopic){
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
            mqttClient.subscribe(subscriptionTopic, 0,listener);

        } catch (MqttException ex){
            Log.d("MQTTConnector","Exception while subscribing");
            ex.printStackTrace();
        }
    }
}
