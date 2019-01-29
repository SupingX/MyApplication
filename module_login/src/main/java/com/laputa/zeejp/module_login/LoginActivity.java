package com.laputa.zeejp.module_login;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.laputa.zeejp.lib_common.http.ApiService;
import com.laputa.zeejp.lib_common.net.ApiServiceManager;
import com.laputa.zeejp.lib_common.rx.RxBus;
import com.laputa.zeejp.lib_common.rx.RxBus4;
import com.laputa.zeejp.lib_common.rx.RxHelper;
import com.laputa.zeejp.module_login.databinding.ActivityLoginBinding;
import com.laputa.zeejp.module_login.event.HelloEvent;
import com.zeejp.laputa.lib_download.DownLoadManager;
import com.zeejp.laputa.lib_mvvm.BaseActivity;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import util.XLoggerUtil;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {


    private Disposable subscribe;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
        setRxJava();
    }

    private void setRxJava() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                Log.i("setRxJava", "accept: e" + e.getMessage());
                if (e instanceof UndeliverableException) {
                    e = e.getCause();
                }
                if ((e instanceof IOException) || (e instanceof SocketException)) {
                    // fine, irrelevant network problem or API that throws on cancellation
                    return;
                }
                if (e instanceof InterruptedException) {
                    // fine, some blocking code was interrupted by a dispose call
                    return;
                }
                if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                    // that's likely a bug in the application
                    Thread.currentThread().getUncaughtExceptionHandler()
                            .uncaughtException(Thread.currentThread(), e);
                    return;
                }
                if (e instanceof IllegalStateException) {
                    // that's a bug in RxJava or in a custom operator
                    Thread.currentThread().getUncaughtExceptionHandler()
                            .uncaughtException(Thread.currentThread(), e);
                    return;
                }
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        mViewDataBing.etU.setText("13040815454");
        mViewDataBing.etP.setText("zfy123456");
        initRxBus();
        initRxBus3();
        XLoggerUtil.verifyStoragePermissions(this);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_2:
                if (subscribe != null) {
                    subscribe.dispose();
                    subscribe = null;
                }
                break;
            case R.id.btn_l:
//                login();
//                download();
//                testRxBus();


//                boolean hello_world = XLoggerUtil.newInstance().saveDefault(this, "hello world");
//                Toast.makeText(this, "保存" + (hello_world ? "成功" : "失败"), Toast.LENGTH_SHORT).show();

//                boolean save = XLoggerUtil.newInstance().save(this, "xxxx/yyyy", "haha.txt", "hello world!!!");
//                Toast.makeText(this, "保存" + (save ? "成功" : "失败"), Toast.LENGTH_SHORT).show();

                testSchedulers();
                break;
        }
    }


    private Observable<Boolean> getLoginObservable(String phone, String password) {
        return ApiServiceManager.newInstance()
                .createApiService()
                .loginObs(phone, encodeMD5(password), ApiService.TYPE_ANDROID, "1.0", "", "", ApiService.ACCESSKEY)
                .compose(RxHelper.parseDataForBoolean2())
                .doOnDispose(() -> Log.i("getLoginObservable", "getLoginObservable: doOnDispose"))
                .doOnError(throwable -> Log.i("getLoginObservable", "getLoginObservable: doOnError"))
                .doOnComplete(() -> Log.i("getLoginObservable", "getLoginObservable: doOnComplete"))
                ;
    }

    private void testSchedulers() {

        /**
         * 连续点击本来有错误的，会执行。线程安全？
         */
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            urls.add("i" + i);
        }
        Log.i("testSchedulers", "-----------------------------: " + Thread.currentThread().getName());
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        }

        subscribe = Observable.fromIterable(urls)
                //.subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(dispose -> {
                    boolean disposed = dispose.isDisposed();
                    Log.i("testSchedulers", "doOnSubscribe: " + Thread.currentThread().getName() + "___[disposed]" + dispose + ":" + disposed);
                    updateBtn("登陆中...", false);
                })
                .doOnComplete(() -> {
                    Log.i("testSchedulers", "doOnComplete: " + Thread.currentThread().getName());
                    updateBtn("完成...", false);
                    // int i = 10/0;

                })
                .flatMap((Function<String, Observable<String>>) s -> {
                    Log.i("testSchedulers", "flatMap step-01: " + Thread.currentThread().getName() + ",subscribe=" + subscribe);
                    updateBtn("验证登陆中...", false);
                    return Observable.just(s);
                })
                .observeOn(Schedulers.io())
                .flatMap((Function<String, Observable<Boolean>>) s -> {
                    Log.i("testSchedulers", "flatMap step-02: " + Thread.currentThread().getName() + ",subscribe=" + subscribe);
                    String phone = mViewDataBing.etU.getText().toString();
                    String password = mViewDataBing.etP.getText().toString();
                    // int i=10/0;
                    return getLoginObservable(phone, password);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Log.i("testSchedulers", "Consumer: " + o + " - " + Thread.currentThread().getName());
                    updateBtn(o ? "登陆成功！" : "登陆失败!", true);
                }, throwable -> {
                    Log.i("testSchedulers", "Throwable: " + throwable.getMessage() + " - " + Thread.currentThread().getName());
                    updateBtn("登陆失败", true);
                    throwable.printStackTrace();
                });

    }

    Random random = new Random();

    private void updateBtn(String msg, boolean changColor) {
        mViewDataBing.btnL.setText(msg);
        if (changColor) {
            mViewDataBing.idUcNewsHeaderPager.setBackgroundColor(Color.argb(random.nextInt(0xFF - 0xAA), random.nextInt(0xff), random.nextInt(0xff), random.nextInt(0xff)));
        }
    }

    private void doOnCompleteTest() {
        Log.i("testSchedulers", "doOnSubscribe: " + Thread.currentThread().getName());
        mViewDataBing.btnL.setTextColor(Color.RED);
    }

    private void logThread(Object obj, Thread thread) {
        Log.v("Schedulers", "onNext:" + obj + " -" + Thread.currentThread().getName());
    }

    private void initRxBus() {
       /* Disposable subscribe = RxBus4.newInstance().toObservable(HelloEvent.class)
                .subscribe(new Consumer<HelloEvent>() {
                               @Override
                               public void accept(HelloEvent helloEvent) {
                                   Log.i("testRxBus", "accept: " + helloEvent.getHello());
                                   int i = 10 / 0;
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i("testRxBus", "throwable: " + throwable.getLocalizedMessage());
                            }
                        }
                );*/
        Disposable disposable = RxBus.newInstance().register(HelloEvent.class, new Consumer<HelloEvent>() {
            @Override
            public void accept(HelloEvent helloEvent) throws Exception {
                Log.i("testRxBus", "accept: " + helloEvent.getHello());
                int i = 10 / 0;
            }
        });
    }

    private void initRxBus3() {
      /*  Disposable testRxBus = RxBus3.newInstance().tObservable(HelloSticktyEvent.class)
                .subscribe(
                        new Consumer<HelloSticktyEvent>() {
                            @Override
                            public void accept(HelloSticktyEvent helloEvent) throws Exception {
                                Log.i("testRxBus", "accept: " + helloEvent.getHello());
                                int i = 10 / 0;
                            }
                        }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i("testRxBus", "throwable: " + throwable.getLocalizedMessage());
                            }
                        }
                );*/
    }

    private void testRxBus() {
        RxBus.newInstance().post(new HelloEvent("hello -  03"));
//        RxBus3.newInstance().post(new HelloSticktyEvent("hello - 04 "));
    }

    private void login() {
        String phone = mViewDataBing.etU.getText().toString();
        String password = mViewDataBing.etP.getText().toString();
        Disposable subscribe = ApiServiceManager.newInstance().createApiService()
                .login(phone, encodeMD5(password), ApiService.TYPE_ANDROID, "1.0", "", "", ApiService.ACCESSKEY)
                .compose(RxHelper.parseDataForBoolean())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Exception {
                        if (success) {
                            Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(LoginActivity.this, "出错：" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void download() {
        //String url = "https://iot2.qinyuan.cn/web/api/common/getFiles/5c1b49d3a039700da2a2d01a";
        String url = "https://iot2.qinyuan.cn/files/qinyuan.apk";
        DownLoadManager.newInstance().download(url, "", null);
    }

    /**
     * 用MD5对字符串进行加密
     *
     * @param s
     * @return
     */
    public static String encodeMD5(String s) {
        if (isEmpty(s)) {
            return null;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        md.update(s.getBytes());
        byte[] datas = md.digest();
        int len = datas.length;
        char str[] = new char[len * 2];
        int k = 0;
        for (int i = 0; i < len; i++) {
            byte byte0 = datas[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    public void doOnSubscribeTest(Subscription subscription) {
        Log.i("testSchedulers", "doOnSubscribe: " + Thread.currentThread().getName());
        Toast.makeText(LoginActivity.this, "doOnSubscribe", Toast.LENGTH_LONG).show();
        mViewDataBing.btnL.setText("登陆中...");
    }

    private void debugLoadData() {
        Observable<Object> data01Observable = Observable.create(emitter -> {
            Thread.sleep(5000);
            emitter.onNext(new Object());
            Log.i("debugLoadData", "getData01 ");
        });
        Observable<Object> data02Observable = Observable.create(emitter -> {
            Thread.sleep(1000);
            emitter.onNext(new Object());
            int i = 10 / 0;
            Log.i("debugLoadData", "getData02 ");
        });
        Observable<Object> data03Observable = Observable.create(emitter -> {
            Thread.sleep(2000);
            emitter.onNext(new Object());
            Log.i("debugLoadData", "getData03 ");
        });
        Disposable subscribe1 = Observable
                .zip(
                        data01Observable.onErrorReturn(throwable -> new Object())
                        , data02Observable.onErrorReturn(throwable -> new Object())
                        , data03Observable.onErrorReturn(throwable -> new Object())
                        , (o, o2, o3) -> doAction(o, o2, o3)
                )
                .subscribe(
                        text -> Log.i("debugLoadData", "onNext " + text)
                        , throwable -> Log.i("debugLoadData", "onError " + throwable.getMessage())
                );
    }

    private Boolean doAction(Object data1, Object data2, Object data3) {
        Log.i("debugLoadData", "doAction: data01 = " + data1 + ",data02 = " + data2 + ",data03 = " + data3);
        return true;
    }
}
