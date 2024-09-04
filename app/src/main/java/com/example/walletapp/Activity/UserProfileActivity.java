package com.example.walletapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.R;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView back_btn, home;
    private String username, full_name;
    private TextView user_full_name, userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }
        home = findViewById(R.id.home);
        back_btn = findViewById(R.id.back_btn);
        user_full_name = findViewById(R.id.user_full_name);
        userid = findViewById(R.id.userid);

        username = getIntent().getStringExtra("key_username_a");
        full_name = getIntent().getStringExtra("key_fullname_a");
        Log.d("USER PROFILE DEBUG", "String test 02: " + username + " " + full_name);

        user_full_name.setText(full_name);

        String userText = "User ID: ";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString blackText = new SpannableString(userText);
        blackText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, userText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString mainColorText = new SpannableString(username);
        mainColorText.setSpan(new ForegroundColorSpan(0xFF141ED4), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(blackText);
        builder.append(mainColorText);
        userid.setText(builder);

        Log.d("USER PROFILE DEBUG", "String test: " + username + " " + full_name);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                intent.putExtra("key_data", username);
                startActivity(new Intent(intent));
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
