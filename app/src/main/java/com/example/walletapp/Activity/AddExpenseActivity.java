package com.example.walletapp.Activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.walletapp.Adapter.DropdownItemAdapter;
import com.example.walletapp.Constants;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.NumberTextWatcher;
import com.example.walletapp.Utils.HeightUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText des, money_input;
    private AutoCompleteTextView autoComplete;
    private ImageView back;
    private Button btn_save;
    private TextView dateView;
    private LinearLayout dateWidget;
    private String datepicker, number, fullname;
    private Calendar datePicker = Calendar.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_add_expense);
        back = findViewById(R.id.back_btn);
        autoComplete = findViewById(R.id.auto_complete);
        btn_save = findViewById(R.id.save_btn);
        des = findViewById(R.id.description_expense);
        money_input = findViewById(R.id.input_expense_money);
        dateView = findViewById(R.id.today_or_not);
        dateWidget = findViewById(R.id.date_picker_widget);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }

        Locale locale = new Locale("en", "US");
        int numDecs = 2;
        TextWatcher watcher = new NumberTextWatcher(this.money_input, locale, numDecs);
        this.money_input.addTextChangedListener(watcher);

        String[] list = getResources().getStringArray(R.array.list_expense);
        DropdownItemAdapter adapter = new DropdownItemAdapter(this, Arrays.asList(list));
        autoComplete.setDropDownBackgroundResource(R.color.color_01);
        autoComplete.setAdapter(adapter);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });

        dateWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    DatePickerDialog endDialog = new DatePickerDialog(AddExpenseActivity.this,
                        R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker.set(Calendar.YEAR, year);
                        datePicker.set(Calendar.MONTH, month);
                        datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String selected = DateFormat.getDateInstance(DateFormat.FULL).format(datePicker.getTime());
                        datepicker = selected;
                        dateView.setText(selected);
                    }
                }, datePicker.get(Calendar.YEAR), datePicker.get(Calendar.MONTH), datePicker.get(Calendar.DAY_OF_MONTH));

                endDialog.show();
            }
        });

        Intent intent = getIntent();
        number = intent.getStringExtra("key_username_data");
        fullname = intent.getStringExtra("key_fullname_data");
        Log.d("NULL VALUE", number + " - " + fullname);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = des.getText().toString();
                String type = autoComplete.getText().toString();
                String money = money_input.getText().toString();
                if (money_input.getText().toString().equals("")) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_01, null);
                    Toast toast = new Toast(AddExpenseActivity.this);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (autoComplete.getText().toString().equals("Chọn nhóm khoản chi")) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_04, null);
                    Toast toast = new Toast(AddExpenseActivity.this);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else {
                    addTransExpense(Constants.BASE_URL_INSERT_TRANS_DATA, "expense_money", type, money, datepicker, description, number, fullname);
                }
            }
        });
    }

    private void addTransExpense(String url, String str1, String str2, String str3, String str4, String str5, String str6, String str7) {
        Log.d("NULL VALUE", str1 + " - " + str2 + " - " +
                str3 + " - " + str4 + " - " + str5 + " - " + str6 + " - " + str7);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_02, null);
                            Toast toast = new Toast(AddExpenseActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            finish();
                            overridePendingTransition(R.anim.close_in, R.anim.close_out);
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_03, null);
                            Toast toast = new Toast(AddExpenseActivity.this);
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
                        Toast toast = new Toast(AddExpenseActivity.this);
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
                        + str2 + " - " + str3 + " - " + str4 + " - " + str5 + " - " +
                        str6 + " - " + str7);
                params.put("insertMainType", str1);
                params.put("insertType", str2);
                params.put("insertMoney", str3);
                params.put("insertDate", str4);
                params.put("insertDescription", str5);
                params.put("insertUserID", str6);
                params.put("insertUserfullname", str7);
                return params;
            }
        };
        queue.add(strRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
