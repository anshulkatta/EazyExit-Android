package com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.model.NewNode;
import com.model.Node;

import java.util.ArrayList;

import test.com.eazyexit.R;

/**
 * Created by Akshay on 25-08-2017.
 */

public class NewSwitchAdapter extends RecyclerView.Adapter<NewSwitchAdapter.ViewHolder>{

    ArrayList<String> dataSet;
    Context context;

    public NewSwitchAdapter(Context context, ArrayList<String> dataSet){
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ssid.setText(dataSet.get(position));
        final int pos = position;
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ssid;
        public ViewHolder(View itemView) {
            super(itemView);
            ssid = (TextView) itemView.findViewById(R.id.ssid_new);
        }
    }
}
