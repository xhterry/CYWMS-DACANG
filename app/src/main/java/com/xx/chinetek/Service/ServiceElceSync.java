package com.xx.chinetek.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xx.chinetek.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GHOST on 2017/7/10.
 */

public class ServiceElceSync extends Service {

   // private MyThread myThread;
   // private boolean flag = true;

    private Timer timer;
    private TimerTask task;
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("oncreate()");
        final Intent intent = new Intent();
        intent.setAction(BaseActivity.ACTION_UPDATEUI);

        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                intent.putExtra("count", ++count);
                sendBroadcast(intent);
            }
        };
        timer.schedule(task, 1000, 1000);


//        this.myThread = new MyThread();
//        this.myThread.start();
//        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

//    private class MyThread extends Thread {
//        @Override
//        public void run() {
//            while (flag) {
//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
