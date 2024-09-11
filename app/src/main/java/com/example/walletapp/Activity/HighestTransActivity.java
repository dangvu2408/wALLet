package com.example.walletapp.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.walletapp.Adapter.EditTransactionAdapter;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Model.TransModel;
import com.example.walletapp.R;
import com.example.walletapp.Render.CustomPieChartRender;
import com.example.walletapp.Utils.HeightUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HighestTransActivity extends AppCompatActivity {
    private ImageView back_btn;
    private ArrayList<TransModel> queryList;
    private Button income_high, comeout_high;
    private PieChart high_balance_pie_chart;
    private BigDecimal pieChartRevenue = BigDecimal.ZERO, pieChartPercentage = BigDecimal.ZERO, pieChartLoanBorrow = BigDecimal.ZERO, pieChartLoanDebt = BigDecimal.ZERO;
    private BigDecimal pieChartExpense = BigDecimal.ZERO, pieChartPercentageA = BigDecimal.ZERO, pieChartLoanLend = BigDecimal.ZERO, pieChartLoanRepay = BigDecimal.ZERO;
    private ArrayList<TransModel> highIncomeList, highOutcomeList;
    private ListView trans_high_type, trans_high_notype;
    private QueryTransactionAdapter adapter;
    private LinearLayout no_data_high_trans, no_data_high_notype;
    private EditTransactionAdapter sortAdapter;
    private TextView current_month;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_highest_balance);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }

        back_btn = findViewById(R.id.back_btn);
        income_high = findViewById(R.id.income_high);
        comeout_high = findViewById(R.id.comeout_high);
        high_balance_pie_chart = findViewById(R.id.high_balance_pie_chart);
        trans_high_type = findViewById(R.id.trans_high_type);
        no_data_high_trans = findViewById(R.id.no_data_high_trans);
        no_data_high_notype = findViewById(R.id.no_data_high_notype);
        trans_high_notype = findViewById(R.id.trans_high_notype);
        current_month = findViewById(R.id.current_month);

        LocalDate today = LocalDate.now();
        int currentMonthValue = today.getMonthValue();
        current_month.setText("Dòng tiền tháng " + currentMonthValue);

        this.queryList = new ArrayList<>();
        this.queryList = getIntent().getParcelableArrayListExtra("key_trans_data");
        listDataInit();
        pieChartInitData();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });
        if (queryList != null) {
            Collections.sort(queryList, new Comparator<TransModel>() {
                @Override
                public int compare(TransModel o1, TransModel o2) {
                    return Float.compare(o2.getMoneyTransFloat(), o1.getMoneyTransFloat());
                }
            });
            sortAdapter = new EditTransactionAdapter(this, queryList);
            trans_high_notype.setAdapter(sortAdapter);
            HeightUtils.setListViewHeight(trans_high_notype);
            if (trans_high_notype.getLayoutParams().height == 10) {
                no_data_high_notype.setVisibility(View.VISIBLE);
            } else {
                no_data_high_notype.setVisibility(View.GONE);
            }
        }

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ffffff"));
        colors.add(Color.parseColor("#ffffff"));
        colors.add(Color.parseColor("#ffffff"));
        colors.add(Color.parseColor("#ffffff"));
        pieGenerate("N/A", "N/A", "N/A", "N/A", 0, 0, 0, 0, colors);

        income_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#99ccff"));
                colors.add(Color.parseColor("#66b2ff"));
                colors.add(Color.parseColor("#3299ff"));
                colors.add(Color.parseColor("#007fff"));
                pieGenerate("Khoản thu", "Thu lãi", "Đi vay", "Thu nợ", pieChartRevenue.floatValue(), pieChartPercentage.floatValue(), pieChartLoanBorrow.floatValue(), pieChartLoanDebt.floatValue(), colors);
                income_high.setBackgroundResource(R.drawable.outline_main_blue);
                comeout_high.setBackgroundColor(0xFFFFFFFF);
                if (highIncomeList != null) {
                    adapter = new QueryTransactionAdapter(getApplicationContext(), highIncomeList);
                    trans_high_type.setAdapter(adapter);
                    HeightUtils.setListViewHeight(trans_high_type);
                    if (trans_high_type.getLayoutParams().height == 10) {
                        no_data_high_trans.setVisibility(View.VISIBLE);
                    } else {
                        no_data_high_trans.setVisibility(View.GONE);
                    }
                }
            }
        });

        comeout_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#f1a7a9"));
                colors.add(Color.parseColor("#ec8385"));
                colors.add(Color.parseColor("#e66063"));
                colors.add(Color.parseColor("#e35053"));
                pieGenerate("Khoản chi", "Trả lãi", "Cho vay", "Trả nợ", pieChartExpense.floatValue(), pieChartPercentageA.floatValue(), pieChartLoanLend.floatValue(), pieChartLoanRepay.floatValue(), colors);
                comeout_high.setBackgroundResource(R.drawable.outline_main_red);
                income_high.setBackgroundColor(0xFFFFFFFF);
                if (highOutcomeList != null) {
                    adapter = new QueryTransactionAdapter(getApplicationContext(), highOutcomeList);
                    trans_high_type.setAdapter(adapter);
                    HeightUtils.setListViewHeight(trans_high_type);
                    if (trans_high_type.getLayoutParams().height == 10) {
                        no_data_high_trans.setVisibility(View.VISIBLE);
                    } else {
                        no_data_high_trans.setVisibility(View.GONE);
                    }
                }
            }
        });


    }

    private void pieChartInitData() {
        if (queryList != null) {
            LocalDate today = LocalDate.now();
            LocalDate localBegin = today.withDayOfMonth(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String beginDay = localBegin.format(formatter);
            LocalDate localEnd = today.with(TemporalAdjusters.lastDayOfMonth());
            String endDay = localEnd.format(formatter);
            LocalDate pieBeginDay = LocalDate.parse(beginDay, formatter);
            LocalDate pieEndDay = LocalDate.parse(endDay, formatter);

            for (TransModel item : queryList) {
                String finalString = item.getMoneyTrans().replace(",", "");
                LocalDate itemDate = item.getDateAsLocalDate();
                if ((itemDate.isEqual(pieBeginDay) || itemDate.isAfter(pieBeginDay)) &&
                        (itemDate.isEqual(pieEndDay) || itemDate.isBefore(pieEndDay))){
                    if (item.getTypeTrans().equals("revenue_money")) {
                        pieChartRevenue = pieChartRevenue.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("percentage_money")
                            && item.getDetailTypeTrans().equals("Thu lãi")) {
                        pieChartPercentage = pieChartPercentage.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("loan_money")
                            && (item.getDetailTypeTrans().equals("Đi vay"))) {
                        pieChartLoanBorrow = pieChartLoanBorrow.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("loan_money")
                            && (item.getDetailTypeTrans().equals("Thu nợ"))) {
                        pieChartLoanDebt = pieChartLoanDebt.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("expense_money")) {
                        pieChartExpense = pieChartExpense.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("percentage_money")
                            && item.getDetailTypeTrans().equals("Trả lãi")) {
                        pieChartPercentageA = pieChartPercentageA.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("loan_money")
                            && (item.getDetailTypeTrans().equals("Cho vay"))) {
                        pieChartLoanLend = pieChartLoanLend.add(new BigDecimal(finalString));
                    } else if (item.getTypeTrans().equals("loan_money")
                            && (item.getDetailTypeTrans().equals("Trả nợ"))) {
                        pieChartLoanRepay = pieChartLoanRepay.add(new BigDecimal(finalString));
                    }
                }
            }
        }
    }

    private void listDataInit() {
        if (queryList != null) {
            this.highIncomeList = new ArrayList<>();
            this.highOutcomeList = new ArrayList<>();
            for (TransModel item : queryList) {
                if (item.getTypeTrans().equals("revenue_money")) {
                    highIncomeList.add(item);
                } else if (item.getTypeTrans().equals("percentage_money")
                        && item.getDetailTypeTrans().equals("Thu lãi")) {
                    highIncomeList.add(item);
                } else if (item.getTypeTrans().equals("loan_money")
                        && (item.getDetailTypeTrans().equals("Đi vay"))) {
                    highIncomeList.add(item);
                } else if (item.getTypeTrans().equals("loan_money")
                        && (item.getDetailTypeTrans().equals("Thu nợ"))) {
                    highIncomeList.add(item);
                } else if (item.getTypeTrans().equals("expense_money")) {
                    highOutcomeList.add(item);
                } else if (item.getTypeTrans().equals("percentage_money")
                        && item.getDetailTypeTrans().equals("Trả lãi")) {
                    highOutcomeList.add(item);
                } else if (item.getTypeTrans().equals("loan_money")
                        && (item.getDetailTypeTrans().equals("Cho vay"))) {
                    highOutcomeList.add(item);
                } else if (item.getTypeTrans().equals("loan_money")
                        && (item.getDetailTypeTrans().equals("Trả nợ"))) {
                    highOutcomeList.add(item);
                }
            }

            Collections.sort(highIncomeList, new Comparator<TransModel>() {
                @Override
                public int compare(TransModel o1, TransModel o2) {
                    return Float.compare(o2.getMoneyTransFloat(), o1.getMoneyTransFloat());
                }
            });
            Collections.sort(highOutcomeList, new Comparator<TransModel>() {
                @Override
                public int compare(TransModel o1, TransModel o2) {
                    return Float.compare(o2.getMoneyTransFloat(), o1.getMoneyTransFloat());
                }
            });
        }
    }

    private void pieGenerate(String str1, String str2, String str3, String str4, float f1, float f2, float f3, float f4, List<Integer> colors) {
        List<PieEntry> entriesPie = new ArrayList<>();
        entriesPie.add(new PieEntry(f1, str1));
        entriesPie.add(new PieEntry(f2, str2));
        entriesPie.add(new PieEntry(f3, str3));
        entriesPie.add(new PieEntry(f4, str4));
        PieDataSet pieDataSetInput = new PieDataSet(entriesPie, "");
        pieDataSetInput.setColors(colors);
        pieDataSetInput.setValueTextSize(12f);
        pieDataSetInput.setValueTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.averta_semibold));
        pieDataSetInput.setValueLinePart1Length(0.6f);
        pieDataSetInput.setValueLinePart2Length(0.3f);
        pieDataSetInput.setValueLineWidth(2f);
        pieDataSetInput.setValueLinePart1OffsetPercentage(115f);
        pieDataSetInput.setUsingSliceColorAsValueLineColor(true);
        pieDataSetInput.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSetInput.setValueTextSize(12f);
        pieDataSetInput.setSelectionShift(3f);
        pieDataSetInput.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f%%", value);
            }
        });
        PieData pieDataInput = new PieData(pieDataSetInput);
        Description des2 = new Description();
        des2.setText("");
        high_balance_pie_chart.setDescription(des2);
        high_balance_pie_chart.setData(pieDataInput);
        CustomPieChartRender pieChartRender = new CustomPieChartRender(high_balance_pie_chart, high_balance_pie_chart.getAnimator(), high_balance_pie_chart.getViewPortHandler(), 10f);
        pieChartRender.initBuffers();
        high_balance_pie_chart.setRenderer(pieChartRender);
        high_balance_pie_chart.setUsePercentValues(true);
        high_balance_pie_chart.setDrawHoleEnabled(true);
        high_balance_pie_chart.setHoleRadius(50f);
        high_balance_pie_chart.setExtraOffsets(40f, 0f, 40f, 0f);
        high_balance_pie_chart.invalidate();
        high_balance_pie_chart.getLegend().setEnabled(true);
        high_balance_pie_chart.getLegend().setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.averta_semibold));
        high_balance_pie_chart.setDrawSliceText(false);
        high_balance_pie_chart.animateX(1000, Easing.EaseInOutCubic);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
