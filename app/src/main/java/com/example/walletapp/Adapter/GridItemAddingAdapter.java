package com.example.walletapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;

import java.util.List;

public class GridItemAddingAdapter extends ArrayAdapter<GridItem> {
    private Context context;
    private List<GridItem> listItems;
    public GridItemAddingAdapter(Context context, List<GridItem> listItems) {
        super(context, 0, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.home_adding_grid_item_layout, parent, false);
        }

        GridItem current = listItems.get(position);
        ImageView img = listItem.findViewById(R.id.re_icon);
        TextView txt = listItem.findViewById(R.id.re_text);
        img.setImageResource(current.getResouceIcon());
        txt.setText(current.getReText());
        CardView cardView = listItem.findViewById(R.id.cardviewClick);
        return listItem;
    }
}
