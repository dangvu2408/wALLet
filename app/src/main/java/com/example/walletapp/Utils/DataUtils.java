package com.example.walletapp.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.walletapp.Model.TransactionItem;

import java.util.ArrayList;

public class DataUtils {
    public static DataUtils instance;
    private Context context;
    private String SRC_DATABASE_NAME = "app_database.db";
    private ArrayList<TransactionItem> queryList;
    private SQLiteDatabase database;
    public static DataUtils getInstance() {
        if (instance == null) {
            instance = new DataUtils();
        }
        return instance;
    }

    public void login(Context context) {
        this.context = context;
        getData();
    }

    public void getData() {
        queryList = new ArrayList<>();
        String src = this.context.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);
        Cursor cursor = database.query("userdata", null, null, null, null, null, null);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            TransactionItem item = new TransactionItem(cursor.getString(3), cursor.getString(2), cursor.getString(0), cursor.getString(1),  cursor.getString(4));
            queryList.add(item);
            cursor.moveToNext();
        }
        cursor.close();
    }
}
