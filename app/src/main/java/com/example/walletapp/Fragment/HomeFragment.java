package com.example.walletapp.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Adapter.GridItemAddingAdapter;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Render.CustomBarChartRender;
import com.example.walletapp.Utils.HeightUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private boolean isOverlayVisible = false;
    Context context;
    QueryTransactionAdapter sortedDayAdapter, sortedMostAdapter;
    private ArrayList<TransactionItem> queryList;
    private String SRC_DATABASE_NAME = "app_database.db";
    TextView no_data_current, no_data, eye_balance;
    ListView currently, most_balance;
    SQLiteDatabase database;
    ImageView menu_top1, eye_view;
    boolean isEyeClose = true;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ScrollView fullLayout = view.findViewById(R.id.scroll_full_view);
        LinearLayout hidden = view.findViewById(R.id.layout_hidden);
        currently = view.findViewById(R.id.currently);
        no_data_current = view.findViewById(R.id.no_data_current);
        no_data = view.findViewById(R.id.no_data);
        most_balance = view.findViewById(R.id.most_balance);
        menu_top1 = view.findViewById(R.id.menu_top1);
        eye_view = view.findViewById(R.id.eye_view);
        eye_balance = view.findViewById(R.id.eye_balance);

        eye_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEyeClose) {
                    eye_view.setImageResource(R.drawable.eyeoff);
                    eye_balance.setText("3.000.000.000 VND");
                    isEyeClose = false;
                } else {
                    eye_view.setImageResource(R.drawable.eye);
                    eye_balance.setText("*** *** VND");
                    isEyeClose = true;
                }

            }
        });

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        menu_top1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });


        Button btn_total_revenue = view.findViewById(R.id.total_revenue);
        Button btn_total_expense = view.findViewById(R.id.total_expense);
        btn_total_expense.setBackgroundColor(0xffE5EDF4);
        btn_total_revenue.setBackgroundColor(0xffE5EDF4);
        BarChart balance_bar = view.findViewById(R.id.balance_bar_chart);
        this.context = getContext();








        queryList = new ArrayList<>();

        ArrayList<BarEntry> revenue = new ArrayList<>();
        revenue.add(new BarEntry(0f, 0f));
        revenue.add(new BarEntry(1f, 0f));
        ArrayList<String> months = new ArrayList<>();
        months.add("Tháng trước");
        months.add("Tháng này");

        Description des = new Description();
        des.setText("");
        balance_bar.setDescription(des);
        balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

        BarDataSet balance_bar_data_set = new BarDataSet(revenue, "");
        balance_bar_data_set.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
        BarData balance_bar_data = new BarData(balance_bar_data_set);

        balance_bar_data_set.setValueTextSize(12f);
        balance_bar_data_set.setColor(Color.GREEN);
        balance_bar_data_set.setLabel("N/A");
        balance_bar_data.setBarWidth(0.5f);
        balance_bar.setData(balance_bar_data);
        balance_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
        balance_bar.getXAxis().enableGridDashedLine(8f, 8f, 0f);
        balance_bar.getXAxis().setLabelCount(months.size());
        balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
        balance_bar.getAxisLeft().setEnabled(false);
        balance_bar.getAxisRight().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
        balance_bar.getAxisRight().enableGridDashedLine(8f, 8f, 0f);
        balance_bar.animateY(1000, Easing.EaseInOutCubic);
        balance_bar.getLegend().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));

        fullLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 0 && !isOverlayVisible) {
                    AlphaAnimation fade = new AlphaAnimation(0.0f, 1.0f);
                    fade.setDuration(200);
                    hidden.setAnimation(fade);
                    hidden.setVisibility(View.VISIBLE);
                    isOverlayVisible = true;
                } else if (scrollY == 0 && isOverlayVisible) {
                    AlphaAnimation fade = new AlphaAnimation(1.0f, 0.0f);
                    fade.setDuration(200);
                    hidden.setAnimation(fade);
                    hidden.setVisibility(View.GONE);
                    isOverlayVisible = false;
                }
            }
        });

        GridView addingGrid = view.findViewById(R.id.gridview_adding);
        List<GridItem> listAdding = new ArrayList<>();
        listAdding.add(new GridItem(R.drawable.expense01, "Khoản chi"));
        listAdding.add(new GridItem(R.drawable.income01, "Khoản thu"));
        listAdding.add(new GridItem(R.drawable.percentage01, "Lãi suất"));
        listAdding.add(new GridItem(R.drawable.target01, "Khoản vay"));
        GridItemAddingAdapter adapter = new GridItemAddingAdapter(this.getContext(), listAdding);
        addingGrid.setAdapter(adapter);
        HeightUtils.setGridViewHeight(addingGrid, 4);

        btn_total_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_total_revenue.setBackgroundResource(R.drawable.outline_green);
                btn_total_expense.setBackgroundColor(0xffE5EDF4);

                ArrayList<BarEntry> revenue = new ArrayList<>();
                revenue.add(new BarEntry(0f, 0f));
                revenue.add(new BarEntry(1f, AnalyzeFragment.sumOfInput.floatValue()));
                ArrayList<String> months = new ArrayList<>();
                months.add("Tháng trước");
                months.add("Tháng này");

                Description des = new Description();
                des.setText("");
                balance_bar.setDescription(des);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

                BarDataSet balance_bar_data_set = new BarDataSet(revenue, "");
                balance_bar_data_set.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
                BarData balance_bar_data = new BarData(balance_bar_data_set);
                balance_bar_data.setBarWidth(0.5f);
                balance_bar_data_set.setValueTextSize(12f);
                balance_bar_data_set.setColor(0xFF279CC5);
                balance_bar_data_set.setLabel("Tổng tiền vào (VND)");

                balance_bar.setData(balance_bar_data);
                balance_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
                balance_bar.getXAxis().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.getXAxis().setLabelCount(months.size());
                balance_bar.getAxisLeft().setEnabled(false);
                balance_bar.getAxisRight().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
                balance_bar.getAxisRight().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.animateY(1000, Easing.EaseInOutCubic);
                balance_bar.getLegend().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
                CustomBarChartRender barChartRender = new CustomBarChartRender(balance_bar, balance_bar.getAnimator(), balance_bar.getViewPortHandler());
                barChartRender.setRadius(15);
                barChartRender.initBuffers();
                balance_bar.setRenderer(barChartRender);
            }
        });

        btn_total_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_total_expense.setBackgroundResource(R.drawable.outline_red);
                btn_total_revenue.setBackgroundColor(0xffE5EDF4);

                ArrayList<BarEntry> expense = new ArrayList<>();
                expense.add(new BarEntry(0f, 0f));
                expense.add(new BarEntry(1f, Math.abs(AnalyzeFragment.sumOfOutput.floatValue())));
                ArrayList<String> months = new ArrayList<>();
                months.add("Tháng trước");
                months.add("Tháng này");

                Description des = new Description();
                des.setText("");
                balance_bar.setDescription(des);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

                BarDataSet balance_bar_data_set = new BarDataSet(expense, "");
                balance_bar_data_set.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
                BarData balance_bar_data = new BarData(balance_bar_data_set);
                balance_bar_data.setBarWidth(0.5f);
                balance_bar_data_set.setValueTextSize(12f);
                balance_bar_data_set.setColor(0xFFE45B65);
                balance_bar_data_set.setLabel("Tổng tiền ra (VND)");

                balance_bar.setData(balance_bar_data);
                balance_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
                balance_bar.getXAxis().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.getXAxis().setLabelCount(months.size());
                balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
                balance_bar.getAxisLeft().setEnabled(false);
                balance_bar.getAxisRight().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
                balance_bar.getAxisRight().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.animateY(1000, Easing.EaseInOutCubic);
                balance_bar.getLegend().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
                CustomBarChartRender barChartRender = new CustomBarChartRender(balance_bar, balance_bar.getAnimator(), balance_bar.getViewPortHandler());
                barChartRender.setRadius(15);
                barChartRender.initBuffers();
                balance_bar.setRenderer(barChartRender);
            }
        });

        String src = context.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);
        queryList.clear();
        Cursor cursor = database.query("userdata", null, null, null, null, null, null);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            TransactionItem item = new TransactionItem(cursor.getString(3), cursor.getString(2), cursor.getString(0), cursor.getString(1),  cursor.getString(4));
            queryList.add(item);
            cursor.moveToNext();

        }
        cursor.close();

        ArrayList<TransactionItem> sortedDayList = new ArrayList<>(queryList);
        ArrayList<TransactionItem> sortedMostList = new ArrayList<>(queryList);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));

        Collections.sort(sortedDayList, new Comparator<TransactionItem>() {
            @Override
            public int compare(TransactionItem o1, TransactionItem o2) {
                LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                return date2.compareTo(date1);
            }
        });

        if (sortedDayList.size() > 3) {
            sortedDayList.subList(3, sortedDayList.size()).clear();
        }

        Collections.sort(sortedMostList, new Comparator<TransactionItem>() {
            @Override
            public int compare(TransactionItem o1, TransactionItem o2) {
                return Float.compare(o2.getMoneyTransFloat(), o1.getMoneyTransFloat());
            }
        });

        if (sortedMostList.size() > 3) {
            sortedMostList.subList(3, sortedMostList.size()).clear();
        }

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
