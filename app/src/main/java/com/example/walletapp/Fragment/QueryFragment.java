package com.example.walletapp.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Activity.AddRevenueActivity;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class QueryFragment extends Fragment {
    LinearLayout no_data_query;
    ListView query_result, currently, most_balance;
    Button query_btn;
    SQLiteDatabase database;
    TextView no_data_current, no_data;
    Context context;
    QueryTransactionAdapter adapter, sortedDayAdapter, sortedMostAdapter;
    private ArrayList<TransactionItem> queryList;
    private String SRC_DATABASE_NAME = "app_database.db";
    public QueryFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_fragment, container, false);
        no_data_query = view.findViewById(R.id.no_data_query_in_range);
        query_result = view.findViewById(R.id.transaction_query);
        query_btn = view.findViewById(R.id.query_btn);
        currently = view.findViewById(R.id.currently);
        no_data_current = view.findViewById(R.id.no_data_current);
        most_balance = view.findViewById(R.id.most_balance);
        no_data = view.findViewById(R.id.no_data);

        this.context = getContext();
        queryList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));


        String src = context.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryList.clear();
                Cursor cursor = database.query("userdata", null, null, null, null, null, null);
                cursor.moveToNext();
                while (!cursor.isAfterLast()) {
                    TransactionItem item = new TransactionItem(cursor.getString(3), cursor.getString(2), cursor.getString(0), cursor.getString(1),  cursor.getString(4));
                    queryList.add(item);
                    cursor.moveToNext();

                }
                cursor.close();
                Collections.reverse(queryList);
                adapter = new QueryTransactionAdapter(context, queryList);
                query_result.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                HeightUtils.setListViewHeight(currently);
                HeightUtils.setListViewHeight(query_result);

                if (query_result.getLayoutParams().height == 10) {
                    no_data_query.setVisibility(View.VISIBLE);
                } else {
                    no_data_query.setVisibility(View.GONE);
                }
            }
        });


        Cursor cursorSort = database.query("userdata", null, null, null, null, null, null);
        cursorSort.moveToNext();
        while (!cursorSort.isAfterLast()) {
            TransactionItem item = new TransactionItem(cursorSort.getString(3), cursorSort.getString(2), cursorSort.getString(0), cursorSort.getString(1),  cursorSort.getString(4));
            this.queryList.add(item);
            cursorSort.moveToNext();

        }
        cursorSort.close();

        ArrayList<TransactionItem> sortedDayList = new ArrayList<>(queryList);
        ArrayList<TransactionItem> sortedMostList = new ArrayList<>(queryList);
        Collections.sort(sortedDayList, new Comparator<TransactionItem>() {
            @Override
            public int compare(TransactionItem o1, TransactionItem o2) {
                LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                return date2.compareTo(date1);
            }
        });



        Collections.sort(sortedMostList, new Comparator<TransactionItem>() {
            @Override
            public int compare(TransactionItem o1, TransactionItem o2) {
                return Float.compare(o2.getMoneyTransFloat(), o1.getMoneyTransFloat());
            }
        });



        sortedDayAdapter = new QueryTransactionAdapter(context, sortedDayList);
        sortedDayAdapter.notifyDataSetChanged();
        currently.setAdapter(sortedDayAdapter);
        HeightUtils.setListViewHeight(currently);

        sortedMostAdapter = new QueryTransactionAdapter(context, sortedMostList);
        sortedMostAdapter.notifyDataSetChanged();
        most_balance.setAdapter(sortedMostAdapter);
        HeightUtils.setListViewHeight(most_balance);

        if (currently.getLayoutParams().height == 10) {
            no_data_current.setVisibility(View.VISIBLE);
        } else {
            no_data_current.setVisibility(View.GONE);
        }

        if (most_balance.getLayoutParams().height == 10) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
        return view;
    }
}
