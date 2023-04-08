package com.example.logintest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

public class HomeFragment extends Fragment {
    private TextView username, userpocket,update_dairy;
    private HomeViewModel homeViewModel;
    String account_owner, message_money, message_transaction;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button back = root.findViewById(R.id.back_login);
        username = (TextView) root.findViewById(R.id.username);
        userpocket = (TextView) root.findViewById(R.id.userpocket);
        update_dairy = (TextView) root.findViewById(R.id.update_dairy);
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    username.setText(login.name+" 您好!");
                                    userpocket.setText(" 錢包:" + login.money);
                                }
                            });
                            if (Integer.parseInt(login.message_money) > 0) {
                                int cost = Integer.parseInt(login.message_money);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                        alertDialog.setTitle("儲值完成!!");
                                        alertDialog.setMessage("您的錢包新增了" + cost + "目前有" + login.money);
                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //將 錢的訊息改成0
                                                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                        alertDialog.setTitle("燈燈!!!!");
                                        alertDialog.setMessage("您的訂單已被拒絕請重新下訂!!");
                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                        alertDialog.setTitle("燈燈!!!!");
                                        alertDialog.setMessage("您的訂單已通過審核!!\n請準時取餐!");
                                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        }).start();//抓資料666
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = php_con.login_url+"update_dairy/readtxt.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            String temp = String.valueOf(response);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    update_dairy.setText(temp.replace("<br />",""));
                                }
                            });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        }).start();//抓資料666
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),login.class);
                startActivity(intent);
                startActivity(intent);
            }
        });
        return root;
    }
}
