package com.example.r.theworld.presentation.favorites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.r.theworld.presentation.data.db.ReaderBdHelper;
import com.example.r.theworld.presentation.data.db.ReaderContract;

import java.util.ArrayList;
import java.util.List;

public class FavoritesDatabase {
    private SQLiteDatabase db;
    private ReaderBdHelper dbHelper;

    private static FavoritesDatabase favoritesDatabase;

    private FavoritesDatabase(Context context) {
        dbHelper = new ReaderBdHelper(context);
    }

    public static FavoritesDatabase create(Context context){
        if (favoritesDatabase == null) {
            favoritesDatabase = new FavoritesDatabase(context);
        }
        return favoritesDatabase;
    }

    public List<String> getAll(){
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(ReaderContract.Entry.TABLE_NAME,
                new String[]{ReaderContract.Entry.COLUMN_NAME_PLACE},
                null, null, null, null, null);

        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex(ReaderContract.Entry.COLUMN_NAME_PLACE)));
        }
        cursor.close();

        return list;
    }

    public void put(String location) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReaderContract.Entry.COLUMN_NAME_PLACE, location);

        db.insert(ReaderContract.Entry.TABLE_NAME, null, values);

    }

    public boolean contains(String location) {
        db = dbHelper.getReadableDatabase();

        String[] projection = {
                ReaderContract.Entry.COLUMN_NAME_PLACE
        };
        Cursor cursor = db.query(ReaderContract.Entry.TABLE_NAME, projection,
                ReaderContract.Entry.COLUMN_NAME_PLACE + " = ?", new String[]{
                        location
                },
                null, null, null);

        boolean res = cursor.moveToNext();
        cursor.close();

        return res;
    }

    public void delete(String location) {
        db = dbHelper.getWritableDatabase();
        db.delete(ReaderContract.Entry.TABLE_NAME,
                ReaderContract.Entry.COLUMN_NAME_PLACE + " = ?",
                new String[]{location});
    }

    public void removeAll(){
        db = dbHelper.getWritableDatabase();

        db.delete(ReaderContract.Entry.TABLE_NAME, null, null);
    }
}
