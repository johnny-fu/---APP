package com.example.logintest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public  class WordAdapter_account extends BaseAdapter {


    private String[] account;
    private String[] name;
    private String[] sendtext1;
    private String[] sendtext2;
    private String[] sendtext_date1;
    private String[] sendtext_date2;
    public WordAdapter_account(String[] account, String[] name, String[] sendtext1, String[] sendtext2, String[] sendtext_date1, String[] sendtext_date2) {
        this.account = account;
        this.name =name;
        this.sendtext1 =sendtext1;
        this.sendtext_date1 =sendtext_date1;
        this.sendtext2 =sendtext2;
        this.sendtext_date2 =sendtext_date2;
    }


    @Override
    public int getCount() {
        return account.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_view, null);
        }

        // 找到TextView
        TextView name_2= (TextView) convertView.findViewById(R.id.textView26);
        TextView account_2= (TextView) convertView.findViewById(R.id.textView25);
        TextView sendtext1_2= (TextView) convertView.findViewById(R.id.des_1);
        TextView sendtext2_2= (TextView) convertView.findViewById(R.id.des_2);
        TextView sendtext3_2= (TextView) convertView.findViewById(R.id.tran_1);
        TextView sendtext4_2= (TextView) convertView.findViewById(R.id.tran_2);
//        Spinner spinner = (Spinner)convertView.findViewById(R.id.spinner);
        // 取出文字



        String account_1 = account[position];
        String name_1 = name[position];
        String sendtext1_1 = sendtext1[2*position];
        String sendtext2_1 = sendtext1[2*position+1];
        String sendtext3_1 = sendtext2[2*position];
        String sendtext4_1 = sendtext2[2*position+1];
        String sendtext_date1_1 = sendtext_date1[2*position];
        String sendtext_date2_1 = sendtext_date1[2*position+1];
        String sendtext_date3_1 = sendtext_date2[2*position];
        String sendtext_date4_1 = sendtext_date2[2*position+1];
        // 將文字內容設定給TextView
        name_2.setText("名稱 :\n"+name_1);
        account_2.setText("帳號 :\n"+account_1);
        sendtext1_2.setText(sendtext_date1_1+":"+sendtext1_1);
        sendtext2_2.setText(sendtext_date2_1+":"+sendtext2_1);
        sendtext3_2.setText(sendtext_date3_1+":"+sendtext3_1);
        sendtext4_2.setText(sendtext_date4_1+":"+sendtext4_1);
        if(sendtext1_1==null){
            sendtext1_2.setText("無資料");
        }
        if(sendtext2_1==null){
            sendtext2_2.setText("無資料");
        }
        if(sendtext3_1==null){
            sendtext3_2.setText("無資料");
        }
        if(sendtext4_1==null){
            sendtext4_2.setText("無資料");
        }
        // 找到ImageView
    //    ImageView icon = (ImageView) convertView.findViewById(R.id.img);
        // 依照位置算出對應的圖片
//        int resId = mIcons[position % mIcons.length];
        // 將圖片設定給ImageView
   //     icon.setImageResource(resId);

        // 一定要將convertView回傳，供ListView呈現使用，並加入重用機制中
        return convertView;
    }

}