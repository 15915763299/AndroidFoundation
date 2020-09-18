package com.demo.ipc.use.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 17:53
 * description :
 */
public class Provider extends ContentProvider {

    private static final String TAG = Provider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER;
    //单表匹配码
    private static final int BOOK = 1;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        //“#”为数据的通配符，“*”为所有文本的通配符，匹配码为第三个参数。
        URI_MATCHER.addURI(authority, Contract.BookEntry.TABLE_NAME, BOOK);
    }

    private DbOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DbOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(
                tableName, projection, selection, selectionArgs, null, null, sortOrder
        );
        Log.w(TAG, "Query: " + uri.toString() + (cursor == null ? " fail" : " success"));
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        long id = dbOpenHelper.getReadableDatabase().insert(tableName, null, values);
        if (id <= 0) {
            throw new SQLException("Failed to insert row into " + uri);
        }

        Uri returnUri = Contract.buildUriWithId(tableName, id);
        Log.w(TAG, "Insert: " + uri.toString() + " success, id: " + id);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int rowsDeleted = dbOpenHelper.getReadableDatabase().delete(tableName, selection, selectionArgs);
        if (rowsDeleted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
            Log.w(TAG, "Delete: " + uri.toString() + " success, amount: " + rowsDeleted);
        } else {
            Log.w(TAG, "Delete: " + uri.toString() + " fail, amount: " + rowsDeleted);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int rowsImpacted = dbOpenHelper.getReadableDatabase().update(tableName, values, selection, selectionArgs);
        if (rowsImpacted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
            Log.w(TAG, "Update: " + uri.toString() + " success, amount: " + rowsImpacted);
        } else {
            Log.w(TAG, "Update: " + uri.toString() + " fail, amount: " + rowsImpacted);
        }
        return rowsImpacted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private String getTableName(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case BOOK:
                return Contract.BookEntry.TABLE_NAME;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
