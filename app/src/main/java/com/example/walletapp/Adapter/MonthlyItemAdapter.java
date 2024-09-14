package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.walletapp.Model.MonthlyBalanceModel;
import com.example.walletapp.Model.UserInfoModel;
import com.example.walletapp.R;

import java.util.List;

public class MonthlyItemAdapter extends BaseAdapter {
    final private Context context;
    final private List<MonthlyBalanceModel> item;

    public MonthlyItemAdapter(Context context, List<MonthlyBalanceModel> item) {
        this.context = context;
        this.item = item;
    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.monthly_balance_listitem, parent, false);
        }

        MonthlyBalanceModel current = item.get(position);
        TextView month_index = listItem.findViewById(R.id.month_index);
        TextView month_money = listItem.findViewById(R.id.month_money);
        if (current.getMoneymonth().substring(0, 1).equals("-")) {
            month_money.setTextColor(0xFFE35053);
        }
        month_index.setText(current.getMonthly());
        month_money.setText(current.getMoneymonth());
        return listItem;
    }
}
