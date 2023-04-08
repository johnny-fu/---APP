package com.example.logintest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class timernotifycation_retailer extends BroadcastReceiver {
    boolean send=false;
    @Override
    public void onReceive(Context context, Intent intent) {

        new Thread(new Runnable(){
            @Override
            public void run() {
                if(send==false) {
                    String url = "http://163.21.235.176/login/check_transaction.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        private String TAG = "123123123";
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (!response.substring(0, 2).equals("<b")) {
                                    send = true;
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        Log.d(TAG, "有訂單囉");
                                        NotificationChannel channel = new NotificationChannel("1",
                                                "商家",
                                                NotificationManager.IMPORTANCE_DEFAULT);
                                        channel.setDescription("商家得到訂單");
                                        mNotificationManager.createNotificationChannel(channel);
                                    }
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1")
                                            .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                            .setContentTitle("有新訂單") // title for notification
                                            .setContentText("請盡快確認您的訂單!")// message for notification
                                            .setAutoCancel(true); // clear notification after click
                                    Intent intent2 = new Intent(context, start.class);
                                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                                    mBuilder.setContentIntent(pi);
                                    mNotificationManager.notify(0, mBuilder.build());
                                    //----------
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("name", login.account);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }}).start();

        //----------
        Intent i = new Intent(context, myservice_retailer.class);
        context.startService(i);
    }
}