package com.example.walletapp.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Render.CustomBarChartRender;
import com.example.walletapp.Render.CustomPieChartRender;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnalyzeFragment extends Fragment {
    public AnalyzeFragment() {}
    BarChart net_income_bar_chart;
    PieChart revenue_income_pie_chart, expense_income_pie_chart;
    SQLiteDatabase database;
    private Context context;
    private ArrayList<TransactionItem> queryList;
    private String SRC_DATABASE_NAME = "app_database.db";
    private TextView net_income, revenue_balance, revenue_1day_balance, expense_balance, expense_1day_balance;
    public static BigDecimal sumOfInput, sumOfOutput;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyze_fragment, container, false);
        net_income_bar_chart = view.findViewById(R.id.net_income_bar_chart);
        net_income = view.findViewById(R.id.net_income);
        revenue_balance = view.findViewById(R.id.revenue_balance);
        revenue_1day_balance = view.findViewById(R.id.revenue_1day_balance);
        expense_balance = view.findViewById(R.id.expense_balance);
        expense_1day_balance = view.findViewById(R.id.expense_1day_balance);
        revenue_income_pie_chart = view.findViewById(R.id.revenue_income_pie_chart);
        expense_income_pie_chart = view.findViewById(R.id.expense_income_pie_chart);

        this.context = getContext();
        queryList = new ArrayList<>();
        String src = context.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);

        Cursor cursorSort = database.query("userdata", null, null, null, null, null, null);
        cursorSort.moveToNext();
        while (!cursorSort.isAfterLast()) {
            TransactionItem item = new TransactionItem(cursorSort.getString(3), cursorSort.getString(2), cursorSort.getString(0), cursorSort.getString(1),  cursorSort.getString(4));
            this.queryList.add(item);
            cursorSort.moveToNext();

        }
        cursorSort.close();



        LocalDate today = LocalDate.now();
        //range1 BarChart XAxis
        LocalDate beginDayRange1 = today.withDayOfMonth(1);
        LocalDate endDayRange1 = today.withDayOfMonth(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String beginDayRange1Str = beginDayRange1.format(formatter);
        String endDayRange1Str = endDayRange1.format(formatter);
        String[] beginPartsRange1 = beginDayRange1Str.split("/");
        String beginDay01 = beginPartsRange1[0];
        String monthYear01 = beginPartsRange1[1] + "/" + beginPartsRange1[2];
        String[] endParts01 = endDayRange1Str.split("/");
        String endDay01 = endParts01[0];
        String range1 = beginDay01 + " - " + endDay01 + "/" + monthYear01;

        //range2 BarChart XAxis
        LocalDate beginDayRange2 = today.withDayOfMonth(11);
        LocalDate endDayRange2 = today.withDayOfMonth(20);
        String beginDayRange2Str = beginDayRange2.format(formatter);
        String endDayRange2Str = endDayRange2.format(formatter);
        String[] beginPartsRange2 = beginDayRange2Str.split("/");
        String beginDay02 = beginPartsRange2[0];
        String monthYear02 = beginPartsRange2[1] + "/" + beginPartsRange2[2];
        String[] endParts02 = endDayRange2Str.split("/");
        String endDay02 = endParts02[0];
        String range2 = beginDay02 + " - " + endDay02 + "/" + monthYear02;

        //range3 BarChart XAxis
        LocalDate now = LocalDate.now();
        LocalDate beginDayRange3 = today.withDayOfMonth(21);
        LocalDate endDayRange3 = now.with(TemporalAdjusters.lastDayOfMonth());
        String beginDayRange3Str = beginDayRange3.format(formatter);
        String endDayRange3Str = endDayRange3.format(formatter);
        String[] beginPartsRange3 = beginDayRange3Str.split("/");
        String beginDay03 = beginPartsRange3[0];
        String monthYear03 = beginPartsRange3[1] + "/" + beginPartsRange3[2];
        String[] endParts03 = endDayRange3Str.split("/");
        String endDay03 = endParts03[0];
        String range3 = beginDay03 + " - " + endDay03 + "/" + monthYear03;

        //barchart set data from database
        LocalDate chartBeginDayRange1 = LocalDate.parse(beginDayRange1Str, formatter);
        LocalDate chartEndDayRange1 = LocalDate.parse(endDayRange1Str, formatter);

        LocalDate chartBeginDayRange2 = LocalDate.parse(beginDayRange2Str, formatter);
        LocalDate chartEndDayRange2 = LocalDate.parse(endDayRange2Str, formatter);

        LocalDate chartBeginDayRange3 = LocalDate.parse(beginDayRange3Str, formatter);
        LocalDate chartEndDayRange3 = LocalDate.parse(endDayRange3Str, formatter);


        BigDecimal inputMoneyRange1 = BigDecimal.ZERO;
        BigDecimal inputMoneyRange2 = BigDecimal.ZERO;
        BigDecimal inputMoneyRange3 = BigDecimal.ZERO;
        BigDecimal outputMoneyRange1 = BigDecimal.ZERO;
        BigDecimal outputMoneyRange2 = BigDecimal.ZERO;
        BigDecimal outputMoneyRange3 = BigDecimal.ZERO;

        for (TransactionItem item : queryList) {
            LocalDate itemDate = item.getDateAsLocalDate();
            if ((itemDate.isEqual(chartBeginDayRange1) || itemDate.isAfter(chartBeginDayRange1)) &&
                    (itemDate.isEqual(chartEndDayRange1) || itemDate.isBefore(chartEndDayRange1))) {
                if (item.getTypeTrans().equals("revenue_money")) {
                    String finalString = item.getMoneyTrans().replace(",", "");
                    inputMoneyRange1 = inputMoneyRange1.add(new BigDecimal(finalString));
                } else if (item.getTypeTrans().equals("expense_money")) {
                    String finalString = item.getMoneyTrans().replace(",", "");
                    outputMoneyRange1 = outputMoneyRange1.subtract(new BigDecimal(finalString));
                } else if (item.getTypeTrans().equals("percentage_money")) {
                    if (item.getDetailTypeTrans().equals("Trả lãi")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyRange1 = outputMoneyRange1.subtract(new BigDecimal(finalString));
                    } else if (item.getDetailTypeTrans().equals("Thu lãi")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyRange1 = inputMoneyRange1.add(new BigDecimal(finalString));
                    }
                } else if (item.getTypeTrans().equals("loan_money")) {
                    if (item.getDetailTypeTrans().equals("Cho vay") || item.getDetailTypeTrans().equals("Trả nợ")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyRange1 = outputMoneyRange1.subtract(new BigDecimal(finalString));
                    } else if (item.getDetailTypeTrans().equals("Đi vay") || item.getDetailTypeTrans().equals("Thu nợ")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyRange1 = inputMoneyRange1.add(new BigDecimal(finalString));
                    }
                }
            }

            if ((itemDate.isEqual(chartBeginDayRange2) || itemDate.isAfter(chartBeginDayRange2)) &&
                    (itemDate.isEqual(chartEndDayRange2) || itemDate.isBefore(chartEndDayRange2))) {
                if (item.getTypeTrans().equals("revenue_money")) {
                    String finalString = item.getMoneyTrans().replace(",", "");
                    inputMoneyRange2 = inputMoneyRange2.add(new BigDecimal(finalString));
                } else if (item.getTypeTrans().equals("expense_money")) {
                    String finalString = item.getMoneyTrans().replace(",", "");
                    outputMoneyRange2 = outputMoneyRange2.subtract(new BigDecimal(finalString));
                } else if (item.getTypeTrans().equals("percentage_money")) {
                    if (item.getDetailTypeTrans().equals("Trả lãi")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyRange2 = outputMoneyRange2.subtract(new BigDecimal(finalString));
                    } else if (item.getDetailTypeTrans().equals("Thu lãi")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyRange2 = inputMoneyRange2.add(new BigDecimal(finalString));
                    }
                } else if (item.getTypeTrans().equals("loan_money")) {
                    if (item.getDetailTypeTrans().equals("Cho vay") || item.getDetailTypeTrans().equals("Trả nợ")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyRange2 = outputMoneyRange2.subtract(new BigDecimal(finalString));
                    } else if (item.getDetailTypeTrans().equals("Đi vay") || item.getDetailTypeTrans().equals("Thu nợ")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyRange2 = inputMoneyRange2.add(new BigDecimal(finalString));
                    }
                }
            }

            if ((itemDate.isEqual(chartBeginDayRange3) || itemDate.isAfter(chartBeginDayRange3)) &&
                    (itemDate.isEqual(chartEndDayRange3) || itemDate.isBefore(chartEndDayRange3))) {
                if (item.getTypeTrans().equals("revenue_money")) {
                    String finalString = item.getMoneyTrans().replace(",", "");
                    inputMoneyRange3 = inputMoneyRange3.add(new BigDecimal(finalString));
                } else if (item.getTypeTrans().equals("expense_money")) {
                    String finalString = item.getMoneyTrans().replace(",", "");
                    outputMoneyRange3 = outputMoneyRange3.subtract(new BigDecimal(finalString));
                } else if (item.getTypeTrans().equals("percentage_money")) {
                    if (item.getDetailTypeTrans().equals("Trả lãi")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyRange3 = outputMoneyRange3.subtract(new BigDecimal(finalString));
                    } else if (item.getDetailTypeTrans().equals("Thu lãi")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyRange3 = inputMoneyRange3.add(new BigDecimal(finalString));
                    }
                } else if (item.getTypeTrans().equals("loan_money")) {
                    if (item.getDetailTypeTrans().equals("Cho vay") || item.getDetailTypeTrans().equals("Trả nợ")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        outputMoneyRange3 = outputMoneyRange3.subtract(new BigDecimal(finalString));
                    } else if (item.getDetailTypeTrans().equals("Đi vay") || item.getDetailTypeTrans().equals("Thu nợ")) {
                        String finalString = item.getMoneyTrans().replace(",", "");
                        inputMoneyRange3 = inputMoneyRange3.add(new BigDecimal(finalString));
                    }
                }
            }
        }
        DecimalFormat numFormat = new DecimalFormat("###,###,###.00");
        sumOfOutput = BigDecimal.ZERO;
        sumOfInput = BigDecimal.ZERO;
        BigDecimal sumOfNetIncome = BigDecimal.ZERO;
        sumOfInput = sumOfInput.add(inputMoneyRange1).add(inputMoneyRange2).add(inputMoneyRange3);
        sumOfOutput = sumOfOutput.add(outputMoneyRange1).add(outputMoneyRange2).add(outputMoneyRange3);

        Log.d("DEBUG", "Input: " + sumOfInput);
        Log.d("DEBUG", "Output: " + sumOfOutput);


        sumOfNetIncome = sumOfNetIncome.add(sumOfInput).add(sumOfOutput);
        String net_income_str = numFormat.format(sumOfNetIncome);
        String revenue_str = numFormat.format(sumOfInput);
        BigDecimal sumOfOutputAbs = sumOfOutput.abs();
        String expense_str = numFormat.format(sumOfOutputAbs);
        net_income.setText(net_income_str + " VND");
        revenue_balance.setText(revenue_str + " VND");
        expense_balance.setText(expense_str + " VND");

        YearMonth currentMonth = YearMonth.now();
        int daynum = currentMonth.lengthOfMonth();

        BigDecimal revenue_perday = sumOfInput.divide(new BigDecimal(daynum), 2, RoundingMode.HALF_UP);
        BigDecimal expense_perday = sumOfOutputAbs.divide(new BigDecimal(daynum), 2, RoundingMode.HALF_UP);
        String revenue_per_str = numFormat.format(revenue_perday);
        String expense_per_str = numFormat.format(expense_perday);
        revenue_1day_balance.setText(revenue_per_str + " VND");
        expense_1day_balance.setText(expense_per_str + " VND");




        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, new float[]{inputMoneyRange1.floatValue(), outputMoneyRange1.floatValue()}));
        entries.add(new BarEntry(1, new float[]{inputMoneyRange2.floatValue(), outputMoneyRange2.floatValue()}));
        entries.add(new BarEntry(2, new float[]{inputMoneyRange3.floatValue(), outputMoneyRange3.floatValue()}));
        Log.d("DEBUG", "Range 1: " + inputMoneyRange1 + " - " + outputMoneyRange1);
        Log.d("DEBUG", "Range 2: " + inputMoneyRange2 + " - " + outputMoneyRange2);
        Log.d("DEBUG", "Range 3: " + inputMoneyRange3 + " - " + outputMoneyRange3);

        ArrayList<String> daysOfMonth = new ArrayList<>();
        daysOfMonth.add(range1);
        daysOfMonth.add(range2);
        daysOfMonth.add(range3);




        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(new int[]{0xFF279CC5, 0xFFE45B65});
        dataSet.setValueTextSize(12f);
        dataSet.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
        dataSet.setStackLabels(new String[]{"Nguồn tiền vào (VND)", "Nguồn tiền ra (VND)"});

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);
        Description des = new Description();
        des.setText("");
        net_income_bar_chart.setDescription(des);
        net_income_bar_chart.setData(barData);
        net_income_bar_chart.setFitBars(true);
        net_income_bar_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        net_income_bar_chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(daysOfMonth));
        net_income_bar_chart.getXAxis().setLabelCount(daysOfMonth.size());
        net_income_bar_chart.getXAxis().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
        net_income_bar_chart.getAxisRight().setEnabled(false);
        net_income_bar_chart.getAxisLeft().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
        net_income_bar_chart.getAxisLeft().enableGridDashedLine(8f, 8f, 0f);
        net_income_bar_chart.getXAxis().enableGridDashedLine(8f, 8f, 0f);
        net_income_bar_chart.animateY(1000, Easing.EaseInOutCubic);
        net_income_bar_chart.getXAxis().setLabelRotationAngle(-30);
        net_income_bar_chart.invalidate();
        net_income_bar_chart.getLegend().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));

        List<PieEntry> entriesPie = new ArrayList<>();
        entriesPie.add(new PieEntry(2.0f, "Khoản thu"));
        entriesPie.add(new PieEntry(3.0f, "Thu lãi"));
        entriesPie.add(new PieEntry(4.0f, "Đi vay"));
        entriesPie.add(new PieEntry(5.0f, "Thu nợ"));
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#99ccff"));
        colors.add(Color.parseColor("#66b2ff"));
        colors.add(Color.parseColor("#3299ff"));
        colors.add(Color.parseColor("#007fff"));

        PieDataSet pieDataSetInput = new PieDataSet(entriesPie, "");
        pieDataSetInput.setColors(colors);
        pieDataSetInput.setValueTextSize(12f);
        pieDataSetInput.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_semibold));
        pieDataSetInput.setValueLinePart1Length(0.6f);
        pieDataSetInput.setValueLinePart2Length(0.3f);
        pieDataSetInput.setValueLineWidth(2f);
        pieDataSetInput.setValueLinePart1OffsetPercentage(115f);
        pieDataSetInput.setUsingSliceColorAsValueLineColor(true);
        pieDataSetInput.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSetInput.setValueTextSize(12f);
        pieDataSetInput.setSelectionShift(3f);


        PieData pieDataInput = new PieData(pieDataSetInput);
        Description des2 = new Description();
        des2.setText("");
        revenue_income_pie_chart.setDescription(des2);
        revenue_income_pie_chart.setData(pieDataInput);
        CustomPieChartRender pieChartRender = new CustomPieChartRender(revenue_income_pie_chart, revenue_income_pie_chart.getAnimator(), revenue_income_pie_chart.getViewPortHandler(), 10f);
        pieChartRender.initBuffers();
        revenue_income_pie_chart.setRenderer(pieChartRender);
        revenue_income_pie_chart.setUsePercentValues(true);
        revenue_income_pie_chart.setUsePercentValues(true);
        revenue_income_pie_chart.setDrawHoleEnabled(true);
        revenue_income_pie_chart.setHoleRadius(50f);
        revenue_income_pie_chart.setExtraOffsets(40f, 0f, 40f, 0f);
        revenue_income_pie_chart.invalidate();
        revenue_income_pie_chart.getLegend().setEnabled(true);
        revenue_income_pie_chart.getLegend().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));
        revenue_income_pie_chart.setDrawSliceText(false);

        return view;
    }

    private void initialData() {
        this.context = getContext();
        queryList = new ArrayList<>();
        String src = context.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);

        Cursor cursorSort = database.query("userdata", null, null, null, null, null, null);
        cursorSort.moveToNext();
        while (!cursorSort.isAfterLast()) {
            TransactionItem item = new TransactionItem(cursorSort.getString(3), cursorSort.getString(2), cursorSort.getString(0), cursorSort.getString(1),  cursorSort.getString(4));
            this.queryList.add(item);
            cursorSort.moveToNext();
        }
        cursorSort.close();
    }
}
