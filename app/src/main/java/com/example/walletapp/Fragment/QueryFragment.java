package com.example.walletapp.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Activity.AddRevenueActivity;
import com.example.walletapp.Activity.NotificationActivity;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.TransModel;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryFragment extends Fragment {
    private LinearLayout no_data_query;
    private String begin_day_STR = "", end_day_STR = "";
    private ListView query_result;
    private Button query_btn;
    private TextView begin_day, end_day;
    private Context context;
    private Calendar beginDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private CardView begin_date_picker, end_date_picker;
    private QueryTransactionAdapter adapter;
    private ArrayList<TransModel> queryList;
    ImageView menu_top, search_top, notification_bell;

    private String[] items;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter1;
    public QueryFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_fragment, container, false);
        no_data_query = view.findViewById(R.id.no_data_query_in_range);
        query_result = view.findViewById(R.id.transaction_query);
        query_btn = view.findViewById(R.id.query_btn);
        end_day = view.findViewById(R.id.end_day);
        begin_day = view.findViewById(R.id.begin_day);
        begin_date_picker = view.findViewById(R.id.begin_date_picker);
        end_date_picker = view.findViewById(R.id.end_date_picker);

        menu_top = view.findViewById(R.id.menu_top);
        search_top = view.findViewById(R.id.search_top);
        notification_bell = view.findViewById(R.id.notification_bell);

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        queryList = new ArrayList<>();
        queryList = getArguments().getParcelableArrayList("trans_data_key"); //important

        menu_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        notification_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            }
        });

        search_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(context);
                View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);
                dialog.setContentView(view);
                dialog.show();

                EditText searching_input = view.findViewById(R.id.searching_input);
                ListView list_searching = view.findViewById(R.id.list_searching);

                initList();
                list_searching.setAdapter(adapter1);

                searching_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter1.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });



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



        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TransModel> filteredList = new ArrayList<>();
                if (queryList != null) {
                    Collections.sort(queryList, new Comparator<TransModel>() {
                        @Override
                        public int compare(TransModel o1, TransModel o2) {
                            LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                            LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                            return date1.compareTo(date2);
                        }
                    });
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate beginDay = LocalDate.parse(begin_day_STR, formatter);
                    LocalDate endDay = LocalDate.parse(end_day_STR, formatter);


                    for (TransModel item : queryList) {
                        LocalDate itemDate = item.getDateAsLocalDate();
                        if ((itemDate.isEqual(beginDay) || itemDate.isAfter(beginDay)) &&
                                (itemDate.isEqual(endDay) || itemDate.isBefore(endDay))) {
                            filteredList.add(item);
                        }
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
        return view;
    }

    public void initList() {
        items = getResources().getStringArray(R.array.searching_list);
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter1 = new ArrayAdapter<String>(context, R.layout.searching_listitem, listItems);
    }

}
