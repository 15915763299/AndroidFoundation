package com.demo.ipc.use.provider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.ipc.R;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 18:18
 * description :
 */
public class ActUseProvider extends AppCompatActivity implements View.OnClickListener {

    private EditText edtBook;
    private TextView txBookCount;
    private int bookNum = 0;
    private Uri bookUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_provider);

        edtBook = findViewById(R.id.edt_book);
        txBookCount = findViewById(R.id.tx_book_count);
        findViewById(R.id.btn_insert).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);

        bookUri = Contract.getContentUri(Contract.BookEntry.TABLE_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookNum();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                String bookName = edtBook.getText().toString();
                if (bookName.length() > 0) {
                    insertBook(bookName);
                }
                break;
            case R.id.btn:
                Intent intent = new Intent(this, ActUseProviderRemote.class);
                startActivity(intent);
                break;
            default:
        }
    }

    private void getBookNum() {
        Cursor cursor = getContentResolver().query(
                bookUri, null, null, null, null
        );
        if (cursor != null) {
            bookNum = cursor.getCount();
            txBookCount.setText("Book Count: " + bookNum);
            cursor.close();
        }
    }

    private void insertBook(String bookName) {
        bookNum++;
        try {
            ContentValues values = new ContentValues();
            values.put(Contract.BookEntry.ID, bookNum);
            values.put(Contract.BookEntry.NAME, bookName);
            getContentResolver().insert(bookUri, values);
        } catch (Exception e) {
            e.printStackTrace();
            bookNum--;
        }
        txBookCount.setText("Book Count: " + bookNum);
    }

}
