package com.example.tanxiaokao.brainwave;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.jonas.jgraph.graph.JcoolGraph;
import com.jonas.jgraph.inter.BaseGraph;
import com.jonas.jgraph.models.Jchart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jonas.jgraph.inter.BaseGraph.SELECETD_MSG_SHOW_TOP;

public class Jaraph extends AppCompatActivity {

    private JcoolGraph mLineChar;
    private int chartNum = 15;
    private static final String TAG = "FindData";
    SimpleDateFormat format;
    private int Data;
    private int dataNum;
    private int interval;
    List<Jchart> lines = new ArrayList<>();
    TimePickerView pvTime;
    OptionsPickerView pvOption;
    ImageButton selectDate;
    private int intentData;
    TextView textAverage;
    TextView textNumber;
    TextView textJudge;

    int []point;
    String []pointSub;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaraph);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("折线图");

        Intent intent = getIntent();
        intentData = intent.getIntExtra("extra_data",0);

        mLineChar = (JcoolGraph)findViewById(R.id.sug_recode_line);

        //mLineChar.setGraphStyle(BaseGraph.BAR);

        textAverage = (TextView) findViewById(R.id.text1_describe);
        textNumber = (TextView) findViewById(R.id.text2_describe);
        textJudge = (TextView) findViewById(R.id.text3_describe);


        pvTime=new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date(System.currentTimeMillis()));//默认当前时间
        //Log.d(TAG, "morenshijain is: "+getTime(new Date(System.currentTimeMillis())));
        pvTime.setCyclic(false);//是否循环滚动
        pvTime.setCancelable(true);

        for(int i = 0; i<chartNum; i++) {
            //Jchart参数：数据条矩形的底，高，横坐标，颜色
            lines.add(new Jchart(0, 0,"", Color.parseColor("#FF4081")));
        }
        mLineChar.setLinePointRadio((int)mLineChar.getLineWidth());
        mLineChar.setNormalColor(Color.parseColor("#949294"));
        mLineChar.feedData(lines);

        selectDate = (ImageButton) findViewById(R.id.But_selectDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                BmobQuery<Wave> query = new BmobQuery<Wave>();
                query = queryDate(date);
                query.setLimit(400);
                query.findObjects(new FindListener<Wave>() {
                    @Override
                    public void done(List<Wave> list, BmobException e) {
                        if(e==null){
                            lines.clear();
                            //Toast.makeText(getApplicationContext(),"查询成功：共"+list.size()+"条数据。",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "查询成功：共"+list.size()+"条数据。");
                            dataNum = list.size();

                            if(dataNum<=15){
                                interval = 1;
                                point = new int[dataNum];
                                pointSub = new String[dataNum];
                            }else{
                                point = new int[dataNum/interval];
                                pointSub = new String[dataNum/interval];
                                interval = dataNum/chartNum+1;

                            }
                            //--------------------------------------------------
                            if (list.size() == 0){
                                textJudge.setText("");
                                textAverage.setText("");

                            }else{
                                int sum = 0;
                                for (int j = 0;j<list.size();j++){
                                    if (intentData == 0){
                                        sum = sum +list.get(j).getAttention();
                                    }else {
                                        sum = sum +list.get(j).getMeditation();
                                    }
                                }
                                if (sum/list.size() <45){
                                    textJudge.setText("较差");
                                }else if (sum/list.size() <80){
                                    textJudge.setText("良好");
                                }else {
                                    textJudge.setText("优秀");
                                }
                                textAverage.setText(Integer.toString(sum/list.size()));
                            }

                            //--------------------------------------------------


                            List<String> strSub = new ArrayList<String>();
                            int pointNum = 0;
                            for (int i = 0; i<dataNum; i = i+interval) {
                                Wave wave = list.get(i);
                                String string = wave.getCreatedAt();
                                string = string.substring(string.length()-8);
                                string = string.substring(0,5);
                                if(intentData == 0){
                                    point[pointNum] = wave.getAttention();
                                    pointSub[pointNum] = string;
                                    pointNum++;
                                    //lines.add(new Jchart(0, wave.getAttention(), string, Color.parseColor("#FF4081")));

                                }else {
                                    point[pointNum] = wave.getMeditation();
                                    pointSub[pointNum] = string;
                                    pointNum++;
                                    //lines.add(new Jchart(0, wave.getMeditation(), string, Color.parseColor("#FF4081")));
                                }
                            }
                            point = moveAverage(point,3);
                            for (int j = 0;j < point.length;j++){
                                lines.add(new Jchart(0,point[j],pointSub[j],Color.parseColor("#FF4081")));
                            }
                            //lines.get(1).getIndex();
                            //-------------------------------
                            textNumber.setText(Integer.toString(list.size())+"/"+Integer.toString(lines.size()));
                            //-------------------------------
                            if (dataNum == 0){
                                for(int i = 0; i<chartNum; i++) {
                                    //Jchart参数：数据条矩形的底，高，横坐标，颜色
                                    lines.add(new Jchart(0, 0,"", Color.parseColor("#FF4081")));
                                    textNumber.setText("0/0");

                                }
                            }
                            mLineChar.setLinePointRadio((int)mLineChar.getLineWidth());
                            mLineChar.setNormalColor(Color.parseColor("#949294"));
                            mLineChar.feedData(lines);
                            mLineChar.aniShow_growing();

                        }else{
                            Snackbar.make(mLineChar, "数据查询失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                Log.d(TAG, "onTimeSelect: "+getTime(date));
            }
        });
        pvOption=new OptionsPickerView(this);

        BmobQuery<Wave> query = new BmobQuery<Wave>();
        query = queryDate(new Date(System.currentTimeMillis()));
        query.setLimit(400);
        query.findObjects(new FindListener<Wave>() {
            @Override
            public void done(List<Wave> list, BmobException e) {
                if(e==null){
                    //Toast.makeText(getApplicationContext(),"查询成功：共"+list.size()+"条数据。",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "查询成功：共"+list.size()+"条数据。");
                    dataNum = list.size();
                    if(dataNum<=15){
                        interval = 1;
                    }else{
                        interval = dataNum/chartNum+1;

                    }
                    lines.clear();
                    int sum = 0;
                    for (int j = 0;j<list.size();j++){
                        if (intentData == 0){
                            sum = sum +list.get(j).getAttention();
                        }else {
                            sum = sum +list.get(j).getMeditation();
                        }
                    }
                    if (sum/list.size() <45){
                        textJudge.setText("较差");
                    }else if (sum/list.size() <80){
                        textJudge.setText("良好");
                    }else {
                        textJudge.setText("优秀");
                    }
                    textAverage.setText(Integer.toString(sum/list.size()));

                    for (int i = 0; i<dataNum; i = i+interval) {
                        Wave wave = list.get(i);
                        String string = wave.getCreatedAt();
                        string = string.substring(string.length()-8);
                        string = string.substring(0,5);
                        if(intentData == 0){
                            lines.add(new Jchart(0, wave.getAttention(), string, Color.parseColor("#FF4081")));

                        }else {
                            lines.add(new Jchart(0, wave.getMeditation(), string, Color.parseColor("#FF4081")));
                        }
                    }
                    //----------------------------------------------------
                    textNumber.setText(Integer.toString(list.size())+"/"+Integer.toString(lines.size()));
                    //----------------------------------------------------
                    if (dataNum !=0){
                        mLineChar.setLinePointRadio((int)mLineChar.getLineWidth());
                        mLineChar.setNormalColor(Color.parseColor("#949294"));
                        mLineChar.feedData(lines);
                        mLineChar.aniShow_growing();
                    }
                }else{
                    Snackbar.make(mLineChar, "数据查询失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ( (FrameLayout)mLineChar.getParent() ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mLineChar.postInvalidate();
                mLineChar.setSelectedMode(SELECETD_MSG_SHOW_TOP);
            }
        });
        mLineChar.setSelectedMode(SELECETD_MSG_SHOW_TOP);
        mLineChar.setScrollAble(true);
        mLineChar.setShaderAreaColors(Color.parseColor("#f2efec"), Color.TRANSPARENT);
        mLineChar.setVisibleNums(10);
        mLineChar.setLineShowStyle(JcoolGraph.LINESHOW_ASWAVE);
        mLineChar.aniShow_growing();

    }
    public static String getTime(Date data){
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public BmobQuery<Wave> queryDate(Date date){
        BmobQuery<Wave> query1 = new BmobQuery<Wave>();
        List<BmobQuery<Wave>> and1 = new ArrayList<BmobQuery<Wave>>();
        BmobQuery<Wave> q1 = new BmobQuery<Wave>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = getTime(date);
        String strStart = str+" 00:00:00";
        try {
            date = format.parse(strStart);

        }catch ( Exception e){
            e.printStackTrace();
        }
        q1.addWhereEqualTo("userName",((App)getApplication()).user.getUserName());
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
        and1.add(q1);
        BmobQuery<Wave> q2 = new BmobQuery<Wave>();
        String strEnd = str +" 23:59:59";
        try {
            date = format.parse(strEnd);

        }catch ( Exception e){
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date));
        and1.add(q2);
        query1.and(and1);
        return query1;
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /*
        简单的平移平均
        @data 需要处理的数组
        @lagTime 平移的距离
         */
    private int[] moveAverage(int [] data,int lagTime){
        if (lagTime == 0){
            return data;
        }
        int []temp = new int[data.length];
        for (int a = 0; a < data.length; a++){
            temp[a] = data[a];
            if (a < lagTime){
                int sum = 0;
                for (int i = 0;i <= a; i++){
                    sum = sum + temp[i];
                }
                data[a] = sum/(a+1);
            }else {
                int sum = 0;
                for (int i = 0; i < lagTime; i++){
                    sum = sum + temp[a-i];
                }
                data[a] = sum/lagTime;
            }
        }
        return data;
    }
}
