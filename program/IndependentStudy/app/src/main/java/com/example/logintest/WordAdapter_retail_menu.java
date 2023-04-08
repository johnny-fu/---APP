package com.example.logintest;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public  class WordAdapter_retail_menu extends BaseAdapter {

    private String[] mWords=new String[20];
    private String[] mSubWords;
    private String[] mmoney;
    private int[] mIcons;
    private String[] mspinner;
    private String[] morder;
    private Bitmap[] mimage;

    public WordAdapter_retail_menu(String[] title, String[] money, String[] detail, String[] order, Bitmap[] image) {
        mWords = title;
        mSubWords =detail;
        mmoney = money;
        morder = order;
        mimage = image;
    }


    @Override
    public int getCount() {
        return mWords.length;
    }

    @Override
    public Object getItem(int position) {
        return mWords[position];
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_1, null);
        }

        // 找到TextView
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subTitle = (TextView) convertView.findViewById(R.id.info);
        TextView money = (TextView) convertView.findViewById(R.id.money);
        ImageView icon = (ImageView) convertView.findViewById(R.id.img);
        // 取出文字
        String text = (String) getItem(position);
        String subText = mSubWords[position];
        String money_1 = mmoney[position];
        Bitmap image_1 = mimage[position];

        // 將文字內容設定給TextView
        title.setText(text);
        subTitle.setText(subText);
        money.setText("$ "+money_1);
        icon.setImageBitmap(image_1);

        // 一定要將convertView回傳，供ListView呈現使用，並加入重用機制中
        return convertView;
    }

}