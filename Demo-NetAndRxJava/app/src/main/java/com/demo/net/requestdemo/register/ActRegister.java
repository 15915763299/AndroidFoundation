package com.demo.net.requestdemo.register;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.demo.net.R;
import com.demo.net.databinding.ActRegisterBinding;
import com.demo.net.requestdemo.ProgressDialogUtil;
import com.demo.net.requestdemo.api.WanAndroidApi;
import com.demo.net.requestdemo.api.WanAndroidRetrofit;
import com.demo.net.requestdemo.bean.BaseResponse;
import com.demo.net.requestdemo.rxclick.RxClickUtil;
import com.demo.net.utils.JsonUtil;
import com.demo.net.utils.Utils;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 23:17
 * description :
 */
public class ActRegister extends AppCompatActivity {

    private static final int SECOND = 20;
    private ActRegisterBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WanAndroidApi wanAndroidApi;

    private RegisterInfo registerInfo;
    private String validateCode = "";
    private boolean isCountDowning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_register);
        registerInfo = new RegisterInfo();
        binding.setInfo(registerInfo);

        wanAndroidApi = WanAndroidRetrofit.get().create(WanAndroidApi.class);

        binding.btn1.setEnabled(false);
        binding.btn2.setEnabled(false);
        binding.setCodeTip(getString(R.string.get_val_code));
        registerObserver();
    }

    private void registerObserver() {
        //实时观察编辑框
        Observable<CharSequence> obAccount = RxTextView.textChanges(binding.edt1);
        Observable<CharSequence> obPwd = RxTextView.textChanges(binding.edt2);
        Observable<CharSequence> obConfirmPwd = RxTextView.textChanges(binding.edt3);
        Observable<CharSequence> obCode = RxTextView.textChanges(binding.edt4);

        // 获取验证码按钮
        compositeDisposable.add(
                obAccount.subscribe(code ->
                        binding.btn1.setEnabled(!isCountDowning && isAccountValid(code.toString()))
                )
        );

        // 注册按钮
        compositeDisposable.add(
                Observable.combineLatest(obAccount, obPwd, obConfirmPwd, obCode,
                        (CharSequence account, CharSequence pwd, CharSequence confirmPwd, CharSequence code) -> {
                            String pwdStr = pwd.toString();
                            return isAccountValid(account.toString()) &&
                                    isPwdValid(pwdStr) &&
                                    isConfirmPwdValid(pwdStr, confirmPwd.toString()) &&
                                    isCodeValid(code.toString());
                        })
                        .subscribe(isEnable -> binding.btn2.setEnabled(isEnable))
        );

        // 获取验证码
        compositeDisposable.add(
                RxClickUtil.clicks(binding.btn1)
                        // 点击防抖
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .doOnNext(o -> {
                            ProgressDialogUtil.showProgress(this);
                            binding.btn1.setEnabled(false);
                        })
                        // 上一步会切换到主线程，这一步切换到其他线程
                        .observeOn(Schedulers.io())
                        .flatMap(o -> {
                            // 延迟两秒，假装请求网络
                            Thread.sleep(2_000);
                            return new Observable<BaseResponse>() {
                                @Override
                                protected void subscribeActual(Observer<? super BaseResponse> observer) {
                                    BaseResponse<String> baseResponse = new BaseResponse<>();
                                    baseResponse.setData(Utils.getRandomString(6));
                                    observer.onNext(baseResponse);
                                    observer.onComplete();
                                }
                            };
                        })
                        // 回到主线程
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(ProgressDialogUtil::dismiss)
                        .subscribe(
                                this::onValidateCodeReceived,
                                Throwable::printStackTrace
                        )
        );

        registerClick();
    }

    private void registerClick(){
        // 注册
        compositeDisposable.add(
                // RxClickUtil 是自定义的，效果和下面这个类似，这是 jakewharton 的
                RxView.clicks(binding.btn2)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .doOnNext(o -> {
                            ProgressDialogUtil.showProgress(this);
                            binding.btn2.setEnabled(false);
                        })
                        .observeOn(Schedulers.io())
                        .flatMap(o -> {
                            //模拟验证码错误
                            if (!validateCode.equals(registerInfo.verifyCode)) {
                                return new Observable<BaseResponse>() {
                                    @Override
                                    protected void subscribeActual(Observer<? super BaseResponse> observer) {
                                        BaseResponse baseResponse = new BaseResponse(-1, "验证码错误", null);
                                        observer.onNext(baseResponse);
                                        observer.onComplete();
                                    }
                                };
                            } else {
                                return wanAndroidApi.register(registerInfo.account, registerInfo.pwd, registerInfo.confirmPwd);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        //.doFinally(() -> 抛异常的时候会被调用)
                        //.doOnNext(() -> 正常执行的时候会被调用)
                        // 出现异常后会自动dispose！！！
                        .subscribe(
                                this::onRegisterResult,
                                this::onRegisterError
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

    private boolean isConfirmPwdValid(String pwd, String confirmPwd) {
        return pwd.equals(confirmPwd);
    }

    private boolean isCodeValid(String code) {
        return code.length() == 6;
    }

    private void onValidateCodeReceived(BaseResponse<String> baseResponse) {
        if (baseResponse.getErrorCode() != BaseResponse.SUCCESS) {
            return;
        }

        validateCode = baseResponse.getData();
        binding.txResult.setText("验证码：" + validateCode);

        // 倒计时
        isCountDowning = true;
        compositeDisposable.add(
                Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .take(SECOND)
                        .subscribe(
                                aLong -> binding.setCodeTip("剩余" + (SECOND - aLong) + "秒"),
                                Throwable::printStackTrace,
                                () -> {
                                    binding.setCodeTip(getString(R.string.get_val_code));
                                    binding.btn1.setEnabled(true);
                                    isCountDowning = false;
                                }
                        )
        );
    }

    private void onRegisterResult(BaseResponse baseResponse) {
        ProgressDialogUtil.dismiss();
        binding.btn2.setEnabled(true);
        binding.txResult.setText(JsonUtil.formatJson(JsonUtil.toJson(baseResponse)));
        //    {
        //    	"data":{
        //    		"admin":false,
        //    		"chapterTops":[
        //
        //    		],
        //    		"collectIds":[
        //
        //    		],
        //    		"email":"",
        //    		"icon":"",
        //    		"id":48416,
        //    		"nickname":"15915763299",
        //    		"password":"",
        //    		"publicName":"15915763299",
        //    		"token":"",
        //    		"type":0,
        //    		"username":"15915763299"
        //    	},
        //    	"errorCode":0,
        //    	"errorMsg":""
        //    }
    }

    private void onRegisterError(Throwable throwable) {
        ProgressDialogUtil.dismiss();
        binding.btn2.setEnabled(true);
        binding.txResult.setText(throwable.toString());
        //    {
        //    	"data":null,
        //    	"errorCode":-1,
        //    	"errorMsg":"用户名已经被注册！"
        //    }
        registerClick();
    }

}
