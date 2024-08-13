package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;

import java.util.List;

public class DropdownItemAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> listItems;
    public DropdownItemAdapter(Context context, List<String> listItems) {
        super(context, 0, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.dropdown_items, parent, false);
        }

        String current = listItems.get(position);
        TextView child = listItem.findViewById(R.id.child_word);
        child.setText(current);
        return listItem;
    }
}
