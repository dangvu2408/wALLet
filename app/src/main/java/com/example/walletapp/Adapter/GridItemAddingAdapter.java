package com.example.walletapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.walletapp.Activity.AddExpenseActivity;
import com.example.walletapp.Activity.AddLoanActivity;
import com.example.walletapp.Activity.AddPercentageActivity;
import com.example.walletapp.Activity.AddRevenueActivity;
import com.example.walletapp.Fragment.HomeFragment;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;

import java.util.List;

public class GridItemAddingAdapter extends ArrayAdapter<GridItem> {
    private Context context;
    private List<GridItem> listItems;
    private String phone, name;
    public GridItemAddingAdapter(Context context, List<GridItem> listItems, String phone, String name) {
        super(context, 0, listItems);
        this.context = context;
        this.listItems = listItems;
        this.phone = phone;
        this.name = name;
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
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null) {
                    Intent intent;
                    switch (position) {
                        case 0:
                            intent = new Intent(new Intent(context, AddExpenseActivity.class));
                            break;
                        case 1:
                            intent = new Intent(new Intent(context, AddRevenueActivity.class));
                            break;
                        case 2:
                            intent = new Intent(new Intent(context, AddPercentageActivity.class));
                            break;
                        case 3:
                            intent = new Intent(new Intent(context, AddLoanActivity.class));
                            break;
                        default:
                            return;
                    }

                    intent.putExtra("key_username_data", phone);
                    intent.putExtra("key_fullname_data", name);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                }
            }
        });
        return listItem;
    }
}
