package com.example.tanxiaokao.brainwave;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private int intentData;
    private CardView cardView;
    private CardView cardView1;
    private ImageView imageTitle;
    private TextView textView1 ;
    private TextView textView2 ;
    private TextView textView3 ;
    private TextView textView4 ;
    private TextView textView5 ;
    private TextView textView6 ;
    private TextView textView7 ;
    private TextView textView8 ;
    private TextView textView9 ;
    private TextView textView10 ;
    private TextView textView11 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        textView1 = (TextView) findViewById(R.id.question_text1);
        textView2 = (TextView) findViewById(R.id.question_text2);
        textView3 = (TextView) findViewById(R.id.question_text3);
        textView4 = (TextView) findViewById(R.id.question_text4);
        textView5 = (TextView) findViewById(R.id.question_text5);
        textView6 = (TextView) findViewById(R.id.question_text6);
        textView7 = (TextView) findViewById(R.id.question_text7);
        textView8 = (TextView) findViewById(R.id.question_text8);
        textView9 = (TextView) findViewById(R.id.question_text9);
        textView10 = (TextView) findViewById(R.id.question_text10);
        textView11 = (TextView) findViewById(R.id.question_text11);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("About");

        Intent intent = getIntent();
        intentData = intent.getIntExtra("extra_data",0);

        if (intentData == 0){
            textView1.setText("专注力");
            textView2.setText("表示大脑的专注程度");
            textView3.setText("数值越高，大脑越专注");
            textView4.setText("与脑电波的Beta波密切相关");
            textView5.setText("进入专注状态的技巧");
            textView6.setText("明切并保持一个想法");
            textView7.setText("盯着一个具体的物体");
            textView8.setText("计算数学");
            textView9.setText("专心听某人讲话");
            textView10.setText("发表一场演说");
            textView11.setText("轻轻哼唱一首歌");

        }else {
            textView1.setText("冥想状态");
            textView2.setText("表示大脑的放松程度");
            textView3.setText("数值越高，大脑越放松");
            textView4.setText("与脑电波的Alpha波密切相关");
            textView5.setText("进入冥想状态的技巧");
            textView6.setText("尽力放松全身肌肉");
            textView7.setText("屏除心中任何的想法");
            textView8.setText("心无杂念");
            textView9.setText("想象自己将要入睡");
            textView10.setText("想象自己在温水上漂浮");
            textView11.setText("初学者可闭上双眼");
        }
        cardView = (CardView) findViewById(R.id.question__card);
        cardView1 = (CardView) findViewById(R.id.question__card1);


        cardView.setRadius(8);//设置图片圆角的半径大小
        cardView.setCardElevation(8);//设置阴影部分大小
        cardView1.setRadius(8);//设置图片圆角的半径大小
        cardView1.setCardElevation(8);//设置阴影部分大小

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
