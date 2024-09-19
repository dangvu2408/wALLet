package com.example.walletapp.Activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.TransModel;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class QueryActivity extends AppCompatActivity {
    private ImageView back_btn;
    private CardView begin_date_picker, end_date_picker;
    private Button query_btn;
    private LinearLayout no_data_query_in_range;
    private ListView transaction_query;
    private Calendar beginDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private TextView begin_day, end_day;
    private String begin_day_STR = "", end_day_STR = "";
    private ArrayList<TransModel> queryList;
    private QueryTransactionAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }
        setContentView(R.layout.activity_query_trans);
        back_btn = findViewById(R.id.back_btn);
        begin_date_picker = findViewById(R.id.begin_date_picker);
        end_date_picker = findViewById(R.id.end_date_picker);
        query_btn = findViewById(R.id.query_btn);
        no_data_query_in_range = findViewById(R.id.no_data_query_in_range);
        transaction_query = findViewById(R.id.transaction_query);
        begin_day = findViewById(R.id.begin_day);
        end_day = findViewById(R.id.end_day);


        queryList = new ArrayList<>();
        queryList = getIntent().getParcelableArrayListExtra("key_trans_value_from_bottomsheet");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
        begin_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog beginDialog = new DatePickerDialog(QueryActivity.this,
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

                DatePickerDialog endDialog = new DatePickerDialog(QueryActivity.this,
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

                adapter = new QueryTransactionAdapter(QueryActivity.this, filteredList);
                transaction_query.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                HeightUtils.setListViewHeight(transaction_query);

                if (transaction_query.getLayoutParams().height == 10) {
                    no_data_query_in_range.setVisibility(View.VISIBLE);
                } else {
                    no_data_query_in_range.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
