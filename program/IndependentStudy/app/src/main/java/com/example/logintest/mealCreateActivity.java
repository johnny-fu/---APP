package com.example.logintest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class mealCreateActivity extends AppCompatActivity {
    private EditText titles, money, subtitles;
    private Button btnAppend;
    private Button btnselectpic;
    private ImageView imageview;
    private TextView path;
    Bitmap bitmap;
    final int CODE_GALLEY_REQUEST = 999;
    private String imagepath=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_create);
        path = (TextView)findViewById(R.id.showpath);
        findViews();
        //  獲得目前時間
        //gettime();
    }


    private void findViews() {
        btnselectpic = (Button) findViewById(R.id.img);
        imageview = (ImageView) findViewById(R.id.imageView);
        titles = (EditText) findViewById(R.id.titles);
        money = (EditText) findViewById(R.id.money);
        subtitles = (EditText) findViewById(R.id.subtitles);
        btnAppend = (Button) findViewById(R.id.button);
        btnselectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        mealCreateActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLEY_REQUEST
                );
            }
        });
        btnAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String url = "http://163.21.235.176/login/meal_creat.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                        {@Override public void onResponse(String response) { }}
                                , new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) { }}) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();
                                data.put("titles", titles.getText().toString());
                                data.put("money", money.getText().toString());
                                data.put("subtitles", subtitles.getText().toString());
                                String imagedata = imagetostring(bitmap);
                                data.put("image", imagedata);
                                return data;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                    }
                }).start();
                Intent intent = new Intent();
                intent.setClass(mealCreateActivity.this  , mealActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLEY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CODE_GALLEY_REQUEST);


                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GALLEY_REQUEST && resultCode == RESULT_OK && data!= null) {
            Uri filepath = data.getData();
            try {
                imagepath = getPath(filepath);
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageview.setImageBitmap(bitmap);
                path.setText(imagepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private String imagetostring(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[] imagebytes = outputStream.toByteArray();
        String encode = Base64.encodeToString(imagebytes,Base64.DEFAULT);
        return encode;
    }

}
