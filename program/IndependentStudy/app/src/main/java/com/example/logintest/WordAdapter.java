package com.example.logintest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WordAdapter extends BaseAdapter {

    private String[] mWords = new String[20];
    private String[] mSubWords;
    private String[] mmoney;
    private int[] mIcons;
    private String[] mspinner;
    private String[] morder;
    private Bitmap[] mimage;

    public WordAdapter(String[] title, String[] money, String[] detail, String[] order, Bitmap[] image) {
        mWords = title;
        mSubWords = detail;
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
        TextView order = (TextView) convertView.findViewById(R.id.order);
        ImageView icon = (ImageView) convertView.findViewById(R.id.img);
//        Spinner spinner = (Spinner)convertView.findViewById(R.id.spinner);
        // 取出文字
        final Bitmap[] bitmap = new Bitmap[1];
        String text = (String) getItem(position);
        String subText = mSubWords[position];
        String money_1 = mmoney[position];
        String order_1 = morder[position];
        Bitmap image_1 = mimage[position];
        System.out.println(image_1+"aa");

        // 將文字內容設定給TextView
        icon.setImageBitmap(image_1);
        title.setText(text);
        subTitle.setText(subText);
        money.setText("$ " + money_1);
        order.setText(order_1);

        // 依照位置算出對應的圖片
//        int resId = mIcons[position % mIcons.length];
        // 將圖片設定給ImageView
        //

        // 一定要將convertView回傳，供ListView呈現使用，並加入重用機制中
        return convertView;
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

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }


}