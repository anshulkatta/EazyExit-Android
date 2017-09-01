package com.fragment;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.ContentValues;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.transition.TransitionManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.provider.EazyExitContract;
import com.util.Util;

import test.com.eazyexit.NodePing;
import test.com.eazyexit.R;

/**
 * Created by Akshay on 14-08-2017.
 */

public class MasterSwitchFragment extends Fragment {

    View masterSwitch;
    boolean state;
    ViewGroup content;
    ScrollView bgHolder;
    ImageView bg, masterButton;
    AnimatedVectorDrawable on, off;

    String[] PROJECTION;


    public MasterSwitchFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_switch, container, false);

        content = (ViewGroup) view.findViewById(R.id.content_masterSwitchFragment);
        masterSwitch = view.findViewById(R.id.masterSwitch);
        bgHolder = (ScrollView) view.findViewById(R.id.bgHolder_masterSwitch);
        bg = (ImageView) view.findViewById(R.id.bg_masterSwitchFragment);
        masterButton = (ImageView) view.findViewById(R.id.masterButton);

        initialize();

        masterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private void initialize() {
        masterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_master_on));
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int width = size.x;


        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setColors(new int[] {
                ContextCompat.getColor(getContext(), R.color.colorPrimaryLight),
                ContextCompat.getColor(getContext(), R.color.colorPrimary)
        });
        bgDrawable.setSize(width, 2*height);
        bgDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);

        bg.setImageDrawable(bgDrawable);
        bgHolder.setVerticalScrollBarEnabled(false);
        bgHolder.setHorizontalScrollBarEnabled(false);
        bgHolder.setSmoothScrollingEnabled(true);
        bgHolder.fullScroll(View.FOCUS_UP);

        on = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.master_off_to_on);
        off = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.master_on_to_off);

        masterButton.setImageDrawable(off);
        off.start();

    }

    private void toggle(){
        AsyncTask switchAsyncTask = new MasterSwitchAsyncTask();
        TransitionManager.beginDelayedTransition(content);
        if(state){
            if(off.isRunning()){
                off.stop();
            }
            switchAsyncTask.execute("ON");
            masterButton.setImageDrawable(on);
            on.start();
            bgHolder.fullScroll(View.FOCUS_DOWN);
        } else {
            if(on.isRunning())
                on.stop();
            switchAsyncTask.execute("OFF");
            masterButton.setImageDrawable(off);
            off.start();
            bgHolder.fullScroll(View.FOCUS_UP);
        }
        state = !state;
        //TODO: Logic to update state of all nodes
        //updateNodes();


    }

    private void updateNodes() {

        ContentValues updateValues = new ContentValues();

        if(state)
            updateValues.put(EazyExitContract.NodeEntry.COLUMN_STATE, "OFF");
        else
            updateValues.put(EazyExitContract.NodeEntry.COLUMN_STATE, "ON");

        String mSelectionClause = EazyExitContract.NodeEntry.COLUMN_LEVEL + " = ?";
        String[] mSelectionArgs = {"PRIMARY"};

        getContext().getContentResolver().update(
                EazyExitContract.NodeEntry.CONTENT_URI,
                updateValues,
                mSelectionClause,
                mSelectionArgs
        );
    }

    class MasterSwitchAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            if(params!=null && params.length > 0 && params[0] != null) {
                NodePing p = new NodePing();
                p.ping((String)params[0], Util.getBrokerURL(getContext()));
            }
            return null;
        }
    }
}
