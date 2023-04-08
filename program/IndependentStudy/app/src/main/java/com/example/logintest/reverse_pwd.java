package com.example.logintest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Timer;
import java.util.TimerTask;

public class reverse_pwd extends AppCompatActivity {
    //最下面多加寄送email;
    int code;

    private static long lastclicktime;
    //login.email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_pwd);
        Button b_new = (Button) findViewById(R.id.b_new);
        Button b_send = (Button) findViewById(R.id.b_send);
        Button b_reverse_pwd = (Button) findViewById(R.id.b_reverse_pwd);
        Button back = (Button) findViewById(R.id.button9);
        Button back2 = (Button) findViewById(R.id.button10);
        Button back3 = (Button) findViewById(R.id.button11);
        Button b_reotp = (Button) findViewById(R.id.b_re_otp);
        EditText acc = (EditText) findViewById(R.id.acc);
        EditText emailotp = (EditText) findViewById(R.id.emailotp);
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText pass_two = (EditText) findViewById(R.id.pass_two);

        b_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfastclick2()) {
                    login.account = acc.getText().toString();
                    //判斷有沒有錯誤
                    if (acc.getText().toString().length() < 4) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                        alertDialog.setTitle("燈燈");
                        alertDialog.setMessage("帳號不能小於四碼!!請確認輸入!");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    } else if (acc.getText().toString().length() > 45) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                        alertDialog.setTitle("燈燈");
                        alertDialog.setMessage("帳號不能大於四十五碼!!請確認輸入!");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    } else {//正確
                        Thread th1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Random rand = new Random();
                                code = rand.nextInt(8999) + 1000;
                                String url = php_con.login_url + "reverse_pwd.php";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            if (response.contains("output")) {
                                                findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                                                findViewById(R.id.background2).setVisibility(View.VISIBLE);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                                                        findViewById(R.id.background2).setVisibility(View.GONE);
                                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
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
                                            } else if (response.contains("Error!")) {
                                                findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                                                findViewById(R.id.background2).setVisibility(View.VISIBLE);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                                                        findViewById(R.id.background2).setVisibility(View.GONE);
                                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                        alertDialog.setTitle("燈燈");
                                                        alertDialog.setMessage("請檢查網路!");
                                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        });
                                                        alertDialog.setCancelable(false);
                                                        alertDialog.show();
                                                    }
                                                });
                                            } else if (response.contains("Successfully")) {

                                                JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                login.email = jsonObject.getString("email");
                                                sendVerifyEmail(view);
//                                                b_new.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//                                                        if(isfastclick2()) {
//                                                            findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
//                                                            findViewById(R.id.background2).setVisibility(View.GONE);
//                                                            sendVerifyEmail(view);
//                                                        }else{
//                                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
//                                                            alertDialog.setTitle("燈燈");
//                                                            alertDialog.setMessage("請過5秒後再按");
//                                                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                                                                public void onClick(DialogInterface dialog, int which) {
//                                                                }
//                                                            });
//                                                            alertDialog.setCancelable(false);
//                                                            alertDialog.show();
//                                                        }
//                                                    }
//                                                });
//                                            } else{
//                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
//                                                alertDialog.setTitle("燈燈");
//                                                alertDialog.setMessage("請過5秒後再按");
//                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                    }
//                                                });
//                                                alertDialog.setCancelable(false);
//                                                alertDialog.show();
                                            }
                                            System.out.println(response);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(reverse_pwd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("account", acc.getText().toString());
                                        params.put("forget_password", String.valueOf(code));
                                        return params;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            }
                        });
                        th1.start();
                        findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                        findViewById(R.id.background2).setVisibility(View.VISIBLE);
                    }
                    SystemClock.sleep(1000);
                }
            }
        });

        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailotp.getText().toString().length()==0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("請輸入驗證碼!");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }else if(emailotp.getText().toString().length()>4){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("驗證碼錯誤!");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }else {
                    int otp = Integer.parseInt(emailotp.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = php_con.login_url + "catch_profile_acc.php";
                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //System.out.println(response);
                                        if (response.substring(0, 3).equals("<br")) {
                                            findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                                            findViewById(R.id.background2).setVisibility(View.GONE);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                    alertDialog.setTitle("燈燈");
                                                    alertDialog.setMessage("沒收到任何訊息QQ!");
                                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.show();
                                                }
                                            });
                                        } else {
                                            findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                                            findViewById(R.id.background2).setVisibility(View.GONE);
                                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            int temp = jsonObject.getInt("forget_password");
                                            if (temp == otp) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                        alertDialog.setTitle("燈燈~!");
                                                        alertDialog.setMessage("正確!!\n請更改密碼~");
                                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                findViewById(R.id.box2).setVisibility(View.GONE);
                                                                findViewById(R.id.box3).setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        alertDialog.setCancelable(false);
                                                        alertDialog.show();
                                                    }
                                                });
                                            } else {
                                                findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                                                findViewById(R.id.background2).setVisibility(View.GONE);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                        alertDialog.setTitle("燈燈");
                                                        alertDialog.setMessage("憑證不正確!!");
                                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                                        alertDialog.setCancelable(false);
                                                        alertDialog.show();
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(reverse_pwd.this, "error", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", acc.getText().toString());
                                    return data;
                                }
                            };
                            RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                            requestQueue2.add(stringRequest2);
                        }
                    }).start();
                    findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                    findViewById(R.id.background2).setVisibility(View.VISIBLE);
                }
            }
        });
        b_reverse_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().length() < 4 || pass_two.getText().toString().length() < 4) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("密碼不能小於四碼!!請確認輸入!");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else if (pass.getText().toString().length() > 45 || pass_two.getText().toString().length() > 45) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("密碼不能大於四十五碼!!請確認輸入!");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else if (!pass.getText().toString().equals(pass_two.getText().toString())) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("請確認密碼一致!!請確認輸入!");
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
                            String url = php_con.login_url + "reverse_pwd2.php";
                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.substring(0, 3).equals("<br")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                alertDialog.setTitle("燈燈");
                                                alertDialog.setMessage("沒收到任何訊息QQ!");
                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                alertDialog.setCancelable(false);
                                                alertDialog.show();
                                            }
                                        });
                                    } else if (response.contains("Error")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                alertDialog.setTitle("燈燈");
                                                alertDialog.setMessage("請檢察網路!");
                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                alertDialog.setCancelable(false);
                                                alertDialog.show();
                                            }
                                        });
                                    } else if (response.contains("Successfully")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                alertDialog.setTitle("燈燈");
                                                alertDialog.setMessage("成功更改密碼!請重新登入");
                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent();
                                                        intent.setClass(reverse_pwd.this, login.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }
                                                });
                                                alertDialog.setCancelable(false);
                                                alertDialog.show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
                                                alertDialog.setTitle("燈燈");
                                                alertDialog.setMessage("未知錯誤!請重啟程式");
                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                });
                                                alertDialog.setCancelable(false);
                                                alertDialog.show();
                                            }
                                        });
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(reverse_pwd.this, "error", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("account", acc.getText().toString());
                                    data.put("password", pass.getText().toString());
                                    return data;
                                }
                            };
                            RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                            requestQueue2.add(stringRequest2);
                        }
                    }).start();
                }

            }
        });
        b_reotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isfastclick2()) {
                    findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                    findViewById(R.id.background2).setVisibility(View.GONE);
                    sendVerifyEmail(view);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
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
                intent.setClass(reverse_pwd.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(reverse_pwd.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(reverse_pwd.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

//    public void trigger(View view) {
//        if(isfastclick2()) {
//            findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
//            findViewById(R.id.background2).setVisibility(View.GONE);
//            sendVerifyEmail(view);
//        }else{
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(reverse_pwd.this);
//            alertDialog.setTitle("燈燈");
//            alertDialog.setMessage("請過5秒後再按");
//            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            alertDialog.setCancelable(false);
//            alertDialog.show();
//        }
//    }

    public static boolean isfastclick(){
        boolean flag =false;
        long curclicktime = System.currentTimeMillis();
        final  int click_delaytime = 1000;
        if((curclicktime - lastclicktime) >= click_delaytime){
            flag = true;
        }
        lastclicktime = curclicktime;
        return  flag;
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

    // email
    public void sendVerifyEmail(View view) {
        //EditText emailTXT = findViewById(R.id.email);
        String url = "http://163.21.245.178/login/sendEmail.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Toast.makeText(reverse_pwd.this, response, Toast.LENGTH_SHORT).show();
                findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                findViewById(R.id.background2).setVisibility(View.GONE);
                findViewById(R.id.box1).setVisibility(View.GONE);
                findViewById(R.id.box2).setVisibility(View.VISIBLE);
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(reverse_pwd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", login.email);
                params.put("code", String.valueOf(code));
                return params;
            }
        };
        queue.add(request);
    }

}
