package com.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mqtt.MQTTConnector;
import com.model.Node;
import com.provider.EazyExitContract;
import com.util.Util;

import org.eclipse.paho.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import test.com.eazyexit.NodePing;
import test.com.eazyexit.R;

/**
 * Created by Akshay on 15-08-2017.
 */

public class ViewSwitchAdapter extends RecyclerView.Adapter<ViewSwitchAdapter.ViewHolder> implements IMqttMessageListener {

    ArrayList<Node> dataSet = new ArrayList<>();
    Context context;
    static MqttAndroidClient ackClient = null;

    public ViewSwitchAdapter(Context context, ArrayList<Node> dataSet){
        this.dataSet = dataSet;
        this.context = context;
    }
    @Override
    public ViewSwitchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewSwitchAdapter.ViewHolder holder, int position) {
        initAckClient();
        holder.location.setText(dataSet.get(position).getLocation());
        holder.name.setText(dataSet.get(position).getIp());
        boolean state = dataSet.get(position).getStatus().equals("ON");
        if(state) {
            holder.on.setVisibility(View.VISIBLE);
            holder.off.setVisibility(View.INVISIBLE);
        } else {
            holder.on.setVisibility(View.INVISIBLE);
            holder.off.setVisibility(View.VISIBLE);
        }
        final int pos = position;
        holder.on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOff(pos);
            }
        });

        holder.off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOn(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, location;
        Button on, off;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_view_row);
            location = (TextView) itemView.findViewById(R.id.location_view_row);
            on = (Button) itemView.findViewById(R.id.on_view_row);
            off = (Button) itemView.findViewById(R.id.off_view_row);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void toOn(int position){
        NodePing n = new NodePing();
        n.ping(dataSet.get(position).getHash()+":ON", Util.getBrokerURL(context));
    }

    private void toOff(int position){
        NodePing n = new NodePing();
        n.ping(dataSet.get(position).getHash()+":OFF", Util.getBrokerURL(context));
    }

    public void swapData(ArrayList<Node> dataSet){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CompareNodeList(this.dataSet, dataSet));

        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        diffResult.dispatchUpdatesTo(this);
    }

    class AsyncAcknowledgeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                if(ackClient == null) {
                    MQTTConnector connector = new MQTTConnector(ViewSwitchAdapter.this);
                    ackClient = connector.getClient(context,Util.getBrokerURL(context),"ackClient");
                    connector.connect(ackClient,Util.ON_OFF_ACK_TOPIC);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void initAckClient() {
        AsyncTask m = new AsyncAcknowledgeTask();
        m.execute();
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
}
