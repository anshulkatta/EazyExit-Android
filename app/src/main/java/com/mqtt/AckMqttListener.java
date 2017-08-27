package com.mqtt;

import com.adapter.ViewSwitchAdapter;
import com.util.Util;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anshul on 27-08-2017.
 */

public class AckMqttListener implements IMqttMessageListener {
    ViewSwitchAdapter adapter ;
    int position ;

    public AckMqttListener(ViewSwitchAdapter adapter,int position){
        this.adapter = adapter;
        this.position = position;
    }

    private Map<String,String> mapofMessage= new HashMap<String,String>();

    public Map<String, String> getMapofMessage() {
        return mapofMessage;
    }

    @Override

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if(topic.equals(Util.ON_OFF_ACK_TOPIC)&& message.toString() != null) {
            String[] arr = message.toString().split(":");
            if(arr != null && arr.length >= 1) {
                mapofMessage.put(arr[0],arr[1]);
            }
            adapter.updateStatus(position,arr[1]);
        }
    }
}
