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

import com.example.walletapp.Adapter.QueryTransactionAdapter;
import com.example.walletapp.Fragment.HomeFragment;
import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private ArrayList<TransactionItem> queryList;
    private SQLiteDatabase database;
    private QueryTransactionAdapter sortedDayAdapter;
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
        String src = this.getDatabasePath(SRC_DATABASE_NAME).getAbsolutePath();
        database = SQLiteDatabase.openOrCreateDatabase(src, null);
        queryList = new ArrayList<>();
        Cursor cursorSort = database.query("userdata", null, null, null, null, null, null);
        cursorSort.moveToNext();
        while (!cursorSort.isAfterLast()) {
            TransactionItem item = new TransactionItem(cursorSort.getString(3), cursorSort.getString(2), cursorSort.getString(0), cursorSort.getString(1),  cursorSort.getString(4));
            this.queryList.add(item);
            cursorSort.moveToNext();
        }
        cursorSort.close();
        ArrayList<TransactionItem> sortedDayList = new ArrayList<>(queryList);
        Collections.sort(sortedDayList, new Comparator<TransactionItem>() {
            @Override
            public int compare(TransactionItem o1, TransactionItem o2) {
                LocalDate date1 = LocalDate.parse(o1.getDateTrans(), formatter);
                LocalDate date2 = LocalDate.parse(o2.getDateTrans(), formatter);
                return date2.compareTo(date1);
            }
        });

        sortedDayAdapter = new QueryTransactionAdapter(this, sortedDayList);
        current_transaction.setAdapter(sortedDayAdapter);
        sortedDayAdapter.notifyDataSetChanged();
        HeightUtils.setListViewHeight(current_transaction);
        if (current_transaction.getLayoutParams().height == 10) {
            no_data_current_trans.setVisibility(View.VISIBLE);
        } else {
            no_data_current_trans.setVisibility(View.GONE);
        }


        DecimalFormat numFormat = new DecimalFormat("###,###,###.00");
        String input_str = numFormat.format(HomeFragment.inputMoney);
        String output_str = numFormat.format(HomeFragment.outputMoney.abs());
        String total_str = numFormat.format(HomeFragment.sumOfBalance);
        come_in_money.setText(input_str + "VND");
        come_out_money.setText(output_str + "VND");
        total_money.setText(total_str + " VND");
    }
}
