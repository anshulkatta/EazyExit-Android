package com.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adapter.ViewSwitchAdapter;
import com.model.Node;
import com.provider.EazyExitContract;

import java.util.ArrayList;

import test.com.eazyexit.R;

/**
 * Created by Akshay on 14-08-2017.
 */

public class ViewSwitchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    CoordinatorLayout content;
    RecyclerView recyclerView;
    ViewSwitchAdapter adapter;
    SwipeRefreshLayout srl;
    Update update;
    LoaderManager.LoaderCallbacks<Cursor> callbacks;
    Reload reload;

    private static final int VIEW_NODES_LOADER_ID = 0;

    String[] PROJECTION;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PROJECTION = new String[]{
                EazyExitContract.NodeEntry._ID,
                EazyExitContract.NodeEntry.COLUMN_LEVEL,
                EazyExitContract.NodeEntry.COLUMN_STATE,
                EazyExitContract.NodeEntry.COLUMN_SSID,
                EazyExitContract.NodeEntry.COLUMN_NAME,
                EazyExitContract.NodeEntry.COLUMN_LOCATION,
                EazyExitContract.NodeEntry.COLUMN_TYPE
        };
        update = new Update();
        reload = new Reload();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_switch,container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_view);

        toolbar.setTitle("All Nodes");

        srl = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
        content = (CoordinatorLayout) view.findViewById(R.id.content_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ViewSwitchAdapter(getContext(), new ArrayList<Node>());
        recyclerView.setAdapter(adapter);

        callbacks = this;

        LoaderManager manager = getLoaderManager();
        manager.initLoader(VIEW_NODES_LOADER_ID, null, callbacks);

        reload.cancel(true);
        reload = new Reload();
        reload.execute();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload.cancel(true);
                reload = new Reload();
                reload.execute();
            }
        });
        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this.getContext(), EazyExitContract.NodeEntry.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case VIEW_NODES_LOADER_ID:
                update.cancel(true);
                update = new Update();
                update.execute(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class Update extends AsyncTask<Cursor, Void, Boolean>{

        ArrayList<Node> dataSet = new ArrayList<>();


        @Override
        protected Boolean doInBackground(Cursor... params) {
            Cursor cursor = params[0];
            dataSet.clear();
            while(cursor.moveToNext()){
                Node node = new Node();
                node.setStatus(cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_STATE)));
                node.setName(cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_NAME)));
                node.setLevel(cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_LEVEL)));
                node.setLocation(cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_LOCATION)));
                node.setSsid(cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_SSID)));
                node.setType(cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_TYPE)));

                dataSet.add(node);

                if(isCancelled()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                adapter.swapData(dataSet);
            }
        }
    }

    public class Reload extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            Cursor cursor = getContext().getContentResolver().query(EazyExitContract.NodeEntry.CONTENT_URI, PROJECTION, null, null, null);
            ContentValues values = new ContentValues();
            String state = "";
            String ssid = "";
            String nState = "";
            while (cursor.moveToNext()){
                state = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_STATE));
                ssid = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_SSID));
                break;
            }
            cursor.close();
            if(state.equals("OFF")){
                nState = "ON";
            } else
                nState = "OFF";

            values.put(EazyExitContract.NodeEntry.COLUMN_STATE, nState);
            getContext().getContentResolver().update(EazyExitContract.NodeEntry.CONTENT_URI, values, EazyExitContract.NodeEntry.COLUMN_SSID + " = ?",
                    new String[]{ssid});

            values.clear();
            values.put(EazyExitContract.NodeEntry.COLUMN_STATE, state);
            getContext().getContentResolver().update(EazyExitContract.NodeEntry.CONTENT_URI, values, EazyExitContract.NodeEntry.COLUMN_SSID + " = ?",
                    new String[]{ssid});
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            srl.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            srl.setRefreshing(false);
        }
    }

}
