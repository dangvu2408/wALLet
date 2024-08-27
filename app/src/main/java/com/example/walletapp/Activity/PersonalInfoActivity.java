package com.example.walletapp.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PersonalInfoActivity extends AppCompatActivity {
    private LinearLayout next_to_verify_btn, date_picker_widget;
    private Calendar datePicker = Calendar.getInstance();
    private String dob_picker, gender_picker;
    private TextView today_or_not;
    private RadioGroup gender_group;
    private EditText username_input;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        next_to_verify_btn = findViewById(R.id.next_to_verify_btn);
        date_picker_widget = findViewById(R.id.date_picker_widget);
        today_or_not = findViewById(R.id.today_or_not);
        gender_group = findViewById(R.id.gender_group);
        username_input = findViewById(R.id.username_input);

        gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male_radio) {
                    gender_picker = "Nam";
                } else if (checkedId == R.id.female_radio) {
                    gender_picker = "Nữ";
                }
            }
        });



        date_picker_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog endDialog = new DatePickerDialog(PersonalInfoActivity.this,
                        R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker.set(Calendar.YEAR, year);
                        datePicker.set(Calendar.MONTH, month);
                        datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String str = dateFormat.format(datePicker.getTime());
                        dob_picker = str;
                        today_or_not.setText(str);
                        today_or_not.setTextColor(0xFF000000);
                    }
                }, datePicker.get(Calendar.YEAR), datePicker.get(Calendar.MONTH), datePicker.get(Calendar.DAY_OF_MONTH));
                endDialog.show();
            }
        });

        String phone_num_otp = getIntent().getStringExtra("key_phone_number");
        String str_password = getIntent().getStringExtra("pass_word");

        next_to_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Date Format: " + dob_picker);
                Log.d("DEBUG", "Gender: " + gender_picker);
                String hoten = username_input.getText().toString();
                Intent intent = new Intent(PersonalInfoActivity.this, VerifyInfoActivity.class);
                intent.putExtra("Full_name", hoten);
                intent.putExtra("Date_of_birth", dob_picker);
                intent.putExtra("Gender", gender_picker);
                intent.putExtra("key_phone_number", phone_num_otp);
                intent.putExtra("pass_word", str_password);
                startActivity(intent);
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
