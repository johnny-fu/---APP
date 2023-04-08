package com.example.logintest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


public class new_account extends AppCompatActivity {
    private ListView pressureList;
    private Button back;
    int temp2 = 0;
    static String[] account, name, sendtext1, sendtext2, sendtext_date1, sendtext_date2;
    static int[] judge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        back = (Button) findViewById(R.id.button17);
        EditText acc = (EditText) findViewById(R.id.search_acc);
        setTitle("訂餐APP");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //-------------------------------
                String url = "http://163.21.235.176/login/serch_acc_transaction_deposit.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                            account = new String[jsonArray.length()];
                            name = new String[jsonArray.length()];
                            sendtext1 = new String[2 * jsonArray.length()];
                            sendtext2 = new String[2 * jsonArray.length()];
                            sendtext_date1 = new String[2 * jsonArray.length()];
                            sendtext_date2 = new String[2 * jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                account[i] = jsonObject.getString("account");
                                name[i] = jsonObject.getString("name");
                            }
                            int temp = name.length;
                            judge = new int[temp+1];

                            for (temp2 = 0; temp2 < temp + 1; temp2++) {
                                RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                                requestQueue2.add(search_acc_transaction_mess(temp2));
                                RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                                requestQueue3.add(search_acc_deposit_mess(temp2));
                            }
                            //-----------------------


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("name", login.account);
                        data.put("search", acc.getText().toString());
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
                //-----------------
            }
        }).start();

        acc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //-------------------------------
                        String url = "http://163.21.235.176/login/serch_acc_transaction_deposit.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                    account = new String[jsonArray.length()];
                                    name = new String[jsonArray.length()];
                                    sendtext1 = new String[2 * jsonArray.length()];
                                    sendtext2 = new String[2 * jsonArray.length()];
                                    sendtext_date1 = new String[2 * jsonArray.length()];
                                    sendtext_date2 = new String[2 * jsonArray.length()];
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        account[i] = jsonObject.getString("account");
                                        name[i] = jsonObject.getString("name");
                                    }
                                    int temp = name.length;

                                    for (temp2 = 0; temp2 < temp + 1; temp2++) {
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue2.add(search_acc_transaction_mess(temp2));
                                        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue3.add(search_acc_deposit_mess(temp2));
                                    }
                                    //-----------------------


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();
                                data.put("name", login.account);
                                data.put("search", acc.getText().toString());
                                return data;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                        //-----------------
                    }
                }).start();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(new_account.this, retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    public StringRequest search_acc_deposit_mess(int temp2) {
        String url = "http://163.21.235.176/login/search_acc_deposit_mess.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    judge[temp2]=1;
                    if (!response.substring(0, 2).equals("<b")) {
                        JSONArray jsonArray = new JSONArray(String.valueOf(response));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject20 = jsonArray.getJSONObject(i);
                            new_account.sendtext2[2 * temp2 + i] = jsonObject20.getString("stored_money");
                            new_account.sendtext_date2[2 * temp2 + i] = jsonObject20.getString("date");
                        }
                    }
                    int kk=0;
                    for(int jj=0;jj<judge.length;jj++){
                        if(judge[jj]==1){
                            kk++;
                            if(kk==judge.length-2)showlist();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                if (temp2 < new_account.account.length) data.put("name", account[temp2]);
                return data;
            }
        };
        return stringRequest;
    }

    public StringRequest search_acc_transaction_mess(int temp2) {
        String url = "http://163.21.235.176/login/search_acc_transaction_mess.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    if (!response.substring(0, 2).equals("<b")) {
                        JSONArray jsonArray2 = new JSONArray(response);
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                            new_account.sendtext1[2 * temp2 + i] = jsonObject2.getString("expend");
                            new_account.sendtext_date1[2 * temp2 + i] = jsonObject2.getString("order_date");
                        }
                    }
                    int kk=0;
                    for(int jj=0;jj<judge.length;jj++){
                        if(judge[jj]==1){
                            kk++;
                            if(kk==judge.length-2)showlist();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data2 = new HashMap<>();
                if (temp2 < new_account.account.length)
                    data2.put("name", new_account.account[temp2]);
                return data2;
            }
        };
        return stringRequest;
    }

    public void showlist() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WordAdapter_account adapter = new WordAdapter_account(account, name, sendtext1, sendtext2, sendtext_date1, sendtext_date2);
                ListView list = (ListView) findViewById(R.id.pressureList);
                list.setAdapter(adapter);
                pressureList = (ListView) findViewById(R.id.pressureList);
                pressureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder mbulider = new AlertDialog.Builder(new_account.this);
                        View mview = getLayoutInflater().inflate(R.layout.dialogspinner, null);
                        mbulider.setTitle("請選擇儲值金額!");
                        Spinner mspinner = (Spinner) mview.findViewById(R.id.spinner_1);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(new_account.this
                                , android.R.layout.simple_spinner_item
                                , getResources().getStringArray(R.array.change_money));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mspinner.setAdapter(adapter);
                        mbulider.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!mspinner.getSelectedItem().toString().equalsIgnoreCase("請選擇要儲值的金額..")) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(new_account.this);
                                    alertDialog.setTitle("給" + name[position]);
                                    alertDialog.setMessage("確定要儲值" + mspinner.getSelectedItem().toString() + "嗎?");
                                    final int[] pocket = {0};
                                    final int[] pack = {0};
                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String url3 = "http://163.21.235.176/login/catch_profile_acc.php";
                                                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response3) {
                                                            try {
                                                                JSONArray jsonArray = new JSONArray(String.valueOf(response3));
                                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                                pocket[0] = jsonObject.getInt("pocket");
                                                                pack[0] = jsonObject.getInt("message_money");
                                                                pack[0] += Integer.parseInt(mspinner.getSelectedItem().toString());
                                                                pocket[0] += Integer.parseInt(mspinner.getSelectedItem().toString());
                                                                //change_message_and_money
                                                                String url4 = "http://163.21.235.176/login/change_message_and_money.php";
                                                                StringRequest stringRequest4 = new StringRequest(Request.Method.POST, url4, new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response4) { }
                                                                }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }) {
                                                                    @Override
                                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                                        Map<String, String> data = new HashMap<>();
                                                                        data.put("name", account[position]);
                                                                        data.put("money", String.valueOf(pocket[0]));
                                                                        data.put("message_money", String.valueOf(pack[0]));
                                                                        return data;
                                                                    }
                                                                };
                                                                RequestQueue requestQueue4 = Volley.newRequestQueue(getApplicationContext());
                                                                requestQueue4.add(stringRequest4);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
                                                        }
                                                    }) {
                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> data = new HashMap<>();
                                                            data.put("name", account[position]);
                                                            return data;
                                                        }
                                                    };
                                                    RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                                                    requestQueue3.add(stringRequest3);

                                                    Calendar mCal = Calendar.getInstance();
                                                    CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());    // kk:24小時制, hh:12小時制
                                                    String url6 = "http://163.21.235.176/login/new_deposit.php";
                                                    StringRequest stringRequest6 = new StringRequest(Request.Method.POST, url6, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response6) {
                                                            Intent intent = getIntent();
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(new_account.this, "error", Toast.LENGTH_LONG).show();
                                                        }
                                                    }) {
                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> data = new HashMap<>();
                                                            data.put("date", s.toString());
                                                            data.put("name", name[position]);
                                                            data.put("stored_money", mspinner.getSelectedItem().toString());
                                                            data.put("account", account[position]);
                                                            data.put("account_retailer", login.account);
                                                            return data;
                                                        }
                                                    };
                                                    RequestQueue requestQueue6 = Volley.newRequestQueue(getApplicationContext());
                                                    requestQueue6.add(stringRequest6);
                                                }
                                            }).start();

                                        }
                                    });
                                    alertDialog.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                                    });
                                    alertDialog.setCancelable(false);
                                    alertDialog.show();
                                }
                            }
                        });
                        mbulider.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                        });
                        mbulider.setView(mview);
                        AlertDialog dialog = mbulider.create();
                        dialog.show();
                    }
                });
                FloatingActionButton fab = findViewById(R.id.fab2);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(new_account.this, register.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                        startActivity(intent);
                    }
                });
            }
        });

    }
}