package edu.sjsu.xuy87.contentproviderapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DBContract implements BaseColumns{
    public static final class NationLists implements BaseColumns{
        public static final String TABLE_NAME = "countries";

        public static final String CONTENT_AUTHORITY = "edu.sjsu.xuy87.contentproviderapp.data.MyContentProvider";

        public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        public static final String PATH_NAME = TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_NAME);

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_CONTINENT = "continent";
    }
}
