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

import com.example.walletapp.Activity.EditUserDataActivity;
import com.example.walletapp.Activity.LoginSettingActivity;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;

import java.util.List;

public class GridItemsSettingAdapter extends ArrayAdapter<GridItem> {
    private Context context;
    private List<GridItem> listItems;
    private String fullname, dateofbirth, gender, username;
    public GridItemsSettingAdapter(Context context, List<GridItem> listItems, String fullname, String dateofbirth, String gender, String username) {
        super(context, 0, listItems);
        this.context = context;
        this.listItems = listItems;
        this.fullname = fullname;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.profile_grid_item_layout, parent, false);
        }

        GridItem current = listItems.get(position);
        ImageView icon_setting = listItem.findViewById(R.id.icon_setting_item);
        TextView label_setting = listItem.findViewById(R.id.label_setting_item);
        icon_setting.setImageResource(current.getResouceIcon());
        label_setting.setText(current.getReText());
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null) {
                    switch (position) {
                        case 0:
                            Intent intent1 = new Intent(context, LoginSettingActivity.class);
                            intent1.putExtra("key_username_a", username);
                            intent1.putExtra("key_fullname_a", fullname);
                            intent1.putExtra("key_dateofbirth_a", dateofbirth);
                            intent1.putExtra("key_gender_a", gender);
                            context.startActivity(intent1);
                            ((Activity) context).overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                            break;
                        case 2:
                            Intent intent2 = new Intent(context, EditUserDataActivity.class);
                            intent2.putExtra("key_username_a", username);
                            intent2.putExtra("key_fullname_a", fullname);
                            intent2.putExtra("key_dateofbirth_a", dateofbirth);
                            intent2.putExtra("key_gender_a", gender);
                            context.startActivity(intent2);
                            ((Activity) context).overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                            break;
                    }
                }
            }
        });
        return listItem;
    }
}
