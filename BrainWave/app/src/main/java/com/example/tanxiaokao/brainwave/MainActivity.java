package com.example.tanxiaokao.brainwave;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jonas.jgraph.models.Jchart;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by LuckyJayce on 2016/6/25.
 */
public class MainActivity extends FragmentActivity implements View.OnTouchListener{
    private IndicatorViewPager indicatorViewPager;

    VerticalSwipe mContainer;
    VerticalSwipe mContainer2;
    private int TIME = 1000;
    private int USERTIME = 10000;
    private ColorArcProgressBar bar1;
    private ColorArcProgressBar barMeditation;
    private TextView textAttention;
    private TextView textMeditation;
    private TextView testUserId;
    private int countSave = 0;
    private int countSaveTime = 0;
    private Wave wave = new Wave();
    private boolean isConnect = false;
    private boolean isConnecting = false;
    private boolean isAttention = false;
    private boolean isMeditation = false;
    private final int attentionRemind = 10;

    private static final String TAG = "findData";
    private int bluetoothCount = 0;
    private int i = 0;
    private int j = 0;
    private int[] attentionArry = new int[attentionRemind];
    private int[] meditationArry = new int[attentionRemind];
    private int sunMeditation = 0;
    private int sumAttention = 0;

    private int  todayAttention = 0;
    private int todayMeditation = 0;
    private int userAttention = 0;
    private int userMeditation = 0;
    private TextView userText1;
    private TextView userText2;
    private TextView userText3;

    BluetoothAdapter bluetoothAdapter;
    TGDevice tgDevice;
    int subjectContactQuality_last;
    int subjectContactQuality_cnt;
    final boolean rawEnabled = true;
    double task_famil_baseline, task_famil_cur, task_famil_change;
    boolean task_famil_first;
    double task_diff_baseline, task_diff_cur, task_diff_change;
    boolean task_diff_first;

    ListView listViewMeditation;
    ListView listViewAttention;
    ListView listViewUser;
    InformationAdapter adapter;
    InformationAdapter adapterAttention;
    InformationAdapter adapterUser;
    private List<InformationListItem> informationList = new ArrayList<>();
    private List<InformationListItem> informationListAttention = new ArrayList<>();
    private List<InformationListItem> informationListUser = new ArrayList<>();

    private OptionsPickerView<String> optionsSex;
    private ArrayList<String> listSex = new ArrayList<>();

    private ImageButton questionButton1;
    private ImageButton questionButton2;



    InformationListItem informationListItem0;
    InformationListItem informationListItem1;
    InformationListItem informationListItem2;
    InformationListItem informationListItem3;
    InformationListItem informationListItem4;

    InformationListItem informationListItem5;
    InformationListItem informationListItem6;
    InformationListItem informationListItem7;
    InformationListItem informationListItem8;
    InformationListItem informationListItem9;

    InformationListItem userListItem0;
    InformationListItem userListItem1;
    InformationListItem userListItem2;
    InformationListItem userListItem3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.moretab_viewPager);
        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) findViewById(R.id.moretab_indicator);

        listSex.add("男");
        listSex.add("女");

        wave.setUserName(((App)getApplication()).user.getUserName());

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("userName",((App)getApplication()).user.getUserName());
        //Log.d(TAG, "onCreate: 123123   1"+((App)getApplication()).user.getUserName());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    ((App)getApplication()).userId = list.get(0).getObjectId();
                    ((App)getApplication()).user.setSex(list.get(0).getSex());
                    testUserId.setText("ID:"+((App)getApplication()).userId);
                    userListItem0.setDescribe(((App)getApplication()).user.getUserName());
                    userListItem3.setDescribe(list.get(0).getSex());
                    adapterUser.notifyDataSetChanged();
                    //Log.d(TAG, "onCreate: 123123   2"+((App)getApplication()).userId);

                } else {

                }
            }
        });


        //mLineChar.setGraphStyle(BaseGraph.BAR);

        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFFFFFFFF, Color.GRAY).setSize(selectSize, unSelectSize));

        scrollIndicatorView.setScrollBar(new ColorBar(this, 0xFF61B7A6, 4));

        viewPager.setOffscreenPageLimit(2);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter());

        /*List View初始化，设置适配器*/
        initInformation();
        adapter = new InformationAdapter(MainActivity.this,
                R.layout.information_list_item,informationList);
        initInformation2();
        adapterAttention = new InformationAdapter(MainActivity.this,
                R.layout.information_list_item,informationListAttention);
        initInformation3();
        adapterUser = new InformationAdapter(MainActivity.this,
                R.layout.user_information,informationListUser);


        /*tv.setText("");
        tv.append("Android version: " + Integer.valueOf(android.os.Build.VERSION.SDK) + "\n");*/

        subjectContactQuality_last = -1;
        //start with impossible value

        subjectContactQuality_cnt = 200;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
            return;
        } else {
            tgDevice = new TGDevice(bluetoothAdapter, handler);
        }

        handlerstart.postDelayed(runnable, TIME);

        userHander.postDelayed(userRunnable,USERTIME);

        /*tv.append("NeuroSky: " + TGDevice.version + " " + TGDevice.build_title);
        tv.append("\n");*/

        task_famil_baseline = task_famil_cur = task_famil_change = 0.0;
        task_famil_first = true;
        task_diff_baseline = task_diff_cur = task_diff_change = 0.0;
        task_diff_first = true;

    }

    private void initInformation(){
        InformationListItem bluetooth = new InformationListItem(R.drawable.bluetooth_off, "蓝牙连接状态", "未连接",0);
        informationList.add(bluetooth);
        InformationListItem attention = new InformationListItem(R.drawable.attention,"冥想水平","未连接传感器"+"\n",R.drawable.corner);
        informationList.add(attention);
        InformationListItem book = new InformationListItem(R.drawable.book,"学习科目","前往\"我\"设置学习科目",R.drawable.corner);
        informationList.add(book);
        InformationListItem eye = new InformationListItem(R.drawable.eye,"眨眼","未连接传感器",0);
        informationList.add(eye);
        InformationListItem single = new InformationListItem(R.drawable.single,"生物信号强度","没有生物信号强度",0);
        informationList.add(single);
    }
    private void initInformation2(){
        InformationListItem bluetooth = new InformationListItem(R.drawable.bluetooth_off, "蓝牙连接状态", "未连接",0);
        informationListAttention.add(bluetooth);
        InformationListItem attention = new InformationListItem(R.drawable.attention,"专注水平","未连接传感器"+"\n",R.drawable.corner);
        informationListAttention.add(attention);
        InformationListItem book = new InformationListItem(R.drawable.book,"学习科目","前往\"我\"设置学习科目",R.drawable.corner);
        informationListAttention.add(book);
        InformationListItem eye = new InformationListItem(R.drawable.eye,"眨眼","未连接传感器",0);
        informationListAttention.add(eye);
        InformationListItem single = new InformationListItem(R.drawable.single,"生物信号强度","没有生物信号强度",0);
        informationListAttention.add(single);
    }
    private void initInformation3(){
        InformationListItem user = new InformationListItem(0, "用户名", "",0);
        informationListUser.add(user);
        InformationListItem passWord = new InformationListItem(R.drawable.corner,"修改密码","",0);
        informationListUser.add(passWord);
        InformationListItem book = new InformationListItem(R.drawable.corner,"学习科目","",0);
        informationListUser.add(book);
        InformationListItem sex = new InformationListItem(R.drawable.corner,"性别","",0);
        informationListUser.add(sex);

    }

    Handler userHander = new Handler();
    Runnable userRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            try {
                handler.postDelayed(this, USERTIME);
                //---------------------------------------------------------------------------------------------------------
                //user界面信息数据更新

                if (userAttention != 0){
                    userText1.setText(Integer.toString(userAttention));
                    userText2.setText(Integer.toString(userMeditation));
                    if (userAttention < 45) {
                        userText3.setText("较差");
                    } else if (userAttention  < 80) {
                        userText3.setText("良好");
                    } else {
                        userText3.setText("优秀");
                    }
                }

                BmobQuery<Wave> query1 = new BmobQuery<Wave>();
                List<BmobQuery<Wave>> and1 = new ArrayList<BmobQuery<Wave>>();
                BmobQuery<Wave> q1 = new BmobQuery<Wave>();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                String str = sdf.format(date);
                String strStart = str+" 00:00:00";
                try {
                    date = format.parse(strStart);

                }catch ( Exception e){
                    e.printStackTrace();
                }
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
                query1.setLimit(400);
                query1.findObjects(new FindListener<Wave>() {
                    @Override
                    public void done(List<Wave> list, BmobException e) {
                        if (e == null) {
                            todayAttention = 0;
                            //Log.d(TAG, "run: user is running!!    查询成功：共" + list.size() + "条数据。");
                            for (int a = 0; a < list.size(); a++) {
                                todayAttention = todayAttention + list.get(a).getAttention();
                            }
                            userAttention = todayAttention / list.size();
                            userMeditation = list.size();

                        } else {
                            //userAttention = 0;
                            Log.d(TAG, "查询数据库失败！");
                        }
                    }
                });

                //---------------------------------------------------------------------------------------------------------

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    };



    Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    attentionPageInit();
                    //设置滑动界面滑动监听器0~1.0f
                    mContainer.setSwipeListener(new OnSwipeListener() {
                        @Override
                        public void progress(float progress) {
                        }
                    });

                    textAttention.setVisibility(View.GONE);

                    listViewAttention = (ListView) findViewById(R.id.listView_attention);
                    listViewAttention.setAdapter(adapterAttention);


                    informationListItem5 = informationListAttention.get(0);
                    informationListItem6 = informationListAttention.get(1);
                    informationListItem7 = informationListAttention.get(2);
                    informationListItem8 = informationListAttention.get(3);
                    informationListItem9 = informationListAttention.get(4);

                    //adapterAttention.notifyDataSetChanged();

                    listViewAttention.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            InformationListItem informationListItem = informationListAttention.get(position);
                            switch (position){
                                case 0:


                                    break;
                                case 1:
                                    Intent intent = new Intent(MainActivity.this, Jaraph.class);
                                    intent.putExtra("extra_data",0);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    Intent intent1 = new Intent(MainActivity.this, Subject.class);
                                    intent1.putExtra("extra_data1",0);
                                    startActivity(intent1);
                                default:
                                    break;
                            }
                        }
                    });

                    questionButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentQuestion = new Intent(MainActivity.this,QuestionActivity.class);
                            intentQuestion.putExtra("extra_data",0);
                            startActivity(intentQuestion);
                        }
                    });

                    break;
                case 1:
                    meditationPageInit();
                    mContainer2.setSwipeListener(new OnSwipeListener() {
                        @Override
                        public void progress(float progress) {
                        }
                    });

                    textMeditation.setVisibility(View.GONE);
                    listViewMeditation = (ListView) findViewById(R.id.listView_meditation);
                    listViewMeditation.setAdapter(adapter);

                    informationListItem0 = informationList.get(0);
                    informationListItem1 = informationList.get(1);
                    informationListItem2 = informationList.get(2);
                    informationListItem3 = informationList.get(3);
                    informationListItem4 = informationList.get(4);

                    listViewMeditation.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //InformationListItem informationListItem = informationList.get(position);
                            switch (position){
                                case 0:

                                    break;
                                case 1:
                                    Intent intent = new Intent(MainActivity.this, Jaraph.class);
                                    intent.putExtra("extra_data",1);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    Intent intent1 = new Intent(MainActivity.this, Subject.class);
                                    intent1.putExtra("extra_data1",1);
                                    startActivity(intent1);
                                default:
                                    break;
                            }
                        }
                    });
                    questionButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentQuestion2 = new Intent(MainActivity.this,QuestionActivity.class);
                            intentQuestion2.putExtra("extra_data",1);
                            startActivity(intentQuestion2);
                        }
                    });
                    break;
                case 2:
                    testUserId = (TextView) findViewById(R.id.user_id);
                    testUserId.setText(((App)getApplication()).userId);
                    listViewUser = (ListView) findViewById(R.id.listView_user);
                    listViewUser.setAdapter(adapterUser);

                    userText1 = (TextView) findViewById(R.id.text1_describe);
                    userText2 = (TextView) findViewById(R.id.text2_describe);
                    userText3 = (TextView) findViewById(R.id.text3_describe);

                    userListItem0 = informationListUser.get(0);
                    userListItem1 = informationListUser.get(1);
                    userListItem2 = informationListUser.get(2);
                    userListItem3 = informationListUser.get(3);


                    listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position){
                                case 0:
                                    break;
                                case 1:
                                    Intent intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                                    final View textEntryView = inflater.inflate(
                                            R.layout.dialoglayout, null);
                                    final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput);
                                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                                    builder.setCancelable(false);
                                    builder.setIcon(R.drawable.book);
                                    builder.setTitle("设置学习科目");
                                    builder.setView(textEntryView);
                                    builder.setPositiveButton("确认",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    //setTitle(edtInput.getText());
                                                    if (!edtInput.getText().toString().isEmpty()){
                                                        ((App) getApplication()).subject = edtInput.getText().toString();
                                                        userListItem2.setDescribe(((App) getApplication()).subject);
                                                        adapterUser.notifyDataSetChanged();
                                                    }

                                                }
                                            });
                                    builder.setNegativeButton("取消",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                }
                                            });
                                    edtInput.setHint(((App) getApplication()).subject);
                                    builder.show();



                                    break;
                                case 3:
                                    /*
                                    -------------------------------------------------------------------------
                                    user界面个人信息的更改
                                    -------------------------------------------------------------------------
                                     */
                                    optionsSex = new OptionsPickerView<>(MainActivity.this);
                                    optionsSex.setPicker(listSex);
                                    optionsSex.setLabels("性别");
                                    optionsSex.setCyclic(false);
                                    optionsSex.setCancelable(true);

                                    optionsSex.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                                        @Override
                                        public void onOptionsSelect(int options1, int option2, int options3) {
                                            final String sex = listSex.get(options1);
                                            ((App)getApplication()).user.setSex(sex);
                                            ((App)getApplication()).user.update(((App) getApplication()).userId, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        userListItem3.setDescribe(sex);
                                                        adapterUser.notifyDataSetChanged();
                                                        ((App)getApplication()).user.update(((App)getApplication()).userId,new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(getApplicationContext(), "性别修改成功！", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "性别修改失败！", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                        });
                                                    }else{
                                                        Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    optionsSex.show();
                                    break;
                                default:
                                    break;
                            }

                        }
                    });


                    break;
                default:
                    break;
            }
        }
    };

    Handler handlerstart = new Handler();
    Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            try {
                if (!isConnect) {
                    if (!isConnecting) {
                        tgDevice.connect(true);
                    }
                }
                countSaveTime++;
                handlerstart.postDelayed(this, TIME);


                if(((App)getApplication()).subject.equals("输入科目")){

                }else {
                    wave.setSubject(((App) getApplication()).subject);
                    informationListItem2.setImageId(R.drawable.book_on);
                    informationListItem2.setDescribe(((App) getApplication()).subject);
                    adapter.notifyDataSetChanged();
                    informationListItem7.setImageId(R.drawable.book_on);
                    informationListItem7.setDescribe(((App) getApplication()).subject);
                    adapterAttention.notifyDataSetChanged();

                }
                if (isConnect && countSave > 5 && countSaveTime > 10 && wave.getAttention() != 0) {
                    countSave = 0;
                    countSaveTime = 0;
                    wave.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //Toast.makeText(getApplicationContext(), "添加数据成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(listViewAttention, "添加数据失败,请检查网络！", Snackbar.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), "添加数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if (isAttention && isConnect) {
                    for (int n = 0; n < attentionRemind; n++) {
                        sumAttention = sumAttention + attentionArry[n];
                    }
                    if ((sumAttention / attentionRemind) < 45) {
                        isAttention = false;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //调用系统震动提醒
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        long[] pattern = {100, 400, 100, 400}; // OFF/ON/OFF/ON......
                        vibrator.vibrate(pattern, -1);
                        informationListItem6.setImageId(R.drawable.attention_remind);
                        informationListItem6.setDescribe("专注度较差，平均专注值为；"+ sumAttention / attentionRemind +"请集中精神！");
                        listViewAttention.setAdapter(adapterAttention);
                    }else if ((sumAttention / attentionRemind) < 80){
                        isAttention = false;
                        informationListItem6.setImageId(R.drawable.attention_on);
                        informationListItem6.setDescribe("专注度良好，请继续努力！");
                        listViewAttention.setAdapter(adapterAttention);
                    }else {
                        isAttention = false;
                        informationListItem6.setImageId(R.drawable.attention_on);
                        informationListItem6.setDescribe("专注专家！请继续保持！");
                        adapterAttention.notifyDataSetChanged();
                    }

                    sumAttention = 0;
                }

                if (isMeditation && isConnect){
                    for (int m = 0; m < attentionRemind; m++){
                        sunMeditation = sunMeditation+meditationArry[m];
                    }
                    if (sunMeditation / attentionRemind < 50){
                        isMeditation = false;
                        if (sumAttention / attentionRemind <50){
                            informationListItem1.setImageId(R.drawable.attention_remind);
                            informationListItem1.setDescribe("你现在学习状态不佳，可能需要休息！");
                            adapter.notifyDataSetChanged();
                        }else {
                            informationListItem1.setImageId(R.drawable.attention_remind);
                            informationListItem1.setDescribe("不要紧张，请放精神！");
                            adapter.notifyDataSetChanged();
                        }

                    }else if (sunMeditation / attentionRemind < 80){
                        isMeditation = false;
                        informationListItem1.setImageId(R.drawable.attention_on);
                        informationListItem1.setDescribe("精神状态良好，请继续努力！");
                        adapter.notifyDataSetChanged();
                    }else {
                        isMeditation = false;
                        informationListItem1.setImageId(R.drawable.attention_on);
                        informationListItem1.setDescribe("冥想专家，精神状态非常好！请继续保持！");
                        adapter.notifyDataSetChanged();
                    }
                    sunMeditation = 0;
                }

                bar1.setCurrentValues(wave.getAttention());
                barMeditation.setCurrentValues(wave.getMeditation());
                textAttention.setText(String.valueOf(wave.getAttention()));
                textMeditation.setText(String.valueOf(wave.getMeditation()));

            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("exception...");
            }
        }
    };


    // *
    // * Handles messages from TGDevice

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case TGDevice.MSG_MODEL_IDENTIFIED:

                    // * now there is something connected,
                    // * time to set the configurations we need

                    //tv.append("Model Identified\n");
                    tgDevice.setPositivityEnable(true);
                    tgDevice.setBlinkDetectionEnabled(true);
                    tgDevice.setTaskDifficultyRunContinuous(true);
                    tgDevice.setTaskDifficultyEnable(true);
                    tgDevice.setTaskFamiliarityRunContinuous(true);
                    tgDevice.setTaskFamiliarityEnable(true);
                    tgDevice.setRespirationRateEnable(true); /// not allowed on EEG hardware, here to show the override message
                    break;

                case TGDevice.MSG_STATE_CHANGE:

                    switch (msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            break;
                        case TGDevice.STATE_CONNECTING:
                            //tv.append("Connecting...\n");
                            informationListItem1.setImageId(R.drawable.attention);
                            informationListItem1.setDescribe("失去连接");
                            informationListItem6.setImageId(R.drawable.attention);
                            informationListItem6.setDescribe("失去连接");
                            informationListItem4.setImageId(R.drawable.single);
                            informationListItem4.setDescribe("没有生物信号强度");
                            informationListItem9.setImageId(R.drawable.single);
                            informationListItem9.setDescribe("没有生物信号强度");
                            informationListItem0.setImageId(R.drawable.bluetooth_off);
                            informationListItem0.setDescribe("正在连接...");
                            adapter.notifyDataSetChanged();
                            informationListItem5.setImageId(R.drawable.bluetooth_off);
                            informationListItem5.setDescribe("正在连接...");
                            adapterAttention.notifyDataSetChanged();
                            isConnecting = true;
                            break;
                        case TGDevice.STATE_CONNECTED:
                            //tv.append("Connected.\n");
                            informationListItem0.setImageId(R.drawable.bluetooth_on);
                            informationListItem0.setDescribe("已连接");
                            adapter.notifyDataSetChanged();
                            informationListItem5.setImageId(R.drawable.bluetooth_on);
                            informationListItem5.setDescribe("已连接");
                            adapterAttention.notifyDataSetChanged();
                            isConnect = true;
                            isConnecting = false;
                            tgDevice.start();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                            //tv.append("Could not connect to any of the paired BT devices.  Turn them on and try again.\n");
                            //tv.append("Bluetooth devices must be paired 1st\n");
                            if(informationListItem3 != null){
                                informationListItem0.setDescribe("请检查传感器开关是否打开");
                                adapter.notifyDataSetChanged();
                                informationListItem5.setDescribe("请检查传感器开关是否打开");
                                adapterAttention.notifyDataSetChanged();
                            }
                            isConnecting = false;
                            isConnect = false;
                            break;
                        case TGDevice.STATE_ERR_NO_DEVICE:
                            //tv.append("No Bluetooth devices paired.  Pair your device and try again.\n");
                            isConnecting = false;
                            isConnect = false;
                            break;
                        case TGDevice.STATE_ERR_BT_OFF:
                            //tv.append("Bluetooth is off.  Turn on Bluetooth and try again.");
                            if (bluetoothCount > 30 || bluetoothCount == 0) {
                                if(informationListItem3 != null){
                                    informationListItem3.setImageId(R.drawable.eye);
                                    informationListItem3.setDescribe("失去连接");
                                    informationListItem8.setImageId(R.drawable.eye);
                                    informationListItem8.setDescribe("失去连接");
                                    informationListItem1.setImageId(R.drawable.attention);
                                    informationListItem1.setDescribe("失去连接");
                                    informationListItem6.setImageId(R.drawable.attention);
                                    informationListItem6.setDescribe("失去连接");
                                    informationListItem4.setImageId(R.drawable.single);
                                    informationListItem4.setDescribe("没有生物信号强度");
                                    informationListItem9.setImageId(R.drawable.single);
                                    informationListItem9.setDescribe("没有生物信号强度");
                                    informationListItem0.setDescribe("系统蓝牙没有打开，请打开系统蓝牙");
                                    adapter.notifyDataSetChanged();
                                    informationListItem5.setDescribe("系统蓝牙没有打开，请打开系统蓝牙");
                                    adapterAttention.notifyDataSetChanged();
                                }
                                Toast.makeText(getApplicationContext(), "系统蓝牙没有打开，请打开系统蓝牙", Toast.LENGTH_SHORT).show();
                                bluetoothCount = 0;
                            }
                            bluetoothCount++;
                            isConnecting = false;
                            isConnect = false;
                            break;

                        case TGDevice.STATE_DISCONNECTED:
                            //tv.append("Disconnected.\n");
                            informationListItem3.setImageId(R.drawable.eye);
                            informationListItem3.setDescribe("失去连接");
                            informationListItem8.setImageId(R.drawable.eye);
                            informationListItem8.setDescribe("失去连接");
                            informationListItem1.setImageId(R.drawable.attention);
                            informationListItem1.setDescribe("失去连接");
                            informationListItem6.setImageId(R.drawable.attention);
                            informationListItem6.setDescribe("失去连接");
                            informationListItem4.setImageId(R.drawable.single);
                            informationListItem4.setDescribe("没有生物信号强度");
                            informationListItem9.setImageId(R.drawable.single);
                            informationListItem9.setDescribe("没有生物信号强度");
                            informationListItem0.setImageId(R.drawable.bluetooth_off);
                            informationListItem0.setDescribe("未连接");
                            informationListItem5.setImageId(R.drawable.bluetooth_off);
                            informationListItem5.setDescribe("未连接");
                            adapter.notifyDataSetChanged();
                            adapterAttention.notifyDataSetChanged();

                            isConnecting = false;
                            isConnect = false;
                            wave.setMeditation(0);
                            wave.setAttention(0);
                    } // end switch on msg.arg1

                    break;

                case TGDevice.MSG_POOR_SIGNAL:
                    //display signal quality when there is a change of state, or every 30 reports (seconds)
                    if (subjectContactQuality_cnt >= 30 || msg.arg1 != subjectContactQuality_last) {
                        if (msg.arg1 == 0){
                            informationListItem4.setImageId(R.drawable.single_on);
                            informationListItem4.setDescribe("信号良好! 100%");
                            informationListItem9.setImageId(R.drawable.single_on);
                            informationListItem9.setDescribe("信号良好! 100%");
                            adapter.notifyDataSetChanged();
                            adapterAttention.notifyDataSetChanged();
                            //tv.append("SignalQuality: is Good: " + msg.arg1 + "\n");
                        }
                        else if (msg.arg1 >0 && msg.arg1 < 100){
                            informationListItem4.setImageId(R.drawable.single);
                            informationListItem4.setDescribe("生物信号微弱，请注意佩戴方式！"+(100-msg.arg1/2)+"%");
                            informationListItem9.setImageId(R.drawable.single);
                            informationListItem9.setDescribe("生物信号微弱，请注意佩戴方式！"+(100-msg.arg1/2)+"%");
                            adapter.notifyDataSetChanged();
                            adapterAttention.notifyDataSetChanged();
                        }else{
                            informationListItem1.setImageId(R.drawable.attention);
                            informationListItem1.setDescribe("失去连接");
                            informationListItem6.setImageId(R.drawable.attention);
                            informationListItem6.setDescribe("失去连接");
                            informationListItem3.setImageId(R.drawable.eye);
                            informationListItem3.setDescribe("失去连接");
                            informationListItem8.setImageId(R.drawable.eye);
                            informationListItem8.setDescribe("失去连接");
                            informationListItem4.setImageId(R.drawable.single);
                            informationListItem4.setDescribe("生物信号较差！"+(100-msg.arg1/2)+"%");
                            informationListItem9.setImageId(R.drawable.single);
                            informationListItem9.setDescribe("生物信号较差！"+(100-msg.arg1/2)+"%");
                            adapter.notifyDataSetChanged();
                            adapterAttention.notifyDataSetChanged();
                        }


                        subjectContactQuality_cnt = 0;
                        subjectContactQuality_last = msg.arg1;
                    } else subjectContactQuality_cnt++;

                    if (msg.arg1 > 100) {
                        wave.setMeditation(0);
                        wave.setAttention(0);
                    }
                    break;

                case TGDevice.MSG_RAW_DATA:
                    //Handle raw EEG/EKG data here
                    break;

                case TGDevice.MSG_ATTENTION://专注度等级
                    if (msg.arg1 != 0){
                        if (i == attentionRemind-1) {
                            attentionArry[i] = msg.arg1;
                            i = 0;
                            isAttention = true;
                        } else {
                            attentionArry[i] = msg.arg1;
                            i++;
                        }
                        wave.setAttention(msg.arg1);
                        countSave++;
                    }

                    break;

                case TGDevice.MSG_MEDITATION://冥想度等级
                    if (msg.arg1 != 0){
                        if (j == attentionRemind-1){
                            meditationArry[j] = msg.arg1;
                            j = 0;
                            isMeditation = true;
                        }else {
                            meditationArry[j] = msg.arg1;
                            j++;
                        }
                        wave.setMeditation(msg.arg1);
                    }

                    break;

                case TGDevice.MSG_EEG_POWER:
                    TGEegPower e = (TGEegPower) msg.obj;
                    //tv.append("delta: " + e.delta + " theta: " + e.theta + " alpha1: " + e.lowAlpha + " alpha2: " + e.highAlpha + "\n");
                    break;

                case TGDevice.MSG_FAMILIARITY:
                    task_famil_cur = (Double) msg.obj;
                    if (task_famil_first) {
                        task_famil_first = false;
                    } else {

                        // * calculate the percentage change from the previous sample

                        task_famil_change = calcPercentChange(task_famil_baseline, task_famil_cur);
                        if (task_famil_change > 500.0 || task_famil_change < -500.0) {
                            //tv.append("     Familiarity: excessive range\n");
                            //Log.i( "familiarity: ", "excessive range" );
                        } else {
                            //tv.append("     Familiarity: " + task_famil_change + " %\n");
                            //Log.i( "familiarity: ", String.valueOf( task_famil_change ) + "%" );
                        }
                    }
                    task_famil_baseline = task_famil_cur;
                    break;
                case TGDevice.MSG_DIFFICULTY:
                    task_diff_cur = (Double) msg.obj;
                    if (task_diff_first) {
                        task_diff_first = false;
                    } else {

                        // * calculate the percentage change from the previous sample

                        task_diff_change = calcPercentChange(task_diff_baseline, task_diff_cur);
                        if (task_diff_change > 500.0 || task_diff_change < -500.0) {
                            //tv.append("     Difficulty: excessive range %\n");
                            //Log.i("difficulty: ", "excessive range" );
                        } else {
                            //tv.append("     Difficulty: " + task_diff_change + " %\n");
                            //Log.i( "difficulty: ", String.valueOf( task_diff_change ) + "%" );
                        }
                    }
                    task_diff_baseline = task_diff_cur;
                    break;

                case TGDevice.MSG_ZONE:
                    switch (msg.arg1) {
                        case 3:
                            //tv.append("          Zone: Elite\n");
                            break;
                        case 2:
                            //tv.append("          Zone: Intermediate\n");
                            break;
                        case 1:
                            //tv.append("          Zone: Beginner\n");
                            break;
                        default:
                        case 0:
                            //tv.append("          Zone: relax and try to focus\n");
                            break;
                    }
                    break;

                case TGDevice.MSG_BLINK:
                    //tv.append("Blink: " + msg.arg1 + "\n");
                    informationListItem3.setImageId(R.drawable.eye_on);
                    informationListItem3.setDescribe("眨眼强度："+msg.arg1);
                    informationListItem8.setImageId(R.drawable.eye_on);
                    informationListItem8.setDescribe("眨眼强度："+msg.arg1);
                    adapter.notifyDataSetChanged();
                    adapterAttention.notifyDataSetChanged();
                    break;

                case TGDevice.MSG_ERR_CFG_OVERRIDE:
                    switch (msg.arg1) {
                        case TGDevice.ERR_MSG_BLINK_DETECT:
                            //tv.append("Override: blinkDetect" + "\n");
                            //Toast.makeText(getApplicationContext(), "Override: blinkDetect", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_TASKFAMILIARITY:
                            //tv.append("Override: Familiarity" + "\n");
                            //Toast.makeText(getApplicationContext(), "Override: Familiarity", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_TASKDIFFICULTY:
                            //tv.append("Override: Difficulty" + "\n");
                            //Toast.makeText(getApplicationContext(), "Override: Difficulty", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_POSITIVITY:
                            //tv.append("Override: Positivity" + "\n");
                            //Toast.makeText(getApplicationContext(), "Override: Positivity", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_RESPIRATIONRATE:
                            //tv.append("Override: Resp Rate" + "\n");
                            //Toast.makeText(getApplicationContext(), "Override: Resp Rate", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            //tv.append("Override: code: " + msg.arg1 + "\n");
                            //Toast.makeText(getApplicationContext(), "Override: code: " + msg.arg1 + "", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case TGDevice.MSG_ERR_NOT_PROVISIONED:
                    switch (msg.arg1) {
                        case TGDevice.ERR_MSG_BLINK_DETECT:
                            //tv.append("No Support: blinkDetect" + "\n");
                            Toast.makeText(getApplicationContext(), "No Support: blinkDetect", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_TASKFAMILIARITY:
                            //tv.append("No Support: Familiarity" + "\n");
                            Toast.makeText(getApplicationContext(), "No Support: Familiarity", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_TASKDIFFICULTY:
                            //tv.append("No Support: Difficulty" + "\n");
                            Toast.makeText(getApplicationContext(), "No Support: Difficulty", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_POSITIVITY:
                            //tv.append("No Support: Positivity" + "\n");
                            Toast.makeText(getApplicationContext(), "No Support: Positivity", Toast.LENGTH_SHORT).show();
                            break;
                        case TGDevice.ERR_MSG_RESPIRATIONRATE:
                            //tv.append("No Support: Resp Rate" + "\n");
                            Toast.makeText(getApplicationContext(), "No Support: Resp Rate", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            //tv.append("No Support: code: " + msg.arg1 + "\n");
                            Toast.makeText(getApplicationContext(), "No Support: code: " + msg.arg1 + "", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                default:
                    break;

            }  //end switch on msg.what

            //sv.fullScroll(View.FOCUS_DOWN);

        }  //end handleMessage()

    };  //end Handler

    private double calcPercentChange(double baseline, double current) {
        double change;

        if (baseline == 0.0) baseline = 1.0; //don't allow divide by zero

        // * calculate the percentage change

        change = current - baseline;
        change = (change / baseline) * 1000.0 + 0.5;
        change = Math.floor(change) / 10.0;
        return (change);
    }

    //*
    //  * This method is called when the user clicks on the "Connect" button.
    //  *
    //* @param view

    public void doStuff(View view) {
        if (tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED) {

            tgDevice.connect(rawEnabled);
        }

    }  //end doStuff()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed: " + getClass().getName());
        if (mContainer.isSwipeViewShowing()) {
            mContainer.setSwipeViewShowing(false);
        } else {
            super.onBackPressed();
            //ActivityManage.finishActivity();
        }
    }



    //turn off app when touch return button of phone
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            tgDevice.close();
            ActivityManage.finishActivity();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        //if (!bluetoothAdapter.isEnabled()) {
        //  Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //startActivityForResult(enableIntent, 1);
        //}
    }

    @Override
    public void onPause() {
        // tgDevice.close();
        super.onPause();
    }

    @Override
    public void onStop() {
        tgDevice.close();
        super.onStop();

    }

    @Override
    public void onDestroy() {
        //tgDevice.close();
        super.onDestroy();
    }

    public  void attentionPageInit(){
        mContainer = (VerticalSwipe) findViewById(R.id.container);
        textAttention = (TextView) findViewById(R.id.text_attention);
        bar1 = (ColorArcProgressBar) findViewById(R.id.bar1);
        questionButton1 = (ImageButton) findViewById(R.id.but_question);
        //sv = (ScrollView) findViewById(R.id.scrollView1);
        //tv = (TextView) findViewById(R.id.textView1);
        //quaryData = (Button) findViewById(R.id.qurryData);
    }
    public void meditationPageInit(){
        mContainer2 = (VerticalSwipe) findViewById(R.id.container_meditation);
        textMeditation = (TextView) findViewById(R.id.text_meditation);
        barMeditation = (ColorArcProgressBar) findViewById(R.id.bar_meditation);
        questionButton2 = (ImageButton) findViewById(R.id.but_question2);
        //sv2 = (ScrollView) findViewById(R.id.scrollView_meditation);
        //tv2 = (TextView) findViewById(R.id.textView_meditation);
        //quaryDataMeditation = (Button) findViewById(R.id.qurryData_meditation);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //listViewAttention.requestDisallowInterceptTouchEvent(true);
        return false;
    }


    private class MyAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        private String[] versions = {"专注度", "冥想度", "我"};

        @Override
        public int getCount() {
            return versions.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(versions[position]);

            int witdh = getTextWidth(textView);
            int padding = DisplayUtil.dipToPix(getApplicationContext(), 8);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (witdh * 1.3f) + padding);

            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null && position == 0) {
                convertView = getLayoutInflater().inflate(R.layout.activity_main, container, false);
                mHander.sendEmptyMessage(0);
            } else if (convertView == null && position == 1) {
                convertView = getLayoutInflater().inflate(R.layout.activity_meditation, container, false);
                mHander.sendEmptyMessage(1);
            } else {
                convertView = getLayoutInflater().inflate(R.layout.user, container, false);
                //Log.d(TAG, "getViewForPage: sdsfsdsdfdss");
                mHander.sendEmptyMessage(2);

            }
            return convertView;
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_UNCHANGED;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }

    }

    public static String getTime(Date data){
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data);
    }

   /* public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        Log.d(TAG, "setListViewHeightBasedOnChildren:      "+totalHeight);
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(50, 50, 50, 50); // 可删除

        listView.setLayoutParams(params);
    }*/



}
