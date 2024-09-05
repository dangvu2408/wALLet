package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.walletapp.Model.UserInfoModel;
import com.example.walletapp.R;

import java.util.List;

public class UserInfoItemAdapter extends BaseAdapter {
    final private Context context;
    final private List<UserInfoModel> item;

    public UserInfoItemAdapter(Context context, List<UserInfoModel> item) {
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
            listItem = LayoutInflater.from(context).inflate(R.layout.user_info_listitem, parent, false);
        }

        UserInfoModel current = item.get(position);
        TextView title_lisitem = listItem.findViewById(R.id.title_lisitem);
        TextView content_lisitem = listItem.findViewById(R.id.content_lisitem);
        title_lisitem.setText(current.getTitle_info());
        content_lisitem.setText(current.getContent_info());
        return listItem;
    }
}
