package com.example.tanxiaokao.brainwave;

import android.app.Application;
import android.text.Editable;

import cn.bmob.v3.Bmob;

/**
 * Created by tanxiaokao on 2017/5/11.
 */

public class App extends Application {

    public String subject = "输入科目";
    //public String userName;
    public User user = new User();
    public String userId;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "48c6312cf9dbfafe2395ba5e113d96c3");//初始化数据库

    }
}
