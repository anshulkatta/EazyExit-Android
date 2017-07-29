package test.com.eazyexit;


import android.nfc.Tag;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class NodePing {

    public String ping(String content,String url) {

        String topic = "myHome";
        int qos = 2;
        String broker = url;
        String clientId = "Ping";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            Log.d("NodePing","Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            Log.d("NodePing","Connected");
            Log.d("NodePing","Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            Log.d("NodePing","Message published");
            sampleClient.disconnect();
            Log.d("NodePing","Disconnected");
            return "success";
        } catch (MqttException me) {
            Log.d("NodePing","reason " + me.getReasonCode());
            Log.d("NodePing","msg " + me.getMessage());
            Log.d("NodePing","loc " + me.getLocalizedMessage());
            Log.d("NodePing","cause " + me.getCause());
            Log.d("NodePing","excep " + me);
            me.printStackTrace();
            return "failure";
        }
    }

}