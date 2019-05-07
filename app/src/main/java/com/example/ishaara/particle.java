package com.example.ishaara;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.IOException;
import java.util.Locale;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.utils.Async;


public class particle extends AccessibilityService {

    private String LoginID = "rishabhbrajabasi@gmail.com"; // "nchecka@andrew.cmu.edu"
    private String password = "QsY7T249WWcSX8s"; // "Nai^pra99"
    private long subscriptionId;
    private ParticleDevice mDevice;
    private String foreApp = "";
    private  boolean no_gesture = true;
    private TextToSpeech talker;
    String notificationContent="";
    AccessibilityNodeInfo nodeWhatsapp;
    AccessibilityNodeInfo nodeGmail;
    AccessibilityNodeInfo nodeYoutube;
    AccessibilityNodeInfo nodeCamera;
    AccessibilityNodeInfo nodePhone;
    AccessibilityNodeInfo nodeCapture;
    AccessibilityNodeInfo nodeCamSwitch;
    String currActiveWindow = "";

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

        talker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    talker.setLanguage(Locale.US);
                }
            }
        });
        setServiceInfo(config);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        int numChildren;
        if(rootNode !=null){
            switch(event.getEventType()){
                case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                    Log.d("BANANA", "Current Active Window " + event.getPackageName());
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    //if(!event.getPackageName().equals("net.oneplus.launcher")) {
                    if(getPackageName() == null) {
                        currActiveWindow = "";
                    }
                    else {
                        currActiveWindow = event.getPackageName().toString();
                    }
                        Log.d("BANANA", "Window Content Changed " + event.getPackageName());
                        Log.d("BANANA", "Class Name " + rootNode.getClassName());
                        numChildren = rootNode.getChildCount();
                        for(int i = 0; i < numChildren; i++){
                            //Log.d("BANANA", "Child Name " + rootNode.getChild(i));
                            CharSequence contentDesc = rootNode.getChild(i).getContentDescription();
                            Log.d("BANANA", "Child Description " + contentDesc);
                            if(currActiveWindow.equals("net.oneplus.launcher")) {
                                if (contentDesc != null && contentDesc.toString().equals("WhatsApp")) {
                                    nodeWhatsapp = rootNode.getChild(i);
                                }
                                if (contentDesc != null && contentDesc.toString().equals("Gmail")) {
                                    nodeGmail = rootNode.getChild(i);
                                }
                                if (contentDesc != null && contentDesc.toString().equals("YouTube")) {
                                    nodeYoutube = rootNode.getChild(i);
                                }
                                if (contentDesc != null && contentDesc.toString().equals("Camera")) {
                                    nodeCamera = rootNode.getChild(i);
                                }
                                if (contentDesc != null && contentDesc.toString().equals("Phone")) {
                                    nodePhone = rootNode.getChild(i);
                                }
                            }
                            if(currActiveWindow.equals("com.oneplus.camera")){
                                if (contentDesc != null && contentDesc.toString().equals("Capture")){
                                    nodeCapture = rootNode.getChild(i);
//                                    rootNode.getChild(i).setClickable(true);
                                }
                                if (contentDesc != null && contentDesc.toString().equals("Switch camera")) {
                                    nodeCamSwitch = rootNode.getChild(i);
                                }
                            }
                        }

                        //If content is camera the capture button can be found
                    //}
                    break;
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    Log.d("BANANA", "Notification State Change " + event.getPackageName());
                    //Need to check the root nodes children
                    numChildren = rootNode.getChildCount();
                    for(int i = 0; i < numChildren; i++){
                        Log.d("BANANA", "Child Name " + rootNode.getChild(i));
                        Log.d("BANANA", "Child Description " + rootNode.getChild(i).getContentDescription());
                    }
                    break;

            }
        }
    }

    @Override
    public void onInterrupt() {}

    //Constructor for the service
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
                                        //talker.speak(notificationContent, TextToSpeech.QUEUE_FLUSH, null);
                                        Log.i("BANANA", "Got required event " +eventName+" with payload: " + event.dataPayload);

                                        if(event.dataPayload.equals("T")){
                                            Log.i("BANANA", "Curr Active Window " +currActiveWindow);
                                            if(currActiveWindow.equals("com.oneplus.camera")){
                                                Log.i("BANANA", "Performing action for capture " +nodeCapture.getContentDescription());
                                                nodeCapture.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                            else if(currActiveWindow.equals("net.oneplus.launcher")) {
                                                nodeYoutube.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                        }
                                        if(event.dataPayload.equals("U")){
                                            //nodePhone.setTraversalBefore();
                                        }
                                        if(event.dataPayload.equals("D")){
                                            nodeCamera.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            if(currActiveWindow.equals("com.oneplus.camera")){
                                                Log.i("BANANA", "Performing action for CamSwitch " +currActiveWindow);
                                                nodeCapture.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                        }
                                        if(event.dataPayload.equals("R")){
                                            nodeWhatsapp.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        }
                                        if(event.dataPayload.equals("L")){
                                            nodeGmail.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        }
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
