package com.example.logintest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class mealActivity extends AppCompatActivity {
    private Cursor cursor;
    private ListView pressureList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView order_day = (TextView) findViewById(R.id.textView4);
        order_day.setText("目前可於 " + login.order_day + " 天前預訂");
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mealActivity.this, retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //開放預訂日期
        Button opendate = (Button) findViewById(R.id.opendate);
        opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbulider = new AlertDialog.Builder(mealActivity.this);
                View mview = getLayoutInflater().inflate(R.layout.dialogspinner, null);
                mbulider.setTitle("請選擇開放訂餐天數!");
                Spinner mspinner = (Spinner) mview.findViewById(R.id.spinner_1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mealActivity.this
                        , android.R.layout.simple_spinner_item
                        , getResources().getStringArray(R.array.change_day));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mspinner.setAdapter(adapter);
                mbulider.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mspinner.getSelectedItem().toString().equalsIgnoreCase("請選擇要設定的開放訂餐天數..")) {
                            String day = mspinner.getSelectedItem().toString();
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(php_con.meal_activity(day, "reverse_orderday", login.account));
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
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
        findViews();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://163.21.235.176/login/search_meal.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(String.valueOf(response));
                            String[] title = new String[jsonArray.length()];
                            String[] money = new String[jsonArray.length()];
                            String[] detail = new String[jsonArray.length()];
                            String[] id = new String[jsonArray.length()];
                            Bitmap[] image = new Bitmap[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                title[i] = jsonObject.getString("name");
                                money[i] = jsonObject.getString("price");
                                detail[i] = jsonObject.getString("detail");
                                id[i] = jsonObject.getString("id_meal");
                                String imagepath = jsonObject.getString("image");
                                int finalI = i;
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String uu = "http://163.21.235.176/login/images/" + imagepath;
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WordAdapter_retail_menu adapter = new WordAdapter_retail_menu(title, money, detail, id, image);
                                    ListView list = (ListView) findViewById(R.id.pressureList);
                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊list
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            AlertDialog.Builder mbulider = new AlertDialog.Builder(mealActivity.this);
                                            View mview = getLayoutInflater().inflate(R.layout.change_meal, null);
                                            mbulider.setTitle("請選擇對" + title[i] + "的動作!");
                                            ImageView icon = (ImageView) mview.findViewById(R.id.imageView9);
                                            icon.setImageBitmap(image[i]);
                                            Button button5 = (Button) mview.findViewById(R.id.button15);
                                            button5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mealActivity.this);
                                                    alertDialog.setTitle("確定要刪除" + title[i] + "?");
                                                    alertDialog.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                                    requestQueue.add(php_con.meal_activity(title[i], "delete", id[i]));
                                                                    restart(dialog);//RESTART ACTIVITY
                                                                }
                                                            }).start();
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
                                            });
                                            Button button6 = (Button) mview.findViewById(R.id.button11);//change_title
                                            button6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    button_activity(id, i, "reverse_name");
                                                }
                                            });
                                            Button button10 = (Button) mview.findViewById(R.id.button13);//change_detail
                                            button10.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    button_activity(id, i, "reverse_detail");
                                                }
                                            });
                                            Button button7 = (Button) mview.findViewById(R.id.button12);//change_money
                                            button7.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder mbulider2 = new AlertDialog.Builder(mealActivity.this);
                                                    View mview2 = getLayoutInflater().inflate(R.layout.change, null);
                                                    mbulider2.setTitle("請輸入要修改的價格");
                                                    EditText input = (EditText) mview2.findViewById(R.id.input);
                                                    mbulider2.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            try {
                                                                int a = Integer.parseInt(input.getText().toString());
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                                        requestQueue.add(php_con.meal_activity(String.valueOf(a), "reverse_price", id[i]));
                                                                        restart(dialog);//RESTART ACTIVITY
                                                                    }
                                                                }).start();
                                                            } catch (Exception e) {
                                                                AlertDialog.Builder mbulider2 = new AlertDialog.Builder(mealActivity.this);
                                                                mbulider2.setTitle("僅可輸入數字!");
                                                                mbulider2.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });

                                                                Dialog dialog3 = mbulider2.create();
                                                                dialog3.show();
                                                            }
                                                        }
                                                    });
                                                    mbulider2.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    mbulider2.setView(mview2);
                                                    Dialog dialog1 = mbulider2.create();
                                                    dialog1.show();
                                                }
                                            });
                                            Button button14 = (Button) mview.findViewById(R.id.button14);//change_detail
                                            button14.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent();
                                                    intent.setClass(mealActivity.this, uploadimg.class);
                                                    intent.putExtra("id", id[i]);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            });
                                            mbulider.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            mbulider.setView(mview);
                                            Dialog dialog = mbulider.create();
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
                        Toast.makeText(mealActivity.this, "error", Toast.LENGTH_LONG).show();
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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://163.21.235.176/login/catch_profile_acc.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            login.order_day= jsonObject.getString("order_day");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    order_day.setText("目前可於 " + login.order_day + " 天前預訂");
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mealActivity.this, "error", Toast.LENGTH_LONG).show();
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
        });
        thread.start();
        try {
            // Thread B 加入 Thread A
            thread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void restart(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        findViews();
    }

    private void findViews() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mealActivity.this, mealCreateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    public void button_activity(String[] id, int i, String judge) {
        AlertDialog.Builder mbulider2 = new AlertDialog.Builder(mealActivity.this);
        View mview2 = getLayoutInflater().inflate(R.layout.change, null);
        if (judge.equals("reverse_name")) {
            mbulider2.setTitle("請輸入要修改的名稱");
        } else if (judge.equals("reverse_detail")) {
            mbulider2.setTitle("請輸入要修改的細節");
        }
        EditText input = (EditText) mview2.findViewById(R.id.input);
        mbulider2.setPositiveButton("確認!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(php_con.meal_activity(input.getText().toString(), judge, id[i]));
                        restart(dialog);//RESTART ACTIVITY
                    }
                }).start();
            }
        });
        mbulider2.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mbulider2.setView(mview2);
        Dialog dialog1 = mbulider2.create();
        dialog1.show();
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
}
