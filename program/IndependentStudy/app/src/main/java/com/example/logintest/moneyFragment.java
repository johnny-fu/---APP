package com.example.logintest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import java.util.Timer;
import java.util.TimerTask;

public class moneyFragment extends Fragment {


    private moneyViewModel moneyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moneyViewModel =
                new ViewModelProvider(this).get(moneyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_money, container, false);
        RadioGroup rg = (RadioGroup) root.findViewById(R.id.tradetype);
        rg.setOnCheckedChangeListener(radGrpRegionOnCheckedChange);
        rg.check(R.id.tran_order);
        return root;
    }
    private RadioGroup.OnCheckedChangeListener radGrpRegionOnCheckedChange = new RadioGroup.OnCheckedChangeListener() {
        
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
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
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    WordAdapter_transaction adapter = new WordAdapter_transaction(retailer, expend, transaction, date);
                                                    ListView list = (ListView) getActivity().findViewById(R.id.listview2);
                                                    list.setAdapter(adapter);
                                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                            if (retailer[i].equals("not")) {//還沒被確定{
                                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                                                alertDialog.setTitle("注意");
                                                                alertDialog.setMessage("你要取消這個訂單嗎?");
                                                                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Thread thread = new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                                                        final Timer t = new Timer();
                                                                        t.schedule(new TimerTask() {
                                                                            public void run() {
                                                                                Intent intent = new Intent();
                                                                                intent.setClass(getActivity(), client_login.class);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                                                                startActivity(intent);
                                                                                dialog.dismiss();
                                                                                t.cancel();
                                                                            }
                                                                        }, 2000);
                                                                        /*client_login mActivity = (client_login) getActivity();
                                                                        mActivity.changeFragment("money");*/

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
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    WordAdapter_transaction adapter = new WordAdapter_transaction(retailer, expend, transaction, date);
                                                    ListView list = (ListView) getActivity().findViewById(R.id.listview2);
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
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", login.account);
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    
                                                    WordAdapter_desposit adapter = new WordAdapter_desposit(retailer, transaction, date);
                                                    ListView list = (ListView) getActivity().findViewById(R.id.listview2);
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
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    WordAdapter_desposit adapter = new WordAdapter_desposit(retailer, transaction, date);
                                                    ListView list = (ListView) getActivity().findViewById(R.id.listview2);
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
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("name", login.account);
                                    return data;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                            requestQueue.add(stringRequest);
                        }
                    }).start();
                    break;
            }
        }
    };



    private void finish() {
    }
}