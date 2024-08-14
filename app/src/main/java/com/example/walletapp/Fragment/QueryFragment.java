package com.example.walletapp.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Activity.AddRevenueActivity;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.util.ArrayList;

public class QueryFragment extends Fragment {
    LinearLayout no_data_query;
    ListView query_result;
    Button query_btn;
    SQLiteDatabase database;
    Context context;
    private String SRC_DATABASE_NAME = "app_database.db";
    public QueryFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_fragment, container, false);
        no_data_query = view.findViewById(R.id.no_data_query_in_range);
        query_result = view.findViewById(R.id.transaction_query);
        query_btn = view.findViewById(R.id.query_btn);
        this.context = getContext();
        no_data_query.setVisibility(View.VISIBLE);
        ArrayList<TransactionItem> queryList = new ArrayList<>();
        QueryTransactionAdapter adapter = new QueryTransactionAdapter(context, queryList);
        query_result.setAdapter(adapter);
        HeightUtils.setListViewHeight(query_result);
        if (queryList == null) {
            no_data_query.setVisibility(View.VISIBLE);
        } else {
            no_data_query.setVisibility(View.GONE);
        }

        String src = context.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.query("userdata", null, null, null, null, null, null);
                cursor.moveToNext();
                while (cursor.isAfterLast() == false) {
                    TransactionItem item = new TransactionItem(cursor.getString(3), cursor.getString(2), cursor.getString(0), cursor.getString(1),  cursor.getString(4));
                    cursor.moveToNext();
                    queryList.add(item);
                }
                cursor.close();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
