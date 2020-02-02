package com.demo.threadandcache.cache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.demo.threadandcache.R;
import com.demo.threadandcache.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 尉迟涛
 * create time : 2020/2/2 12:50
 * description : 存在cache里的是原图，如果空间不足，建议优化一下，如果空间充足，还是保留，因为可能需要显示不同大小的图片
 */
public class ActDiskLruCache extends Activity {

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int IMG_SIZE = Utils.dip2px(300);
    private static final String IMG_URL = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2051295295,2845094769&fm=26&gp=0.jpg";

    private static MyHandler myHandler;
    private static final int MSG_PROGRESS = 0;
    private static final int MSG_FINISH = 1;

    private static class MyHandler extends Handler {
        WeakReference<ActDiskLruCache> wr;

        MyHandler(ActDiskLruCache act) {
            this.wr = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            if (wr != null && wr.get() != null) {
                switch (msg.what) {
                    case MSG_PROGRESS:
                        wr.get().pb.setProgress((int) msg.obj);
                        break;
                    case MSG_FINISH:
                        Bitmap bitmap = wr.get().readCache((String) msg.obj);
                        if (bitmap != null) {
                            wr.get().img.setImageBitmap(bitmap);
                        }
                        break;
                    default:
                }
            }
        }
    }

    private class MyRunnable implements Runnable {
        private String url;

        MyRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            downloadFileToDiskCache(url);
            Message msg = myHandler.obtainMessage(MSG_FINISH, url);
            myHandler.sendMessage(msg);
        }
    }

    private DiskLruCache diskLruCache;
    private ExecutorService cachedThreadPool;
    private ImageView img;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_disk_lru_cache);
        img = findViewById(R.id.img);
        pb = findViewById(R.id.pb);
        initCache();
        cachedThreadPool = Executors.newCachedThreadPool();
        myHandler = new MyHandler(this);

        findViewById(R.id.btn1).setOnClickListener((View v) -> {
            pb.setProgress(0);
            getImg(IMG_URL);
        });
        findViewById(R.id.btn2).setOnClickListener((View v) -> {
            String key = Utils.hashKeyFormUrl(IMG_URL);
            try {
                diskLruCache.remove(key);
                pb.setProgress(0);
                img.setImageBitmap(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        findViewById(R.id.btn3).setOnClickListener((View v) -> {
            pb.setProgress(0);
            img.setImageBitmap(null);
        });
    }

    private void initCache() {
        File file = getExternalCacheDir();
        if (file != null && file.exists()) {
            if (Utils.getAllocatableBytes(file) > DISK_CACHE_SIZE) {
                try {
                    diskLruCache = DiskLruCache.open(
                            file.getAbsoluteFile(), 1, 1, DISK_CACHE_SIZE
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getImg(String url) {
        DiskLruCache.Snapshot snapshot = getSnapshot(url);
        if (snapshot == null) {
            cachedThreadPool.execute(new MyRunnable(url));
        } else {
            Bitmap bitmap = readCache(snapshot);
            img.setImageBitmap(bitmap);
        }
    }

    private DiskLruCache.Snapshot getSnapshot(String url) {
        String key = Utils.hashKeyFormUrl(url);
        try {
            return diskLruCache.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void downloadFileToDiskCache(String url) {
        String key = Utils.hashKeyFormUrl(url);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                boolean isSuccess = downLoadUrlToStream(url, outputStream);
                if (isSuccess) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap readCache(DiskLruCache.Snapshot snapshot) {
        Bitmap bitmap = null;
        try {
            FileInputStream in = (FileInputStream) snapshot.getInputStream(0);
            FileDescriptor fileDescriptor = in.getFD();
            bitmap = Utils.decodeSampledBitmapFromFileDescriptor(
                    fileDescriptor, IMG_SIZE, IMG_SIZE
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap readCache(String url) {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = getSnapshot(url);
        if (snapshot != null) {
            bitmap = readCache(snapshot);
        }
        return bitmap;
    }

    /**
     * 下载文件到存储文件
     *
     * @param urlStr
     * @param os
     * @return
     */
    private boolean downLoadUrlToStream(String urlStr, OutputStream os) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }
            float totalSize = urlConnection.getContentLength();
            float currentSize = 0;

            // in 进内存
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            // out 出内存，进存储文件
            out = new BufferedOutputStream(os, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);

                currentSize += b;
                Message msg = myHandler.obtainMessage(MSG_PROGRESS, (int) (currentSize / totalSize * 100));
                myHandler.sendMessage(msg);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Utils.close(out);
            Utils.close(in);
        }
        return false;
    }
}
