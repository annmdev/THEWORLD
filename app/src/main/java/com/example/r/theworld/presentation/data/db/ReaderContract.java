package com.example.r.theworld.presentation.data.db;

import android.provider.BaseColumns;

public class ReaderContract {

    protected static final String SQL_CREATE_FAVORITES = "CREATE TABLE " + Entry.TABLE_NAME
            + " (" + Entry._ID + " INTEGER PRIMARY KEY," + Entry.COLUMN_NAME_PLACE + " TEXT);";

    protected static final String SQL_DELETE_FAVORITES = "DROP TABLE IF EXISTS "
            + Entry.TABLE_NAME;

    private ReaderContract(){}

    public static class Entry implements BaseColumns{

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_PLACE = "place";
    }

}
