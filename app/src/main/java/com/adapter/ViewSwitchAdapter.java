package com.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.activity.NodeActivity;
import com.mqtt.AcknowledgeListener;
import com.model.Node;
import com.util.Util;
import java.util.ArrayList;
import test.com.eazyexit.NodePing;
import test.com.eazyexit.R;

/**
 * Created by Akshay on 15-08-2017.
 */

public class ViewSwitchAdapter extends RecyclerView.Adapter<ViewSwitchAdapter.ViewHolder> {

    ArrayList<Node> dataSet = new ArrayList<>();
    Context context;


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
        holder.location.setText(dataSet.get(position).getLocation());
        holder.name.setText(dataSet.get(position).getName());
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
        holder.hash = dataSet.get(pos).getHash();

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, location;
        Button on, off;
        String hash;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_view_row);
            location = (TextView) itemView.findViewById(R.id.location_view_row);
            on = (Button) itemView.findViewById(R.id.on_view_row);
            off = (Button) itemView.findViewById(R.id.off_view_row);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, NodeActivity.class);
            i.putExtra("hash",hash);
            context.startActivity(i);

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
}
