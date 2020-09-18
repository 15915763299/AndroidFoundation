package com.demo.ui.module.conflict;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.ui.R;
import com.demo.ui.util.DisplayUtils;

import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2019/11/22 14:35
 * description : 滑动冲突
 */
public class ActScrollConflict extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scroll_conflict);

        initHorizontalScrollView();
        initStickView();
    }

    private void initHorizontalScrollView() {
        LayoutInflater inflater = getLayoutInflater();
        HorizontalScrollView horizontalScrollView = findViewById(R.id.container);
        int itemWidth = DisplayUtils.getScreenMetrics().widthPixels - DisplayUtils.dip2px(40);
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    R.layout.view_conflict_content, horizontalScrollView, false
            );
            layout.getLayoutParams().width = itemWidth;
            TextView textView = layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 255 / (i + 1)));
            createList(layout);
            horizontalScrollView.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListView listView = layout.findViewById(R.id.list);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("item " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.view_simple_list_item, R.id.tx, data
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) ->
                Toast.makeText(ActScrollConflict.this, "click item", Toast.LENGTH_SHORT).show()
        );
    }

    private void initStickView() {
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("data" + i);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleAdapter simpleAdapter = new SimpleAdapter(data);
        recyclerView.setAdapter(simpleAdapter);
    }
}
