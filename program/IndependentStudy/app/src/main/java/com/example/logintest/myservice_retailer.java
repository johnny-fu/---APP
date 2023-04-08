package com.example.logintest;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;


public class myservice_retailer extends Service{

    @Override
    public IBinder onBind(Intent arg0) {
// TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String TAG="123123";
        Log.d(TAG, "開跑囉");
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int Minutes = 30*1000;
        //SystemClock.elapsedRealtime()表示1970年1月1日0點至今所經歷的時間
        long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
//此處設定開啟AlarmReceiver這個Service
        Intent i = new Intent(myservice_retailer.this, timernotifycation_retailer.class);
        PendingIntent pi = PendingIntent.getBroadcast(myservice_retailer.this, 0, i, 0);
//ELAPSED_REALTIME_WAKEUP表示讓定時任務的出發時間從系統開機算起，並且會喚醒CPU。
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Service start", Toast.LENGTH_SHORT).show();
        //----------
        //showNotification("有新訂單!","快速確認您的訂單");
        //----------
    }
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Service stop", Toast.LENGTH_SHORT).show();
    }
}
