package com.example.walletapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletapp.Activity.LoginActivity;
import com.example.walletapp.Activity.RecentTransActivity;
import com.example.walletapp.Activity.UpdatingActivity;
import com.example.walletapp.Adapter.GridItemsSettingAdapter;
import com.example.walletapp.Adapter.GridItemsSettingOthersAdapter;
import com.example.walletapp.Model.GridItem;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private Button signout, update_data;
    private Context context;
    private String fullname, dob, gender;
    public ProfileFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ShapeableImageView avatar = view.findViewById(R.id.profile_avatar);
        TextView user_fullname = view.findViewById(R.id.user_full_name);
        GridView grid_setting = view.findViewById(R.id.gridview_setting);
        GridView grid_others = view.findViewById(R.id.gridview_others);
        signout = view.findViewById(R.id.signout);
        update_data = view.findViewById(R.id.update_data);
        this.context = getContext();

        fullname = getArguments().getString("key_fullname_data");
        dob = getArguments().getString("key_dob_data");
        gender = getArguments().getString("key_gender_data");
        user_fullname.setText(fullname);


        List<GridItem> listSetting = new ArrayList<>();
        List<GridItem> listOthers = new ArrayList<>();
        listSetting.add(new GridItem(R.drawable.shield, "Đăng nhập và bảo mật"));
        listSetting.add(new GridItem(R.drawable.bell, "Cài đặt thông báo"));
        listSetting.add(new GridItem(R.drawable.usercheck, "Chỉnh sửa thông tin cá nhân"));

        listOthers.add(new GridItem(R.drawable.dollarsign, "Tỉ giá ngoại tệ"));
        listOthers.add(new GridItem(R.drawable.airplay, "Thông tin chung"));

        String str_username = getArguments().getString("key_username_data");
        String str_password = getArguments().getString("key_password_data");


        GridItemsSettingAdapter adapter01 = new GridItemsSettingAdapter(this.getContext(), listSetting, fullname, dob, gender, str_username);
        GridItemsSettingOthersAdapter adapter02 = new GridItemsSettingOthersAdapter(this.getContext(), listOthers, fullname, dob, gender);
        grid_setting.setAdapter(adapter01);
        grid_others.setAdapter(adapter02);
        HeightUtils.setGridViewHeight(grid_setting, 1);
        HeightUtils.setGridViewHeight(grid_others, 1);

        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatingActivity.class);
                intent.putExtra("key_update_username", str_username);
                intent.putExtra("key_update_password", str_password);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                getActivity().finish();
            }
        });
        return view;
    }
}
