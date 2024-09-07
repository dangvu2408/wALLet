package com.example.walletapp.Fragment;

import static java.lang.Math.abs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.walletapp.Activity.HighestTransActivity;
import com.example.walletapp.Activity.LoginActivity;
import com.example.walletapp.Activity.RecentTransActivity;
import com.example.walletapp.Activity.SplashActivity;
import com.example.walletapp.Adapter.GridItemAddingAdapter;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.Model.TransModel;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private boolean isOverlayVisible = false;
    private Context context;
    private QueryTransactionAdapter sortedDayAdapter, sortedMostAdapter;
    private ArrayList<TransModel> sortedDayList, sortedMostList;
    private ArrayList<TransModel> userTransData;
    private TextView eye_balance, view_all_1, view_all_2, total_balance, user_full_name, more_detail;
    private TextView des_title_trans, percent_moving;
    private ImageView arrow_direct;
    private LinearLayout no_data_current, no_data;
    private ListView currently, most_balance;
    private ImageView menu_top1, eye_view;
    boolean isEyeClose = true;
    private BigDecimal inputMoney = BigDecimal.ZERO, outputMoney = BigDecimal.ZERO;
    private BigDecimal inputMoneyLast = BigDecimal.ZERO, outputMoneyLast = BigDecimal.ZERO;
    private BigDecimal sumOfBalance = BigDecimal.ZERO;
    private ViewPager pager;

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
        view_all_2 = view.findViewById(R.id.view_all_2);
        view_all_1 = view.findViewById(R.id.view_all_1);
        total_balance = view.findViewById(R.id.total_balance);
        more_detail = view.findViewById(R.id.more_detail);
        user_full_name = view.findViewById(R.id.user_full_name);
        des_title_trans = view.findViewById(R.id.des_title_trans);
        percent_moving = view.findViewById(R.id.percent_moving);
        arrow_direct = view.findViewById(R.id.arrow_direct);

        this.context = getContext();
        this.userTransData = new ArrayList<>();
        this.userTransData = getArguments().getParcelableArrayList("trans_data_key"); //important
        initialData();

        pager = getActivity().findViewById(R.id.viewPager);
        more_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
            }
        });



        DecimalFormat numFormat = new DecimalFormat("###,###,###.00");
        String net_income_str = numFormat.format(sumOfBalance);
        if (net_income_str.equals(",00")) {
            net_income_str = "0,00";
        }
        total_balance.setText("Tổng số dư: " + net_income_str + " VND");


        String finalNet_income_str = net_income_str;
        eye_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEyeClose) {
                    eye_view.setImageResource(R.drawable.eyeoff);
                    eye_balance.setText(finalNet_income_str + " VND");
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

        view_all_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecentTransActivity.class);
                intent.putExtra("key_trans_data", userTransData);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            }
        });

        view_all_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HighestTransActivity.class);
                intent.putExtra("key_trans_data", userTransData);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            }
        });

        Button btn_total_revenue = view.findViewById(R.id.total_revenue);
        Button btn_total_expense = view.findViewById(R.id.total_expense);
        btn_total_expense.setBackgroundColor(0xffE5EDF4);
        btn_total_revenue.setBackgroundColor(0xffE5EDF4);
        BarChart balance_bar = view.findViewById(R.id.balance_bar_chart);

        des_title_trans.setText("Hiển thị thống kê dòng tiền vào/ra");
        percent_moving.setVisibility(View.GONE);
        arrow_direct.setVisibility(View.GONE);

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
        balance_bar_data_set.setValueTypeface(ResourcesCompat.getFont(context, R.font.averta_semibold));
        BarData balance_bar_data = new BarData(balance_bar_data_set);

        balance_bar_data_set.setValueTextSize(12f);
        balance_bar_data_set.setColor(Color.GREEN);
        balance_bar_data_set.setLabel("N/A");
        balance_bar_data.setBarWidth(0.5f);
        balance_bar.setData(balance_bar_data);
        balance_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
        balance_bar.getXAxis().enableGridDashedLine(8f, 8f, 0f);
        balance_bar.getXAxis().setLabelCount(months.size());
        balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
        balance_bar.getAxisLeft().setEnabled(false);
        balance_bar.getAxisRight().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
        balance_bar.getAxisRight().enableGridDashedLine(8f, 8f, 0f);
        balance_bar.animateY(1000, Easing.EaseInOutCubic);
        balance_bar.getLegend().setTypeface(ResourcesCompat.getFont(context, R.font.averta_semibold));
        balance_bar.invalidate();

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
        String phone = getArguments().getString("key_username_data");
        String fullname = getArguments().getString("key_fullname_data");

        user_full_name.setText("Xin chào, " + fullname);
        GridItemAddingAdapter adapter = new GridItemAddingAdapter(this.context, listAdding, phone, fullname);
        addingGrid.setAdapter(adapter);
        HeightUtils.setGridViewHeight(addingGrid, 4);

        btn_total_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_total_revenue.setBackgroundResource(R.drawable.outline_green);
                btn_total_expense.setBackgroundColor(0xffE5EDF4);

                des_title_trans.setText("Tổng tiền vào tháng này: ");
                float percent;
                String result;
                if (inputMoneyLast.floatValue() != 0) {
                    percent = inputMoney.floatValue() / inputMoneyLast.floatValue() * 100;
                } else {
                    percent = 100;
                }
                arrow_direct.setVisibility(View.VISIBLE);
                if (percent - 100 >= 0) {
                    arrow_direct.setImageResource(R.drawable.arrow_up_circle_blue);
                } else {
                    arrow_direct.setImageResource(R.drawable.arrow_down_circle_blue);
                }
                result = String.format("%.1f", abs(percent - 100));
                percent_moving.setVisibility(View.VISIBLE);
                percent_moving.setText(result + "%");
                percent_moving.setTextColor(0xFF279CC5);

                ArrayList<BarEntry> revenue = new ArrayList<>();
                revenue.add(new BarEntry(0f, inputMoneyLast.floatValue()));
                revenue.add(new BarEntry(1f, inputMoney.floatValue()));
                ArrayList<String> months = new ArrayList<>();
                months.add("Tháng trước");
                months.add("Tháng này");

                Description des = new Description();
                des.setText("");
                balance_bar.setDescription(des);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

                BarDataSet balance_bar_data_set = new BarDataSet(revenue, "");
                balance_bar_data_set.setValueTypeface(ResourcesCompat.getFont(context, R.font.averta_semibold));
                BarData balance_bar_data = new BarData(balance_bar_data_set);
                balance_bar_data.setBarWidth(0.5f);
                balance_bar_data_set.setValueTextSize(12f);
                balance_bar_data_set.setColor(0xFF279CC5);
                balance_bar_data_set.setLabel("Tổng tiền vào (VND)");

                balance_bar.setData(balance_bar_data);
                balance_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
                balance_bar.getXAxis().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.getXAxis().setLabelCount(months.size());
                balance_bar.getAxisLeft().setEnabled(false);
                balance_bar.getAxisRight().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
                balance_bar.getAxisRight().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.animateY(1000, Easing.EaseInOutCubic);
                balance_bar.getLegend().setTypeface(ResourcesCompat.getFont(context, R.font.averta_semibold));
                CustomBarChartRender barChartRender = new CustomBarChartRender(balance_bar, balance_bar.getAnimator(), balance_bar.getViewPortHandler());
                barChartRender.setRadius(15);
                barChartRender.initBuffers();
                balance_bar.setRenderer(barChartRender);
                balance_bar.invalidate();
            }
        });

        btn_total_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_total_expense.setBackgroundResource(R.drawable.outline_red);
                btn_total_revenue.setBackgroundColor(0xffE5EDF4);

                des_title_trans.setText("Tổng tiền ra tháng này: ");
                float percent;
                String result;
                if (outputMoneyLast.floatValue() != 0) {
                    percent = outputMoney.floatValue() / outputMoneyLast.floatValue() * 100;
                } else {
                    percent = 100;
                }
                arrow_direct.setVisibility(View.VISIBLE);
                if (percent - 100 >= 0) {
                    arrow_direct.setImageResource(R.drawable.arrow_up_circle_red);
                } else {
                    arrow_direct.setImageResource(R.drawable.arrow_down_circle_red);
                }
                result = String.format("%.1f", abs(percent - 100));
                percent_moving.setVisibility(View.VISIBLE);
                percent_moving.setText(result + "%");
                percent_moving.setTextColor(0xFFE45B65);

                ArrayList<BarEntry> expense = new ArrayList<>();
                expense.add(new BarEntry(0f, abs(outputMoneyLast.floatValue())));
                expense.add(new BarEntry(1f, abs(outputMoney.floatValue())));
                ArrayList<String> months = new ArrayList<>();
                months.add("Tháng trước");
                months.add("Tháng này");

                Description des = new Description();
                des.setText("");
                balance_bar.setDescription(des);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

                BarDataSet balance_bar_data_set = new BarDataSet(expense, "");
                balance_bar_data_set.setValueTypeface(ResourcesCompat.getFont(context, R.font.averta_semibold));
                BarData balance_bar_data = new BarData(balance_bar_data_set);
                balance_bar_data.setBarWidth(0.5f);
                balance_bar_data_set.setValueTextSize(12f);
                balance_bar_data_set.setColor(0xFFE45B65);
                balance_bar_data_set.setLabel("Tổng tiền ra (VND)");

                balance_bar.setData(balance_bar_data);
                balance_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                balance_bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
                balance_bar.getXAxis().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.getXAxis().setLabelCount(months.size());
                balance_bar.getXAxis().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
                balance_bar.getAxisLeft().setEnabled(false);
                balance_bar.getAxisRight().setTypeface(ResourcesCompat.getFont(context, R.font.averta_regular));
                balance_bar.getAxisRight().enableGridDashedLine(8f, 8f, 0f);
                balance_bar.animateY(1000, Easing.EaseInOutCubic);
                balance_bar.getLegend().setTypeface(ResourcesCompat.getFont(context, R.font.averta_semibold));
                CustomBarChartRender barChartRender = new CustomBarChartRender(balance_bar, balance_bar.getAnimator(), balance_bar.getViewPortHandler());
                barChartRender.setRadius(15);
                barChartRender.initBuffers();
                balance_bar.setRenderer(barChartRender);
                balance_bar.invalidate();
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

    private void initialData() {
        if (this.context != null) {
            if (userTransData != null) {
                for (TransModel item : userTransData) {
                    if (item.getTypeTrans().equals("revenue_money")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        sumOfBalance = sumOfBalance.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("expense_money")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        sumOfBalance = sumOfBalance.subtract(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("percentage_money")) {
                        if (item.getDetailTypeTrans().equals("Trả lãi")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            sumOfBalance = sumOfBalance.subtract(new BigDecimal(finalString));
                        } else if (item.getDetailTypeTrans().equals("Thu lãi")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            sumOfBalance = sumOfBalance.add(new BigDecimal(finalString));
                        }
                    } else if (item.getTypeTrans().equals("loan_money")) {
                        if (item.getDetailTypeTrans().equals("Cho vay") || item.getDetailTypeTrans().equals("Trả nợ")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            sumOfBalance = sumOfBalance.subtract(new BigDecimal(finalString));
                        } else if (item.getDetailTypeTrans().equals("Đi vay") || item.getDetailTypeTrans().equals("Thu nợ")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            sumOfBalance = sumOfBalance.add(new BigDecimal(finalString));
                        }
                    }
                }
            }
            getListViewData();
            getBarData();
        }
    }

    private void getBarData() {
        if (userTransData != null) {
            LocalDate today = LocalDate.now();
            LocalDate localBegin = today.withDayOfMonth(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String beginDay = localBegin.format(formatter);
            LocalDate localEnd = today.with(TemporalAdjusters.lastDayOfMonth());
            String endDay = localEnd.format(formatter);
            LocalDate chartBeginDay = LocalDate.parse(beginDay, formatter);
            LocalDate chartEndDay = LocalDate.parse(endDay, formatter);

            for (TransModel item : userTransData) {
                LocalDate itemDate = item.getDateAsLocalDate();
                if ((itemDate.isEqual(chartBeginDay) || itemDate.isAfter(chartBeginDay)) &&
                        (itemDate.isEqual(chartEndDay) || itemDate.isBefore(chartEndDay))) {
                    if (item.getTypeTrans().equals("revenue_money")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoney = inputMoney.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("expense_money")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoney = outputMoney.subtract(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("percentage_money")) {
                        if (item.getDetailTypeTrans().equals("Trả lãi")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            outputMoney = outputMoney.subtract(new BigDecimal(finalString));
                        } else if (item.getDetailTypeTrans().equals("Thu lãi")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            inputMoney = inputMoney.add(new BigDecimal(finalString));
                        }
                    } else if (item.getTypeTrans().equals("loan_money")) {
                        if (item.getDetailTypeTrans().equals("Cho vay") || item.getDetailTypeTrans().equals("Trả nợ")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            outputMoney = outputMoney.subtract(new BigDecimal(finalString));
                        } else if (item.getDetailTypeTrans().equals("Đi vay") || item.getDetailTypeTrans().equals("Thu nợ")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            inputMoney = inputMoney.add(new BigDecimal(finalString));
                        }
                    }
                }
            }

            YearMonth lastMonth = YearMonth.from(today).minusMonths(1);
            LocalDate firstDayOfLastMonth = lastMonth.atDay(1);
            LocalDate lastDayOfLastMonth = lastMonth.atEndOfMonth();
            String beginDayLast = firstDayOfLastMonth.format(formatter);
            String endDayLast = lastDayOfLastMonth.format(formatter);
            LocalDate chartBeginDayLast = LocalDate.parse(beginDayLast, formatter);
            LocalDate chartEndDayLast = LocalDate.parse(endDayLast, formatter);

            for (TransModel item : userTransData) {
                LocalDate itemDate = item.getDateAsLocalDate();
                if ((itemDate.isEqual(chartBeginDayLast) || itemDate.isAfter(chartBeginDayLast)) &&
                        (itemDate.isEqual(chartEndDayLast) || itemDate.isBefore(chartEndDayLast))) {
                    if (item.getTypeTrans().equals("revenue_money")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyLast = inputMoneyLast.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("expense_money")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyLast = outputMoneyLast.subtract(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("percentage_money")) {
                        if (item.getDetailTypeTrans().equals("Trả lãi")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            outputMoneyLast = outputMoneyLast.subtract(new BigDecimal(finalString));
                        } else if (item.getDetailTypeTrans().equals("Thu lãi")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            inputMoneyLast = inputMoneyLast.add(new BigDecimal(finalString));
                        }
                    } else if (item.getTypeTrans().equals("loan_money")) {
                        if (item.getDetailTypeTrans().equals("Cho vay") || item.getDetailTypeTrans().equals("Trả nợ")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            outputMoneyLast = outputMoneyLast.subtract(new BigDecimal(finalString));
                        } else if (item.getDetailTypeTrans().equals("Đi vay") || item.getDetailTypeTrans().equals("Thu nợ")) {
                            String finalString = item.getMoneyTrans().replace(",", "");
                            inputMoneyLast = inputMoneyLast.add(new BigDecimal(finalString));
                        }
                    }
                }
            }
        }
    }

    private void getListViewData() {
        if (userTransData != null) {
            this.sortedDayList = new ArrayList<>(userTransData);
            this.sortedMostList = new ArrayList<>(userTransData);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
            Collections.sort(sortedDayList, new Comparator<TransModel>() {
                @Override
                public int compare(TransModel o1, TransModel o2) {
                    LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                    LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                    return date2.compareTo(date1);
                }
            });
            if (sortedDayList.size() > 3) {
                sortedDayList.subList(3, sortedDayList.size()).clear();
            }
            Collections.sort(sortedMostList, new Comparator<TransModel>() {
                @Override
                public int compare(TransModel o1, TransModel o2) {
                    return Float.compare(o2.getMoneyTransFloat(), o1.getMoneyTransFloat());
                }
            });
            if (sortedMostList.size() > 3) {
                sortedMostList.subList(3, sortedMostList.size()).clear();
            }
        } else {
            this.sortedDayList = new ArrayList<>();
            this.sortedMostList = new ArrayList<>();
        }

    }
}
