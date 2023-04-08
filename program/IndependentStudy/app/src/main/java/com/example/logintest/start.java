package com.example.logintest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class start extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView imageView;
    private Context context;
    static int my_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = this;
        imageView = findViewById(R.id.imageView10);
        //imageView 設定動畫元件(透明度調整)
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setAnimationListener(this);
        imageView.setAnimation(animation);
        SharedPreferences text = getSharedPreferences("autologin", MODE_PRIVATE);
        SharedPreferences.Editor editor = text.edit();
        Intent intent = new Intent();
        if (text.getString("account", "").equals("")) {//沒有帳號的話
        } else {//有自動登入
            String temp = text.getString("account", null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //連線
                    String url = "http://163.21.235.176/login/login.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                Integer id_profile = jsonObject.getInt("id_profile");
                                String account2 = jsonObject.getString("account");
                                String password2 = jsonObject.getString("password");
                                String name = jsonObject.getString("name");
                                Integer pocket = jsonObject.getInt("pocket");
                                String account_owner = jsonObject.getString("account_owner");
                                String message_money = jsonObject.getString("message_money");
                                login.id = id_profile;
                                login.account = account2;
                                login.name = name;
                                login.money = pocket;
                                login.password = password2;
                                login.message_money = message_money;
                                login.account_owner = account_owner;
                                my_wallet = pocket;
                                if (temp.charAt(0) == 'u') {//users
                                    intent.setClass(start.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if (temp.substring(0, 5).equals("store")) {//retailer
                                    intent.setClass(start.this, retail.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, error -> runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(start.this);
                            alertDialog.setTitle("無法連線");
                            alertDialog.setMessage("請確認網路，或通知工作人員!");
                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    })) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("name", text.getString("account", null));
                            data.put("pass", text.getString("password", null));
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }).start();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startActivity(new Intent(context, login.class));
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

}
