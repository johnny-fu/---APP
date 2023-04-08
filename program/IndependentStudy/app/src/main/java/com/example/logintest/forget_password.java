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

import androidx.annotation.Nullable;
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
import java.util.Random;

public class forget_password extends AppCompatActivity {

    int code;//憑證
    private static long lastclicktime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Button b_new = (Button) findViewById(R.id.b_new2);
        Button back = (Button) findViewById(R.id.button16);
        EditText acc = (EditText) findViewById(R.id.acc2);

        b_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acc.getText().toString().length() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(forget_password.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("帳號輸入為空");
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
                            Random rand = new Random();
                            code = rand.nextInt(8999) + 1000;
                            String url = "http://163.21.235.176/login/catch_profile_email.php";

                            //String url = "http://163.21.245.178/login/sendEmail.php";
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("Error!")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(forget_password.this);
                                                alertDialog.setTitle("燈燈");
                                                alertDialog.setMessage("沒有此帳號喔!");
                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                });
                                                alertDialog.setCancelable(false);
                                                alertDialog.show();
                                            }
                                        });
                                    }
                                    else if(response.equals("Successfully")){

                                        //跳到輸入憑證
                                        //送出email憑證

                                    }

                                    System.out.println(response);
                                }


                                // System.out.println("Here~");

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(forget_password.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("account", acc.toString());
                                    params.put("forget_password", String.valueOf(code));
                                    return params;
                                }
                            };
                            queue.add(request);



                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if (response.substring(0, 3).equals("<br")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder alertDialog =
                                                            new AlertDialog.Builder(forget_password.this);
                                                    alertDialog.setTitle("燈燈");
                                                    alertDialog.setMessage("無此帳號!!請重新輸入!!");
                                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.show();
                                                }
                                            });
                                        } else {
                                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder alertDialog =
                                                            new AlertDialog.Builder(forget_password.this);
                                                    alertDialog.setTitle("鑰匙憑證");
                                                    //alertDialog.setMessage("請取此鑰匙 :" + key + " \n請至信箱收信");
                                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                                                                    //requestQueue2.add(php_con.change_forget_password(acc.getText().toString(), String.valueOf(key)));
                                                                }
                                                            }).start();
                                                            Intent intent = new Intent();
                                                            intent.setClass(forget_password.this, login.class);
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
                                    Toast.makeText(forget_password.this, "error", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", acc.getText().toString());
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //將 錢的訊息改成0
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(php_con.change_message_money("0"));
                        }
                    }).start();
                }

                if(isfastclick2()) {
                    findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                    findViewById(R.id.background2).setVisibility(View.GONE);
                    sendVerifyEmail(view);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(forget_password.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("請過5秒後再按");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(forget_password.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    //收鍵盤
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用???的?候
            View v = getCurrentFocus();      //得到?前?面的焦?,ps:有?入框的?面焦?一般?被?入框占据
            if (isShouldHideKeyboard(v, me)) { //判?用???的是否是?入框以外的?域
                hideKeyboard(v.getWindowToken());   //收起??
            }
        }
        return super.dispatchTouchEvent(me);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判?得到的焦?控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到?入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // ??位置如果是EditText的?域，忽略它，不收起??。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦?不是EditText?忽略
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void sendVerifyEmail(View view) {
        EditText emailTXT = findViewById(R.id.email);
        String url = "http://163.21.245.178/login/sendEmail.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(forget_password.this, response, Toast.LENGTH_SHORT).show();
                findViewById(R.id.box1).setVisibility(View.GONE);
                findViewById(R.id.box2).setVisibility(View.VISIBLE);
                System.out.println(response);
                // System.out.println("Here~");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(forget_password.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailTXT.getText().toString());
                params.put("code", String.valueOf(code));
                return params;
            }
        };
        queue.add(request);
    }

    public static boolean isfastclick2(){
        boolean flag =false;
        long curclicktime = System.currentTimeMillis();
        final  int click_delaytime = 5000;
        if((curclicktime - lastclicktime) >= click_delaytime){
            flag = true;
        }
        lastclicktime = curclicktime;
        return  flag;
    }

}
