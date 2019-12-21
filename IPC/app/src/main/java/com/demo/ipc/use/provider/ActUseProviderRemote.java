package com.demo.ipc.use.provider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.demo.ipc.R;
import com.demo.ipc.model.Book;
import com.demo.ipc.use.file.BookAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 18:44
 * description :
 */
public class ActUseProviderRemote extends AppCompatActivity {

    private BookAdapter adapter;
    private Uri bookUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_provider_remote);

        findViewById(R.id.btn_clear).setOnClickListener((View v) -> deleteAllBooks());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BookAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        bookUri = Contract.getContentUri(Contract.BookEntry.TABLE_NAME);
        getBooks();
    }

    private void getBooks() {
        String[] columns = {
                Contract.BookEntry.NAME,
                Contract.BookEntry.ID
        };

        List<Book> books = new ArrayList<>();
        Cursor cursor = getContentResolver().query(
                bookUri, columns, null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    books.add(new Book(
                            cursor.getString(0),
                            cursor.getInt(1)
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        adapter.clearAndAddAll(books);
    }

    private void deleteAllBooks() {
        getContentResolver().delete(bookUri, null, null);
        adapter.clear();
    }
}
