package com.util;

import android.content.ContentValues;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.provider.EazyExitContract;

/**
 * Created by Anshul on 26-08-2017.
 */

public class Util {

    public static String DISCOVERY_TOPIC = "discoverReceive";
    public static String ON_OFF_ACK_TOPIC = "EazyExit/ACK";

    public static String getBrokerURL(Context ctx) {
        WifiManager wm = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return "tcp://"+ip+":1883";
    }

    public static ContentValues createContentValue(String name,String hash,String ip,String state,String level,
                                                   String location,String type) {
        ContentValues values = new ContentValues();

        values.put(EazyExitContract.NodeEntry.COLUMN_NAME, name);
        values.put(EazyExitContract.NodeEntry.COLUMN_IP, ip);
        values.put(EazyExitContract.NodeEntry.COLUMN_HASH, hash);
        values.put(EazyExitContract.NodeEntry.COLUMN_STATE, state);
        values.put(EazyExitContract.NodeEntry.COLUMN_LEVEL, level);
        values.put(EazyExitContract.NodeEntry.COLUMN_LOCATION, location);
        values.put(EazyExitContract.NodeEntry.COLUMN_TYPE, type);
        return values;
    }
}
