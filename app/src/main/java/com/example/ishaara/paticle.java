package com.example.ishaara;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;


public class paticle extends Service {

    private String LoginID = "rishabhbrajabasi@gmail.com"; // "nchecka@andrew.cmu.edu"
    private String password = "QsY7T249WWcSX8s"; // "Nai^pra99"
    private long subscriptionId;
    private ParticleDevice mDevice;


    public paticle() {

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


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
                //mDevice = sparkCloud.getDevices().get(0);
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
                                    Log.i("BANANA", "Received event with payload: " + event.dataPayload);
                                    if(eventName.matches("GestureDetected")){
                                        Log.d("BANANA", "necessary event");
//                                        gesture = event.dataPayload;
//                                        display.setText(gesture);
//                                        counter++;
                                        //wait = false;
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



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
