package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.chaos.view.PinView;
import com.example.walletapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OTPActivity extends AppCompatActivity {
    private TextView countdown_time_otp, resend_otp;
    private LinearLayout next_to_pass_btn;
    private PinView firstPinView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        countdown_time_otp = findViewById(R.id.countdown_time_otp);
        resend_otp = findViewById(R.id.resend_otp);
        next_to_pass_btn = findViewById(R.id.next_to_pass_btn);
        firstPinView = findViewById(R.id.firstPinView);
        new CountDownTimer(20*600, 1000) {
            public void onTick(long millisUntilFinished) {
                countdown_time_otp.setText("Bạn có thể yêu cầu gửi mã mới sau " + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
                resend_otp.setOnClickListener(null);
            }
            public void onFinish() {
                countdown_time_otp.setText("Bạn đã có thể yêu cầu gửi mã mới");
                resend_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CountDownTimer(20*600, 1000) {
                            public void onTick(long millisUntilFinished) {
                                countdown_time_otp.setText("Bạn có thể yêu cầu gửi mã mới sau " + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
                            }
                            public void onFinish() {
                                countdown_time_otp.setText("Bạn đã có thể yêu cầu gửi mã mới");
                            }
                        }.start();
                    }
                });
            }
        }.start();

        next_to_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTPActivity.this, PasswordActivity.class));
                overridePendingTransition(0, 0);
            }
        });

    }
}
