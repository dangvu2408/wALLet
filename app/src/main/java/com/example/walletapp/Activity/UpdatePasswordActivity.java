package com.example.walletapp.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordActivity extends AppCompatActivity {
    private ImageView back_btn;
    private LinearLayout password_wrong;
    private Button update_pass_btn;
    private TextInputEditText old_password, new_password;
    private String oldPassStr, newPassStr, usernameStr;
    private ImageView check_length, check_capital_letter, check_number;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }
        setContentView(R.layout.activity_update_password);

        back_btn = findViewById(R.id.back_btn);
        password_wrong = findViewById(R.id.password_wrong);
        update_pass_btn = findViewById(R.id.update_pass_btn);
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        check_length = findViewById(R.id.check_length);
        check_capital_letter = findViewById(R.id.check_capital_letter);
        check_number = findViewById(R.id.check_number);
        usernameStr = getIntent().getStringExtra("key_username_b");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });

        new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (input.length() >= 7 && input.length() <= 20) {
                    check_length.setImageResource(R.drawable.checkcirclegreen);
                } else {
                    check_length.setImageResource(R.drawable.checkcirclegray);
                }
                if (input.matches(".*[A-Z].*") && input.matches(".*[a-z].*")) {
                    check_capital_letter.setImageResource(R.drawable.checkcirclegreen);
                } else {
                    check_capital_letter.setImageResource(R.drawable.checkcirclegray);
                }
                if (input.matches(".*\\d.*")) {
                    check_number.setImageResource(R.drawable.checkcirclegreen);
                } else {
                    check_number.setImageResource(R.drawable.checkcirclegray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        update_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword(Constants.BASE_URL_UPDATE_PASSWORD, usernameStr, old_password.getText().toString(), new_password.getText().toString());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }

    private void updatePassword(String url, String user, String pass, String newpass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Update uccess")) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.custom_toast_18, null);
                            Toast toast = new Toast(UpdatePasswordActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            finish();
                            overridePendingTransition(R.anim.close_in, R.anim.close_out);
                            password_wrong.setVisibility(View.GONE);
                        } else if (response.trim().equals("Update error")) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.custom_toast_19, null);
                            Toast toast = new Toast(UpdatePasswordActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            password_wrong.setVisibility(View.GONE);
                        } else if (response.trim().equals("Error")) {
                            password_wrong.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Nullable
            @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("usernameUpdateA", user);
                    params.put("passwordUpdateA", pass);
                    params.put("passwordUpdateB", newpass);
                    Log.d("UPDATE PASS DEBUG", "Damn: " + user + " " + pass + " " + newpass);
                    return params;
                }
        };
        queue.add(strRequest);
    }
}
