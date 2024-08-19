package com.example.walletapp.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Activity.AddRevenueActivity;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryFragment extends Fragment {
    LinearLayout no_data_query;
    String datepicker, begin_day_STR = "", end_day_STR = "";
    String outputString = "";
    ListView query_result, currently, most_balance;
    Button query_btn;
    SQLiteDatabase database;
    TextView no_data_current, no_data, begin_day, end_day;
    Context context;
    private Calendar beginDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    CardView begin_date_picker, end_date_picker;
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
        end_day = view.findViewById(R.id.end_day);
        begin_day = view.findViewById(R.id.begin_day);
        begin_date_picker = view.findViewById(R.id.begin_date_picker);
        end_date_picker = view.findViewById(R.id.end_date_picker);

        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarBegin = Calendar.getInstance();
        calendarBegin.add(Calendar.DAY_OF_MONTH, -7);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDateEnd = dateFormat.format(calendarEnd.getTime());
        String formattedDateBegin = dateFormat.format(calendarBegin.getTime());
        end_day.setText(formattedDateEnd);
        begin_day.setText(formattedDateBegin);
        begin_day_STR = formattedDateBegin;
        end_day_STR = formattedDateEnd;

        this.context = getContext();
        queryList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
        begin_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog beginDialog = new DatePickerDialog(context,
                        R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        beginDate.set(Calendar.YEAR, year);
                        beginDate.set(Calendar.MONTH, month);
                        beginDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        begin_day_STR = dateFormat.format(beginDate.getTime());
                        begin_day.setText(begin_day_STR);
                    }
                }, beginDate.get(Calendar.YEAR), beginDate.get(Calendar.MONTH), beginDate.get(Calendar.DAY_OF_MONTH));

                beginDialog.show();
            }
        });

        end_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog endDialog = new DatePickerDialog(context,
                        R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.set(Calendar.YEAR, year);
                        endDate.set(Calendar.MONTH, month);
                        endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        end_day_STR = dateFormat.format(endDate.getTime());
                        end_day.setText(end_day_STR);
                    }
                }, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

                endDialog.show();
            }
        });




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

                Collections.sort(queryList, new Comparator<TransactionItem>() {
                    @Override
                    public int compare(TransactionItem o1, TransactionItem o2) {
                        LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                        LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                        return date1.compareTo(date2);
                    }
                });
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate beginDay = LocalDate.parse(begin_day_STR, formatter);
                LocalDate endDay = LocalDate.parse(end_day_STR, formatter);

                List<TransactionItem> filteredList = new ArrayList<>();
                for (TransactionItem item : queryList) {
                    LocalDate itemDate = item.getDateAsLocalDate();
                    if ((itemDate.isEqual(beginDay) || itemDate.isAfter(beginDay)) &&
                            (itemDate.isEqual(endDay) || itemDate.isBefore(endDay))) {
                        filteredList.add(item);
                    }
                }


                adapter = new QueryTransactionAdapter(context, filteredList);
                query_result.setAdapter(adapter);
                adapter.notifyDataSetChanged();

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
        currently.setAdapter(sortedDayAdapter);
        sortedDayAdapter.notifyDataSetChanged();
        HeightUtils.setListViewHeight(currently);

        sortedMostAdapter = new QueryTransactionAdapter(context, sortedMostList);
        most_balance.setAdapter(sortedMostAdapter);
        sortedMostAdapter.notifyDataSetChanged();
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
