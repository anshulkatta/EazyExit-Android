package com.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.provider.EazyExitContract;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import test.com.eazyexit.R;

/**
 * Created by Anshul on 04-09-2017.
 */

public class NodeActivity extends AppCompatActivity {

    Map<String,String> NvpChangeMap = new HashMap<String,String>();
    EditText nodeTextView ;
    ToggleButton editNodeName ;
    TextView ipTextView;
    FloatingActionButton igmvNodeName ;
    TextView hashTextView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.node_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.nodeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String hash = intent.getStringExtra("hash");
        nodeTextView = (EditText) findViewById(R.id.nodeName);
        editNodeName = (ToggleButton) findViewById(R.id.editNodeName);
        ipTextView = (TextView) findViewById(R.id.nodeIp);
        igmvNodeName = (FloatingActionButton) findViewById(R.id.saveButton);
        hashTextView = (TextView) findViewById(R.id.nodeHash);
        populateValues(hash,toolbar);

    }
    @Override
    public boolean onSupportNavigateUp(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void populateValues(String hash,Toolbar toolbar){


        setEventListeners(igmvNodeName,nodeTextView,editNodeName);

        Cursor cursor = getApplicationContext().getContentResolver().query(EazyExitContract.NodeEntry.CONTENT_URI,
                new String[]{EazyExitContract.NodeEntry.COLUMN_HASH,
                        EazyExitContract.NodeEntry.COLUMN_NAME,
                        EazyExitContract.NodeEntry.COLUMN_IP},
                EazyExitContract.NodeEntry.COLUMN_HASH+" =? ",new String[]{hash},null);
        if(cursor!=null && cursor.moveToNext()){
            NvpChangeMap.put(EazyExitContract.NodeEntry.COLUMN_HASH,hash);
            String hashValue = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_HASH));
            String nodeName = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_NAME));
            String ip = cursor.getString(cursor.getColumnIndex(EazyExitContract.NodeEntry.COLUMN_IP));
            if(hash.equals(hashValue)){
                toolbar.setTitle(nodeName);
                nodeTextView.setText(nodeName);
                ipTextView.setText(ip);
                hashTextView.setText(hashValue);
            }
        }
    }
    private void setEventListeners(final FloatingActionButton saveButton,final EditText nodeTextView,
                                   final ToggleButton editNodeName){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveInfo();
            }
        });
        editNodeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nodeTextView.isEnabled()) {
                    nodeTextView.setSelection(nodeTextView.getText().length());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(nodeTextView, InputMethodManager.SHOW_FORCED);
                    nodeTextView.setEnabled(true);
                }
                else
                    nodeTextView.setEnabled(false);
            }
        });

        nodeTextView.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            NvpChangeMap.put(EazyExitContract.NodeEntry.COLUMN_NAME,s.toString());
        }
    };

    private void saveInfo() {
        beforeSave();
        String hash ="";
        ContentValues values = new ContentValues();
        if(NvpChangeMap.containsKey(EazyExitContract.NodeEntry.COLUMN_HASH)){
            hash = NvpChangeMap.get(EazyExitContract.NodeEntry.COLUMN_HASH);
        }
        Set<String> setOfChangedvalues = NvpChangeMap.keySet();
        if(!setOfChangedvalues.isEmpty()) {
            for(String s:setOfChangedvalues){
                values.put(s,NvpChangeMap.get(s));
            }
        }

        if(values !=null && !hash.isEmpty()) {
            getContentResolver().update(EazyExitContract.NodeEntry.CONTENT_URI, values,
                    EazyExitContract.NodeEntry.COLUMN_HASH + " =?", new String[]{
                           hash});
        }
        NvpChangeMap.clear();
        NvpChangeMap.put(EazyExitContract.NodeEntry.COLUMN_HASH,hash);
        values.clear();
    }

    private void beforeSave() {
        nodeTextView.setEnabled(false);
    }
}
