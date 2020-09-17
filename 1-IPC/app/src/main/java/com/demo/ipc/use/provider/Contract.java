package com.demo.ipc.use.provider;

import android.content.ContentUris;
import android.net.Uri;

import com.demo.ipc.BuildConfig;

/**
 * Defines table and column names for the database.
 * Defines URIs and URI get methods
 *
 * @author WeiTao
 */
class Contract {

    static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    static final Uri BASE_CONTEXT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Base Uri
     */
    static Uri getContentUri(String tableName) {
        return BASE_CONTEXT_URI.buildUpon().appendPath(tableName).build();
    }
//
//    /**
//     * MIME type：vnd.android.cursor.itemName. Used for multiple records, which is bg_talk_big_lower_half dir
//     */
//    static String getContentType(String tableName) {
//        return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + tableName;
//    }
//
//    /**
//     * MIME type：vnd.android.cursor.dir. Used for single records
//     */
//    static String getContentItemType(String tableName) {
//        return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + tableName;
//    }

    /**
     * Appends the given ID to the end of the path.(rowId 是隐藏列)
     */
    static Uri buildUriWithId(String tableName, long id) {
        return ContentUris.withAppendedId(getContentUri(tableName), id);
    }

    /**
     * 图书表
     */
    static final class BookEntry {
        static final String TABLE_NAME = "T_Book";
        static final String ID = "Id";
        static final String NAME = "Name";
    }
}
