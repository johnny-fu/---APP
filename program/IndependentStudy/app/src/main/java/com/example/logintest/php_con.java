package com.example.logintest;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class php_con {
    public static String login_url ="http://163.21.235.176/login/";
    public static StringRequest remove_transaction_client(int id) {//刪除餐點給controller用
        String url = login_url+"remove_transaction_client_return_money.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", String.valueOf(id));
                return data;
            }
        };
        return stringRequest;
    }
    public static StringRequest change_message_money(String money) {//變更 錢的訊息
        String url = login_url+"change_message_money.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", login.account);
                data.put("pass", login.password);
                data.put("money", money);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest change_detailer_message_transactionr(String client, int id, int i) {
        String url = login_url+"change_detailer_message_transactionr.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("client", client);
                data.put("id", String.valueOf(id));
                data.put("judge", String.valueOf(i));
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest new_account(String name) {
        String url = login_url+"new_account.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("account_owner", login.account);
                data.put("name", name);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest meal_activity(String k, String judge, String id_meal) {
        String url = login_url+"meal_activity.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("k", k);
                data.put("judge", judge);
                data.put("id_meal", id_meal);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest change_acc(String account, String password, String name, String email) {
        String url = login_url+"change_acc.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("account", account);
                data.put("password", password);
                data.put("name", name);
                data.put("email", email);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest change_message_transaction_context() {
        String url = login_url+"change_message_transaction_context.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
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
        return stringRequest;
    }

    public static StringRequest change_forget_password(String account, String key) {
        String url = login_url+"change_forget_password.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("account", account);
                data.put("key", key);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest change_password(String account, String password) {
        String url = login_url+"change_password.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("account", account);
                data.put("password", password);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest catch_profile_acc() {
        String url = login_url+"catch_profile_acc.php";
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
                    login.order_day = jsonObject.getString("order_day");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", login.account);
                return data;
            }
        };
        return stringRequest;
    }

    public static StringRequest catch_profile_emailotp() {
        String url = login_url+"catch_profile_acc.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(String.valueOf(response));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    login.email = jsonObject.getString("email");
                    login.otp = String.valueOf(jsonObject.getInt("forgot_password"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", login.account);
                return data;
            }
        };
        return stringRequest;
    }
}
