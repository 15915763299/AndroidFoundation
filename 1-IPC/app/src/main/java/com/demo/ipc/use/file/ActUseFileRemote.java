package com.demo.ipc.use.file;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.ipc.R;
import com.demo.ipc.ThreadPoolManager;
import com.demo.ipc.model.Library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 14:43
 * description :
 */
public class ActUseFileRemote extends AppCompatActivity {

    private TextView txLibraryName;
    private BookAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_file_remote);

        findViewById(R.id.btn_clear).setOnClickListener((View v) -> deleteFile());

        txLibraryName = findViewById(R.id.tx_library_name);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BookAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recoverFromView();
    }

    private void recoverFromView() {
        ThreadPoolManager.get().execute(() -> {
            File cachedFile = new File(ActUseFile.FILE_PATH, ActUseFile.FILE_NAME);
            if (cachedFile.exists()) {
                ObjectInputStream is = null;
                try {
                    is = new ObjectInputStream(new FileInputStream(cachedFile));
                    final Library library = (Library) is.readObject();
                    runOnUiThread(() -> showLibrary(library));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void deleteFile() {
        File cachedFile = new File(ActUseFile.FILE_PATH, ActUseFile.FILE_NAME);
        if (cachedFile.exists()) {
            boolean isSuccess = cachedFile.delete();
            if (isSuccess) {
                adapter.clear();
                txLibraryName.setText("");
            }
        }
    }

    private void showLibrary(Library library) {
        if (library != null) {
            txLibraryName.setText(library.getName());
            adapter.clearAndAddAll(library.getBooks());
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}
