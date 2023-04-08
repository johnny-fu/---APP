package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView username, userpocket;
    String account_owner, message_money, message_transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.textView10);
        userpocket = (TextView) findViewById(R.id.textView12);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = php_con.login_url+"login.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            login.id = jsonObject.getInt("id_profile");
                            login.account = jsonObject.getString("account");
                            login.password = jsonObject.getString("password");
                            login.name = jsonObject.getString("name");
                            login.money = jsonObject.getInt("pocket");
                            login.message_money = jsonObject.getString("message_money");
                            message_transaction = jsonObject.getString("message_transaction");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    username.setText(login.name+" 您好!");
                                    userpocket.setText(" 錢包:" + login.money);
                                }
                            });
                            if (Integer.parseInt(login.message_money) > 0) {
                                int cost = Integer.parseInt(login.message_money);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                        alertDialog.setTitle("儲值完成!!");
                                        alertDialog.setMessage("您的錢包新增了" + cost + "目前有" + login.money);
                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //將 錢的訊息改成0
                                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                        requestQueue.add(php_con.change_message_money("0"));
                                                    }
                                                }).start();
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.setCancelable(false);
                                        alertDialog.show();
                                    }
                                });
                            }
                            if (message_transaction.equals("NO")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                        alertDialog.setTitle("燈燈!!!!");
                                        alertDialog.setMessage("您的訂單已被拒絕請重新下訂!!");
                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                        requestQueue.add(php_con.change_message_transaction_context());
                                                    }
                                                }).start();
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.setCancelable(false);
                                        alertDialog.show();
                                    }
                                });
                            } else if (message_transaction.equals("OK")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                        alertDialog.setTitle("燈燈!!!!");
                                        alertDialog.setMessage("您的訂單已通過審核!!\n請準時取餐!");
                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                        requestQueue.add(php_con.change_message_transaction_context());
                                                    }
                                                }).start();
                                                dialog.dismiss();
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
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("name", login.account);
                        data.put("pass", login.password);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }).start();//抓資料666
        //取餐前一天跟當天顯示通知
        //日期還沒用完
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = php_con.login_url+"search_transaction_acc.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            String date;
                            Calendar mCal = Calendar.getInstance();
                            CharSequence today = DateFormat.format("yyyy-MM-dd", mCal.getTime());
                            mCal.add(Calendar.DATE, 1);
                            CharSequence tomorrow = DateFormat.format("yyyy-MM-dd", mCal.getTime());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                date = jsonObject.getString("date");
                                if (date.equals(today.toString()) && login.notice_judge == 0) {
                                    login.notice_judge = 1;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                            alertDialog.setTitle("燈燈!!!!");
                                            alertDialog.setMessage("提醒你!今天記得取餐!");
                                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            alertDialog.setCancelable(false);
                                            alertDialog.show();
                                        }
                                    });
                                } else if (date.equals(tomorrow.toString()) && login.notice_judge == 0) {
                                    login.notice_judge = 1;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                            alertDialog.setTitle("燈燈!!!!");
                                            alertDialog.setMessage("提醒你!明天有您預定的餐點!");
                                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
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
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("name", login.account);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }).start();

        Button button_1 = (Button) findViewById(R.id.button_1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, order.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
            }
        });
        Button button_2 = (Button) findViewById(R.id.button2);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, transaction_client.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
            }
        });
        Button button_3 = (Button) findViewById(R.id.button3);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    protected void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}