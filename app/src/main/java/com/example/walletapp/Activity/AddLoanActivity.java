package com.example.walletapp.Activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import com.example.walletapp.Adapter.DropdownItemAdapter;
import com.example.walletapp.NumberTextWatcher;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddLoanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    SQLiteDatabase database;
    EditText des, money_input;
    AutoCompleteTextView autoComplete;
    ImageView back;
    Button btn_save;
    TextView dateView;
    LinearLayout dateWidget;
    String datepicker;
    ListView listDatabase;
    private String SRC_DATABASE_NAME = "app_database.db";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);
        back = findViewById(R.id.back_btn);
        btn_save = findViewById(R.id.save_btn_loan);
        autoComplete = findViewById(R.id.auto_complete_loan);
        des = findViewById(R.id.description_loan);
        money_input = findViewById(R.id.input_loan_money);
        dateView = findViewById(R.id.today_or_not);
        dateWidget = findViewById(R.id.date_picker_widget_loan);
        listDatabase = findViewById(R.id.list_database_test);

        Locale locale = new Locale("en", "US");
        int numDecs = 2;
        TextWatcher watcher = new NumberTextWatcher(this.money_input, locale, numDecs);
        this.money_input.addTextChangedListener(watcher);

        ArrayList<String> list01 = new ArrayList<>();
        ArrayAdapter<String> adapter01 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list01);
        listDatabase.setAdapter(adapter01);
        HeightUtils.setListViewHeight(listDatabase);


        String[] list = getResources().getStringArray(R.array.list_loan);
        DropdownItemAdapter adapter = new DropdownItemAdapter(this, Arrays.asList(list));
        autoComplete.setDropDownBackgroundResource(R.color.color_01);
        autoComplete.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatePickerDialog dialog = new DatePickerDialog(this, this::onDateSet, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dateWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        String src = AddLoanActivity.this.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
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
                ContentValues values = new ContentValues();
                values.put("mainType", "loan_money");
                values.put("type", type);
                values.put("money", money);
                values.put("date", datepicker); // not yet
                values.put("description", description);
                String msg = "";
                if (database.insert("userdata", null, values) == -1) {
                    msg = "Fail!";
                } else {
                    msg = "Sucess!";
                }
                Toast.makeText(AddLoanActivity.this, msg, Toast.LENGTH_SHORT).show();

                list01.clear();
                Cursor cursor = database.query("userdata", null, null, null, null, null, null);
                cursor.moveToNext();
                String data = "";
                while (cursor.isAfterLast() == false) {
                    data = cursor.getString(0) + " - "
                            + cursor.getString(1) + " - "
                            + cursor.getString(2) + " - "
                            + cursor.getString(3) + " - "
                            + cursor.getString(4);
                    cursor.moveToNext();
                    list01.add(data);
                }
                cursor.close();
                adapter01.notifyDataSetChanged();
//                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selected = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        this.datepicker = selected;
        dateView.setText(selected);
    }
}
