package com.example.logintest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public  class WordAdapter_desposit extends BaseAdapter {
    private String[] mretailer;
    private String[] mtransaction;
    private String[] mdate;
    public WordAdapter_desposit(String[] retailer, String[] transaction, String[] date) {
        mretailer = retailer;
        mtransaction = transaction;
        mdate = date;
    }

    @Override
    public int getCount() {
        return mdate.length;
    }

    @Override
    public Object getItem(int position) {
        return mdate[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 檢查convertView是否有值，有值表示是重複使用的
        if (convertView == null) {
            // 沒有值就要自己建立一個
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_2, null);
        }
        // 找到TextView
        TextView confirm = (TextView) convertView.findViewById(R.id.confirm);
        TextView order_meal = (TextView) convertView.findViewById(R.id.order_meal);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView spend = (TextView) convertView.findViewById(R.id.spend);
        // 取出文字
        String date2 = mdate[position];
        String retailer = mretailer[position];
        String transaction = mtransaction[position];
        // 將文字內容設定給TextView
        date.setText(date2);
        order_meal.setText("金額");
        spend.setText(retailer);
        confirm.setText(transaction);
        if(date2.equals("沒有資料")){
            spend.setText("");
            order_meal.setText("");
        }
        return convertView;
    }

}