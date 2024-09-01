package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class VerifyInfoActivity extends AppCompatActivity {
    private Button confirm_btn;
    private TextView full_name_confirm, date_of_birth_confirm, gender_confirm, phone_number_confirm, user_login_confirm, password_confirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_verify_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }


        confirm_btn = findViewById(R.id.confirm_btn);
        full_name_confirm = findViewById(R.id.full_name_confirm);
        date_of_birth_confirm = findViewById(R.id.date_of_birth_confirm);
        gender_confirm = findViewById(R.id.gender_confirm);
        phone_number_confirm = findViewById(R.id.phone_number_confirm);
        user_login_confirm = findViewById(R.id.user_login_confirm);
        password_confirm = findViewById(R.id.password_confirm);

        String phone_num_otp = getIntent().getStringExtra("key_phone_number");
        String str_password = getIntent().getStringExtra("pass_word");
        String fullname = getIntent().getStringExtra("Full_name");
        String dateob = getIntent().getStringExtra("Date_of_birth");
        String gender = getIntent().getStringExtra("Gender");

        full_name_confirm.setText(fullname.toUpperCase());
        date_of_birth_confirm.setText(dateob);
        gender_confirm.setText(gender);
        phone_number_confirm.setText(phone_num_otp);
        user_login_confirm.setText(phone_num_otp);
        password_confirm.setText(str_password);


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG SERVER ERROR", "Params: " + phone_num_otp + " - "
                        + str_password + " - " + fullname + " - " + dateob + " - " + gender);
                insertData(Constants.BASE_URL_ADD_USER, phone_num_otp, str_password, fullname, dateob, gender);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void insertData(String url, String str1, String str2, String str3, String str4, String str5) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            startActivity(new Intent(VerifyInfoActivity.this, SigninSuccessActivity.class));
                            overridePendingTransition(0, 0);
                        } else {
                            Toast.makeText(VerifyInfoActivity.this, "Thêm người dùng thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerifyInfoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "SERVER ERROR");
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("DEBUG SERVER ERROR", "Params func: " + str1 + " - "
                        + str2 + " - " + str3 + " - " + str4 + " - " + str5);
                params.put("usernameSignin", str1);
                params.put("passwordSignin", str2);
                params.put("fullnameSignin", str3);
                params.put("dobSignin", str4);
                params.put("genderSignin", str5);
                return params;
            }
        };
        requestQueue.add(strRequest);
    }
}
