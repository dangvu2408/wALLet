package com.example.walletapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.walletapp.Constants;
import com.example.walletapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout login_btn, fill_username_now, fill_pass_now;
    private TextView create_account;
    private EditText username_input;
    private TextInputEditText password_input;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);
        login_btn = findViewById(R.id.login_btn);
        create_account = findViewById(R.id.create_account);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        fill_username_now = findViewById(R.id.fill_username_now);
        fill_pass_now = findViewById(R.id.fill_pass_now);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username_input.getText().toString().trim();
                String pass = password_input.getText().toString().trim();
                if (user.equals("")) {
                    fill_username_now.setVisibility(View.VISIBLE);
                } else if (pass.equals("")) {
                    fill_pass_now.setVisibility(View.VISIBLE);
                    fill_username_now.setVisibility(View.GONE);
                } else {
                    fill_username_now.setVisibility(View.GONE);
                    fill_pass_now.setVisibility(View.GONE);
                    login(Constants.BASE_URL_LOGIN, user, pass);
                }
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PhoneNumberActivity.class));
                overridePendingTransition(0,  0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void login(String url, String str1, String str2) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_17, null);
                            Toast toast = new Toast(LoginActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            String phonenumSTR = username_input.getText().toString().trim();
                            intent.putExtra("key_data", phonenumSTR);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                            finish();
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_16, null);
                            Toast toast = new Toast(LoginActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_14, null);
                        Toast toast = new Toast(LoginActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usernameSignin", str1);
                params.put("passwordSignin", str2);
                return params;
            }
        };
        queue.add(strRequest);
    }
}
