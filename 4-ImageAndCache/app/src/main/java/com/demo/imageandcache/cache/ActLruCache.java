package com.demo.imageandcache.cache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.demo.imageandcache.R;

import java.util.Locale;

/**
 * @author 尉迟涛
 * create time : 2020/2/2 10:05
 * description :
 */
public class ActLruCache extends Activity implements View.OnClickListener {

    private TextView tx;
    private ImageView img;
    private LruCache<Integer, Bitmap> lruCache;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lru_cache);

        tx = findViewById(R.id.tx);
        img = findViewById(R.id.img);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);

        initCache();
        refreshText();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                Bitmap bitmap = BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.ic_launcher
                );
                lruCache.put(count++, bitmap);
                break;
            case R.id.btn2:
                if (count > 0) {
                    lruCache.remove(--count);
                }
                break;
            case R.id.btn3:
                lruCache.evictAll();
                count = 0;
                break;
            case R.id.btn4:
                refreshText();
                break;
            case R.id.btn5:
                img.setImageBitmap(lruCache.get(count - 1));
                break;
            default:
        }
    }

    private void refreshText() {
        //这三个方法反映的都是java这个进程的内存情 况，跟操作系统的内存根本没有关系
        //maxMemory()这个方法返回的是java虚拟机（这个进程）能构从操作系统那里挖到的最大的内存
        //totalMemory()这个方法返回的是java虚拟机现在已经从操作系统那里挖过来的内存大小
        //挖过来而又没有用上的内存，实际上就是 freeMemory()

//        Runtime runtime = Runtime.getRuntime();
//        String result = "maxMemory: " + runtime.maxMemory() / 1024 + "\n" +
//                "freeMemory: " + runtime.freeMemory() / 1024 + "\n" +
//                "totalMemory: " + runtime.totalMemory() / 1024;

        int hitCount = lruCache.hitCount();
        int missCount = lruCache.missCount();
        int accesses = hitCount + missCount;
        int hitPercent = accesses != 0 ? (100 * hitCount / accesses) : 0;
        String tip = String.format(
                Locale.CHINA,
                "maxSize=%d\nhits=%d\nmisses=%d\nhitRate=%d%%",
                lruCache.maxSize(), hitCount, missCount, hitPercent
        );
        tx.setText(tip);
    }

    private void initCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    protected void onDestroy() {
        lruCache.evictAll();
        super.onDestroy();
    }
}
