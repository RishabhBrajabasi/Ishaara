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

    AccessibilityNodeInfo gestureTap;
    AccessibilityNodeInfo gestureDown;
    AccessibilityNodeInfo gestureUp;
    AccessibilityNodeInfo gestureLeft;
    AccessibilityNodeInfo gestureRight;

    String lockedScreen = "com.oneplus.aod";
    String homeScreen = "net.oneplus.launcher"; //net.oneplus.launcher
    String camera = "net.sourceforge.opencamera"; // net.sourceforge.opencamera
    String currActiveWindow = "";
    String music = "com.spotify.music";
    String phone = "com.android.dialer";

    //Need to read out notifications for
    //0 - whatsapp
    //1 - gmail
    //2 - missed calls
    AccessibilityNodeInfo[] nodeNotifications = new AccessibilityNodeInfo[5];
    String[] readOutNotifications = new String[5];

    int currAddress = 0;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();

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

        if(event.getPackageName()!= null) {
            currActiveWindow = event.getPackageName().toString();
            Log.d("BANANA_PACK_NAME", currActiveWindow);
            if(rootNode!=null && rootNode.getContentDescription() != null)
                Log.d("BANANA_ROOT_NODE", rootNode.getContentDescription().toString());
        }
        if(rootNode !=null){
            switch(event.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    Log.d("BANANA_WIN_CONTENT", "GET Text" + event.getText());
                    numChildren = rootNode.getChildCount();
                    Log.d("BANANA_WIN_CONTENT", "Number of Children: "+ numChildren);
                    if(currActiveWindow.equals(lockedScreen)) {
                        readOutNotifications[0] = "Notification List";
                        readOutNotifications[1] = "No Whatsapp Notifications";
                        readOutNotifications[2] = "No Gmail  Notifications";
                        readOutNotifications[3] = "No Missed Calls";
                    }
                    for(int i = 0; i < numChildren; i++){
                        if(rootNode.getChild(i) == null) {
                            continue;
                        }
                        CharSequence childContentDesc =rootNode.getChild(i).getContentDescription();
                        Log.d("BANANA_WIN_CONTENT", "Child Desc: "+ childContentDesc);
                        if(childContentDesc == null){
                            int numGrandChildren = rootNode.getChild(i).getChildCount();
                            Log.d("BANANA_WIN_CONTENT:", "Number of GrandChildren: "+ numGrandChildren);
                            for(int j = 0; j < numGrandChildren; j++) {
                                if(rootNode.getChild(i).getChild(j) == null) {
                                    continue;
                                }
                                CharSequence grandChildContentDesc =rootNode.getChild(i).getChild(j).getContentDescription();
                                if(grandChildContentDesc == null) {
                                    continue;
//                                    int numGrandChildren = rootNode.getChild(i).getChildCount();
                                }
                                Log.d("BANANA_WIN_CONTENT", "Grand Child Desc: "+ grandChildContentDesc);
                                if(currActiveWindow.equals(music)){
                                    if(grandChildContentDesc.toString().equals("Play") || grandChildContentDesc.toString().equals("Pause")){
                                        gestureTap = rootNode.getChild(i).getChild(j);
                                    }
                                    else if(grandChildContentDesc.toString().equals("Previous")){
                                        gestureLeft = rootNode.getChild(i).getChild(j);
                                    }
                                    else if(grandChildContentDesc.toString().equals("Next")){
                                        gestureRight = rootNode.getChild(i).getChild(j);
                                    }
                                }
                                if(currActiveWindow.equals("com.android.systemui") || currActiveWindow.equals(phone)){
                                    Log.d("BANANA", "Enteredddddddddd syssui");
                                    if(grandChildContentDesc.toString().startsWith("Phone notification")){
                                        int greatgrand = rootNode.getChild(i).getChild(j).getChildCount();
                                        Log.d("BANANA_WIN_CONTENT", "Great Grand Child NUM: "+ greatgrand);
                                        for(int k = 0; k < greatgrand; k++){
                                            CharSequence greatgrandCont = rootNode.getChild(i).getChild(j).getChild(k).getContentDescription();
                                            if(greatgrandCont == null){
                                                continue;
                                            }
                                            Log.d("BANANA_WIN_CONTENT", " Great Grand Child Desc: "+ grandChildContentDesc);
                                        }
                                    }
                                }
                            } //Grandchild for
                            continue;
                        }
                        if(currActiveWindow.equals(homeScreen)){
                            //From the homescreen we should be able to launch 4 apps.
                            //Phone -- 0
                            //Open Camera -- 1
                            //Gmail -- 2
                            //Whatsapp -- 3
                            //Spotify -- 4
                            if(childContentDesc.toString().startsWith("Open Camera")) {
                                Log.d("BANANA_OPEN_CAM", childContentDesc.toString());
                                gestureTap = rootNode.getChild(i);
                            }
                            else if(childContentDesc.toString().startsWith("Phone")){
                                gestureDown = rootNode.getChild(i);
                            }
                            else if(childContentDesc.toString().startsWith("Gmail")){
                                gestureUp = rootNode.getChild(i);
                            }
                            else if(childContentDesc.toString().startsWith("WhatsApp")){
                                gestureLeft = rootNode.getChild(i);
                            }
                            else if(childContentDesc.toString().startsWith("Spotify")){
                                gestureRight = rootNode.getChild(i);
                            }
                        }
                        else if(currActiveWindow.equals(camera)) {
                            if (childContentDesc.toString().equals("Take Photo")) {
                                gestureTap = rootNode.getChild(i);
                            }
                            else if (childContentDesc.toString().equals("Switch to front camera")) {
                                gestureRight = rootNode.getChild(i);
                            }
                        }//if camera closing
                        else if(currActiveWindow.equals(music)){
                            //TODO: FINISH THIS
                            if(childContentDesc.toString().equals("Play") || childContentDesc.toString().equals("Pause")){
                                gestureTap = rootNode.getChild(i);
                            }
                            else if(childContentDesc.toString().equals("Show Now Playing")){
                                gestureRight = rootNode.getChild(i);
                            }
                        }
                        else if(currActiveWindow.equals(lockedScreen)){
                            if(childContentDesc.toString().startsWith("WhatsApp")){
                                nodeNotifications[1] = rootNode.getChild(i);
                                readOutNotifications[1] = "You've Unread Whatsapp Messages";
                            }
                            else if(childContentDesc.toString().startsWith("Gmail")){
                                nodeNotifications[2] = rootNode.getChild(i);
                                readOutNotifications[2] = "You've got Mail";
                            }
                            else if(childContentDesc.toString().startsWith("Phone")){
                                nodeNotifications[3] = rootNode.getChild(i);
                                readOutNotifications[3] = "You've missed calls";
                            }
                        }
                    }//closes for loop
                    break;
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    Log.d("BANANA_NOTI_CHANGED", "GET Text" + event.getText());
                    numChildren = rootNode.getChildCount();
                    Log.d("BANANA_NOTI_CHANGED:", "Number of Children: "+ numChildren);
                    readOutNotifications[0] = "Notification List";
                    readOutNotifications[1] = "No Whatsapp Notifications";
                    readOutNotifications[2] = "No Gmail  Notifications";
                    readOutNotifications[3] = "No Missed Calls";
                    for(int i = 0; i < numChildren; i++) {
                        if (rootNode.getChild(i) == null) {
                            continue;
                        }
                        CharSequence childContentDesc = rootNode.getChild(i).getContentDescription();
                        if (childContentDesc == null) {

                            continue;
                        }
                        Log.d("BANANA_NOTI_CHANGED", "Child Desc: "+ childContentDesc);
                        int numGrandChildren = rootNode.getChild(i).getChildCount();
                        Log.d("BANANA_NOTI_CHANGED:", "Number of Grand Children: "+ numGrandChildren);
                        for(int j = 0; j < numGrandChildren; j++){
                            if(rootNode.getChild(i).getChild(j) == null){
                                continue;
                            }
                            CharSequence grandChildContentDesc =rootNode.getChild(i).getChild(j).getContentDescription();
                            if(grandChildContentDesc == null) {
                                continue;
//                                    int numGrandChildren = rootNode.getChild(i).getChildCount();
                            }
                            Log.d("BANANA_NOTI_CONTENT", "Grand Child Desc: "+ grandChildContentDesc);
                        }

                        if(childContentDesc.toString().startsWith("WhatsApp")){
                            nodeNotifications[1] = rootNode.getChild(i);
                            readOutNotifications[1] = "You've Unread Whatsapp Messages";
                        }
                        else if(childContentDesc.toString().startsWith("Gmail")){
                            nodeNotifications[2] = rootNode.getChild(i);
                            readOutNotifications[2] = "You've got Mail";
                        }
                        else if(childContentDesc.toString().startsWith("Phone")){
                            nodeNotifications[3] = rootNode.getChild(i);
                            readOutNotifications[3] = "You've missed calls";
                        }
                    } // closes for loop
                    break;
            }//switch case closing
        }//root node not null
    }//AccessibilityEvent

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
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                public void onEvent(String eventName, ParticleEvent event) {
                                    Log.i("BANANA", "Received event " + eventName + " with payload: " + event.dataPayload);
                                    if (eventName.equals("GestureDetected")) {
                                        //talker.speak(notificationContent, TextToSpeech.QUEUE_FLUSH, null);
                                        Log.i("BANANA", "Got required event " + eventName + " with payload: " + event.dataPayload);
                                        if(event.dataPayload.equals("T")) {
                                            if (currActiveWindow.equals(lockedScreen)) {
                                                if(currAddress == 0) {
                                                    talker.speak(readOutNotifications[0], TextToSpeech.QUEUE_FLUSH, null);
                                                }
                                                else if(nodeNotifications[currAddress]!=null){
                                                    nodeNotifications[currAddress].performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
                                            }
                                            else{
                                                if(gestureTap != null) {
                                                    gestureTap.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
                                            }
                                        }
                                        else if(event.dataPayload.equals("R")){
                                            if(currActiveWindow.equals(lockedScreen)){
                                                currAddress++;
                                                if(currAddress > 3){
                                                    currAddress = 0;
                                                }
                                                talker.speak(readOutNotifications[currAddress], TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                            else{
                                                if(gestureRight != null) {
                                                    gestureRight.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
                                            }

                                        }
                                        else if(event.dataPayload.equals("L")){
                                            if(currActiveWindow.equals(lockedScreen)){
                                                currAddress--;
                                                if(currAddress < 0){
                                                    currAddress = 0;
                                                }
                                                talker.speak(readOutNotifications[currAddress], TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                            else{
                                                if(gestureLeft!= null) {
                                                    gestureLeft.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
                                            }

                                        }
                                        else if(event.dataPayload.equals("U")) {
                                            if(gestureUp!=null) {
                                                gestureUp.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                        }
                                        else if(event.dataPayload.equals("D")) {
                                            if(gestureDown!=null) {
                                                gestureDown.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
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
