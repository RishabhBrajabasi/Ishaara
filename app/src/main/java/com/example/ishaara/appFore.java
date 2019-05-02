package com.example.ishaara;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.List;


public class appFore extends Service {


    public appFore() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = activityManager.getRunningAppProcesses();


            Log.d("BANANA", "Start");
            for (int i = 0; i < runningAppProcessInfo.size(); i++) {
                Log.d("BANANA", "Process:" + runningAppProcessInfo.get(i).processName);
            }
            Log.d("BANANA", "Stop");


            Context context = this;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

                Log.d("BANANA", "ForeGround Loli:" + foregroundTaskPackageName);

                return START_STICKY;
            } else {
                ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
                String cool = appProcessInfo.processName;
                ActivityManager.getMyMemoryState(appProcessInfo);
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    Log.d("BANANA", "Not Sure:" + cool);
                }

                KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                // App is foreground, but screen is locked, so show notification
                return START_STICKY;
            }
        }
//        return super.onStartCommand(intent, flags, startId);


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }




}