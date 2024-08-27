package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.R;

public class VerifyInfoActivity extends AppCompatActivity {
    private Button confirm_btn;
    private TextView full_name_confirm, date_of_birth_confirm, gender_confirm, phone_number_confirm, user_login_confirm, password_confirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                startActivity(new Intent(VerifyInfoActivity.this, SigninSuccessActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
