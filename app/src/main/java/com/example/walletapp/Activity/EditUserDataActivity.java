package com.example.walletapp.Activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditUserDataActivity extends AppCompatActivity {
    private ImageView back_btn;
    private EditText name_edit, gender_edit;
    private TextView today_or_not;
    private String name_str, date_str, gender_str, username_str;
    private LinearLayout pick_day_now;
    private Calendar datePicker = Calendar.getInstance();
    private Button user_edit_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }
        setContentView(R.layout.activity_edit_user_data);
        back_btn = findViewById(R.id.back_btn);
        name_edit = findViewById(R.id.name_edit);
        today_or_not = findViewById(R.id.today_or_not);
        gender_edit = findViewById(R.id.gender_edit);
        pick_day_now = findViewById(R.id.pick_day_now);
        user_edit_btn = findViewById(R.id.user_edit_btn);
        name_str = getIntent().getStringExtra("key_fullname_a");
        date_str = getIntent().getStringExtra("key_dateofbirth_a");
        gender_str = getIntent().getStringExtra("key_gender_a");
        username_str = getIntent().getStringExtra("key_username_a");

        name_edit.setHint(name_str);
        today_or_not.setText(date_str);
        gender_edit.setHint(gender_str);



        pick_day_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog endDialog = new DatePickerDialog(EditUserDataActivity.this,
                        R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker.set(Calendar.YEAR, year);
                        datePicker.set(Calendar.MONTH, month);
                        datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String str = dateFormat.format(datePicker.getTime());
                        date_str = str;
                        today_or_not.setText(str);
                        today_or_not.setTextColor(0xFF000000);
                    }
                }, datePicker.get(Calendar.YEAR), datePicker.get(Calendar.MONTH), datePicker.get(Calendar.DAY_OF_MONTH));

                endDialog.show();
            }
        });


        user_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_name, edt_date, edt_gen;
                if (name_edit.getText().toString().equals("")) {
                    edt_name = name_edit.getHint().toString();
                } else {
                    edt_name = name_edit.getText().toString();
                }
                edt_date = date_str;
                if (gender_edit.getText().toString().equals("")) {
                    edt_gen = gender_edit.getHint().toString();;
                } else {
                    edt_gen = gender_edit.getText().toString();
                }
                updateUserData(Constants.BASE_URL_UPDATE_USER_DATA, username_str, edt_name, edt_date, edt_gen);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }

    private void updateUserData(String url, String str1, String str2, String str3, String str4) {
        Log.d("NULL VALUE", str1 + " - " + str2 + " - " +
                str3 + " - " + str4);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_12, null);
                            Toast toast = new Toast(EditUserDataActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            finish();
                            overridePendingTransition(R.anim.close_in, R.anim.close_out);
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_13, null);
                            Toast toast = new Toast(EditUserDataActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_14, null);
                        Toast toast = new Toast(EditUserDataActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("DEBUG SERVER ERROR", "Params func: " + str1 + " - "
                        + str2 + " - " + str3 + " - " + str4);
                params.put("updateUsername", str1);
                params.put("updateFullname", str2);
                params.put("updateDateofbirth", str3);
                params.put("updateGender", str4);
                return params;
            }
        };
        queue.add(strRequest);
    }
}
