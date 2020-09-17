package com.demo.ipc.use.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 17:30
 * description :
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "book_provider.db";
    private static final int DB_VERSION = 1;

    DbOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sqlCreateBookTable =
                "CREATE TABLE IF NOT EXISTS " + Contract.BookEntry.TABLE_NAME + " (" +
                        Contract.BookEntry.ID + " INTEGER PRIMARY KEY," +
                        Contract.BookEntry.NAME + " VARCHAR(50) NOT NULL);";
        db.execSQL(sqlCreateBookTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DbOpenHelper", "oldVersion: " + oldVersion + ", newVersion: " + newVersion);
    }
}
