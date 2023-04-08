package com.example.logintest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;

public class transaction_client extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_client);
        RadioGroup rg = (RadioGroup) findViewById(R.id.tradetype);
        rg.setOnCheckedChangeListener(radGrpRegionOnCheckedChange);
        rg.check(R.id.tran_order);
        TextView username = (TextView) findViewById(R.id.textView7);
        TextView userpocket = (TextView) findViewById(R.id.textView9);
        Thread catch_profile_acc = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(php_con.catch_profile_acc());
            }
        });//抓資料
        catch_profile_acc.start();
        try {
            catch_profile_acc.join();
            username.setText(login.name+" 您好!");
            userpocket.setText(" 錢包:" + login.money);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button button5 = (Button) findViewById(R.id.button);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(transaction_client.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    private RadioGroup.OnCheckedChangeListener radGrpRegionOnCheckedChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            TextView username = (TextView) findViewById(R.id.textView7);
            TextView userpocket = (TextView) findViewById(R.id.textView9);
            switch (checkedId) {
                case R.id.tran_order: //case mRadioButton0.getId():
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = php_con.login_url+"search_transaction_acc.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        System.out.println("aa");
                                        System.out.println(response);
                                        if (!response.substring(0, 2).equals("<b")) {
                                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                            String[] retailer = new String[jsonArray.length()];
                                            String[] expend = new String[jsonArray.length()];
                                            String[] transaction = new String[jsonArray.length()];
                                            String[] date = new String[jsonArray.length()];
                                            String[] id = new String[jsonArray.length()];
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                id[i] = String.valueOf(jsonObject.getInt("id_transaction"));
                                                retailer[i] = jsonObject.getString("retailer");
                                                expend[i] = String.valueOf(jsonObject.getInt("expend"));
                                                transaction[i] = jsonObject.getString("transaction_message");
                                                date[i] = jsonObject.getString("date");
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    username.setText("使用者:" + login.name);
                                                    userpocket.setText("錢包" + login.money);
                                                    WordAdapter_transaction adapter = new WordAdapter_transaction(retailer, expend, transaction, date);
                                                    ListView list = (ListView) findViewById(R.id.listview2);
                                                    list.setAdapter(adapter);
                                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                            if (retailer[i].equals("not")) {//還沒被確定{
                                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(transaction_client.this);
                                                                alertDialog.setTitle("注意");
                                                                alertDialog.setMessage("你要取消這個訂單嗎?");
                                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Thread thread = new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                                                requestQueue.add(php_con.remove_transaction_client(Integer.parseInt(id[i])));
                                                                            }
                                                                        });
                                                                        thread.start();
                                                                        try {
                                                                            // Thread B 加入 Thread A
                                                                            thread.join();
                                                                        } catch (InterruptedException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        restart();
                                                                    }
                                                                });
                                                                alertDialog.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                                alertDialog.setCancelable(false);
                                                                alertDialog.show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }else if(response.contains("null")){
                                            String[] retailer = new String[1];
                                            retailer[0]="";
                                            String[] expend = new String[1];
                                            expend[0]="";
                                            String[] transaction = new String[1];
                                            transaction[0]="";
                                            String[] date = new String[1];
                                            date[0]="沒有資料";
                                            String[] id = new String[1];
                                            id[0]="";
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    username.setText("使用者:" + login.name);
                                                    userpocket.setText("錢包" + login.money);
                                                    WordAdapter_transaction adapter = new WordAdapter_transaction(retailer, expend, transaction, date);
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
                                    Toast.makeText(transaction_client.this, "error", Toast.LENGTH_LONG).show();
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
                    break;
                case R.id.tran_des: //case mRadioButton1.getId():
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = php_con.login_url+"search_deposit_acc.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if (!response.substring(0, 2).equals("<b")) {
                                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                            String[] retailer = new String[jsonArray.length()];
                                            String[] transaction = new String[jsonArray.length()];
                                            String[] date = new String[jsonArray.length()];
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                retailer[i] = String.valueOf(jsonObject.getString("account_retailer"));
                                                transaction[i] = String.valueOf(jsonObject.getString("stored_money"));
                                                date[i] = String.valueOf(jsonObject.getString("date"));
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    username.setText("使用者:" + login.name);
                                                    userpocket.setText("錢包" + login.money);
                                                    WordAdapter_desposit adapter = new WordAdapter_desposit(retailer, transaction, date);
                                                    ListView list = (ListView) findViewById(R.id.listview2);
                                                    list.setAdapter(adapter);
                                                }
                                            });
                                        }else if(response.contains("null")){
                                            String[] retailer = new String[1];
                                            retailer[0]="";
                                            String[] transaction = new String[1];
                                            transaction[0]="";
                                            String[] date = new String[1];
                                            date[0]="沒有資料";
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    username.setText("使用者:" + login.name);
                                                    userpocket.setText("錢包" + login.money);
                                                    WordAdapter_desposit adapter = new WordAdapter_desposit(retailer, transaction, date);
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
                                    Toast.makeText(transaction_client.this, "error", Toast.LENGTH_LONG).show();
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
                    break;
            }
        }
    };

    protected void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}