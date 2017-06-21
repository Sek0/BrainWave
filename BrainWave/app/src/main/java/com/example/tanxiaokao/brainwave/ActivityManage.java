package com.example.tanxiaokao.brainwave;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanxiaokao on 2017/5/22.
 */

public class ActivityManage {
    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishActivity() {
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
    }

}
