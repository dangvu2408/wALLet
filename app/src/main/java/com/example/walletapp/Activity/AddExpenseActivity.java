package com.example.walletapp.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walletapp.Adapter.DropdownItemAdapter;
import com.example.walletapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    SQLiteDatabase database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        ImageView back = findViewById(R.id.back_btn);
        AutoCompleteTextView autoComplete = findViewById(R.id.auto_complete);
        Button btn_save = findViewById(R.id.save_btn);
        String[] list = getResources().getStringArray(R.array.list_expense);
        DropdownItemAdapter adapter = new DropdownItemAdapter(this, Arrays.asList(list));
        autoComplete.setDropDownBackgroundResource(R.color.color_01);
        autoComplete.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        database = SQLiteDatabase.openOrCreateDatabase("app_database.db", null);
        try {
            String sql = "CREATE TABLE userdata(mainType VARCHAR(255), type VARCHAR(255), money INTERGER, date VARCHAR(255), description VARCHAR(255))";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.d("DEBUG ERROR", "Table is already exits!");
        }
    }
}
