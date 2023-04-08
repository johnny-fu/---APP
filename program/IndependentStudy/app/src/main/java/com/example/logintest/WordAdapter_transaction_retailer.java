package com.example.logintest;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public  class WordAdapter_transaction_retailer extends BaseAdapter {
    private String[] mretailer;
    private String[] mexpend;
    private String[] mtransaction;
    private String[] mdate_s,mdate_e,mbad;
    public WordAdapter_transaction_retailer(String[] retailer, String[] expend, String[] transaction, String[] date_s, String[] date_e, String[] bad) {
        mretailer = retailer;
        mexpend =expend;
        mtransaction = transaction;
        mdate_s = date_s;
        mdate_e = date_e;
        mbad=bad;
    }

    @Override
    public int getCount() {
        return mdate_s.length;
    }

    @Override
    public Object getItem(int position) {
        return mdate_s[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 檢查convertView是否有值，有值表示是重複使用的
        if (convertView == null) {
            // 沒有值就要自己建立一個
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_3, null);
        }
        // 找到TextView
        TextView confirm = (TextView) convertView.findViewById(R.id.confirm);
        TextView order_meal = (TextView) convertView.findViewById(R.id.order_meal);
        TextView date_s = (TextView) convertView.findViewById(R.id.date_s);
        TextView date_e = (TextView) convertView.findViewById(R.id.date_e);
        TextView spend = (TextView) convertView.findViewById(R.id.spend);
        TextView baddarw = (TextView) convertView.findViewById(R.id.textView22);

        // 取出文字
        String date_s1 = mdate_s[position];
        String date_e1 = mdate_e[position];
        String retailer = mretailer[position];
        String expend = mexpend[position];
        String transaction = mtransaction[position];
        String badraw = mbad[position];
        if(retailer.equals("OK"))
            spend.setTextColor(Color.parseColor("#4CAF50"));
               else
            spend.setTextColor(Color.parseColor("#FF0000"));
        // 將文字內容設定給TextView
        date_s.setText("取餐日期:"+date_s1);
        date_e.setText("訂購日期:"+date_e1);
        order_meal.setText(transaction);
        spend.setText("訂單狀況:"+retailer);
        confirm.setText("金額:"+expend);
        baddarw.setText("顧客備註: \n"+badraw);
        if(retailer.equals("沒有資料")){
            date_e.setText("沒有資料:");
        }
        return convertView;
    }

}