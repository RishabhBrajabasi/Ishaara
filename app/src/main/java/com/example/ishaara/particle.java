package com.example.ishaara;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;
import android.view.accessibility.AccessibilityNodeInfo;


public class particle extends AccessibilityService {

    private String LoginID = "rishabhbrajabasi@gmail.com"; // "nchecka@andrew.cmu.edu"
    private String password = "QsY7T249WWcSX8s"; // "Nai^pra99"
    private long subscriptionId;
    private ParticleDevice mDevice;
    private String foreApp = "";
    private  boolean no_gesture = true;
    private TextToSpeech t1;
    String notificationContent="";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();

        config.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        config.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        config.notificationTimeout = 100;
        config.packageNames = null;

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        //        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
//            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(no_gesture == true){
            //Log.d("BANANA", "returning early from Accessibility Event");
            //return;
        }
        int eventType = event.getEventType();
        AccessibilityNodeInfo currentNode=getRootInActiveWindow();
        if(event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED){
            Log.d("BANANA", "Content Clicked");
        }


        if (currentNode!=null && event.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            if (event.getPackageName().equals("com.spotify.music")) {
                Log.d("BANANA","PACKAGE NAME : "+event.getPackageName().toString());
                Log.d  ("BANANA", "ClassName: "+ currentNode.getClassName());
                int childCnt = currentNode.getChildCount();
                for(int i = 0; i < childCnt; i++){
                    Log.d("BANANA", "ChildNum "+i+"Child Name "+currentNode.getChild(i).getContentDescription());
                    //List<AccessibilityNodeInfo.AccessibilityAction> actionList= currentNode.getChild(i).getActionList();
                    //Log.d("BANANA", "ChildNum "+i+"Action List "+actionList);
                    //currentNode.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

            if (event.getPackageName().equals("com.android.dialer")) {

                Log.d("BANANA", "Window Accessed "+currentNode);
                Log.d  ("BANANA", "ClassName: "+ currentNode.getClassName());
                int childCnt = currentNode.getChildCount();
                for(int i = 0; i < childCnt; i++){
                    Log.d("BANANA", "ChildNum "+i+"Child Name "+currentNode.getChild(i).isFocusable());
                    List<AccessibilityNodeInfo.AccessibilityAction> actionList= currentNode.getChild(i).getActionList();
                    Log.d("BANANA", "ChildNum "+i+"Action List "+actionList);
                    currentNode.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
//                if (currentNode!=null && currentNode.getClassName().equals("android.widget.FrameLayout") &&
// currentNode.getChild(2)!=null && currentNode.getChild(2).getClassName().equals("android.widget.TextView") &&
// currentNode.getChild(2).getContentDescription().equals("Search")) {
//                    Log.d("BANANA", "Clicked Something");
//                    currentNode.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
            }
        }

        if(currentNode!=null && eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            Log.d("BANANA","PACKAGE NAME : "+event.getPackageName().toString());

            if (currentNode!=null && event.getPackageName().toString().equals("com.android.dialer")){

                //AccessibilityNodeInfo currentNode=getRootInActiveWindow();
                Log.d("BANANA", "Dialer NODE:"+currentNode);

                StringBuilder message = new StringBuilder();
                if (!event.getText().isEmpty()) {
                    for (CharSequence subText : event.getText()) {
                        message.append(subText);
                    }
                    String name="Pallavi";
                    if (message.toString().contains("Message from")){
                        name=message.toString().substring(13);
                    }
                    Log.d("BANANA","M : "+message);
                }
            }

            if (event.getPackageName().toString().equals("com.whatsapp")){

                //AccessibilityNodeInfo currentNode=getRootInActiveWindow();
                Log.d("BANANA", "NODE:"+currentNode);
                Log.d("BANANA","PACKAGE NAME : "+event.getPackageName().toString());
                Log.d  ("BANANA", "ClassName: "+ currentNode.getClassName());
                int childCnt = currentNode.getChildCount();
                for(int i = 0; i < childCnt; i++){
                    Log.d("BANANA", "ChildNum "+i+"Child Name "+currentNode.getChild(i).getContentDescription());
                    //List<AccessibilityNodeInfo.AccessibilityAction> actionList= currentNode.getChild(i).getActionList();
                    //Log.d("BANANA", "ChildNum "+i+"Action List "+actionList);
                    //currentNode.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                StringBuilder message = new StringBuilder();
                if (!event.getText().isEmpty()) {
                    for (CharSequence subText : event.getText()) {
                        message.append(subText);
                    }
                    if (message.toString().contains("Message from")){
                        String name=message.toString().substring(13);
                        Log.d("BANANA","NAME : "+name);
                    }
                    Log.d("BANANA","M : "+message);
                    notificationContent = notificationContent+ message.toString();
                }
            }
        }


//        switch(eventType){
//
//            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                Log.i("BANANA", "TYPE_WINDOW_CONTENT_CHANGED");
//                if (event.getPackageName() != null && event.getClassName() != null) {
//                    ComponentName componentName = new ComponentName(
//                            event.getPackageName().toString(),
//                            event.getClassName().toString()
//                    );
//
//                    ActivityInfo activityInfo = tryGetActivity(componentName);
//                    boolean isActivity = activityInfo != null;
//                    if (isActivity)
//                        Log.i("BANANA", "1"+componentName.flattenToShortString());
//                    foreApp = componentName.flattenToShortString();
//                }
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                Log.i("BANANA", "TYPE_WINDOW_STATE_CHANGED");
//                if (event.getPackageName() != null && event.getClassName() != null) {
//                    Log.d("BANANA", "PCK NAME"+event.getPackageName().toString());
//                    Log.d("BANANA", "CLS NAME"+event.getClassName().toString());
//
//                    ComponentName componentName = new ComponentName(
//                            event.getPackageName().toString(),
//                            event.getClassName().toString()
//
//                    );
//
//                    ActivityInfo activityInfo = tryGetActivity(componentName);
//                    boolean isActivity = activityInfo != null;
//                    if (isActivity)
//                        Log.i("BANANA", "2"+componentName.flattenToShortString());
//                    foreApp = componentName.flattenToShortString();
//                }
//                break;
//            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                Log.i("BANANA", "TYPE_NOTIFICATION_STATE_CHANGED");
//                if (event.getPackageName() != null && event.getClassName() != null) {
//                    ComponentName componentName = new ComponentName(
//                            event.getPackageName().toString(),
//                            event.getClassName().toString()
//                    );
//
//                    ActivityInfo activityInfo = tryGetActivity(componentName);
//                    boolean isActivity = activityInfo != null;
//                    if (isActivity)
//                        Log.i("BANANA", "3"+componentName.flattenToShortString());
//                    foreApp = componentName.flattenToShortString();
//                }
//                //for (String id : installBtnId) {
////                    AccessibilityNodeInfo node = AccessibilityNodeInfo.
////                            AccessibilityNodeUtil.findNodeById(event.getSource(), id);
////                    if (node != null) {
////                        AccessibilityNodeUtil.click(node);
////                        Log.d("BANANA", "Some accessibility event occured");
////                        break;
////                    }
////                //}
//                break;
//            default:
//                break;
//        }
//        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            if (event.getPackageName() != null && event.getClassName() != null) {
//                ComponentName componentName = new ComponentName(
//                        event.getPackageName().toString(),
//                        event.getClassName().toString()
//                );
//
//                ActivityInfo activityInfo = tryGetActivity(componentName);
//                boolean isActivity = activityInfo != null;
//                if (isActivity)
//                    Log.i("BANANA", componentName.flattenToShortString());
//                    foreApp = componentName.flattenToShortString();
//            }
//        }
        no_gesture = false;
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {}

    public particle() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ParticleCloudSDK.init(this);

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
                sparkCloud.logIn(LoginID, password);
                sparkCloud.getDevices();

                try {
                    mDevice = sparkCloud.getDevices().get(0);
                } catch (IndexOutOfBoundsException ioEx) {
                    throw new RuntimeException("Your account must have at least one device for this example app to work");
                }

                Log.d("BANANA", "analogvalue: " + mDevice.toString());

                Object obj = 1;
                return START_STICKY;
            }

            @Override
            public void onSuccess(@NonNull Object value) {

            }

            @Override
            public void onFailure(@NonNull ParticleCloudException e) {

            }
        });

        //Subscribe to events
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Integer>() {

            @Override
            public Integer callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {

                int resultCode = 0;
                try {

                    subscriptionId = ParticleCloudSDK.getCloud().subscribeToMyDevicesEvents(
                            null,  // the first argument, "eventNamePrefix", is optional
                            new ParticleEventHandler() {
                                public void onEvent(String eventName, ParticleEvent event) {
                                    Log.i("BANANA", "Received event " +eventName+" with payload: " + event.dataPayload);
                                    if(eventName.equals("GestureDetected")) {
                                        String hello = "Hello";
                                        t1.speak(notificationContent, TextToSpeech.QUEUE_FLUSH, null);
                                                /*                                        b1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String toSpeak = ed1.getText().toString();
                                                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });*/
                                        Log.i("BANANA", "Got required event " +eventName+" with payload: " + event.dataPayload);
                                        no_gesture = false;
                                    }
                                }

                                public void onEventError(Exception e) {
                                    Log.e("BANANA", "Event error: ", e);
                                }
                            });
                }
                catch(Exception e) {
                    Log.d("BANANA", "Exception: " + mDevice.toString());
                }
                return resultCode;
            }

            @Override
            public void onSuccess(@NonNull Integer value) {
//                Toaster.l(MainActivity.this, "Subscribed");
            }

            @Override
            public void onFailure(@NonNull ParticleCloudException e) {
//                Toaster.l(MainActivity.this, e.getBestMessage());
            }
        });




        return super.onStartCommand(intent, flags, startId);
    }




}
