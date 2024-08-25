package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walletapp.R;

public class PhoneNumberActivity extends AppCompatActivity {
    private LinearLayout back_to_main, next_to_otp_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        back_to_main = findViewById(R.id.back_to_main);
        next_to_otp_btn = findViewById(R.id.next_to_otp_btn);


        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,  0);
            }
        });

        next_to_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneNumberActivity.this, OTPActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }
}
