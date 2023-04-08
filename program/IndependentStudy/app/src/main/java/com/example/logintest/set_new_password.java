package com.example.logintest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class set_new_password extends AppCompatActivity {
//密碼確認 response不應該放在裡面
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button b_new = (Button) findViewById(R.id.b_new3);
        Button back = (Button) findViewById(R.id.button19);
        EditText acc = (EditText) findViewById(R.id.acc18);
        EditText acc2 = (EditText) findViewById(R.id.acc19);
        EditText acc3 = (EditText) findViewById(R.id.acc20);
        EditText acc4 = (EditText) findViewById(R.id.acc21);

        b_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(acc.getText().toString().length() == 0||acc2.getText().toString().length() == 0||acc3.getText().toString().length() == 0||acc4.getText().toString().length() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(set_new_password.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("有輸入為空!!請確認輸入!");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                else {
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            String url = php_con.login_url+"check_forgot_password.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if(response.substring(0,3).equals("<br") || !acc3.getText().toString().equals(acc4.getText().toString())){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder alertDialog =
                                                            new AlertDialog.Builder(set_new_password.this);
                                                    alertDialog.setTitle("更改失敗!!");
                                                    alertDialog.setMessage("資料有誤!請重新輸入資料!!");
                                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) { }
                                                    });
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.show();
                                                }
                                            });
                                        }
                                        else {
                                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                            Thread thread = new Thread(new Runnable(){
                                                @Override
                                                public void run(){
                                                    //更改密碼
                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                                                    requestQueue2.add(php_con.change_forget_password(acc.getText().toString(),"NULL"));
                                                    RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                                                    requestQueue3.add(php_con.change_password(acc.getText().toString(),acc3.getText().toString()));
                                                }
                                            });
                                            thread.start();
                                            try{
                                                thread.join();
                                            }catch (InterruptedException e){
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(set_new_password.this);
                                                    alertDialog.setTitle("燈燈~!");
                                                    alertDialog.setMessage("密碼!!更改成功");
                                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //跳回retail頁面
                                                            Intent intent= new Intent();
                                                            intent.setClass(set_new_password.this, retail.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.show();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(set_new_password.this, "error", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", acc.getText().toString());
                                    data.put("key", acc2.getText().toString());
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                        }
                    }).start();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setClass(set_new_password.this,retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
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

}
