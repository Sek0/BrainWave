package com.example.tanxiaokao.brainwave;

import cn.bmob.v3.BmobObject;

/**
 * Created by tanxiaokao on 2017/5/11.
 */

public class Wave extends BmobObject {
    private int attention;
    private int meditation;
    private String subject;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getMeditation() {
        return meditation;
    }

    public void setMeditation(int meditation) {
        this.meditation = meditation;
    }

}
