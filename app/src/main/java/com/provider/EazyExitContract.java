package com.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Akshay on 14-08-2017.
 */

public class EazyExitContract {
    public static final String CONTENT_AUTHORITY = "com.eazyexit.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NODE = "node";

    public static final class NodeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NODE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir" + CONTENT_URI + "/" + PATH_NODE;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item" + CONTENT_URI + "/" + PATH_NODE;

        public static final String TABLE_NAME = "NodeTable";
        public static final String COLUMN_NAME = "nodeName";
        public static final String COLUMN_IP= "nodeIp";
        public static final String COLUMN_HASH = "nodeHash";
        public static final String COLUMN_TYPE = "nodeType";
        public static final String COLUMN_LEVEL = "nodeLevel";
        public static final String COLUMN_STATE = "nodeState";
        public static final String COLUMN_LOCATION = "nodeLocation";

        public static Uri buildNodeUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
