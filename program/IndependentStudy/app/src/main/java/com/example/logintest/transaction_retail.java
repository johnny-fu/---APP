package com.example.logintest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class transaction_retail extends AppCompatActivity {
    static int t_order, t_des, d_all, d_today, d_yes;
    private TextView income;
    String in1 = "";
    int in2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_retail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        income = (TextView) findViewById(R.id.income);
        income.setText("");
        RadioGroup rg_trade = (RadioGroup) findViewById(R.id.tradetype);
        RadioButton order = (RadioButton) findViewById(R.id.tran_order);
        RadioButton des = (RadioButton) findViewById(R.id.tran_des);
        rg_trade.setOnCheckedChangeListener(radGrpRegionOnCheckedChange);
        rg_trade.check(R.id.tran_order);

        RadioGroup rg_day = (RadioGroup) findViewById(R.id.daytype);
        RadioButton all = (RadioButton) findViewById(R.id.day_all);
        RadioButton today = (RadioButton) findViewById(R.id.day_today);
        RadioButton yesterday = (RadioButton) findViewById(R.id.day_yes);
        rg_day.setOnCheckedChangeListener(radGrpRegionOnCheckedChange2);
        rg_day.check(R.id.day_all);
        //依選取項目顯示不同訊息
        TextView username = (TextView) findViewById(R.id.textView7);
        // TextView userpocket = (TextView) findViewById(R.id.textView9);
        username.setText("使用者:" + login.name);
        // userpocket.setText("錢包:" + login.money);
        Button button5 = (Button) findViewById(R.id.button);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(transaction_retail.this, retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        vshow();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public RadioGroup.OnCheckedChangeListener radGrpRegionOnCheckedChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.tran_order: //case mRadioButton0.getId():
                    t_order = 1;
                    t_des = 0;
                    break;
                case R.id.tran_des: //case mRadioButton1.getId():
                    t_order = 0;
                    t_des = 1;
                    break;
            }
            vshow();
        }
    };

    public RadioGroup.OnCheckedChangeListener radGrpRegionOnCheckedChange2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.day_all:
                    d_all = 1;
                    d_today = 0;
                    d_yes = 0;
                    break;
                case R.id.day_today:
                    d_all = 0;
                    d_today = 1;
                    d_yes = 0;
                    break;
                case R.id.day_yes:
                    d_all = 0;
                    d_today = 0;
                    d_yes = 1;
                    break;
            }
            vshow();
        }
    };

    protected void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void vshow() {
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
        if (t_order == 1 && d_all == 1) {
            show_transaction("t_order", "d_all", s.toString());
        } else if (t_order == 1 && d_today == 1) {
            show_transaction("t_order", "d_today", s.toString());
        } else if (t_order == 1 && d_yes == 1) {
            mCal.add(Calendar.DATE, -1);
            s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
            show_transaction("t_order", "d_yes", s.toString());
        } else if (t_des == 1 && d_all == 1) {
            show_depoit("t_des", "d_all", s.toString());
        } else if (t_des == 1 && d_today == 1) {
            show_depoit("t_des", "d_today", s.toString());
        } else if (t_des == 1 && d_yes == 1) {
            mCal.add(Calendar.DATE, -1);
            s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
            show_depoit("t_des", "d_yes", s.toString());
        }
    }

    public void show_transaction(String judge_t, String judge_d, String day) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = php_con.login_url+"search_transaction_retail.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            if(response.substring(0,3).equals("<br")){
                                String[] retailer = new String[0];
                                String[] expend = new String[0];
                                String[] transaction = new String[0];
                                String[] date_s = new String[0];
                                String[] date_e = new String[0];
                                String[] id = new String[0];
                                String[] client = new String[0];
                                String[] bad = new String[0];
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WordAdapter_transaction_retailer adapter = new WordAdapter_transaction_retailer(retailer, expend, transaction, date_s, date_e,bad);
                                        ListView list = (ListView) findViewById(R.id.listview2);
                                        list.setAdapter(adapter);
                                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                JSONArray jsonArray = new JSONArray(response);
                                String[] retailer = new String[jsonArray.length()];
                                String[] expend = new String[jsonArray.length()];
                                String[] transaction = new String[jsonArray.length()];
                                String[] date_s = new String[jsonArray.length()];
                                String[] date_e = new String[jsonArray.length()];
                                String[] id = new String[jsonArray.length()];
                                String[] client = new String[jsonArray.length()];
                                String[] bad = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    id[i] = String.valueOf(jsonObject.getInt("id_transaction"));
                                    retailer[i] = jsonObject.getString("retailer");
                                    expend[i] = String.valueOf(jsonObject.getInt("expend"));
                                    transaction[i] = jsonObject.getString("transaction_message");
                                    date_s[i] = jsonObject.getString("date");
                                    date_e[i] = jsonObject.getString("order_date");
                                    client[i] = jsonObject.getString("client");
                                    bad[i] = jsonObject.getString("baddraw");
                                    if(bad[i].equals("null")){
                                        bad[i]="無";
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WordAdapter_transaction_retailer adapter = new WordAdapter_transaction_retailer(retailer, expend, transaction, date_s, date_e,bad);
                                        ListView list = (ListView) findViewById(R.id.listview2);
                                        list.setAdapter(adapter);
                                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                reduce(retailer,id,client,i,expend[i],transaction[i],date_s[i],date_e[i],bad[i]);
                                            }
                                        });
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
                        Toast.makeText(transaction_retail.this, "error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("judge_t", judge_t);
                        data.put("judge_d", judge_d);
                        data.put("day", day);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }).start();
    }

    public void show_depoit(String judge_t, String judge_d, String day) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://163.21.235.176/login/search_transaction_retail.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.substring(0,3).equals("<br")){
                                String[] retailer = new String[0];
                                String[] expend = new String[0];
                                String[] date = new String[0];
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WordAdapter_desposit adapter = new WordAdapter_desposit(retailer, expend, date);
                                        ListView list = (ListView) findViewById(R.id.listview2);
                                        list.setAdapter(adapter);
                                    }
                                });
                            }
                            else {
                                JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                String[] retailer = new String[jsonArray.length()];
                                String[] expend = new String[jsonArray.length()];
                                String[] date = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    retailer[i] = jsonObject.getString("account");
                                    expend[i] = jsonObject.getString("stored_money");
                                    date[i] = jsonObject.getString("date");
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WordAdapter_desposit adapter = new WordAdapter_desposit(retailer, expend, date);
                                        ListView list = (ListView) findViewById(R.id.listview2);
                                        list.setAdapter(adapter);
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
                        Toast.makeText(transaction_retail.this, "error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("judge_t", judge_t);
                        data.put("judge_d", judge_d);
                        data.put("day", day);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }).start();
    }
    public void reduce(String[] retailer,String[] id ,String[] client,int i,String expend,String transaction,String date_s,String date_e,String bad){
        if (retailer[i].equals("not")) {//還沒被確定
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(transaction_retail.this);
            alertDialog.setTitle("你確定要接受這個訂單嗎?");
            alertDialog.setMessage("訂單訊息\n");
            alertDialog.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            restart();
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(php_con.change_detailer_message_transactionr(client[i],Integer.parseInt(id[i]), 1));

                        }
                    }).start();
                }
            });
            alertDialog.setNegativeButton("婉拒!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            restart();
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(php_con.change_detailer_message_transactionr(client[i],Integer.parseInt(id[i]), 0));
                        }
                    }).start();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {//顯示詳情
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(transaction_retail.this);
            alertDialog.setTitle("訂單訊息");
            alertDialog.setMessage("\n\n訂單狀況:"+retailer[i]+"\n取餐日期:"+date_s+"\n訂購日期:"+date_e+"\n餐點:"+transaction+"\n金額:"+expend+"\n備註:\n"+bad);
            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();

        }
    }
}