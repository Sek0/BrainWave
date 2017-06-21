package com.example.tanxiaokao.brainwave;

import java.util.PriorityQueue;

import cn.bmob.v3.BmobObject;

/**
 * Created by tanxiaokao on 2017/5/22.
 */

public class User extends BmobObject {
    private String userName;
    private String PassWord;
    private String position;
    private String sex;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
