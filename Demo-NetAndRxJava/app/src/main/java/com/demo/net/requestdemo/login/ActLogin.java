package com.demo.net.requestdemo.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.demo.net.R;
import com.demo.net.databinding.ActLoginBinding;
import com.demo.net.requestdemo.ProgressDialogUtil;
import com.demo.net.requestdemo.api.WanAndroidApi;
import com.demo.net.requestdemo.api.WanAndroidRetrofit;
import com.demo.net.requestdemo.bean.BaseResponse;
import com.demo.net.utils.JsonUtil;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 尉迟涛
 * create time : 2020/3/7 16:47
 * description :
 */
public class ActLogin extends AppCompatActivity {

    private ActLoginBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WanAndroidApi wanAndroidApi;
    private LoginInfo loginInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_login);
        loginInfo = new LoginInfo();
        binding.setInfo(loginInfo);

        wanAndroidApi = WanAndroidRetrofit.get().create(WanAndroidApi.class);
        binding.btn1.setEnabled(false);
        registerObserver();
    }

    private void registerObserver() {
        Observable<CharSequence> obAccount = RxTextView.textChanges(binding.edt1);
        Observable<CharSequence> obPwd = RxTextView.textChanges(binding.edt2);

        // 登录按钮
        compositeDisposable.add(
                Observable.combineLatest(obAccount, obPwd,
                        (CharSequence account, CharSequence pwd) ->
                                isAccountValid(account.toString()) && isPwdValid(pwd.toString()))
                        .subscribe(isEnable -> binding.btn1.setEnabled(isEnable))
        );

        loginClick();
    }

    private void loginClick(){
        // 注册
        compositeDisposable.add(
                RxView.clicks(binding.btn1)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .doOnNext(o -> {
                            ProgressDialogUtil.showProgress(this);
                            binding.btn1.setEnabled(false);
                        })
                        .observeOn(Schedulers.io())
                        .flatMap(o -> wanAndroidApi.login(loginInfo.account, loginInfo.pwd))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onLoginResult,
                                this::onLoginError
                        )
        );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable = null;
        super.onDestroy();
    }

    private boolean isAccountValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPwdValid(String pwd) {
        return pwd.length() >= 6;
    }

    private void onLoginResult(BaseResponse baseResponse) {
        ProgressDialogUtil.dismiss();
        binding.btn1.setEnabled(true);
        binding.txResult.setText(JsonUtil.formatJson(JsonUtil.toJson(baseResponse)));
    }

    private void onLoginError(Throwable throwable) {
        ProgressDialogUtil.dismiss();
        binding.btn1.setEnabled(true);
        binding.txResult.setText(throwable.toString());
        loginClick();
    }
}
