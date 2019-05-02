package com.example.ishaara;

import android.Manifest;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
//import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class MainActivity extends AppCompatActivity {

//    private String LoginID = "rishabhbrajabasi@gmail.com"; // "nchecka@andrew.cmu.edu"
//    private String password = "QsY7T249WWcSX8s"; // "Nai^pra99"
//    private long subscriptionId;
//    private ParticleDevice mDevice;
//    String gesture = "";
    TextView display;
//    int counter = 0;
//    String number = ("tel:4125964529");
    //String phnum = number;
//    public void call_action(){
//
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse(number));
//        startActivity(callIntent);
//    }

//    public  boolean isCallPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("TAG","Permission is granted");
//                return true;
//            } else {
//
//                Log.v("TAG","Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG","Permission is granted");
//            return true;
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
//                    call_action();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView) findViewById(R.id.Gesture);



//        CallReceiver call = new CallReceiver();
//
//        Intent intent = new Intent(Intent.ACTION_ANSWER);
//        call.onReceive(this, intent);
//        if(gesture.equals("<")) {
//            if (isCallPermissionGranted()) {
//                call_action();
//            }
//        }

            // Start a new thread to update the TextView
//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(500);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                Log.d("BANANA", "Gesture about to be displayed :"+gesture);
//                                display.setText(gesture);
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                    Log.d("BANANA", "Something went wrong");
//                }
//            }
//        };
//        t.start();
//

//
//        ParticleCloudSDK.init(this);
//        //login
//        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
//
//            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
//                sparkCloud.logIn(LoginID, password);
//                sparkCloud.getDevices();
//
//                try {
//                    mDevice = sparkCloud.getDevices().get(0);
//                } catch (IndexOutOfBoundsException ioEx) {
//                    throw new RuntimeException("Your account must have at least one device for this example app to work");
//                }
//                //mDevice = sparkCloud.getDevices().get(0);
//                Log.d("BANANA", "analogvalue: " + mDevice.toString());
//
//                Object obj = 1;
//                return obj;
//            }
//
//            @Override
//            public void onSuccess(@NonNull Object value) {
//                Toaster.l(MainActivity.this, "Logged in");
//            }
//
//            @Override
//            public void onFailure(@NonNull ParticleCloudException e) {
//                Toaster.l(MainActivity.this, e.getBestMessage());
//            }
//        });
//        //Subscribe to events
//        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Integer>() {
//
//            @Override
//            public Integer callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
//
//                int resultCode = 0;
//                try {
//
//                    subscriptionId = ParticleCloudSDK.getCloud().subscribeToMyDevicesEvents(
//                            null,  // the first argument, "eventNamePrefix", is optional
//                            new ParticleEventHandler() {
//                                public void onEvent(String eventName, ParticleEvent event) {
//                                    Log.i("BANANA", "Received event with payload: " + event.dataPayload);
//                                    if(eventName.matches("GestureDetected")){
//                                        Log.d("BANANA", "necessary event");
//                                        gesture = event.dataPayload;
////                                        display.setText(gesture);
//                                        counter++;
//                                    //wait = false;
//                                     }
//                                }
//
//                                public void onEventError(Exception e) {
//                                    Log.e("BANANA", "Event error: ", e);
//                                }
//                            });
//                }
//                catch(Exception e) {
//                    Log.d("BANANA", "Exception: " + mDevice.toString());
//                }
//                return resultCode;
//            }
//
//            @Override
//            public void onSuccess(@NonNull Integer value) {
//                Toaster.l(MainActivity.this, "Subscribed");
//            }
//
//            @Override
//            public void onFailure(@NonNull ParticleCloudException e) {
//                Toaster.l(MainActivity.this, e.getBestMessage());
//            }
//        });

//        Intent intent = new Intent(this, paticle.class);
//        startService(intent);

        Intent appF = new Intent(this, appFore.class);
        startService(appF);

        Intent appService = new Intent(this, AppService.class);
        startService(appService);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
//
//            public Object callApi(@NonNull ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
//                mDevice.unsubscribeFromEvents(subscriptionId);
//                sparkCloud.logOut();
//
//                Object obj = 1;
//                return obj;
//            }
//
//            @Override
//            public void onSuccess(@NonNull Object value) {
//                Toaster.l(MainActivity.this, "Logged out");
//            }
//
//            @Override
//            public void onFailure(@NonNull ParticleCloudException e) {
//                Toaster.l(MainActivity.this, e.getBestMessage());
//            }
//        });
    }
}


