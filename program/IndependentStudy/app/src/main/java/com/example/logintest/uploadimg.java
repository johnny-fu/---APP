package com.example.logintest;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class uploadimg extends AppCompatActivity {

    private Button btnAppend,btnselectpic,back,btnphotograph;
    Bitmap bitmap;
    private ImageView imageview;
    private TextView path;
    private String imagepath=null;
    public static final int CAMERA_PERMISSION = 100;//檢測相機權限用
    //camera
    private String TAG = "tag";
    //需要的权限数组 读/写/相机
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri ImageUri;
    public static final int TAKE_PHOTO = 101;
    public static final int TAKE_CAMARA = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimg);
        Intent intent = this.getIntent();
        String id = intent.getStringExtra("id");
        btnselectpic = (Button) findViewById(R.id.buttonChoose);
        btnphotograph = (Button) findViewById(R.id.photograph);
        imageview = (ImageView) findViewById(R.id.imageView11);
        btnAppend = (Button) findViewById(R.id.buttonUpload);
        back= (Button) findViewById(R.id.back);
        //取得相機權限
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        //相簿
        btnselectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //檢查權限
                if (verifyPermissions(uploadimg.this, PERMISSIONS_STORAGE[2]) == 0) {
                    Log.i(TAG, "提示是否授權");
                    ActivityCompat.requestPermissions(uploadimg.this, PERMISSIONS_STORAGE, 3);
                } else {
                    toPicture();  //開相簿
                }
            }
        });
        //相機
        btnphotograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //檢查權限
                if (verifyPermissions(uploadimg.this, PERMISSIONS_STORAGE[2]) == 0) {
                    Log.i(TAG, "提示是否授權");
                    ActivityCompat.requestPermissions(uploadimg.this, PERMISSIONS_STORAGE, 3);
                } else {
                    toCamera();  //開相機
                }
            }
        });
        //上傳圖片
        btnAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(php_con.meal_activity(imagetostring(bitmap), "reverse_images", id));
                    }
                }).start();
                Intent intent = new Intent();
                intent.setClass(uploadimg.this  , mealActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(uploadimg.this  , mealActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public int verifyPermissions(Activity activity, java.lang.String permission) {
        int Permission = ActivityCompat.checkSelfPermission(activity, permission);
        if (Permission == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "同意");
            return 1;
        } else {
            Log.i(TAG, "未同意");
            return 0;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            Log.i(TAG, "用戶授權");
            toCamera();
        } else {
            Log.i(TAG, "用戶未授權");
        }
    }
    //跳到相機
    private void toCamera() {
        File outputImage = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        } else {
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断SDK版本高低，ImageUri方法不同
        if (Build.VERSION.SDK_INT >= 24) {
            ImageUri = FileProvider.getUriForFile(uploadimg.this, "com.example.logintest.FileProvider", outputImage);
        } else {
            ImageUri = Uri.fromFile(outputImage);
        }

        //啟動相機
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
        Log.i(TAG, "跳到相機成功");
    }
    //跳到相簿
    private void toPicture() {
        //啟動相簿
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, TAKE_CAMARA);
        Log.i(TAG, "跳到相簿成功");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //拍照的相片顯示出來
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
                        imageview.setImageBitmap(bitmap);
                        //path.setText(imagepath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_CAMARA:
                if (resultCode == RESULT_OK) {
                    try {
                        //相簿照片顯示出來
                        Uri uri_photo = data.getData();
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri_photo));
                        imageview.setImageBitmap(bitmap);
                        path.setText(imagepath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private String imagetostring(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[] imagebytes = outputStream.toByteArray();
        String encode = Base64.encodeToString(imagebytes,Base64.DEFAULT);
        return encode;
    }
}
