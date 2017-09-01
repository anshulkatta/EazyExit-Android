package com.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.model.Node;
import com.provider.EazyExitContract;

import java.util.ArrayList;

/**
 * Created by Akshay on 25-08-2017.
 */

public class CompareNodeList extends DiffUtil.Callback {

    ArrayList<Node> oldNodes = new ArrayList<>(), newNodes = new ArrayList<>();

    public CompareNodeList(ArrayList<Node> oldNodes, ArrayList<Node> newNodes){
        this.oldNodes = oldNodes;
        this.newNodes = newNodes;
    }

    @Override
    public int getOldListSize() {
        return oldNodes.size();
    }

    @Override
    public int getNewListSize() {
        return newNodes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNodes.get(oldItemPosition).getHash().equals(newNodes.get(newItemPosition).getHash());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNodes.get(oldItemPosition).equals(newNodes.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Node oldData = oldNodes.get(oldItemPosition);
        Node newData = newNodes.get(newItemPosition);

        Bundle payload = new Bundle();

        if(!oldData.getHash().equals(newData.getHash()))
            payload.putString(EazyExitContract.NodeEntry.COLUMN_HASH, newData.getHash());
        if(!oldData.getName().equals(newData.getName()))
            payload.putString(EazyExitContract.NodeEntry.COLUMN_NAME, newData.getName());
        if(!oldData.getLevel().equals(newData.getLevel()))
            payload.putString(EazyExitContract.NodeEntry.COLUMN_LEVEL, newData.getLevel());
        if(!oldData.getType().equals(newData.getType()))
            payload.putString(EazyExitContract.NodeEntry.COLUMN_TYPE, newData.getType());
        if(!oldData.getStatus().equals(newData.getStatus()))
            payload.putString(EazyExitContract.NodeEntry.COLUMN_STATE, newData.getStatus());
        if(!oldData.getLocation().equals(newData.getLocation()))
            payload.putString(EazyExitContract.NodeEntry.COLUMN_LOCATION, newData.getLocation());

        if(payload.isEmpty())
            return null;
        else
            return payload;
    }
}
