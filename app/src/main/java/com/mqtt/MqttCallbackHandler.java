package com.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;

import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackHandler implements MqttCallbackExtended {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            if (reconnect) {
                Log.d("MQTTConnector","NOT Connected to: " + serverURI);
            } else {
                Log.d("MQTTConnector","Connected to: " + serverURI);
            }
        }

        @Override
        public void connectionLost(Throwable throwable) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            Log.w("Anjing", mqttMessage.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }

  }