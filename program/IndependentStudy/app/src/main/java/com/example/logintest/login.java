package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private EditText acc, pass;
    static int id, money,notice_judge=0;
    static String account, name,password,message_money,order_day,account_owner,email,otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent(login.this, myservice_retailer.class);
        stopService(intent);
        Intent intent2 = new Intent(login.this, timernotifycation_retailer.class);
        stopService(intent2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        notice_judge=0;
        acc = (EditText) findViewById(R.id.editTextTextPersonName);
        pass = (EditText) findViewById(R.id.editTextTextPassword);
        SharedPreferences text =  getApplicationContext().getSharedPreferences("autologin", MODE_PRIVATE);
        CheckBox autologin = (CheckBox) findViewById(R.id.checkBox);
        Intent intent3 = new Intent();
 
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(login.this);
                    alertDialog.setTitle("警告!!!!!");
                    alertDialog.setMessage("你將在此裝置上!\n存取您的帳號密碼");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }else{
                }
            }
        });
    }

    //登入
    public void perform_action_2(View v) {
        Intent intent = new Intent();
        CheckBox autologin = (CheckBox) findViewById(R.id.checkBox);
        SharedPreferences text = getSharedPreferences("autologin", MODE_PRIVATE);
        SharedPreferences.Editor editor = text.edit();
        if (acc.getText().length() == 0 || pass.getText().length() == 0) {
            AlertDialog.Builder alertDialog =
                    new AlertDialog.Builder(login.this);
            alertDialog.setTitle("燈燈");
            alertDialog.setMessage("請輸入帳號跟密碼");
            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String temp = acc.getText().toString();
                    String temp2 = pass.getText().toString();
                    //連線
                    String url = php_con.login_url+"login.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println(response);
                                if(response.contains("output")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(login.this);
                                            alertDialog.setTitle("燈燈");
                                            alertDialog.setMessage("帳號或密碼錯誤!");
                                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                            alertDialog.setCancelable(false);
                                            alertDialog.show();
                                        }
                                    });
                                }
                                else {
                                    JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    Integer id_profile = jsonObject.getInt("id_profile");
                                    String account2 = jsonObject.getString("account");
                                    String password2 = jsonObject.getString("password");
                                    String name = jsonObject.getString("name");
                                    Integer pocket = jsonObject.getInt("pocket");
                                    String account_owner = jsonObject.getString("account_owner");
                                    String message_money = jsonObject.getString("message_money");
                                    if (temp.trim().equals(account2)) {
                                        login.id = id_profile;
                                        login.account = account2;
                                        login.name = name;
                                        login.money = pocket;
                                        start.my_wallet=pocket;
                                        login.password = password2;
                                        login.message_money = message_money;
                                        login.account_owner = account_owner;
                                        if(autologin.isChecked()){
                                            editor.clear().commit();
                                            editor.putString("account", acc.getText().toString())
                                                    .putString("password",pass.getText().toString())
                                                    .commit();
                                        }else{
                                            editor.clear().commit();
                                        }
                                        if(login.account_owner.equals("root")) {//retailer
                                            intent.setClass(login.this, retail.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }else{//users
                                            intent.setClass(login.this, client_login.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, error -> runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(login.this);
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
                            data.put("name", acc.getText().toString());
                            data.put("pass", pass.getText().toString());
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }).start();
        }
    }
    //首次登入
    public void perform_action_3(View v)
    {
        Intent intent = new Intent();
        intent.setClass(login.this  , first_login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //忘記密碼
    public void perform_action(View v)
    {
        Intent intent = new Intent();
        intent.setClass(login.this, reverse_pwd.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //收鍵盤
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
