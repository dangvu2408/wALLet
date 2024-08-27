package com.example.walletapp.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.R;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout login_btn;
    private TextView create_account;
    private SQLiteDatabase database;
    private String SRC_DATABASE_NAME = "app_database.db";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = findViewById(R.id.login_btn);
        create_account = findViewById(R.id.create_account);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String src = getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
                database = SQLiteDatabase.openOrCreateDatabase(src, null);
                database.delete("userdata", null, null);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
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

}
