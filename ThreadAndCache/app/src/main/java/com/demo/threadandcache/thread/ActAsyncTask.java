package com.demo.threadandcache.thread;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.demo.threadandcache.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/2/1 13:23
 * description :
 */
public class ActAsyncTask extends Activity {

    private static int[] colors = {0XFF4CAF50, 0XFF8BC34A, 0XFFCDDC39, 0XFFFFEB3B, 0XFFFFC107};
    private TextView[] txArr;
    private List<AsyncTask> asyncTasks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_async_task);

        TextView tx1 = findViewById(R.id.tx1);
        TextView tx2 = findViewById(R.id.tx2);
        TextView tx3 = findViewById(R.id.tx3);
        TextView tx4 = findViewById(R.id.tx4);
        TextView tx5 = findViewById(R.id.tx5);
        txArr = new TextView[]{tx1, tx2, tx3, tx4, tx5};

        Button btn = findViewById(R.id.btn);
        asyncTasks = new ArrayList<>();
        btn.setOnClickListener((View v) -> {
            for (int i = 0; i < txArr.length; i++) {
                MyAsyncTask asyncTask = new MyAsyncTask(this, i);
                asyncTask.execute(String.valueOf(1));
                asyncTasks.add(asyncTask);
            }
        });
    }


    private static class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private WeakReference<ActAsyncTask> wr;
        private int index, progress;

        MyAsyncTask(ActAsyncTask act, int index) {
            wr = new WeakReference<>(act);
            this.index = index;
            progress = 0;
        }

        @Override
        protected void onPreExecute() {
            if (wr != null && wr.get() != null) {
                TextView tx = wr.get().txArr[index];
                tx.setBackgroundColor(colors[index]);
                tx.setText("Ready");
            }
            System.out.println(index + "-start: " + Thread.currentThread().getName());
        }

        @Override
        protected String doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            System.out.println(index + ": " + Thread.currentThread().getName());
            for (int i = 0; i < 5; i++) {
                sleep();
                publishProgress(++progress);
            }
            sleep();
            return index + "-" + strings[0];
        }

        private void sleep() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (isCancelled()) {
                return;
            }
            System.out.println(index + "-progress: " + values[0]);
            if (wr != null && wr.get() != null) {
                wr.get().txArr[index].setText(String.valueOf(values[0]));
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (isCancelled()) {
                return;
            }
            System.out.println(index + "-result: " + s);
            if (wr != null && wr.get() != null) {
                TextView tx = wr.get().txArr[index];
                tx.setBackground(null);
                tx.setText("");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (asyncTasks != null) {
            for (AsyncTask asyncTask : asyncTasks) {
                if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    asyncTask.cancel(false);
                }
            }
        }
    }

}
