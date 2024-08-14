package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;

import java.util.List;

public class QueryTransactionAdapter extends BaseAdapter {
    private Context context;
    private List<TransactionItem> item;
    public QueryTransactionAdapter(Context context, List<TransactionItem> item) {
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
            listItem = LayoutInflater.from(context).inflate(R.layout.query_listitem, parent, false);
        }
        TransactionItem current = item.get(position);

        TextView date_query = listItem.findViewById(R.id.date_query);
        TextView money_query = listItem.findViewById(R.id.money_query);
        TextView type_of_transaction = listItem.findViewById(R.id.type_of_transaction);
        TextView time_of_transaction = listItem.findViewById(R.id.time_of_transaction);
        TextView description_of_transaction = listItem.findViewById(R.id.description_of_transaction);

        date_query.setText(current.getDateTrans());
        money_query.setText(current.getMoneyTrans());
        type_of_transaction.setText("Loại giao dịch: " + current.getTypeTrans() + " - " + current.getDetailTypeTrans());
        time_of_transaction.setText("Thời gian: " + current.getDateTrans());
        description_of_transaction.setText("Ghi chú: " + current.getDescriptionTrans());
        return listItem;
    }
}
