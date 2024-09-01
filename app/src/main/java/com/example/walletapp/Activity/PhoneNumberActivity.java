package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {
    private LinearLayout back_to_main, next_to_otp_btn, note_fill, note_already_exits;
    private EditText phone_number_input;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_create_new_account);
        back_to_main = findViewById(R.id.back_to_main);
        next_to_otp_btn = findViewById(R.id.next_to_otp_btn);
        phone_number_input = findViewById(R.id.phone_number_input);
        note_already_exits = findViewById(R.id.note_already_exits);
        note_fill = findViewById(R.id.note_fill);
        note_fill.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }

        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,  0);
            }
        });

        phone_number_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (input.equals("")) {
                    note_fill.setVisibility(View.VISIBLE);
                    note_already_exits.setVisibility(View.GONE);
                    next_to_otp_btn.setOnClickListener(null);
                } else {
                    note_fill.setVisibility(View.GONE);
                    note_already_exits.setVisibility(View.GONE);
                    next_to_otp_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String phone_num = phone_number_input.getText().toString();
                            checkPhoneNum(Constants.BASE_URL_PHONE_NUMBER_EXITS, phone_num);
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void checkPhoneNum(String url, String phone) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Already")) {
                            note_already_exits.setVisibility(View.VISIBLE);
                        } else {
                            note_already_exits.setVisibility(View.GONE);
                            Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                            intent.putExtra("key_phone_number", phone);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PhoneNumberActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "SERVER ERROR");
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usernameSignin", phone);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
