package com.example.walletapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Adapter.GridItemAddingAdapter;
import com.example.walletapp.Model.GridItem;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private boolean isOverlayVisible = false;
    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        CardView cardView = view.findViewById(R.id.layout_root);
        ScrollView fullLayout = view.findViewById(R.id.scroll_full_view);
        LinearLayout hidden = view.findViewById(R.id.layout_hidden);

        Button btn_total_revenue = view.findViewById(R.id.total_revenue);
        Button btn_total_expense = view.findViewById(R.id.total_expense);
        btn_total_expense.setBackgroundColor(0xffE5EDF4);
        btn_total_revenue.setBackgroundColor(0xffE5EDF4);
        BarChart balance_bar = view.findViewById(R.id.balance_bar_chart);

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
                revenue.add(new BarEntry(0f, 32000000f));
                revenue.add(new BarEntry(1f, 30000000f));
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
                balance_bar_data_set.setColor(Color.GREEN);
                balance_bar_data_set.setLabel("Tổng tiền vào (VND)");

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
                expense.add(new BarEntry(0f, 7000000f));
                expense.add(new BarEntry(1f, 7500000f));
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
                balance_bar_data_set.setColor(Color.RED);
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

                CustomBarChartRender barChartRender = new CustomBarChartRender(balance_bar, balance_bar.getAnimator(), balance_bar.getViewPortHandler());
                barChartRender.setRadius(15);
                barChartRender.initBuffers();
                balance_bar.setRenderer(barChartRender);
            }
        });



        return view;
    }
}
