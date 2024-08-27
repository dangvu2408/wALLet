package com.example.walletapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class PasswordActivity extends AppCompatActivity {
    private ImageView check_length, check_capital_letter, check_number;
    private TextInputEditText password_input;
    private LinearLayout next_to_person_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        check_length = findViewById(R.id.check_length);
        check_capital_letter = findViewById(R.id.check_capital_letter);
        check_number = findViewById(R.id.check_number);
        password_input = findViewById(R.id.password_input);
        next_to_person_btn = findViewById(R.id.next_to_person_btn);

        password_input.addTextChangedListener(new TextWatcher() {
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

        String phone_num_otp = getIntent().getStringExtra("key_phone_number");

        next_to_person_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_pass = password_input.getText().toString();
                Intent intent = new Intent(PasswordActivity.this, PersonalInfoActivity.class);
                intent.putExtra("key_phone_number", phone_num_otp);
                intent.putExtra("pass_word", str_pass);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialog = inflater.inflate(R.layout.dialog_cancel_disable, null);
        builder.setView(dialog);
        AlertDialog mainDialog = builder.create();
        mainDialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent_background);
        ImageView close_dialog = dialog.findViewById(R.id.close_dialog);
        Button back_to_login_btn = dialog.findViewById(R.id.back_to_login_btn);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDialog.dismiss();
            }
        });
        back_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        mainDialog.show();
    }
}
