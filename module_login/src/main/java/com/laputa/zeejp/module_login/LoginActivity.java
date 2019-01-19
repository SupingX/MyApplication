package com.laputa.zeejp.module_login;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.laputa.zeejp.lib_common.http.ApiService;
import com.laputa.zeejp.lib_common.net.ApiServiceManager;
import com.laputa.zeejp.lib_common.rx.RxBus;
import com.laputa.zeejp.lib_common.rx.RxHelper;
import com.laputa.zeejp.module_login.databinding.ActivityLoginBinding;
import com.laputa.zeejp.module_login.event.HelloEvent;
import com.zeejp.laputa.lib_download.DownLoadManager;
import com.zeejp.laputa.lib_mvvm.BaseActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import util.XLoggerUtil;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
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
            case R.id.btn_l:
//                login();
//                download();
//                testRxBus();


//                boolean hello_world = XLoggerUtil.newInstance().saveDefault(this, "hello world");
//                Toast.makeText(this, "保存" + (hello_world ? "成功" : "失败"), Toast.LENGTH_SHORT).show();

                boolean save = XLoggerUtil.newInstance().save(this, "xxxx/yyyy", "haha.txt", "hello world!!!");
                Toast.makeText(this, "保存" + (save ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initRxBus() {
       /* Disposable subscribe = RxBus.newInstance().toObservable(HelloEvent.class)
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
}
