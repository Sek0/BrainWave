package com.example.tanxiaokao.brainwave;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends AppCompatActivity {

    private static int displayWidth;


    private static int displayHeight; //屏幕高度

    private CardView cardView;
    private EditText textView1;
    private EditText textView2;
    private EditText textView3;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        cardView = (CardView) findViewById(R.id.change_card);
        textView1 = (EditText) findViewById(R.id.text_old);
        textView2 = (EditText) findViewById(R.id.text_new1);
        textView3 = (EditText) findViewById(R.id.text_new2);
        submit = (Button) findViewById(R.id.but_submit);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (displayWidth * 0.7f + 0.7f),(int)(displayHeight * 0.5f + 0.5f));
        cardView.setLayoutParams(params);
        cardView.setRadius(17);
        cardView.setCardElevation(15);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(cardView, "注册成功!", Snackbar.LENGTH_LONG).show();

                if (textView1.getText().toString().isEmpty()){
                    Snackbar.make(cardView, "请输入账号原密码！", Snackbar.LENGTH_LONG).show();
                    return;
                }else if(textView2.getText().toString().isEmpty()){
                    Snackbar.make(cardView, "请输入新密码！", Snackbar.LENGTH_LONG).show();
                    return;
                }else if(textView2.getText().toString().equals(textView3.getText().toString())){
                    if (textView1.getText().toString().equals(((App)getApplication()).user.getPassWord())){
                        ((App)getApplication()).user.setPassWord(textView2.getText().toString());
                        ((App)getApplication()).user.update(((App) getApplication()).userId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Snackbar.make(cardView, "密码修改成功！", Snackbar.LENGTH_LONG).show();
                                    Log.i("bmob","更新成功："+e.getMessage()+","+e.getErrorCode());
                                }else{
                                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }else {
                        Snackbar.make(cardView, "愿密码输入错误", Snackbar.LENGTH_LONG).show();
                        return;
                    }


                }else {
                    Snackbar.make(cardView, "两次新密码输入不一致，请重新输入！", Snackbar.LENGTH_LONG).show();
                    return;
                }

            }
        });

    }

}
