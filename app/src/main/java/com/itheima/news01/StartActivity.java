package com.itheima.news01;

import android.content.Intent;
import android.os.SystemClock;

/**
 * 启动界面
 */
public class StartActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_start;
    }

    @Override
    public void initView() {
        new Thread(){
            public void run(){
                SystemClock.sleep(1500);

                enterGuideActiviy();
            }
        }.start();
    }

    //进入引导界面
    private void enterGuideActiviy() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();      //销毁启动界面
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
