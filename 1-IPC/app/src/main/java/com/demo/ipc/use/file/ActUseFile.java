package com.demo.ipc.use.file;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ipc.App;
import com.demo.ipc.R;
import com.demo.ipc.ThreadPoolManager;
import com.demo.ipc.model.Book;
import com.demo.ipc.model.Library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 14:00
 * description : 使用文件存储来实现进程通讯，注意不能并发写，包括SharePreferance
 */
public class ActUseFile extends AppCompatActivity implements View.OnClickListener {

    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ipc/";
    public static final String FILE_NAME = "cache";

    private EditText edtLibrary, edtBook;
    private TextView txBookCount;
    private Library library;
    private int bookCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_file);

        edtLibrary = findViewById(R.id.edt_library);
        edtBook = findViewById(R.id.edt_book);
        txBookCount = findViewById(R.id.tx_book_count);
        findViewById(R.id.btn_create).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_jump).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookCount = 0;
        library = null;
        txBookCount.setText("Book Count: " + bookCount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                String libraryName = edtLibrary.getText().toString();
                if (libraryName.length() > 0) {
                    library = new Library(libraryName);
                    Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add:
                if (library == null) {
                    Toast.makeText(this, "请先创建Library", Toast.LENGTH_SHORT).show();
                    return;
                }
                String bookName = edtBook.getText().toString();
                if (bookName.length() > 0 && library != null) {
                    library.addBook(new Book(bookName, ++bookCount));
                    txBookCount.setText("Book Count: " + bookCount);
                }
                break;
            case R.id.btn_save:
                persistToFile();
                break;
            case R.id.btn_jump:
                Intent intent = new Intent(this, ActUseFileRemote.class);
                startActivity(intent);
                break;
            default:
        }
    }

    private void persistToFile() {
        if (library == null) {
            return;
        }

        ThreadPoolManager.get().execute(() -> {
            File dir = new File(FILE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File cachedFile = new File(dir, FILE_NAME);
            ObjectOutputStream os = null;
            try {
                os = new ObjectOutputStream(new FileOutputStream(cachedFile));
                os.writeObject(library);
                runOnUiThread(() -> Toast.makeText(App.getApp(), "保存成功", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
