package com.example.logintest;

// u10716030@go.utaipei.edu.tw

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;

public class email_otp extends AppCompatActivity{

    int code;
    EditText passcode1, passcode2, passcode3, passcode4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_otp);
        passcode1 = findViewById(R.id.digit1);
        passcode2 = findViewById(R.id.digit2);
        passcode3 = findViewById(R.id.digit3);
        passcode4 = findViewById(R.id.digit4);

        passcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    passcode2.requestFocus();
                }
            }
        });

        passcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    passcode3.requestFocus();
                }
            }
        });

        passcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    passcode4.requestFocus();
                }
            }
        });
    }


    public void sendVerifyEmail(View view) {
        SecureRandom rand = new SecureRandom();
        code = rand.nextInt(8999) + 1000;
        EditText emailTXT = findViewById(R.id.email);
        String url = "http://163.21.245.178/login/sendEmail.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(email_otp.this, response, Toast.LENGTH_SHORT).show();
                findViewById(R.id.box1).setVisibility(View.GONE);
                findViewById(R.id.box2).setVisibility(View.VISIBLE);
                System.out.println(response);
                // System.out.println("Here~");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(email_otp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailTXT.getText().toString());
                params.put("code", String.valueOf(code));
                return params;
            }
        };
        queue.add(request);
    }

    public void checkCode(View view) {
        String inputCode;
        inputCode = passcode1.getText().toString() + passcode2.getText().toString() + passcode3.getText().toString() + passcode4.getText().toString();

        if (inputCode.equals(String.valueOf(code))) {
            Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
        }
    }
}

