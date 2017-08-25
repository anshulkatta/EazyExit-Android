package com.activity;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fragment.MasterSwitchFragment;
import com.fragment.NewSwitchFragment;
import com.fragment.ViewSwitchFragment;
import com.provider.EazyExitContract;

import java.util.ArrayList;

import test.com.eazyexit.R;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        mockUpdate();

    }

    private void mockUpdate() {

        getContentResolver().delete(EazyExitContract.NodeEntry.CONTENT_URI, null, null);

        ContentValues values = new ContentValues();
        values.put(EazyExitContract.NodeEntry.COLUMN_NAME, "ZZZ");
        values.put(EazyExitContract.NodeEntry.COLUMN_SSID, "111");
        values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "OFF");
        values.put(EazyExitContract.NodeEntry.COLUMN_LEVEL, "PRIMARY");
        values.put(EazyExitContract.NodeEntry.COLUMN_LOCATION, "NEW");
        values.put(EazyExitContract.NodeEntry.COLUMN_TYPE, "LIGHT");

        getContentResolver().insert(EazyExitContract.NodeEntry.CONTENT_URI, values);

        values.clear();
        values.put(EazyExitContract.NodeEntry.COLUMN_NAME, "ZZZZ");
        values.put(EazyExitContract.NodeEntry.COLUMN_SSID, "1111");
        values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "OFF");
        values.put(EazyExitContract.NodeEntry.COLUMN_LEVEL, "PRIMARY");
        values.put(EazyExitContract.NodeEntry.COLUMN_LOCATION, "NEW");
        values.put(EazyExitContract.NodeEntry.COLUMN_TYPE, "LIGHT");

        getContentResolver().insert(EazyExitContract.NodeEntry.CONTENT_URI, values);

        values.clear();
        values.put(EazyExitContract.NodeEntry.COLUMN_NAME, "yyy");
        values.put(EazyExitContract.NodeEntry.COLUMN_SSID, "11");
        values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "OFF");
        values.put(EazyExitContract.NodeEntry.COLUMN_LEVEL, "SECONDARY");
        values.put(EazyExitContract.NodeEntry.COLUMN_LOCATION, "NEW");
        values.put(EazyExitContract.NodeEntry.COLUMN_TYPE, "LIGHT");

        getContentResolver().insert(EazyExitContract.NodeEntry.CONTENT_URI, values);


        values.clear();
        values.put(EazyExitContract.NodeEntry.COLUMN_NAME, "yyyy");
        values.put(EazyExitContract.NodeEntry.COLUMN_SSID, "222");
        values.put(EazyExitContract.NodeEntry.COLUMN_STATE, "ON");
        values.put(EazyExitContract.NodeEntry.COLUMN_LEVEL, "VITAL");
        values.put(EazyExitContract.NodeEntry.COLUMN_LOCATION, "NEW");
        values.put(EazyExitContract.NodeEntry.COLUMN_TYPE, "LIGHT");

        getContentResolver().insert(EazyExitContract.NodeEntry.CONTENT_URI, values);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return new ViewSwitchFragment();
                case 1:
                    return new MasterSwitchFragment();
                case 2:
                    return new NewSwitchFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
