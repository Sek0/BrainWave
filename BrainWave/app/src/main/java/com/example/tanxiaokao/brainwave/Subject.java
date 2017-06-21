package com.example.tanxiaokao.brainwave;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.jonas.jgraph.graph.JcoolGraph;
import com.jonas.jgraph.graph.NChart;
import com.jonas.jgraph.inter.BaseGraph;
import com.jonas.jgraph.models.Jchart;
import com.jonas.jgraph.models.NExcel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.cacheColorHint;
import static android.R.attr.columnCount;
import static android.R.attr.factor;
import static android.R.attr.firstDayOfWeek;
import static android.R.attr.lines;
import static android.R.attr.numberPickerStyle;
import static android.R.attr.singleUser;
import static com.jonas.jgraph.inter.BaseGraph.SELECETD_MSG_SHOW_TOP;

public class Subject extends AppCompatActivity {


    private static final String TAG = "SubjectFindData";
    private NChart mChart;
    List<NExcel> nExcelList = new ArrayList<>();
    List<List<NExcel>> timeExcelList = new ArrayList<>();
    private boolean[] isOver = {false,false,false,false,false,false,false};
    private int chartNum = 15;
    EditText editText;
    TimePickerView pvTime;
    ImageButton selectDate;
    OptionsPickerView pvOption;
    Set<String> subjectList = new HashSet();
    Set<String> timeSubjectList = new HashSet();

    int sumSubject = 0;
    int countSubject = 0;
    private int intentData;
    //选项
    private OptionsPickerView<String> optionsPickerView;
    private ArrayList<String> listTime = new ArrayList<>();
    private OptionsPickerView<String> optionsModel;
    private ArrayList<String> listModel = new ArrayList<>();

    private List<List<String>> test = new ArrayList<List<String>>();
    private List<String> testList = new ArrayList<>();

    private List<NExcel> timeExcelList0 = new ArrayList<>();
    private List<NExcel> timeExcelList1 = new ArrayList<>();
    private List<NExcel> timeExcelList2 = new ArrayList<>();
    private List<NExcel> timeExcelList3 = new ArrayList<>();
    private List<NExcel> timeExcelList4 = new ArrayList<>();
    private List<NExcel> timeExcelList5 = new ArrayList<>();
    private List<NExcel> timeExcelList6 = new ArrayList<>();

    private ImageButton imageButtonModel;
    private ImageButton buttonSelectTime;

    private int sumDate = 0;//按日期统计的总数据
    private int sumTime = 0;//按时间段统计的总数据
    private int maxDate = 0;//按日期统计的最大数据
    private String maxdateSubject;//按日期统计的最大数据对应的科目
    private String maxtimeSubject;//按时间段统计的最大数据对应的枯木
    private int isMaxTime = 0;//判断按时间段统计最大数据的中间变量
    private int countTime = 0;//判断按时间段统计的科目个数
    private int[] maxTime = {0,0,0,0,0,0,0};//存放按时间段统计7天的数据
    private String[] maxTimeSub = {"","","","","","",""};//存放按时间段统计7天的数据对用的科目
    private int maxTimeSujNum = 0;//存放按时间段统计最大值对应的科目的次序
    private TextView textView11;
    private TextView textView12;
    private TextView textView13;
    private TextView textView21;
    private TextView textView22;
    private TextView textView23;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("科目统计");

        Intent intent = getIntent();
        intentData = intent.getIntExtra("extra_data",0);

        textView11 = (TextView) findViewById(R.id.text1_subject_describe);
        textView12 = (TextView) findViewById(R.id.text2_subject_describe);
        textView13 = (TextView) findViewById(R.id.text3_subject_describe);
        textView21 = (TextView) findViewById(R.id.text21_subject_describe);
        textView22 = (TextView) findViewById(R.id.text22_subject_describe);
        textView23 = (TextView) findViewById(R.id.text23_subject_describe);

        mChart = (NChart)findViewById(R.id.sug_recode_schar);
        nExcelList.add(new NExcel(0, ""));
        mChart.setNormalColor(Color.parseColor("#ccc7c2"));
        mChart.cmdFill(nExcelList);
        mChart.setBarWidth(40);//设置柱状条的宽度
        mChart.setInterval(100);//设置柱状条之间的间距

        optionsPickerView = new OptionsPickerView<>(this);
        listTime.add("08:00-09:00");
        listTime.add("09:00-10:00");
        listTime.add("10:00-11:00");
        listTime.add("11:00-12:00");
        listTime.add("12:00-13:00");
        listTime.add("13:00-14:00");
        listTime.add("14:00-15:00");
        listTime.add("15:00-16:00");
        listTime.add("16:00-17:00");
        listTime.add("17:00-18:00");

        optionsModel = new OptionsPickerView<>(this);
        listModel.add("按日期统计");
        listModel.add("按时间段统计");

        buttonSelectTime = (ImageButton) findViewById(R.id.but_select_time);
        imageButtonModel = (ImageButton) findViewById(R.id.model);
        selectDate = (ImageButton) findViewById(R.id.sub_but_selectDate);
        buttonSelectTime.setEnabled(false);

        imageButtonModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsModel.setPicker(listModel);
                optionsModel.setCyclic(false);
                optionsModel.setCancelable(true);
                optionsModel.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        final String strModel = listModel.get(options1);
                        if (strModel.equals("按日期统计")){
                            nExcelList.clear();
                            //nExcelList.add(new NExcel(0, ""));
                            mChart.setNormalColor(Color.parseColor("#ccc7c2"));
                            mChart.cmdFill(nExcelList);

                            buttonSelectTime.setEnabled(false);
                            buttonSelectTime.setImageResource(R.drawable.date_off);
                            selectDate.setEnabled(true);
                            selectDate.setImageResource(R.drawable.date_on);
                            textView21.setText("-");
                            textView22.setText("-");
                            textView23.setText("-");

                        }else {
                            nExcelList.clear();
                            //nExcelList.add(new NExcel(0, ""));
                            mChart.setNormalColor(Color.parseColor("#ccc7c2"));
                            mChart.cmdFill(nExcelList);

                            buttonSelectTime.setEnabled(true);
                            buttonSelectTime.setImageResource(R.drawable.date_on);
                            selectDate.setEnabled(false);
                            selectDate.setImageResource(R.drawable.date_off);
                            textView11.setText("-");
                            textView12.setText("-");
                            textView13.setText("-");
                        }
                    }
                });
                optionsModel.show();
            }
        });


        pvTime=new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date(System.currentTimeMillis()));//默认当前时间
        pvTime.setCyclic(false);//是否循环滚动
        pvTime.setCancelable(true);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSelect(Date date) {
                BmobQuery<Wave> query = new BmobQuery<Wave>();
                query = queryDate(date);
                query.setLimit(500);
                countSubject = 0;
                sumDate = 0;
                maxDate = 0;
                nExcelList.clear();
                query.findObjects(new FindListener<Wave>() {
                    @Override
                    public void done(List<Wave> list, BmobException e) {
                        if(e==null){
                            if (list.size() ==0){
                                nExcelList.add(new NExcel(0, ""));
                                mChart.setNormalColor(Color.parseColor("#ccc7c2"));
                                mChart.cmdFill(nExcelList);
                                mChart.setBarWidth(40);//设置柱状条的宽度
                                mChart.setInterval(100);//设置柱状条之间的间距
                                textView11.setText("-");
                                textView12.setText("-");
                                textView13.setText("-");
                            }
                            nExcelList.clear();
                            subjectList.clear();
                            for (int i = 0; i < list.size(); i++){
                                Wave wave = list.get(i);
                                if (wave.getSubject() == null){
                                    continue;
                                }
                                subjectList.add(wave.getSubject());
                            }
                            Iterator k = subjectList.iterator();
                            while (k.hasNext()){
                                String sub = k.next().toString();
                                sumSubject = 0;
                                countSubject = 0;
                                for (int i = 0; i < list.size(); i++){
                                    Wave wave = list.get(i);
                                    if(sub.equals(wave.getSubject())){
                                        countSubject ++;
                                        if (intentData == 0){
                                            sumSubject = sumSubject +wave.getAttention();
                                        }else {
                                            sumSubject = sumSubject +wave.getMeditation();
                                        }
                                    }
                                }
                                sumDate = sumDate + (sumSubject/countSubject);
                                nExcelList.add(new NExcel(sumSubject/countSubject, sub));
                                if (maxDate <= (sumSubject/countSubject)){
                                    maxDate = sumSubject/countSubject;
                                    maxdateSubject = sub;
                                }
                            }
                            sumDate = sumDate/subjectList.size();
                            if (sumDate<45){
                                textView13.setText("较差");
                            }else if (sumDate<80){
                                textView13.setText("良好");
                            }else {
                                textView13.setText("优秀");
                            }
                            textView11.setText(Integer.toString(sumDate));
                            textView12.setText(maxdateSubject);


                            mChart.setNormalColor(Color.parseColor("#ccc7c2"));
                            mChart.cmdFill(nExcelList);
                            //mChart.setTextSize(1);  //mAbscissaMsgSize
                            mChart.setBarWidth(40);//设置柱状条的宽度
                            mChart.setInterval(100);//设置柱状条之间的间距
                        }else{
                            Snackbar.make(mChart, "数据查询失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
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

                    nExcelList.clear();
                    countSubject = 0;
                    for (int i = 0; i < list.size(); i++){
                        Wave wave = list.get(i);
                        if (wave.getSubject() == null){
                            continue;
                        }
                        subjectList.add(wave.getSubject());
                    }
                    Iterator k = subjectList.iterator();

                    while (k.hasNext()){
                        String sub = k.next().toString();
                        countSubject = 0;
                        sumSubject = 0;
                        for (int i = 0; i < list.size(); i++){
                            Wave wave = list.get(i);
                            if(sub.equals(wave.getSubject())){
                                countSubject ++;
                                if (intentData == 0){
                                    sumSubject = sumSubject +wave.getAttention();
                                }else {
                                    sumSubject = sumSubject +wave.getMeditation();

                                }
                            }
                        }
                        sumDate = sumDate + (sumSubject/countSubject);
                        nExcelList.add(new NExcel(sumSubject/countSubject, sub));
                        if (maxDate < (sumSubject/countSubject)){
                            maxDate = sumSubject/countSubject;
                            maxdateSubject = sub;
                        }
                    }
                    sumDate = sumDate/subjectList.size();
                    if (sumDate<45){
                        textView13.setText("较差");
                    }else if (sumDate<80){
                        textView13.setText("良好");
                    }else {
                        textView13.setText("优秀");
                    }
                    textView11.setText(Integer.toString(sumDate));
                    textView12.setText(maxdateSubject);

                    if (list.size() ==0){
                        nExcelList.add(new NExcel(0, ""));
                    }
                    mChart.setNormalColor(Color.parseColor("#ccc7c2"));
                    mChart.cmdFill(nExcelList);
                    mChart.setBarWidth(40);//设置柱状条的宽度
                    mChart.setInterval(100);//设置柱状条之间的间距

                }else{
                    Snackbar.make(mChart, "数据查询失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                }
            }
        });





        //buttonSelectTime.setEnabled(false);
        buttonSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //optionsPickerView = new OptionsPickerView<String>(this);
                optionsPickerView.setPicker(listTime);
                optionsPickerView.setCyclic(false);
                optionsPickerView.setCancelable(true);
                countTime = 0;
                sumTime = 0;
                isMaxTime = 0;
                for (int m = 0;m<7;m++){
                    isOver[m] = false;
                    maxTime[m] = 0;
                }
                optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {

                        final String strOptionTime = listTime.get(options1).substring(0,2);
                        textView21.setText(listTime.get(options1));
                        //Log.d(TAG, "onOptionsSelect: "+strOptionTime);
                        //timeExcelList.clear();
                        nExcelList.clear();
                        timeSubjectList.clear();
                        countSubject = 0;
                        timeExcelList0.clear();
                        timeExcelList1.clear();
                        timeExcelList2.clear();
                        timeExcelList3.clear();
                        timeExcelList4.clear();
                        timeExcelList5.clear();
                        timeExcelList6.clear();

                        BmobQuery<Wave> querytime = new BmobQuery<Wave>();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        for (int i = 0;i<7;i++){

                            final int temppp = i;
                            //依次查询出当前日期前七天的数据
                            timeSubjectList.clear();

                            final String string = getPastDate(i);
                            //Log.d(TAG, "onOptionsSelect: "+string);
                            try {
                                Date date = format.parse(string);
                                querytime = queryDate(date);
                                querytime.setLimit(400);
                                querytime.findObjects(new FindListener<Wave>() {
                                    @Override
                                    public void done(List<Wave> list, BmobException e) {
                                        //-----------------------------------------------------------------
                                        //统计时间段数据代码
                                        if(e==null){

                                            for (int i = 0;i < list.size();i=i+1) {
                                                Wave wave = list.get(i);
                                                String temp = wave.getCreatedAt().substring(11,13);
                                                if (temp.equals(strOptionTime)){
                                                    //Log.d(TAG, "done: ddddddd    "+wave.getUpdatedAt());
                                                    if (wave.getSubject() == null){
                                                        continue;
                                                    }else {
                                                        timeSubjectList.add(wave.getSubject());
                                                    }
                                                    //Iterator<String> k = timeSubjectList.iterator();

                                                }
                                            }

                                            Iterator k = timeSubjectList.iterator();

                                            while (k.hasNext()){
                                                String sub = k.next().toString();
                                                Log.d(TAG, "done: "+sub);
                                                sumSubject = 0;
                                                int sumTimeSubject = 0;
                                                int sunTimeSubjectCount = 0;
                                                for (int i = 0; i < list.size(); i++){
                                                    Wave wave = list.get(i);
                                                    if(sub.equals(wave.getSubject())){
                                                        Log.d(TAG, "done: "+wave.getSubject());
                                                        sunTimeSubjectCount ++;
                                                        if (intentData == 0){
                                                            sumTimeSubject = sumTimeSubject +wave.getAttention();
                                                        }else {
                                                            sumTimeSubject = sumTimeSubject +wave.getMeditation();

                                                        }
                                                    }
                                                }
                                                String strDate = string.substring(5,7)+"/"+string.substring(8,10);
                                                maxTime[temppp] = (sumTimeSubject/sunTimeSubjectCount);
                                                maxTimeSub[temppp] = strDate + sub;
                                                //timeExcelList.get(temppp).add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                switch (temppp){
                                                    case 0:
                                                        timeExcelList0.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                    case 1:
                                                        timeExcelList1.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                    case 2:
                                                        timeExcelList2.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                    case 3:
                                                        timeExcelList3.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                    case 4:
                                                        timeExcelList4.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                    case 5:
                                                        timeExcelList5.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                    case 6:
                                                        timeExcelList6.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                                        break;
                                                }
                                                //nExcelList.add(new NExcel(sumTimeSubject/sunTimeSubjectCount, strDate + sub));
                                            }

                                        }else{
                                            //Snackbar.make(mChart, "数据查询失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                                        }
                                        isOver[temppp] = true;
                                        timeHander.sendEmptyMessage(temppp);
                                    }
                                });
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                       //for循环结束位置

                    }

                });

                optionsPickerView.show();
            }
        });



    }


    android.os.Handler timeHander = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (timeExcelList0.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList0);
                        Log.d(TAG, "handleMessage:    " +0);
                        if (isMaxTime < maxTime[0]){
                            isMaxTime = maxTime[0];
                            maxTimeSujNum = 0;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[0];
                    }
                    break;
                case 1:
                    if (timeExcelList1.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList1);
                        Log.d(TAG, "handleMessage:    " +1);
                        if (isMaxTime < maxTime[1]){
                            isMaxTime = maxTime[1];
                            maxTimeSujNum = 1;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[1];
                    }
                    break;
                case 2:
                    if (timeExcelList2.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList2);
                        Log.d(TAG, "handleMessage:    " +2);
                        if (isMaxTime < maxTime[2]){
                            isMaxTime = maxTime[2];
                            maxTimeSujNum = 2;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[2];
                    }
                    break;
                case 3:
                    if (timeExcelList3.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList3);
                        Log.d(TAG, "handleMessage:    " +3);
                        if (isMaxTime < maxTime[3]){
                            isMaxTime = maxTime[3];
                            maxTimeSujNum = 3;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[3];
                    }
                    break;
                case 4:
                    if (timeExcelList4.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList4);
                        Log.d(TAG, "handleMessage:    " +4);
                        if (isMaxTime < maxTime[4]){
                            isMaxTime = maxTime[4];
                            maxTimeSujNum = 4;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[4];
                    }
                    break;
                case 5:
                    if (timeExcelList5.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList5);
                        Log.d(TAG, "handleMessage:    " +5);
                        if (isMaxTime < maxTime[5]){
                            isMaxTime = maxTime[5];
                            maxTimeSujNum = 5;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[5];
                    }
                    break;
                case 6:
                    if (timeExcelList6.size() == 0){
                        break;
                    }else{
                        nExcelList.addAll(timeExcelList6);
                        Log.d(TAG, "handleMessage:    " +6);
                        if (isMaxTime < maxTime[6]){
                            isMaxTime = maxTime[6];
                            maxTimeSujNum = 6;
                        }
                        countTime++;
                        sumTime = sumTime + maxTime[6];
                    }
                    break;
            }
            //Log.d(TAG, "handleMessage:   后面到底执行了没有啊！！！！！");
            if (isOver[0] && isOver[1] && isOver[2] && isOver[3] && isOver[4] && isOver[5] && isOver[6]){
                //Iterator iterator = timeExcelList.iterator();
                mChart.cmdFill(nExcelList);
                mChart.getBarAniStyle(NChart.ChartAniStyle.BAR_DISPERSED);
                if (nExcelList.size() == 0){
                    textView21.setText("-");
                    textView22.setText("-");
                    textView23.setText("-");
                }else{
                    maxtimeSubject = maxTimeSub[maxTimeSujNum];
                    sumTime = sumTime/countTime;
                    textView22.setText(maxtimeSubject.toString());
                    if (sumTime<45){
                        textView23.setText("较差");
                    }else if (sumTime<80){
                        textView23.setText("良好");
                    }else {
                        textView23.setText("优秀");
                    }
                }

            }
        }
    };


    public static String getTime(Date data){
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  BmobQuery<Wave> queryDate(Date date){
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        //Log.e(null, result);
        return result;
    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            ((App)getApplication()).subject = editText.getText().toString();
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            ((App)getApplication()).subject = editText.getText().toString();
            Toast.makeText(getApplicationContext(),((App)getApplication()).subject,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
