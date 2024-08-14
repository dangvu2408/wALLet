package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.walletapp.Model.TransactionItem;
import com.example.walletapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        String outputString = "";
        String plusOrDevideMoney = "";
        String typeStr = "";
        SimpleDateFormat input = new SimpleDateFormat("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = input.parse(current.getDateTrans());
            outputString = output.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (current.getTypeTrans().equals("revenue_money")) {
            plusOrDevideMoney = "+" + current.getMoneyTrans();
            typeStr = "Khoản thu";
            money_query.setTextColor(0xFF279CC5);
            money_query.setText(plusOrDevideMoney);
        } else if (current.getTypeTrans().equals("expense_money")) {
            plusOrDevideMoney = "-" + current.getMoneyTrans();
            typeStr = "Khoản chi";
            money_query.setTextColor(0xFFE45B65);
            money_query.setText(plusOrDevideMoney);
        } else if (current.getTypeTrans().equals("percentage_money")) {
            typeStr = "Khoản lãi suất";
            if (current.getDetailTypeTrans().equals("Trả lãi")) {
                plusOrDevideMoney = "-" + current.getMoneyTrans();
                money_query.setTextColor(0xFFE45B65);
                money_query.setText(plusOrDevideMoney);
            } else if (current.getDetailTypeTrans().equals("Thu lãi")) {
                plusOrDevideMoney = "+" + current.getMoneyTrans();
                money_query.setTextColor(0xFF279CC5);
                money_query.setText(plusOrDevideMoney);
            }
        } else if (current.getTypeTrans().equals("loan_money")) {
            typeStr = "Khoản vay";
            if (current.getDetailTypeTrans().equals("Cho vay") || current.getDetailTypeTrans().equals("Trả nợ")) {
                plusOrDevideMoney = "-" + current.getMoneyTrans();
                money_query.setTextColor(0xFFE45B65);
                money_query.setText(plusOrDevideMoney);
            } else if (current.getDetailTypeTrans().equals("Đi vay") || current.getDetailTypeTrans().equals("Thu nợ")) {
                plusOrDevideMoney = "+" + current.getMoneyTrans();
                money_query.setTextColor(0xFF279CC5);
                money_query.setText(plusOrDevideMoney);
            }
        }

        date_query.setText(outputString);
        type_of_transaction.setText("Loại giao dịch: " + typeStr + " - " + current.getDetailTypeTrans());
        time_of_transaction.setText("Thời gian: " + current.getDateTrans());
        description_of_transaction.setText("Ghi chú: " + current.getDescriptionTrans());
        return listItem;
    }
}
