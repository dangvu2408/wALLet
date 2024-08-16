package com.example.walletapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.walletapp.R;
import com.example.walletapp.Render.CustomBarChartRender;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeFragment extends Fragment {
    public AnalyzeFragment() {}
    BarChart net_income_bar_chart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyze_fragment, container, false);
        net_income_bar_chart = view.findViewById(R.id.net_income_bar_chart);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, new float[]{4500000f, -3000000f}));
        entries.add(new BarEntry(1, new float[]{7000000f, -1500000f}));
        entries.add(new BarEntry(2, new float[]{5600000f, -4500000f}));

        ArrayList<String> daysOfMonth = new ArrayList<>();
        daysOfMonth.add("16/08/2024");
        daysOfMonth.add("18/08/2024");
        daysOfMonth.add("30/08/2024");


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
        net_income_bar_chart.invalidate();
        net_income_bar_chart.getLegend().setTypeface(ResourcesCompat.getFont(getContext(), R.font.sfpro_regular));

        return view;
    }
}
