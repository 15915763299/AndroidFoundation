package com.demo.ipc.use.binderpool.client;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ipc.R;
import com.demo.ipc.ThreadPoolManager;
import com.demo.ipc.binderpool.IEncrypt;
import com.demo.ipc.binderpool.ISort;
import com.demo.ipc.use.binderpool.BinderConstant;

/**
 * @author 尉迟涛
 * create time : 2019/11/16 16:58
 * description :
 */
public class ActBinderPool extends AppCompatActivity implements View.OnClickListener {

    private IEncrypt iEncrypt;
    private ISort iSort;
    private EditText edt1, edt2;
    private TextView tx1, tx2;
    private Button btn1, btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_binder_pool);

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        tx1 = findViewById(R.id.tx1);
        tx2 = findViewById(R.id.tx2);

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn1.setEnabled(false);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn2.setEnabled(false);

        ThreadPoolManager.get().execute(() -> {
            getEncryptBinder();
            getSortBinder();
        });
    }

    private void getEncryptBinder() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder encryptBinder = binderPool.queryBinder(BinderConstant.BINDER_ENCRYPT);
        iEncrypt = IEncrypt.Stub.asInterface(encryptBinder);
        runOnUiThread(() -> btn1.setEnabled(true));
    }

    private void getSortBinder() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder sortBinder = binderPool.queryBinder(BinderConstant.BINDER_SORT);
        iSort = ISort.Stub.asInterface(sortBinder);
        runOnUiThread(() -> btn2.setEnabled(true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: {
                //如果断连需要重连，每个业务的binder都要
                if (iEncrypt == null || !iEncrypt.asBinder().isBinderAlive()) {
                    btn1.setEnabled(false);
                    getEncryptBinder();
                } else {
                    String str = edt1.getText().toString();
                    if (str.length() > 0) {
                        ThreadPoolManager.get().execute(() -> {
                            try {
                                String result = iEncrypt.encryptSha256(str);
                                runOnUiThread(() -> tx1.setText(result));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

            }
            break;
            case R.id.btn2: {
                if (iSort != null && !iSort.asBinder().isBinderAlive()) {
                    btn2.setEnabled(false);
                    getSortBinder();
                } else {
                    String str = edt2.getText().toString();
                    if (str.length() > 0) {
                        ThreadPoolManager.get().execute(() -> {
                            try {
                                String result = iSort.sortChar(str);
                                runOnUiThread(() -> tx2.setText(result));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
            break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BinderPool.getInstance(this).destroy();
    }
}
