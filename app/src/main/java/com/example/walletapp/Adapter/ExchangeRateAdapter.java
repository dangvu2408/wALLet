package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.walletapp.Model.ExchangeModel;
import com.example.walletapp.Model.UserInfoModel;
import com.example.walletapp.R;

import java.util.List;

public class ExchangeRateAdapter extends BaseAdapter {
    final private Context context;
    final private List<ExchangeModel> item;

    public ExchangeRateAdapter(Context context, List<ExchangeModel> item) {
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
            listItem = LayoutInflater.from(context).inflate(R.layout.exchange_rate_item, parent, false);
        }

        ExchangeModel current = item.get(position);

        TextView currencyCode = listItem.findViewById(R.id.currencyCode);
        TextView buyMoney = listItem.findViewById(R.id.buyMoney);
        TextView transferMoney = listItem.findViewById(R.id.transferMoney);
        TextView sellMoney = listItem.findViewById(R.id.sellMoney);

        currencyCode.setText(current.getCurrencyCode());
        buyMoney.setText(current.getBuy());
        transferMoney.setText(current.getTransfer());
        sellMoney.setText(current.getSell());

        return listItem;
    }
}
