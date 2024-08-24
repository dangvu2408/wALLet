package com.example.walletapp.Activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

import com.example.walletapp.Adapter.DropdownItemAdapter;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.NumberTextWatcher;
import com.example.walletapp.Utils.HeightUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText des, money_input;
    private AutoCompleteTextView autoComplete;
    private ImageView back;
    private Button btn_save, btn_delete_all;
    private TextView dateView;
    private LinearLayout dateWidget;
    private String datepicker;
    private Calendar datePicker = Calendar.getInstance();
    private String SRC_DATABASE_NAME = "app_database.db";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        back = findViewById(R.id.back_btn);
        autoComplete = findViewById(R.id.auto_complete);
        btn_save = findViewById(R.id.save_btn);
        btn_delete_all = findViewById(R.id.delete_all_data);
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

        String src = AddExpenseActivity.this.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);
        try {
            String sql = "CREATE TABLE userdata(mainType TEXT, type TEXT, money VARCHAR(255), date VARCHAR(255), description TEXT)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.d("DEBUG ERROR", "Table is already exits!");
        }

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
                    ContentValues values = new ContentValues();
                    values.put("mainType", "expense_money");
                    values.put("type", type);
                    values.put("money", money);
                    values.put("date", datepicker); // not yet
                    values.put("description", description);
                    if (database.insert("userdata", null, values) == -1) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_03, null);
                        Toast toast = new Toast(AddExpenseActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_02, null);
                        Toast toast = new Toast(AddExpenseActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                    finish();
                    overridePendingTransition(R.anim.close_in, R.anim.close_out);
                }
            }
        });

        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = database.delete("userdata", "mainType = ?", new String[]{"expense_money"});
                String msg = "";
                if (n == 0) {
                    msg = "no delete";
                } else {
                    msg = n + " deleted";
                }
                Toast.makeText(AddExpenseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
