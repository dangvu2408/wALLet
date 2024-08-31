package com.example.walletapp.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.walletapp.Adapter.EditTransactionAdapter;
import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Fragment.HomeFragment;
import com.example.walletapp.Model.TransModel;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecentTransActivity extends AppCompatActivity {
    private TextView come_in_money, come_out_money, total_money;
    private LinearLayout no_data_current_trans;
    private ListView current_transaction;
    private ImageView back_btn;
    private String SRC_DATABASE_NAME = "app_database.db";
    private ArrayList<TransModel> queryList;
    private SQLiteDatabase database;
    private EditTransactionAdapter sortedDayAdapter;
    private BigDecimal inputMoney = BigDecimal.ZERO, outputMoney = BigDecimal.ZERO;
    private BigDecimal sumOfBalance = BigDecimal.ZERO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_balance);
        current_transaction = findViewById(R.id.current_transaction);
        no_data_current_trans = findViewById(R.id.no_data_current_trans);
        back_btn = findViewById(R.id.back_btn);
        come_in_money = findViewById(R.id.come_in_money);
        come_out_money = findViewById(R.id.come_out_money);
        total_money = findViewById(R.id.total_money);
        this.queryList = new ArrayList<>();
        this.queryList = getIntent().getParcelableArrayListExtra("key_trans_data");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));

        ArrayList<TransModel> sortedDayList = new ArrayList<>(queryList);
        Collections.sort(sortedDayList, new Comparator<TransModel>() {
            @Override
            public int compare(TransModel o1, TransModel o2) {
                LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                return date2.compareTo(date1);
            }
        });

        sortedDayAdapter = new EditTransactionAdapter(this, sortedDayList);
        current_transaction.setAdapter(sortedDayAdapter);
        sortedDayAdapter.notifyDataSetChanged();
        HeightUtils.setListViewHeight(current_transaction);
        if (current_transaction.getLayoutParams().height == 10) {
            no_data_current_trans.setVisibility(View.VISIBLE);
        } else {
            no_data_current_trans.setVisibility(View.GONE);
        }

        LocalDate today = LocalDate.now();
        LocalDate localBegin = today.withDayOfMonth(1);
        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String beginDay = localBegin.format(formatDay);
        LocalDate localEnd = today.with(TemporalAdjusters.lastDayOfMonth());
        String endDay = localEnd.format(formatDay);
        LocalDate chartBeginDay = LocalDate.parse(beginDay, formatDay);
        LocalDate chartEndDay = LocalDate.parse(endDay, formatDay);

        for (TransModel item : queryList) {
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

        sumOfBalance = sumOfBalance.add(inputMoney).add(outputMoney);


        DecimalFormat numFormat = new DecimalFormat("###,###,###.00");
        String input_str = numFormat.format(inputMoney);
        String output_str = numFormat.format(outputMoney.abs());
        String total_str = numFormat.format(sumOfBalance);
        if (input_str.equals(",00")) {
            input_str = "0,00";
        }
        if (output_str.equals(",00")) {
            output_str = "0,00";
        }
        if (total_str.equals(",00")) {
            total_str = "0,00";
        }
        come_in_money.setText(input_str + " VND");
        come_out_money.setText(output_str + " VND");
        total_money.setText(total_str + " VND");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
