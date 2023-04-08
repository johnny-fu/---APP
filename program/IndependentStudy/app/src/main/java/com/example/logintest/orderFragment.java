package com.example.logintest;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
public class orderFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private orderViewModel orderViewModel;
    int expensive = 0;
    boolean order_key = false;
    boolean date_key = false;
    static String DATE1, DATE2;
    String context_2,orderd,baddraw;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day;
    Calendar calendar ;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        orderViewModel =
                new ViewModelProvider(this).get(orderViewModel.class);
        root = inflater.inflate(R.layout.fragment_order, container, false);

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = php_con.login_url+"catch_profile_acc.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            orderd = jsonObject.getString("order_day");
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
                        data.put("name", login.account_owner);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = php_con.login_url+"search_meal.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                            String[] title = new String[jsonArray.length()];
                            String[] money = new String[jsonArray.length()];
                            String[] detail = new String[jsonArray.length()];
                            String[] order = new String[jsonArray.length()];
                            Bitmap[] image = new Bitmap[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                title[i] = jsonObject.getString("name");
                                money[i] = jsonObject.getString("price");
                                detail[i] = jsonObject.getString("detail");
                                order[i] = "";
                                String imagepath = jsonObject.getString("image");
                                int finalI = i;
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String uu = php_con.login_url+"images/" + imagepath;
                                        Bitmap bitmap = getBitmapFromURL(uu);
                                        image[finalI] = bitmap;
                                    }
                                });
                                thread.start();
                                try {
                                    // Thread B 加入 Thread A
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WordAdapter adapter = new WordAdapter(title, money, detail, order, image);
                                    ListView list = (ListView) root.findViewById(R.id.listview);
                                    list.setAdapter(adapter);
                                    expensive = 0;
                                    TextView price = (TextView) root.findViewById(R.id.price);
                                    setbutton_buy(date_key, order_key);
                                    price.setText("金額 :" + expensive);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊list
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            AlertDialog.Builder mbulider = new AlertDialog.Builder(getActivity());
                                            View mview = getLayoutInflater().inflate(R.layout.dialogspinner, null);
                                            mbulider.setTitle("請選擇數量!");
                                            Spinner mspinner = (Spinner) mview.findViewById(R.id.spinner_1);
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                                                    , android.R.layout.simple_spinner_item
                                                    , getResources().getStringArray(R.array.value));
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            mspinner.setAdapter(adapter);
                                            mbulider.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (!mspinner.getSelectedItem().toString().equalsIgnoreCase("請選擇要訂購的數量..")) {
                                                        order[i] = mspinner.getSelectedItem().toString();
                                                        expensive = 0;
                                                        for (int i = 0; i < order.length; i++) {
                                                            if (!order[i].equals(""))
                                                                expensive += Integer.parseInt(order[i]) * Integer.parseInt(money[i].toString());
                                                        }
                                                        if (login.money < expensive) {
                                                            order[i] = "";
                                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                                            alertDialog.setTitle("燈燈");
                                                            alertDialog.setMessage("餘額不足!!!!!!");
                                                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            });
                                                            alertDialog.setCancelable(false);
                                                            alertDialog.show();

                                                        } else {
                                                            order_key = true;
                                                            setbutton_buy(date_key, order_key);
                                                            order[i] = mspinner.getSelectedItem().toString();
                                                            dialog.dismiss();
                                                            WordAdapter adapter = new WordAdapter(title, money, detail, order, image);
                                                            ListView list = (ListView) root.findViewById(R.id.listview);
                                                            list.setAdapter(adapter);
                                                            expensive = 0;
                                                            for (int i = 0; i < order.length; i++) {
                                                                if (!order[i].equals(""))
                                                                    expensive += Integer.parseInt(order[i]) * Integer.parseInt(money[i].toString());
                                                            }
                                                            price.setText("金額 :" + expensive);
                                                        }
                                                    }
                                                }
                                            });
                                            mbulider.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            mbulider.setView(mview);
                                            AlertDialog dialog = mbulider.create();
                                            dialog.show();
                                        }
                                    });
                                    Button select_date = (Button) root.findViewById(R.id.select_date);//選日期
                                    select_date.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentManager fm = getActivity().getFragmentManager();
                                            datePickerDialog = DatePickerDialog.newInstance(orderFragment.this , Year, Month, Day);
                                            datePickerDialog.setThemeDark(false);
                                            datePickerDialog.showYearPickerFirst(false);
                                            datePickerDialog.setTitle("選擇取餐日期");

                                            // Setting Min Date to today date
                                            Calendar min_date_c = Calendar.getInstance();
                                            min_date_c.add(Calendar.DATE, 1);
                                            datePickerDialog.setMinDate(min_date_c);

                                            // Setting Max Date to next 2 years

                                            Calendar max_date_c = Calendar.getInstance();
                                            max_date_c.add(Calendar.DATE, Integer.parseInt(orderd));
                                            datePickerDialog.setMaxDate(max_date_c);
                                            int tmp=0;
                                            //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
                                            for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                                                int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                                                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                                                    tmp++;
                                                    Calendar[] disabledDays =  new Calendar[1];
                                                    disabledDays[0] = loopdate;
                                                    datePickerDialog.setDisabledDays(disabledDays);
                                                }
                                            }
                                            Calendar loopdate=max_date_c;
                                            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                                            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                                                tmp++;
                                                Calendar[] disabledDays =  new Calendar[1];
                                                disabledDays[0] = loopdate;
                                                datePickerDialog.setDisabledDays(disabledDays);
                                            }
                                             max_date_c = Calendar.getInstance();
                                            if(tmp<=2&&tmp!=0) {
                                                max_date_c.add(Calendar.DATE, Integer.parseInt(orderd) + 2);
                                            }else {
                                                max_date_c.add(Calendar.DATE, Integer.parseInt(orderd) + tmp);
                                            }
                                            datePickerDialog.setMaxDate(max_date_c);

                                            //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
                                            for ( loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                                                 dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                                                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                                                    Calendar[] disabledDays =  new Calendar[1];
                                                    disabledDays[0] = loopdate;
                                                    datePickerDialog.setDisabledDays(disabledDays);
                                                }
                                            }
                                             loopdate=max_date_c;
                                             dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                                            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                                                Calendar[] disabledDays =  new Calendar[1];
                                                disabledDays[0] = loopdate;
                                                datePickerDialog.setDisabledDays(disabledDays);
                                            }

                                            datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                                @Override
                                                public void onCancel(DialogInterface dialogInterface) {
                                                    //Toast.makeText(order.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            datePickerDialog.show(fm,"dialog");
                                        }

                                    });




                                    Button button_buy = (Button) root.findViewById(R.id.buy);
                                    button_buy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String context = "";
                                            TextView date = (TextView) root.findViewById(R.id.date_1);
                                            String my_order = date.getText() + "\n";
                                            AlertDialog.Builder mbulider = new AlertDialog.Builder(getActivity());
                                            View mview = getLayoutInflater().inflate(R.layout.order_meal, null);
                                            mbulider.setTitle("請確認數量!");
                                            TextView order_meal = (TextView) mview.findViewById(R.id.meal);
                                            for (int i = 0; i < order.length; i++) {
                                                if (!order[i].equals("")) {
                                                    my_order += title[i];
                                                    my_order += "  共  ";
                                                    my_order += Integer.parseInt(order[i]);
                                                    my_order += " 個\n";
                                                    context += title[i];
                                                    context += " 共 ";
                                                    context += Integer.parseInt(order[i]);
                                                    context += " 個\n";
                                                }
                                            }
                                            context_2 = context;
                                            my_order += "----------------------\n";
                                            my_order += "總價格為 :" + expensive;
                                            order_meal.setText(my_order);
                                            //-------------------------------------有新增備註
                                            EditText editTextTextPersonName = (EditText) mview.findViewById(R.id.editTextTextPersonName2);
                                            //baddraw存到交易紀錄的備註欄;
                                            mbulider.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String user_input = editTextTextPersonName.getText().toString();
                                                    System.out.println(editTextTextPersonName.getText().toString()+"cc");
                                                    baddraw = "";
                                                    for (int i = 0; i < user_input.toCharArray().length; i++) {
                                                        baddraw += user_input.charAt(i);
                                                        if (i % 8 == 7 && i != user_input.toCharArray().length - 1)
                                                            baddraw += "\n";
                                                    }
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String url = "http://163.21.235.176/login/new_transaction.php";
                                                            String url2 = "http://163.21.235.176/login/change_money.php";
                                                            String url3 = "http://163.21.235.176/login/catch_profile_acc.php";
                                                            Calendar mCal = Calendar.getInstance();
                                                            CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());    // kk:24小時制, hh:12小時制
                                                            //------------
                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                                                                }
                                                            }) {
                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    System.out.println(baddraw+"aa");
                                                                    Map<String, String> data = new HashMap<>();
                                                                    data.put("client", login.account);
                                                                    data.put("expend", String.valueOf(expensive));
                                                                    data.put("transaction_message", context_2);
                                                                    data.put("date", DATE1);
                                                                    data.put("order_date", s.toString());
                                                                    data.put("baddraw", baddraw);
                                                                    return data;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                                            requestQueue.add(stringRequest);
                                                            //----------
                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        if (response.equals("Error!")) {

                                                                        }
                                                                    } catch (Exception e) {
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
                                                                    data.put("money", String.valueOf((login.money - expensive)));
                                                                    return data;
                                                                }
                                                            };
                                                            RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
                                                            requestQueue2.add(stringRequest2);

                                                            //--------------------------------
                                                            StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                                        Integer id_profile = jsonObject.getInt("id_profile");
                                                                        String account2 = jsonObject.getString("account");
                                                                        String name = jsonObject.getString("name");
                                                                        Integer pocket = jsonObject.getInt("pocket");
                                                                        login.id = id_profile;
                                                                        login.account = account2;
                                                                        login.name = name;
                                                                        login.money = pocket;
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
                                                                    return data;
                                                                }
                                                            };
                                                            RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
                                                            requestQueue3.add(stringRequest3);

                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    AlertDialog.Builder mbulider = new AlertDialog.Builder(getActivity());
                                                                    View mview = getLayoutInflater().inflate(R.layout.dialogprobar, null);
                                                                    mbulider.setTitle("訂單完成中.......");
                                                                    mbulider.setCancelable(true);
                                                                    mbulider.setView(mview);
                                                                    AlertDialog dialog = mbulider.create();
                                                                    dialog.show();
                                                                    final Timer t = new Timer();
                                                                    t.schedule(new TimerTask() {
                                                                        public void run() {
                                                                            start.my_wallet -= expensive;
                                                                            Intent intent = new Intent();
                                                                            intent.setClass(getActivity(), client_login.class);
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                                                            startActivity(intent);
                                                                            dialog.dismiss();
                                                                            t.cancel();
                                                                        }
                                                                    }, 2000);
                                                                }
                                                            });
                                                        }
                                                    }).start();
                                                    dialog.dismiss();
                                                }
                                            });
                                            mbulider.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            mbulider.setView(mview);
                                            AlertDialog dialog = mbulider.create();
                                            dialog.show();
                                        }
                                    });
                                }
                            });
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
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        }).start();
        return root;
    }
    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public void setbutton_buy(boolean order_key, boolean date_key) {
        Button button_buy = (Button) root.findViewById(R.id.buy);
        if (order_key && date_key) {
            button_buy.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button_buy.setBackgroundTintList(getResources().getColorStateList(R.color.yellow));
            }
        } else {
            button_buy.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button_buy.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
            }
        }
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        TextView date = (TextView) root.findViewById(R.id.date_1);
        String jj = String.valueOf(monthOfYear+1);
        String kk = String.valueOf(dayOfMonth);
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (monthOfYear + 1 < 10) jj = "0" + String.valueOf(monthOfYear+1);
        if (dayOfMonth < 10) kk = "0" + String.valueOf(dayOfMonth);
        date.setText("取餐日 : " + year + "-" + jj + "-" + kk);
        DATE1 = year + "-" + jj + "-" + kk;
        date_key = true;
        setbutton_buy(date_key, order_key);
    }
}